package pe.edu.upt.efjavbiblioteca.application.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upt.efjavbiblioteca.domain.model.EfjavClsPenalidad;
import pe.edu.upt.efjavbiblioteca.domain.port.out.EfjavInfaAuditoriaRepository;
import pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.entity.*;
import pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.repository.*;
import pe.edu.upt.efjavbiblioteca.infrastructure.config.EfjavTimeService;
import pe.edu.upt.efjavbiblioteca.infrastructure.security.EfjavDispositivoSecurityService;
import pe.edu.upt.efjavbiblioteca.infrastructure.storage.EfjavStorageService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EfjavBibliotecaService {

    private final EfjavLibroJpaRepository efjavLibroJpaRepository;
    private final EfjavPrestamoJpaRepository efjavPrestamoJpaRepository;
    private final EfjavPenalidadJpaRepository efjavPenalidadJpaRepository;
    private final EfjavInfaAuditoriaRepository efjavAuditoriaRepository;
    private final EfjavNotificacionJpaRepository efjavNotificacionJpaRepository;
    private final EfjavStorageService efjavStorageService;
    private final EfjavTimeService efjavTimeService;
    private final EfjavDispositivoSecurityService efjavDispositivoSecurityService;

    public EfjavBibliotecaService(EfjavLibroJpaRepository efjavLibroJpaRepository,
                                  EfjavPrestamoJpaRepository efjavPrestamoJpaRepository,
                                  EfjavPenalidadJpaRepository efjavPenalidadJpaRepository,
                                  EfjavInfaAuditoriaRepository efjavAuditoriaRepository,
                                  EfjavNotificacionJpaRepository efjavNotificacionJpaRepository,
                                  EfjavStorageService efjavStorageService,
                                  EfjavTimeService efjavTimeService,
                                  EfjavDispositivoSecurityService efjavDispositivoSecurityService) {
        this.efjavLibroJpaRepository = efjavLibroJpaRepository;
        this.efjavPrestamoJpaRepository = efjavPrestamoJpaRepository;
        this.efjavPenalidadJpaRepository = efjavPenalidadJpaRepository;
        this.efjavAuditoriaRepository = efjavAuditoriaRepository;
        this.efjavNotificacionJpaRepository = efjavNotificacionJpaRepository;
        this.efjavStorageService = efjavStorageService;
        this.efjavTimeService = efjavTimeService;
        this.efjavDispositivoSecurityService = efjavDispositivoSecurityService;
    }

    public List<EfjavLibroEntity> efjavMtdListarLibros() {
        return efjavLibroJpaRepository.findAll();
    }

    public List<EfjavPrestamoEntity> efjavMtdListarPrestamosEstudiante(Long estudianteId) {
        return efjavPrestamoJpaRepository.findByEfjavEstudianteId(estudianteId);
    }

    public List<EfjavPrestamoEntity> efjavMtdListarTodosLosPrestamos() {
        return efjavPrestamoJpaRepository.findAll();
    }

    public Optional<EfjavPenalidadEntity> efjavFncVerificarPenalidadVigente(Long estudianteId) {
        Optional<EfjavPenalidadEntity> penOpt = efjavPenalidadJpaRepository.findFirstByEfjavEstudianteIdAndEfjavEstado(estudianteId, "VIGENTE");
        if (penOpt.isPresent()) {
            LocalDate hoy = efjavTimeService.efjavFncObtenerFechaHoraServidor().toLocalDate();
            if (penOpt.get().getEfjavFechaFinSuspension().isBefore(hoy)) {
                // Ya venció la penalidad
                EfjavPenalidadEntity p = penOpt.get();
                p.setEfjavEstado("CUMPLIDA");
                efjavPenalidadJpaRepository.save(p);
                return Optional.empty();
            }
        }
        return penOpt;
    }

    // Proceso 6.4: Solicitud de préstamo por estudiante
    @Transactional
    public EfjavPrestamoEntity efjavMtdSolicitarPrestamo(Long estudianteId, String estudianteCodigo, Long libroId, String ip, String equipo) {
        // RN03: Validar horario de solicitud
        if (!efjavTimeService.efjavFncEsHorarioSolicitudPrestamo()) {
            throw new IllegalStateException("Horario no permitido: Las solicitudes de préstamo se atienden de Lunes a Viernes de 08:00 a 20:00 (Hora del servidor).");
        }

        // RN14: Validar si el estudiante está suspendido
        if (efjavFncVerificarPenalidadVigente(estudianteId).isPresent()) {
            throw new IllegalStateException("El estudiante cuenta con una suspensión o penalidad vigente de biblioteca.");
        }

        EfjavLibroEntity libro = efjavLibroJpaRepository.findById(libroId)
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado"));

        if (libro.getEfjavEjemplaresDisponibles() <= 0) {
            throw new IllegalStateException("No hay ejemplares disponibles para el libro seleccionado.");
        }

        EfjavPrestamoEntity prestamo = new EfjavPrestamoEntity();
        prestamo.setEfjavCodigo("BIB-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        prestamo.setEfjavLibroId(libroId);
        prestamo.setEfjavEstudianteId(estudianteId);
        prestamo.setEfjavFechaSolicitud(efjavTimeService.efjavFncObtenerFechaHoraServidor());
        prestamo.setEfjavFechaLimiteDevolucion(prestamo.getEfjavFechaSolicitud().plusDays(7));
        prestamo.setEfjavEstado("SOLICITADO");

        EfjavPrestamoEntity guardado = efjavPrestamoJpaRepository.save(prestamo);

        efjavAuditoriaRepository.efjavMtdRegistrar(estudianteCodigo, "SOLICITAR_PRESTAMO", "PRESTAMO",
                "Solicitud de préstamo de libro ID: " + libroId, ip, equipo, "EXITOSO");

        return guardado;
    }

    // Proceso 6.4 (Pasos 5-6): Atención y confirmación con 2 firmas desde PC autorizada
    @Transactional
    public EfjavPrestamoEntity efjavMtdConfirmarPrestamoConDosFirmas(Long prestamoId, Long bibliotecarioId, String bibliotecarioCodigo,
                                                                    String firmaEstudianteBase64, String firmaBibliotecarioBase64,
                                                                    String ip, String equipo) {
        // RN06: Verificar equipo autorizado real mediante enrolamiento
        if (!efjavDispositivoSecurityService.efjavFncEsEquipoAutorizadoParaOficina(equipo, "BIBLIOTECA")) {
            throw new IllegalStateException("Operación restringida: Este equipo físico no se encuentra enrolado ni autorizado para operar como PC de Biblioteca (RN06).");
        }

        // RN04: Verificar horario de atención
        if (!efjavTimeService.efjavFncEsHorarioAtencionBiblioteca()) {
            throw new IllegalStateException("Biblioteca fuera de horario de atención (08:00 a 13:00 y 14:00 a 20:00).");
        }

        if (firmaEstudianteBase64 == null || firmaEstudianteBase64.trim().isEmpty() ||
            firmaBibliotecarioBase64 == null || firmaBibliotecarioBase64.trim().isEmpty()) {
            throw new IllegalArgumentException("Validación obligatoria: No se puede confirmar un préstamo si falta cualquiera de las dos firmas.");
        }

        EfjavPrestamoEntity prestamo = efjavPrestamoJpaRepository.findById(prestamoId)
                .orElseThrow(() -> new IllegalArgumentException("Préstamo no encontrado"));

        EfjavLibroEntity libro = efjavLibroJpaRepository.findById(prestamo.getEfjavLibroId())
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado"));

        if (libro.getEfjavEjemplaresDisponibles() <= 0) {
            throw new IllegalStateException("El ejemplar ya no está disponible.");
        }

        // Guardar firmas digitales
        EfjavFirmaEntity firmaEst = efjavStorageService.efjavMtdGuardarFirmaBase64(
                prestamo.getEfjavEstudianteId(), "Estudiante", "ESTUDIANTE", firmaEstudianteBase64, "PRESTAMO_ENTREGA"
        );
        EfjavFirmaEntity firmaBib = efjavStorageService.efjavMtdGuardarFirmaBase64(
                bibliotecarioId, "Jefe de Biblioteca", "BIBLIOTECA", firmaBibliotecarioBase64, "PRESTAMO_DESPACHO"
        );

        // Descontar stock
        libro.setEfjavEjemplaresDisponibles(libro.getEfjavEjemplaresDisponibles() - 1);
        if (libro.getEfjavEjemplaresDisponibles() == 0) {
            libro.setEfjavEstado("PRESTADO");
        }
        efjavLibroJpaRepository.save(libro);

        prestamo.setEfjavBibliotecarioId(bibliotecarioId);
        prestamo.setEfjavFechaEntrega(efjavTimeService.efjavFncObtenerFechaHoraServidor());
        prestamo.setEfjavFechaLimiteDevolucion(prestamo.getEfjavFechaEntrega().plusDays(7));
        prestamo.setEfjavFirmaEstudianteId(firmaEst.getEfjavId());
        prestamo.setEfjavFirmaBibliotecarioId(firmaBib.getEfjavId());
        prestamo.setEfjavEstado("ACTIVO");

        EfjavPrestamoEntity actualizado = efjavPrestamoJpaRepository.save(prestamo);

        efjavAuditoriaRepository.efjavMtdRegistrar(bibliotecarioCodigo, "ENTREGA_PRESTAMO", "PRESTAMO",
                "Préstamo confirmado con 2 firmas. Código: " + prestamo.getEfjavCodigo(), ip, equipo, "EXITOSO");

        return actualizado;
    }

    // Proceso 6.5 & 6.6: Devolución con cálculo de penalidad (2x días demora)
    @Transactional
    public EfjavPrestamoEntity efjavMtdRegistrarDevolucion(Long prestamoId, Long bibliotecarioId, String bibliotecarioCodigo,
                                                          String firmaEstudianteBase64, String firmaBibliotecarioBase64,
                                                          String estadoFisico, String observacion, Integer diasRetrasoManual,
                                                          String ip, String equipo) {
        if (!efjavDispositivoSecurityService.efjavFncEsEquipoAutorizadoParaOficina(equipo, "BIBLIOTECA")) {
            throw new IllegalStateException("Operación restringida: Las devoluciones requieren un equipo físico enrolado y autorizado para Biblioteca.");
        }

        if (firmaEstudianteBase64 == null || firmaBibliotecarioBase64 == null) {
            throw new IllegalArgumentException("Se requieren ambas firmas (estudiante y recepcionista) para la devolución.");
        }

        EfjavPrestamoEntity prestamo = efjavPrestamoJpaRepository.findById(prestamoId)
                .orElseThrow(() -> new IllegalArgumentException("Préstamo no encontrado"));

        LocalDateTime fechaDevolucion = efjavTimeService.efjavFncObtenerFechaHoraServidor();
        LocalDateTime fechaLimite = prestamo.getEfjavFechaLimiteDevolucion();

        long diasRetraso = 0;
        if (diasRetrasoManual != null && diasRetrasoManual > 0) {
            diasRetraso = diasRetrasoManual;
        } else if (fechaDevolucion.isAfter(fechaLimite)) {
            diasRetraso = ChronoUnit.DAYS.between(fechaLimite.toLocalDate(), fechaDevolucion.toLocalDate());
            if (diasRetraso <= 0) diasRetraso = 1;
        }

        // Si hubo retraso, aplicar la regla de la UPT: Suspensión = 2 * días de retraso
        if (diasRetraso > 0) {
            int suspensionDias = (int) (diasRetraso * 2);
            LocalDate inicioSuspension = fechaDevolucion.toLocalDate();
            LocalDate finSuspension = inicioSuspension.plusDays(suspensionDias);

            EfjavPenalidadEntity penalidad = new EfjavPenalidadEntity();
            penalidad.setEfjavPrestamoId(prestamo.getEfjavId());
            penalidad.setEfjavEstudianteId(prestamo.getEfjavEstudianteId());
            penalidad.setEfjavFechaVencimiento(fechaLimite);
            penalidad.setEfjavFechaRealDevolucion(fechaDevolucion);
            penalidad.setEfjavDiasRetraso((int) diasRetraso);
            penalidad.setEfjavDiasSuspension(suspensionDias);
            penalidad.setEfjavReglaAplicada("Resolución N.º 246-2023-UPT-CU: Suspensión por el doble de días de demora (" + diasRetraso + " días retraso = " + suspensionDias + " días suspensión)");
            penalidad.setEfjavFechaInicioSuspension(inicioSuspension);
            penalidad.setEfjavFechaFinSuspension(finSuspension);
            penalidad.setEfjavEstado("VIGENTE");

            efjavPenalidadJpaRepository.save(penalidad);

            // Crear notificación automática para el estudiante
            EfjavNotificacionEntity notif = new EfjavNotificacionEntity();
            notif.setEfjavDestinatarioId(prestamo.getEfjavEstudianteId());
            notif.setEfjavCanal("SISTEMA");
            notif.setEfjavTitulo("Penalidad por devolución fuera de plazo");
            notif.setEfjavContenido("Se ha registrado una suspensión del servicio de biblioteca por " + suspensionDias +
                    " días debido a " + diasRetraso + " días de retraso en la devolución del libro. Vigente hasta: " + finSuspension);
            notif.setEfjavTipo("PENALIDAD");
            efjavNotificacionJpaRepository.save(notif);
        }

        // Reponer ejemplar a catálogo
        EfjavLibroEntity libro = efjavLibroJpaRepository.findById(prestamo.getEfjavLibroId()).orElse(null);
        if (libro != null) {
            libro.setEfjavEjemplaresDisponibles(libro.getEfjavEjemplaresDisponibles() + 1);
            libro.setEfjavEstado("DISPONIBLE");
            efjavLibroJpaRepository.save(libro);
        }

        prestamo.setEfjavEstado("DEVUELTO");
        EfjavPrestamoEntity devuelto = efjavPrestamoJpaRepository.save(prestamo);

        efjavAuditoriaRepository.efjavMtdRegistrar(bibliotecarioCodigo, "DEVOLUCION_LIBRO", "PRESTAMO",
                "Devolución registrada. Retraso: " + diasRetraso + " días. Estado físico: " + estadoFisico, ip, equipo, "EXITOSO");

        return devuelto;
    }
}
