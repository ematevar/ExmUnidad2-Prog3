package pe.edu.upt.efjavbiblioteca.domain.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class EfjavClsUsuario {
    public static final int EFJAV_CTE_MAX_INTENTOS_LOGIN = 5;

    private Long efjavId;
    private String efjavCodigo;
    private String efjavNombres;
    private String efjavCorreo;
    private String efjavPasswordHash;
    private String efjavTotpSecret;
    private Boolean efjavTotpActivo;
    private String efjavOtpActual;
    private LocalDateTime efjavOtpExpiracion;
    private Integer efjavIntentosFallidos;
    private Boolean efjavBloqueado;
    private String efjavEstado;
    private LocalDateTime efjavFechaCreacion;
    private Set<EfjavEnuRol> efjavRoles = new HashSet<>();

    public EfjavClsUsuario() {
        this.efjavIntentosFallidos = 0;
        this.efjavBloqueado = false;
        this.efjavTotpActivo = true;
        this.efjavEstado = "ACTIVO";
    }

    public EfjavClsUsuario(Long efjavId, String efjavCodigo, String efjavNombres, String efjavCorreo) {
        this();
        this.efjavId = efjavId;
        this.efjavCodigo = efjavCodigo;
        this.efjavNombres = efjavNombres;
        this.efjavCorreo = efjavCorreo;
    }

    public boolean efjavFncTieneRol(EfjavEnuRol rol) {
        return efjavRoles != null && efjavRoles.contains(rol);
    }

    // Getters y Setters
    public Long getEfjavId() { return efjavId; }
    public void setEfjavId(Long efjavId) { this.efjavId = efjavId; }

    public String getEfjavCodigo() { return efjavCodigo; }
    public void setEfjavCodigo(String efjavCodigo) { this.efjavCodigo = efjavCodigo; }

    public String getEfjavNombres() { return efjavNombres; }
    public void setEfjavNombres(String efjavNombres) { this.efjavNombres = efjavNombres; }

    public String getEfjavCorreo() { return efjavCorreo; }
    public void setEfjavCorreo(String efjavCorreo) { this.efjavCorreo = efjavCorreo; }

    public String getEfjavPasswordHash() { return efjavPasswordHash; }
    public void setEfjavPasswordHash(String efjavPasswordHash) { this.efjavPasswordHash = efjavPasswordHash; }

    public String getEfjavTotpSecret() { return efjavTotpSecret; }
    public void setEfjavTotpSecret(String efjavTotpSecret) { this.efjavTotpSecret = efjavTotpSecret; }

    public Boolean getEfjavTotpActivo() { return efjavTotpActivo; }
    public void setEfjavTotpActivo(Boolean efjavTotpActivo) { this.efjavTotpActivo = efjavTotpActivo; }

    public String getEfjavOtpActual() { return efjavOtpActual; }
    public void setEfjavOtpActual(String efjavOtpActual) { this.efjavOtpActual = efjavOtpActual; }

    public LocalDateTime getEfjavOtpExpiracion() { return efjavOtpExpiracion; }
    public void setEfjavOtpExpiracion(LocalDateTime efjavOtpExpiracion) { this.efjavOtpExpiracion = efjavOtpExpiracion; }

    public Integer getEfjavIntentosFallidos() { return efjavIntentosFallidos; }
    public void setEfjavIntentosFallidos(Integer efjavIntentosFallidos) { this.efjavIntentosFallidos = efjavIntentosFallidos; }

    public Boolean getEfjavBloqueado() { return efjavBloqueado; }
    public void setEfjavBloqueado(Boolean efjavBloqueado) { this.efjavBloqueado = efjavBloqueado; }

    public String getEfjavEstado() { return efjavEstado; }
    public void setEfjavEstado(String efjavEstado) { this.efjavEstado = efjavEstado; }

    public LocalDateTime getEfjavFechaCreacion() { return efjavFechaCreacion; }
    public void setEfjavFechaCreacion(LocalDateTime efjavFechaCreacion) { this.efjavFechaCreacion = efjavFechaCreacion; }

    public Set<EfjavEnuRol> getEfjavRoles() { return efjavRoles; }
    public void setEfjavRoles(Set<EfjavEnuRol> efjavRoles) { this.efjavRoles = efjavRoles; }
}
