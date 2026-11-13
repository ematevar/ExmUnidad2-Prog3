package pe.edu.upt.efjavbiblioteca.domain.model;

import java.time.LocalDateTime;

public class EfjavClsEquipoAutorizado {
    private Long efjavId;
    private String efjavIdentificadorEquipo;
    private String efjavNombreEquipo;
    private String efjavOficina;
    private String efjavIpAsignada;
    private Long efjavUsuarioResponsableId;
    private String efjavEstado;
    private LocalDateTime efjavFechaEnrolamiento;

    public EfjavClsEquipoAutorizado() {
        this.efjavEstado = "AUTORIZADO";
        this.efjavFechaEnrolamiento = LocalDateTime.now();
    }

    public boolean efjavFncEsAutorizadoPara(String oficinaRequerida) {
        if (!"AUTORIZADO".equalsIgnoreCase(efjavEstado)) return false;
        return efjavOficina != null && efjavOficina.equalsIgnoreCase(oficinaRequerida);
    }

    // Getters y Setters
    public Long getEfjavId() { return efjavId; }
    public void setEfjavId(Long efjavId) { this.efjavId = efjavId; }

    public String getEfjavIdentificadorEquipo() { return efjavIdentificadorEquipo; }
    public void setEfjavIdentificadorEquipo(String efjavIdentificadorEquipo) { this.efjavIdentificadorEquipo = efjavIdentificadorEquipo; }

    public String getEfjavNombreEquipo() { return efjavNombreEquipo; }
    public void setEfjavNombreEquipo(String efjavNombreEquipo) { this.efjavNombreEquipo = efjavNombreEquipo; }

    public String getEfjavOficina() { return efjavOficina; }
    public void setEfjavOficina(String efjavOficina) { this.efjavOficina = efjavOficina; }

    public String getEfjavIpAsignada() { return efjavIpAsignada; }
    public void setEfjavIpAsignada(String efjavIpAsignada) { this.efjavIpAsignada = efjavIpAsignada; }

    public Long getEfjavUsuarioResponsableId() { return efjavUsuarioResponsableId; }
    public void setEfjavUsuarioResponsableId(Long efjavUsuarioResponsableId) { this.efjavUsuarioResponsableId = efjavUsuarioResponsableId; }

    public String getEfjavEstado() { return efjavEstado; }
    public void setEfjavEstado(String efjavEstado) { this.efjavEstado = efjavEstado; }

    public LocalDateTime getEfjavFechaEnrolamiento() { return efjavFechaEnrolamiento; }
    public void setEfjavFechaEnrolamiento(LocalDateTime efjavFechaEnrolamiento) { this.efjavFechaEnrolamiento = efjavFechaEnrolamiento; }
}
