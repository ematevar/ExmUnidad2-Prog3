package pe.edu.upt.efjavbiblioteca.application.usecase;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.upt.efjavbiblioteca.domain.port.out.EfjavInfaAuditoriaRepository;
import pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.entity.*;
import pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.repository.*;
import pe.edu.upt.efjavbiblioteca.infrastructure.config.EfjavTimeService;
import pe.edu.upt.efjavbiblioteca.infrastructure.storage.EfjavStorageService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class EfjavTramiteService {

    private final EfjavTramiteJpaRepository efjavTramiteJpaRepository;
    private final EfjavMovimientoTramiteJpaRepository efjavMovimientoJpaRepository;
    private final EfjavNotificacionJpaRepository efjavNotificacionJpaRepository;
    private final EfjavInfaAuditoriaRepository efjavAuditoriaRepository;
    private final EfjavStorageService efjavStorageService;
    private final EfjavTimeService efjavTimeService;

    public EfjavTramiteService(EfjavTramiteJpaRepository efjavTramiteJpaRepository,
                               EfjavMovimientoTramiteJpaRepository efjavMovimientoJpaRepository,
                               EfjavNotificacionJpaRepository efjavNotificacionJpaRepository,
                               EfjavInfaAuditoriaRepository efjavAuditoriaRepository,
                               EfjavStorageService efjavStorageService,
                               EfjavTimeService efjavTimeService) {
        this.efjavTramiteJpaRepository = efjavTramiteJpaRepository;
        this.efjavMovimientoJpaRepository = efjavMovimientoJpaRepository;
        this.efjavNotificacionJpaRepository = efjavNotificacionJpaRepository;
        this.efjavAuditoriaRepository = efjavAuditoriaRepository;
        this.efjavStorageService = efjavStorageService;
        this.efjavTimeService = efjavTimeService;
    }

    public List<EfjavTramiteEntity> efjavMtdListarPorEstudiante(Long estudianteId) {
        return efjavTramiteJpaRepository.findByEfjavEstudianteId(estudianteId);
    }

    public List<EfjavTramiteEntity> efjavMtdListarPorUbicacion(Long instanciaId) {
        // Excluir culminados y archivados de la bandeja activa (RN16)
        return efjavTramiteJpaRepository.findByEfjavUbicacionActualIdAndEfjavEstadoNotIn(
                instanciaId, Arrays.asList("CONCLUIDO", "ARCHIVADO")
        );
    }

    public List<EfjavTramiteEntity> efjavMtdListarCulminados(LocalDateTime inicio, LocalDateTime fin) {
        return efjavTramiteJpaRepository.findByEfjavEstadoInAndEfjavFechaEnvioBetween(
                Arrays.asList("CONCLUIDO", "ARCHIVADO"), inicio, fin
        );
    }

    public List<EfjavMovimientoTramiteEntity> efjavMtdObtenerHistorial(Long tramiteId) {
        return efjavMovimientoJpaRepository.findByEfjavTramiteIdOrderByEfjavFechaMovimientoAsc(tramiteId);
    }

    // Proceso 6.8: Presentación de trámite académico con PDF y firma
    @Transactional
    public EfjavTramiteEntity efjavMtdRegistrarTramite(Long estudianteId, String estudianteCodigo,
                                                       String tipoTramite, String asunto, Long instanciaDestinoId,
                                                       MultipartFile documentoPdf, String firmaEstudianteBase64,
                                                       String ip, String equipo) throws IOException {
        // RN: Horario de trámites 08:00 a 17:00
        if (!efjavTimeService.efjavFncEsHorarioTramites()) {
            throw new IllegalStateException("Horario no permitido: La presentación de trámites se habilita de Lunes a Viernes de 08:00 a 17:00.");
        }

        if (firmaEstudianteBase64 == null || firmaEstudianteBase64.trim().isEmpty()) {
            throw new IllegalArgumentException("Validación obligatoria: No se puede enviar el trámite sin la firma del estudiante.");
        }

        // Guardar archivo PDF con validación de MIME y cabecera
        String rutaPdf = efjavStorageService.efjavMtdGuardarArchivoPdf(documentoPdf, "tramites");

        // Guardar firma digitalizada
        EfjavFirmaEntity firma = efjavStorageService.efjavMtdGuardarFirmaBase64(
                estudianteId, "Estudiante", "ESTUDIANTE", firmaEstudianteBase64, "PRESENTACION_TRAMITE"
        );

        String codigoSeguimiento = "TR-" + String.format("%06d", (int)(Math.random() * 900000 + 100000));

        EfjavTramiteEntity tramite = new EfjavTramiteEntity();
        tramite.setEfjavCodigoSeguimiento(codigoSeguimiento);
        tramite.setEfjavEstudianteId(estudianteId);
        tramite.setEfjavTipoTramite(tipoTramite);
        tramite.setEfjavAsunto(asunto);
        tramite.setEfjavInstanciaDestinoId(instanciaDestinoId);
        tramite.setEfjavUbicacionActualId(instanciaDestinoId);
        tramite.setEfjavEstado("ENVIADO");
        tramite.setEfjavFechaEnvio(efjavTimeService.efjavFncObtenerFechaHoraServidor());
        tramite.setEfjavFirmaEstudianteId(firma.getEfjavId());

        EfjavTramiteEntity guardado = efjavTramiteJpaRepository.save(tramite);

        // Registrar primer movimiento en el historial
        EfjavMovimientoTramiteEntity mov = new EfjavMovimientoTramiteEntity();
        mov.setEfjavTramiteId(guardado.getEfjavId());
        mov.setEfjavInstanciaOrigenId(instanciaDestinoId);
        mov.setEfjavInstanciaDestinoId(instanciaDestinoId);
        mov.setEfjavResponsableId(estudianteId);
        mov.setEfjavEstadoResultante("ENVIADO");
        mov.setEfjavObservacion("Expediente presentado por el estudiante con firma y documento adjunto");
        efjavMovimientoJpaRepository.save(mov);

        efjavAuditoriaRepository.efjavMtdRegistrar(estudianteCodigo, "PRESENTAR_TRAMITE", "TRAMITE",
                "Trámite presentado con código: " + codigoSeguimiento, ip, equipo, "EXITOSO");

        return guardado;
    }

    // Proceso 6.10 & 6.11: Atender en Facultad (Aprobar, Rechazar, Observar o Derivar a EPIS)
    @Transactional
    public EfjavTramiteEntity efjavMtdAtenderEnFacultad(Long tramiteId, Long decanoId, String decanoCodigo,
                                                        String accion, String observacion, String firmaDecanoBase64,
                                                        String ip, String equipo) {
        // RN07: Requiere PC asignada
        if (!"PC-DEC-01".equalsIgnoreCase(equipo)) {
            throw new IllegalStateException("Operación restringida: La atención y decisión de Facultad solo puede realizarse desde la PC asignada (PC-DEC-01).");
        }

        EfjavTramiteEntity tramite = efjavTramiteJpaRepository.findById(tramiteId)
                .orElseThrow(() -> new IllegalArgumentException("Trámite no encontrado"));

        EfjavFirmaEntity firma = efjavStorageService.efjavMtdGuardarFirmaBase64(
                decanoId, "Decano", "DECANO", firmaDecanoBase64, "ATENCION_FACULTAD_" + accion
        );

        EfjavMovimientoTramiteEntity mov = new EfjavMovimientoTramiteEntity();
        mov.setEfjavTramiteId(tramite.getEfjavId());
        mov.setEfjavInstanciaOrigenId(1L); // Facultad
        mov.setEfjavResponsableId(decanoId);
        mov.setEfjavFirmaResponsableId(firma.getEfjavId());
        mov.setEfjavObservacion(observacion);

        if ("DERIVAR_EPIS".equalsIgnoreCase(accion)) {
            tramite.setEfjavUbicacionActualId(2L); // 2 = EPIS
            tramite.setEfjavEstado("ENVIADO");
            mov.setEfjavInstanciaDestinoId(2L);
            mov.setEfjavEstadoResultante("ENVIADO_A_EPIS");
        } else if ("APROBAR".equalsIgnoreCase(accion)) {
            tramite.setEfjavEstado("CONCLUIDO");
            tramite.setEfjavFechaConclusion(efjavTimeService.efjavFncObtenerFechaHoraServidor());
            mov.setEfjavInstanciaDestinoId(1L);
            mov.setEfjavEstadoResultante("CONCLUIDO");
            efjavMtdNotificarEstudiante(tramite.getEfjavEstudianteId(), "Trámite Aprobado en Facultad", "Su expediente " + tramite.getEfjavCodigoSeguimiento() + " ha sido aprobado satisfactoriamente.");
        } else if ("RECHAZAR".equalsIgnoreCase(accion)) {
            tramite.setEfjavEstado("RECHAZADO");
            tramite.setEfjavFechaConclusion(efjavTimeService.efjavFncObtenerFechaHoraServidor());
            mov.setEfjavInstanciaDestinoId(1L);
            mov.setEfjavEstadoResultante("RECHAZADO");
            efjavMtdNotificarEstudiante(tramite.getEfjavEstudianteId(), "Trámite Rechazado", "Su expediente " + tramite.getEfjavCodigoSeguimiento() + " ha sido rechazado. Motivo: " + observacion);
        }

        efjavMovimientoJpaRepository.save(mov);
        EfjavTramiteEntity actualizado = efjavTramiteJpaRepository.save(tramite);

        efjavAuditoriaRepository.efjavMtdRegistrar(decanoCodigo, "ATENDER_FACULTAD", "TRAMITE",
                "Acción: " + accion + " sobre expediente: " + tramite.getEfjavCodigoSeguimiento(), ip, equipo, "EXITOSO");

        return actualizado;
    }

    // Proceso 6.11 & 6.12: Atender en EPIS (Aprobar y devolver a Facultad u Observar)
    @Transactional
    public EfjavTramiteEntity efjavMtdAtenderEnEpis(Long tramiteId, Long directorId, String directorCodigo,
                                                    String accion, String observacion, String firmaDirectorBase64,
                                                    String ip, String equipo) {
        // RN07: Requiere PC asignada
        if (!"PC-EPI-01".equalsIgnoreCase(equipo)) {
            throw new IllegalStateException("Operación restringida: La atención en Dirección EPIS requiere su PC asignada (PC-EPI-01).");
        }

        EfjavTramiteEntity tramite = efjavTramiteJpaRepository.findById(tramiteId)
                .orElseThrow(() -> new IllegalArgumentException("Trámite no encontrado"));

        EfjavFirmaEntity firma = efjavStorageService.efjavMtdGuardarFirmaBase64(
                directorId, "Director EPIS", "EPIS", firmaDirectorBase64, "ATENCION_EPIS_" + accion
        );

        EfjavMovimientoTramiteEntity mov = new EfjavMovimientoTramiteEntity();
        mov.setEfjavTramiteId(tramite.getEfjavId());
        mov.setEfjavInstanciaOrigenId(2L); // EPIS
        mov.setEfjavResponsableId(directorId);
        mov.setEfjavFirmaResponsableId(firma.getEfjavId());
        mov.setEfjavObservacion(observacion);

        if ("DEVOLVER_FACULTAD".equalsIgnoreCase(accion)) {
            tramite.setEfjavUbicacionActualId(1L); // Devuelve a Facultad
            tramite.setEfjavEstado("RECIBIDO");
            mov.setEfjavInstanciaDestinoId(1L);
            mov.setEfjavEstadoResultante("DEVUELTO_A_FACULTAD");
        } else if ("OBSERVAR".equalsIgnoreCase(accion)) {
            tramite.setEfjavEstado("OBSERVADO");
            // Si vino derivado de facultad, regresa a facultad para gestión, sino directo
            tramite.setEfjavUbicacionActualId(1L);
            mov.setEfjavInstanciaDestinoId(1L);
            mov.setEfjavEstadoResultante("OBSERVADO");
            efjavMtdNotificarEstudiante(tramite.getEfjavEstudianteId(), "Trámite Observado en EPIS", "Su expediente " + tramite.getEfjavCodigoSeguimiento() + " tiene observaciones: " + observacion);
        }

        efjavMovimientoJpaRepository.save(mov);
        EfjavTramiteEntity actualizado = efjavTramiteJpaRepository.save(tramite);

        efjavAuditoriaRepository.efjavMtdRegistrar(directorCodigo, "ATENDER_EPIS", "TRAMITE",
                "Acción EPIS: " + accion + " sobre expediente: " + tramite.getEfjavCodigoSeguimiento(), ip, equipo, "EXITOSO");

        return actualizado;
    }

    // Proceso 6.13: Atender en Cobranzas
    @Transactional
    public EfjavTramiteEntity efjavMtdAtenderEnCobranzas(Long tramiteId, Long responsableId, String responsableCodigo,
                                                         String accion, String observacion, String firmaBase64,
                                                         String ip, String equipo) {
        if (!"PC-COB-01".equalsIgnoreCase(equipo)) {
            throw new IllegalStateException("Operación restringida: Cobranzas solo puede atender desde la PC autorizada (PC-COB-01).");
        }

        EfjavTramiteEntity tramite = efjavTramiteJpaRepository.findById(tramiteId)
                .orElseThrow(() -> new IllegalArgumentException("Trámite no encontrado"));

        EfjavFirmaEntity firma = efjavStorageService.efjavMtdGuardarFirmaBase64(
                responsableId, "Cobranzas", "COBRANZAS", firmaBase64, "ATENCION_COBRANZAS_" + accion
        );

        EfjavMovimientoTramiteEntity mov = new EfjavMovimientoTramiteEntity();
        mov.setEfjavTramiteId(tramite.getEfjavId());
        mov.setEfjavInstanciaOrigenId(3L); // Cobranzas
        mov.setEfjavInstanciaDestinoId(3L);
        mov.setEfjavResponsableId(responsableId);
        mov.setEfjavFirmaResponsableId(firma.getEfjavId());
        mov.setEfjavObservacion(observacion);

        if ("APROBAR".equalsIgnoreCase(accion)) {
            tramite.setEfjavEstado("CONCLUIDO");
            tramite.setEfjavFechaConclusion(efjavTimeService.efjavFncObtenerFechaHoraServidor());
            mov.setEfjavEstadoResultante("CONCLUIDO");
            efjavMtdNotificarEstudiante(tramite.getEfjavEstudianteId(), "Trámite Concluido en Cobranzas", "Su expediente fue aprobado.");
        } else {
            tramite.setEfjavEstado("RECHAZADO");
            tramite.setEfjavFechaConclusion(efjavTimeService.efjavFncObtenerFechaHoraServidor());
            mov.setEfjavEstadoResultante("RECHAZADO");
            efjavMtdNotificarEstudiante(tramite.getEfjavEstudianteId(), "Trámite Denegado en Cobranzas", "Observación: " + observacion);
        }

        efjavMovimientoJpaRepository.save(mov);
        EfjavTramiteEntity actualizado = efjavTramiteJpaRepository.save(tramite);

        efjavAuditoriaRepository.efjavMtdRegistrar(responsableCodigo, "ATENDER_COBRANZAS", "TRAMITE",
                "Atención Cobranzas: " + accion + " expediente: " + tramite.getEfjavCodigoSeguimiento(), ip, equipo, "EXITOSO");

        return actualizado;
    }

    private void efjavMtdNotificarEstudiante(Long estudianteId, String titulo, String contenido) {
        EfjavNotificacionEntity notif = new EfjavNotificacionEntity();
        notif.setEfjavDestinatarioId(estudianteId);
        notif.setEfjavTitulo(titulo);
        notif.setEfjavContenido(contenido);
        notif.setEfjavTipo("ESTADO_TRAMITE");
        efjavNotificacionJpaRepository.save(notif);
    }
}
