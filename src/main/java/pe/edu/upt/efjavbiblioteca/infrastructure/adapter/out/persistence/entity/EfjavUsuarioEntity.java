package pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "efjav_usuarios")
public class EfjavUsuarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "efjav_id")
    private Long efjavId;

    @Column(name = "efjav_codigo", nullable = false, unique = true, length = 50)
    private String efjavCodigo;

    @Column(name = "efjav_nombres", nullable = false, length = 150)
    private String efjavNombres;

    @Column(name = "efjav_correo", nullable = false, unique = true, length = 100)
    private String efjavCorreo;

    @Column(name = "efjav_password_hash", nullable = false, length = 255)
    private String efjavPasswordHash;

    @Column(name = "efjav_totp_secret", length = 64)
    private String efjavTotpSecret;

    @Column(name = "efjav_totp_activo")
    private Boolean efjavTotpActivo;

    @Column(name = "efjav_otp_actual", length = 10)
    private String efjavOtpActual;

    @Column(name = "efjav_otp_expiracion")
    private LocalDateTime efjavOtpExpiracion;

    @Column(name = "efjav_intentos_fallidos")
    private Integer efjavIntentosFallidos;

    @Column(name = "efjav_bloqueado")
    private Boolean efjavBloqueado;

    @Column(name = "efjav_estado", length = 20)
    private String efjavEstado;

    @Column(name = "efjav_fecha_creacion")
    private LocalDateTime efjavFechaCreacion;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "efjav_usuarios_roles",
        joinColumns = @JoinColumn(name = "efjav_usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "efjav_rol_id")
    )
    private Set<EfjavRolEntity> efjavRoles = new HashSet<>();

    public EfjavUsuarioEntity() {}

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

    public Set<EfjavRolEntity> getEfjavRoles() { return efjavRoles; }
    public void setEfjavRoles(Set<EfjavRolEntity> efjavRoles) { this.efjavRoles = efjavRoles; }
}
