package pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "efjav_movimientos_tramite")
public class EfjavMovimientoTramiteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "efjav_id")
    private Long efjavId;

    @Column(name = "efjav_tramite_id", nullable = false)
    private Long efjavTramiteId;

    @Column(name = "efjav_instancia_origen_id", nullable = false)
    private Long efjavInstanciaOrigenId;

    @Column(name = "efjav_instancia_destino_id", nullable = false)
    private Long efjavInstanciaDestinoId;

    @Column(name = "efjav_responsable_id", nullable = false)
    private Long efjavResponsableId;

    @Column(name = "efjav_estado_resultante", nullable = false, length = 30)
    private String efjavEstadoResultante;

    @Column(name = "efjav_firma_responsable_id")
    private Long efjavFirmaResponsableId;

    @Column(name = "efjav_observacion", columnDefinition = "TEXT")
    private String efjavObservacion;

    @Column(name = "efjav_fecha_movimiento")
    private LocalDateTime efjavFechaMovimiento;

    public EfjavMovimientoTramiteEntity() {
        this.efjavFechaMovimiento = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getEfjavId() { return efjavId; }
    public void setEfjavId(Long efjavId) { this.efjavId = efjavId; }

    public Long getEfjavTramiteId() { return efjavTramiteId; }
    public void setEfjavTramiteId(Long efjavTramiteId) { this.efjavTramiteId = efjavTramiteId; }

    public Long getEfjavInstanciaOrigenId() { return efjavInstanciaOrigenId; }
    public void setEfjavInstanciaOrigenId(Long efjavInstanciaOrigenId) { this.efjavInstanciaOrigenId = efjavInstanciaOrigenId; }

    public Long getEfjavInstanciaDestinoId() { return efjavInstanciaDestinoId; }
    public void setEfjavInstanciaDestinoId(Long efjavInstanciaDestinoId) { this.efjavInstanciaDestinoId = efjavInstanciaDestinoId; }

    public Long getEfjavResponsableId() { return efjavResponsableId; }
    public void setEfjavResponsableId(Long efjavResponsableId) { this.efjavResponsableId = efjavResponsableId; }

    public String getEfjavEstadoResultante() { return efjavEstadoResultante; }
    public void setEfjavEstadoResultante(String efjavEstadoResultante) { this.efjavEstadoResultante = efjavEstadoResultante; }

    public Long getEfjavFirmaResponsableId() { return efjavFirmaResponsableId; }
    public void setEfjavFirmaResponsableId(Long efjavFirmaResponsableId) { this.efjavFirmaResponsableId = efjavFirmaResponsableId; }

    public String getEfjavObservacion() { return efjavObservacion; }
    public void setEfjavObservacion(String efjavObservacion) { this.efjavObservacion = efjavObservacion; }

    public LocalDateTime getEfjavFechaMovimiento() { return efjavFechaMovimiento; }
    public void setEfjavFechaMovimiento(LocalDateTime efjavFechaMovimiento) { this.efjavFechaMovimiento = efjavFechaMovimiento; }
}
