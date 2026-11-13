package pe.edu.upt.efjavbiblioteca.domain.model;

import java.time.LocalDateTime;

public class EfjavClsTramite {
    private Long efjavId;
    private String efjavCodigoSeguimiento;
    private Long efjavEstudianteId;
    private String efjavEstudianteNombre;
    private String efjavTipoTramite;
    private String efjavAsunto;
    private Long efjavInstanciaDestinoId;
    private String efjavInstanciaDestinoNombre;
    private Long efjavUbicacionActualId;
    private String efjavUbicacionActualNombre;
    private EfjavEnuEstadoTramite efjavEstado;
    private LocalDateTime efjavFechaEnvio;
    private LocalDateTime efjavFechaConclusion;
    private Long efjavFirmaEstudianteId;

    public EfjavClsTramite() {
        this.efjavFechaEnvio = LocalDateTime.now();
        this.efjavEstado = EfjavEnuEstadoTramite.ENVIADO;
    }

    public boolean efjavFncEsCulminado() {
        return efjavEstado == EfjavEnuEstadoTramite.CONCLUIDO || efjavEstado == EfjavEnuEstadoTramite.ARCHIVADO;
    }

    // Getters y Setters
    public Long getEfjavId() { return efjavId; }
    public void setEfjavId(Long efjavId) { this.efjavId = efjavId; }

    public String getEfjavCodigoSeguimiento() { return efjavCodigoSeguimiento; }
    public void setEfjavCodigoSeguimiento(String efjavCodigoSeguimiento) { this.efjavCodigoSeguimiento = efjavCodigoSeguimiento; }

    public Long getEfjavEstudianteId() { return efjavEstudianteId; }
    public void setEfjavEstudianteId(Long efjavEstudianteId) { this.efjavEstudianteId = efjavEstudianteId; }

    public String getEfjavEstudianteNombre() { return efjavEstudianteNombre; }
    public void setEfjavEstudianteNombre(String efjavEstudianteNombre) { this.efjavEstudianteNombre = efjavEstudianteNombre; }

    public String getEfjavTipoTramite() { return efjavTipoTramite; }
    public void setEfjavTipoTramite(String efjavTipoTramite) { this.efjavTipoTramite = efjavTipoTramite; }

    public String getEfjavAsunto() { return efjavAsunto; }
    public void setEfjavAsunto(String efjavAsunto) { this.efjavAsunto = efjavAsunto; }

    public Long getEfjavInstanciaDestinoId() { return efjavInstanciaDestinoId; }
    public void setEfjavInstanciaDestinoId(Long efjavInstanciaDestinoId) { this.efjavInstanciaDestinoId = efjavInstanciaDestinoId; }

    public String getEfjavInstanciaDestinoNombre() { return efjavInstanciaDestinoNombre; }
    public void setEfjavInstanciaDestinoNombre(String efjavInstanciaDestinoNombre) { this.efjavInstanciaDestinoNombre = efjavInstanciaDestinoNombre; }

    public Long getEfjavUbicacionActualId() { return efjavUbicacionActualId; }
    public void setEfjavUbicacionActualId(Long efjavUbicacionActualId) { this.efjavUbicacionActualId = efjavUbicacionActualId; }

    public String getEfjavUbicacionActualNombre() { return efjavUbicacionActualNombre; }
    public void setEfjavUbicacionActualNombre(String efjavUbicacionActualNombre) { this.efjavUbicacionActualNombre = efjavUbicacionActualNombre; }

    public EfjavEnuEstadoTramite getEfjavEstado() { return efjavEstado; }
    public void setEfjavEstado(EfjavEnuEstadoTramite efjavEstado) { this.efjavEstado = efjavEstado; }

    public LocalDateTime getEfjavFechaEnvio() { return efjavFechaEnvio; }
    public void setEfjavFechaEnvio(LocalDateTime efjavFechaEnvio) { this.efjavFechaEnvio = efjavFechaEnvio; }

    public LocalDateTime getEfjavFechaConclusion() { return efjavFechaConclusion; }
    public void setEfjavFechaConclusion(LocalDateTime efjavFechaConclusion) { this.efjavFechaConclusion = efjavFechaConclusion; }

    public Long getEfjavFirmaEstudianteId() { return efjavFirmaEstudianteId; }
    public void setEfjavFirmaEstudianteId(Long efjavFirmaEstudianteId) { this.efjavFirmaEstudianteId = efjavFirmaEstudianteId; }
}
