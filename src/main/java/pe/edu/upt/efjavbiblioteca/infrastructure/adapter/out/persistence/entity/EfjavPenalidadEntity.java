package pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "efjav_penalidades")
public class EfjavPenalidadEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "efjav_id")
    private Long efjavId;

    @Column(name = "efjav_prestamo_id", nullable = false)
    private Long efjavPrestamoId;

    @Column(name = "efjav_estudiante_id", nullable = false)
    private Long efjavEstudianteId;

    @Column(name = "efjav_fecha_vencimiento", nullable = false)
    private LocalDateTime efjavFechaVencimiento;

    @Column(name = "efjav_fecha_real_devolucion", nullable = false)
    private LocalDateTime efjavFechaRealDevolucion;

    @Column(name = "efjav_dias_retraso", nullable = false)
    private Integer efjavDiasRetraso;

    @Column(name = "efjav_dias_suspension", nullable = false)
    private Integer efjavDiasSuspension;

    @Column(name = "efjav_regla_aplicada", nullable = false, length = 255)
    private String efjavReglaAplicada;

    @Column(name = "efjav_fecha_inicio_suspension", nullable = false)
    private LocalDate efjavFechaInicioSuspension;

    @Column(name = "efjav_fecha_fin_suspension", nullable = false)
    private LocalDate efjavFechaFinSuspension;

    @Column(name = "efjav_estado", length = 20)
    private String efjavEstado; // VIGENTE, CUMPLIDA, ANULADA

    @Column(name = "efjav_fecha_creacion")
    private LocalDateTime efjavFechaCreacion;

    public EfjavPenalidadEntity() {
        this.efjavFechaCreacion = LocalDateTime.now();
        this.efjavEstado = "VIGENTE";
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

    public LocalDateTime getEfjavFechaCreacion() { return efjavFechaCreacion; }
    public void setEfjavFechaCreacion(LocalDateTime efjavFechaCreacion) { this.efjavFechaCreacion = efjavFechaCreacion; }
}
