package pe.edu.upt.efjavbiblioteca.application.usecase;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.entity.EfjavNotificacionEntity;
import pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.entity.EfjavPrestamoEntity;
import pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.repository.EfjavNotificacionJpaRepository;
import pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.repository.EfjavPrestamoJpaRepository;
import pe.edu.upt.efjavbiblioteca.infrastructure.config.EfjavTimeService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EfjavRecordatorioService {

    private final EfjavPrestamoJpaRepository efjavPrestamoJpaRepository;
    private final EfjavNotificacionJpaRepository efjavNotificacionJpaRepository;
    private final EfjavTimeService efjavTimeService;

    public EfjavRecordatorioService(EfjavPrestamoJpaRepository efjavPrestamoJpaRepository,
                                   EfjavNotificacionJpaRepository efjavNotificacionJpaRepository,
                                   EfjavTimeService efjavTimeService) {
        this.efjavPrestamoJpaRepository = efjavPrestamoJpaRepository;
        this.efjavNotificacionJpaRepository = efjavNotificacionJpaRepository;
        this.efjavTimeService = efjavTimeService;
    }

    // Se ejecuta periódicamente y también bajo demanda para sustentación en vivo (Prueba 19)
    @Scheduled(fixedRate = 300000) // Cada 5 minutos
    @Transactional
    public int efjavMtdEjecutarBarridoRecordatorios() {
        LocalDateTime ahora = efjavTimeService.efjavFncObtenerFechaHoraServidor();
        LocalDateTime umbralProximo = ahora.plusDays(2); // Préstamos que vencen en las próximas 48 horas

        List<EfjavPrestamoEntity> activos = efjavPrestamoJpaRepository.findByEfjavEstado("ACTIVO");
        int countGenerados = 0;

        for (EfjavPrestamoEntity p : activos) {
            if (p.getEfjavFechaLimiteDevolucion() != null &&
                p.getEfjavFechaLimiteDevolucion().isBefore(umbralProximo) &&
                p.getEfjavFechaLimiteDevolucion().isAfter(ahora)) {

                // Generar notificación interna y simular canal de correo institucional
                EfjavNotificacionEntity notif = new EfjavNotificacionEntity();
                notif.setEfjavDestinatarioId(p.getEfjavEstudianteId());
                notif.setEfjavCanal("AMBOS"); // Sistema y Correo
                notif.setEfjavTitulo("Recordatorio de Vencimiento de Préstamo - " + p.getEfjavCodigo());
                notif.setEfjavContenido("Estimado estudiante, el préstamo de su ejemplar vence el " +
                        p.getEfjavFechaLimiteDevolucion().toLocalDate() + ". Recuerde realizar la devolución dentro del horario para evitar sanciones.");
                notif.setEfjavTipo("RECORDATORIO_VENCIMIENTO");
                notif.setEfjavEstadoEnvio("ENTREGADO"); // Con confirmación simulada de entrega de correo

                efjavNotificacionJpaRepository.save(notif);
                countGenerados++;
            }
        }
        return countGenerados;
    }
}
