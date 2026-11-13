package pe.edu.upt.efjavbiblioteca.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class EfjavTimeService {

    @Value("${efjav.business.enforce-schedule:true}")
    private boolean efjavEnforceSchedule;

    @Value("${efjav.business.simulated-hour:}")
    private String efjavSimulatedHour;

    public LocalDateTime efjavFncObtenerFechaHoraServidor() {
        if (efjavSimulatedHour != null && !efjavSimulatedHour.trim().isEmpty()) {
            try {
                LocalTime sim = LocalTime.parse(efjavSimulatedHour.trim());
                return LocalDateTime.now().withHour(sim.getHour()).withMinute(sim.getMinute());
            } catch (Exception ignored) {}
        }
        return LocalDateTime.now();
    }

    // RN03: Solicitud de préstamo de lunes a viernes, 08:00 a 20:00
    public boolean efjavFncEsHorarioSolicitudPrestamo() {
        if (!efjavEnforceSchedule) return true;
        LocalDateTime now = efjavFncObtenerFechaHoraServidor();
        DayOfWeek day = now.getDayOfWeek();
        if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) return false;
        LocalTime time = now.toLocalTime();
        return !time.isBefore(LocalTime.of(8, 0)) && !time.isAfter(LocalTime.of(20, 0));
    }

    // RN04: Biblioteca atiende 08:00 a 13:00 y 14:00 a 20:00 (Pausa 13:00 a 14:00)
    public boolean efjavFncEsHorarioAtencionBiblioteca() {
        if (!efjavEnforceSchedule) return true;
        LocalDateTime now = efjavFncObtenerFechaHoraServidor();
        DayOfWeek day = now.getDayOfWeek();
        if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) return false;
        LocalTime time = now.toLocalTime();
        boolean turno1 = !time.isBefore(LocalTime.of(8, 0)) && !time.isAfter(LocalTime.of(13, 0));
        boolean turno2 = !time.isBefore(LocalTime.of(14, 0)) && !time.isAfter(LocalTime.of(20, 0));
        return turno1 || turno2;
    }

    // RN: Trámites académicos habilitados de 08:00 a 17:00
    public boolean efjavFncEsHorarioTramites() {
        if (!efjavEnforceSchedule) return true;
        LocalDateTime now = efjavFncObtenerFechaHoraServidor();
        DayOfWeek day = now.getDayOfWeek();
        if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) return false;
        LocalTime time = now.toLocalTime();
        return !time.isBefore(LocalTime.of(8, 0)) && !time.isAfter(LocalTime.of(17, 0));
    }
}
