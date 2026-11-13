package pe.edu.upt.efjavbiblioteca.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class EfjavClsPenalidad {
    private Long efjavId;
    private Long efjavPrestamoId;
    private Long efjavEstudianteId;
    private LocalDateTime efjavFechaVencimiento;
    private LocalDateTime efjavFechaRealDevolucion;
    private Integer efjavDiasRetraso;
    private Integer efjavDiasSuspension;
    private String efjavReglaAplicada;
    private LocalDate efjavFechaInicioSuspension;
    private LocalDate efjavFechaFinSuspension;
    private String efjavEstado; // VIGENTE, CUMPLIDA, ANULADA

    public EfjavClsPenalidad() {
        this.efjavEstado = "VIGENTE";
        this.efjavReglaAplicada = "Resolución N.º 246-2023-UPT-CU (Suspensión = 2x días de retraso)";
    }

    public boolean efjavFncEstaVigente(LocalDate fechaConsulta) {
        if (!"VIGENTE".equalsIgnoreCase(this.efjavEstado)) return false;
        if (efjavFechaFinSuspension == null) return false;
        return !fechaConsulta.isAfter(efjavFechaFinSuspension);
    }

    // Getters y Setters
    public Long getEfjavId() { return efjavId; }
    public void setEfjavId(Long efjavId) { this.efjavId = efjavId; }

    public Long getEfjavPrestamoId() { return efjavPrestamoId; }
    public void setEfjavPrestamoId(Long efjavPrestamoId) { this.efjavPrestamoId = efjavPrestamoId; }

    public Long getEfjavEstudianteId() { return efjavEstudianteId; }
    public void setEfjavEstudianteId(Long efjavEstudianteId) { this.efjavEstudianteId = efjavEstudianteId; }

    public LocalDateTime getEfjavFechaVencimiento() { return efjavFechaVencimiento; }
    public void setEfjavFechaVencimiento(LocalDateTime efjavFechaVencimiento) { this.efjavFechaVencimiento = efjavFechaVencimiento; }

    public LocalDateTime getEfjavFechaRealDevolucion() { return efjavFechaRealDevolucion; }
    public void setEfjavFechaRealDevolucion(LocalDateTime efjavFechaRealDevolucion) { this.efjavFechaRealDevolucion = efjavFechaRealDevolucion; }

    public Integer getEfjavDiasRetraso() { return efjavDiasRetraso; }
    public void setEfjavDiasRetraso(Integer efjavDiasRetraso) { this.efjavDiasRetraso = efjavDiasRetraso; }

    public Integer getEfjavDiasSuspension() { return efjavDiasSuspension; }
    public void setEfjavDiasSuspension(Integer efjavDiasSuspension) { this.efjavDiasSuspension = efjavDiasSuspension; }

    public String getEfjavReglaAplicada() { return efjavReglaAplicada; }
    public void setEfjavReglaAplicada(String efjavReglaAplicada) { this.efjavReglaAplicada = efjavReglaAplicada; }

    public LocalDate getEfjavFechaInicioSuspension() { return efjavFechaInicioSuspension; }
    public void setEfjavFechaInicioSuspension(LocalDate efjavFechaInicioSuspension) { this.efjavFechaInicioSuspension = efjavFechaInicioSuspension; }

    public LocalDate getEfjavFechaFinSuspension() { return efjavFechaFinSuspension; }
    public void setEfjavFechaFinSuspension(LocalDate efjavFechaFinSuspension) { this.efjavFechaFinSuspension = efjavFechaFinSuspension; }

    public String getEfjavEstado() { return efjavEstado; }
    public void setEfjavEstado(String efjavEstado) { this.efjavEstado = efjavEstado; }
}
