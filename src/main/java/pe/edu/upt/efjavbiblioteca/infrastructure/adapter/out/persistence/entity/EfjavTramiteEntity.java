package pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "efjav_tramites")
public class EfjavTramiteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "efjav_id")
    private Long efjavId;

    @Column(name = "efjav_codigo_seguimiento", nullable = false, unique = true, length = 50)
    private String efjavCodigoSeguimiento;

    @Column(name = "efjav_estudiante_id", nullable = false)
    private Long efjavEstudianteId;

    @Column(name = "efjav_tipo_tramite", nullable = false, length = 100)
    private String efjavTipoTramite;

    @Column(name = "efjav_asunto", nullable = false, length = 255)
    private String efjavAsunto;

    @Column(name = "efjav_instancia_destino_id", nullable = false)
    private Long efjavInstanciaDestinoId;

    @Column(name = "efjav_ubicacion_actual_id", nullable = false)
    private Long efjavUbicacionActualId;

    @Column(name = "efjav_estado", nullable = false, length = 30)
    private String efjavEstado; // BORRADOR, ENVIADO, RECIBIDO, EN_PROCESO, OBSERVADO, RECHAZADO, ARCHIVADO, CONCLUIDO

    @Column(name = "efjav_fecha_envio")
    private LocalDateTime efjavFechaEnvio;

    @Column(name = "efjav_fecha_conclusion")
    private LocalDateTime efjavFechaConclusion;

    @Column(name = "efjav_firma_estudiante_id", nullable = false)
    private Long efjavFirmaEstudianteId;

    public EfjavTramiteEntity() {}

    // Getters y Setters
    public Long getEfjavId() { return efjavId; }
    public void setEfjavId(Long efjavId) { this.efjavId = efjavId; }

    public String getEfjavCodigoSeguimiento() { return efjavCodigoSeguimiento; }
    public void setEfjavCodigoSeguimiento(String efjavCodigoSeguimiento) { this.efjavCodigoSeguimiento = efjavCodigoSeguimiento; }

    public Long getEfjavEstudianteId() { return efjavEstudianteId; }
    public void setEfjavEstudianteId(Long efjavEstudianteId) { this.efjavEstudianteId = efjavEstudianteId; }

    public String getEfjavTipoTramite() { return efjavTipoTramite; }
    public void setEfjavTipoTramite(String efjavTipoTramite) { this.efjavTipoTramite = efjavTipoTramite; }

    public String getEfjavAsunto() { return efjavAsunto; }
    public void setEfjavAsunto(String efjavAsunto) { this.efjavAsunto = efjavAsunto; }

    public Long getEfjavInstanciaDestinoId() { return efjavInstanciaDestinoId; }
    public void setEfjavInstanciaDestinoId(Long efjavInstanciaDestinoId) { this.efjavInstanciaDestinoId = efjavInstanciaDestinoId; }

    public Long getEfjavUbicacionActualId() { return efjavUbicacionActualId; }
    public void setEfjavUbicacionActualId(Long efjavUbicacionActualId) { this.efjavUbicacionActualId = efjavUbicacionActualId; }

    public String getEfjavEstado() { return efjavEstado; }
    public void setEfjavEstado(String efjavEstado) { this.efjavEstado = efjavEstado; }

    public LocalDateTime getEfjavFechaEnvio() { return efjavFechaEnvio; }
    public void setEfjavFechaEnvio(LocalDateTime efjavFechaEnvio) { this.efjavFechaEnvio = efjavFechaEnvio; }

    public LocalDateTime getEfjavFechaConclusion() { return efjavFechaConclusion; }
    public void setEfjavFechaConclusion(LocalDateTime efjavFechaConclusion) { this.efjavFechaConclusion = efjavFechaConclusion; }

    public Long getEfjavFirmaEstudianteId() { return efjavFirmaEstudianteId; }
    public void setEfjavFirmaEstudianteId(Long efjavFirmaEstudianteId) { this.efjavFirmaEstudianteId = efjavFirmaEstudianteId; }
}
