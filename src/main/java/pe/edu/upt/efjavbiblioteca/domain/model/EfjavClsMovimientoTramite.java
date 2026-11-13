package pe.edu.upt.efjavbiblioteca.domain.model;

import java.time.LocalDateTime;

public class EfjavClsMovimientoTramite {
    private Long efjavId;
    private Long efjavTramiteId;
    private Long efjavInstanciaOrigenId;
    private String efjavInstanciaOrigenNombre;
    private Long efjavInstanciaDestinoId;
    private String efjavInstanciaDestinoNombre;
    private Long efjavResponsableId;
    private String efjavResponsableNombre;
    private String efjavEstadoResultante;
    private Long efjavFirmaResponsableId;
    private String efjavObservacion;
    private LocalDateTime efjavFechaMovimiento;

    public EfjavClsMovimientoTramite() {
        this.efjavFechaMovimiento = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getEfjavId() { return efjavId; }
    public void setEfjavId(Long efjavId) { this.efjavId = efjavId; }

    public Long getEfjavTramiteId() { return efjavTramiteId; }
    public void setEfjavTramiteId(Long efjavTramiteId) { this.efjavTramiteId = efjavTramiteId; }

    public Long getEfjavInstanciaOrigenId() { return efjavInstanciaOrigenId; }
    public void setEfjavInstanciaOrigenId(Long efjavInstanciaOrigenId) { this.efjavInstanciaOrigenId = efjavInstanciaOrigenId; }

    public String getEfjavInstanciaOrigenNombre() { return efjavInstanciaOrigenNombre; }
    public void setEfjavInstanciaOrigenNombre(String efjavInstanciaOrigenNombre) { this.efjavInstanciaOrigenNombre = efjavInstanciaOrigenNombre; }

    public Long getEfjavInstanciaDestinoId() { return efjavInstanciaDestinoId; }
    public void setEfjavInstanciaDestinoId(Long efjavInstanciaDestinoId) { this.efjavInstanciaDestinoId = efjavInstanciaDestinoId; }

    public String getEfjavInstanciaDestinoNombre() { return efjavInstanciaDestinoNombre; }
    public void setEfjavInstanciaDestinoNombre(String efjavInstanciaDestinoNombre) { this.efjavInstanciaDestinoNombre = efjavInstanciaDestinoNombre; }

    public Long getEfjavResponsableId() { return efjavResponsableId; }
    public void setEfjavResponsableId(Long efjavResponsableId) { this.efjavResponsableId = efjavResponsableId; }

    public String getEfjavResponsableNombre() { return efjavResponsableNombre; }
    public void setEfjavResponsableNombre(String efjavResponsableNombre) { this.efjavResponsableNombre = efjavResponsableNombre; }

    public String getEfjavEstadoResultante() { return efjavEstadoResultante; }
    public void setEfjavEstadoResultante(String efjavEstadoResultante) { this.efjavEstadoResultante = efjavEstadoResultante; }

    public Long getEfjavFirmaResponsableId() { return efjavFirmaResponsableId; }
    public void setEfjavFirmaResponsableId(Long efjavFirmaResponsableId) { this.efjavFirmaResponsableId = efjavFirmaResponsableId; }

    public String getEfjavObservacion() { return efjavObservacion; }
    public void setEfjavObservacion(String efjavObservacion) { this.efjavObservacion = efjavObservacion; }

    public LocalDateTime getEfjavFechaMovimiento() { return efjavFechaMovimiento; }
    public void setEfjavFechaMovimiento(LocalDateTime efjavFechaMovimiento) { this.efjavFechaMovimiento = efjavFechaMovimiento; }
}
