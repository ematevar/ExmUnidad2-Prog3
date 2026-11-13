package pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "efjav_sesiones")
public class EfjavSesionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "efjav_id")
    private Long efjavId;

    @Column(name = "efjav_usuario_id", nullable = false)
    private Long efjavUsuarioId;

    @Column(name = "efjav_session_token", nullable = false, unique = true, length = 255)
    private String efjavSessionToken;

    @Column(name = "efjav_identificador_equipo", length = 100)
    private String efjavIdentificadorEquipo;

    @Column(name = "efjav_ip", nullable = false, length = 45)
    private String efjavIp;

    @Column(name = "efjav_user_agent", length = 255)
    private String efjavUserAgent;

    @Column(name = "efjav_inicio")
    private LocalDateTime efjavInicio;

    @Column(name = "efjav_ultima_actividad")
    private LocalDateTime efjavUltimaActividad;

    @Column(name = "efjav_expiracion", nullable = false)
    private LocalDateTime efjavExpiracion;

    @Column(name = "efjav_estado", length = 20)
    private String efjavEstado; // ACTIVA, EXPIRADA, CERRADA, BLOQUEADA

    public EfjavSesionEntity() {}

    // Getters y Setters
    public Long getEfjavId() { return efjavId; }
    public void setEfjavId(Long efjavId) { this.efjavId = efjavId; }

    public Long getEfjavUsuarioId() { return efjavUsuarioId; }
    public void setEfjavUsuarioId(Long efjavUsuarioId) { this.efjavUsuarioId = efjavUsuarioId; }

    public String getEfjavSessionToken() { return efjavSessionToken; }
    public void setEfjavSessionToken(String efjavSessionToken) { this.efjavSessionToken = efjavSessionToken; }

    public String getEfjavIdentificadorEquipo() { return efjavIdentificadorEquipo; }
    public void setEfjavIdentificadorEquipo(String efjavIdentificadorEquipo) { this.efjavIdentificadorEquipo = efjavIdentificadorEquipo; }

    public String getEfjavIp() { return efjavIp; }
    public void setEfjavIp(String efjavIp) { this.efjavIp = efjavIp; }

    public String getEfjavUserAgent() { return efjavUserAgent; }
    public void setEfjavUserAgent(String efjavUserAgent) { this.efjavUserAgent = efjavUserAgent; }

    public LocalDateTime getEfjavInicio() { return efjavInicio; }
    public void setEfjavInicio(LocalDateTime efjavInicio) { this.efjavInicio = efjavInicio; }

    public LocalDateTime getEfjavUltimaActividad() { return efjavUltimaActividad; }
    public void setEfjavUltimaActividad(LocalDateTime efjavUltimaActividad) { this.efjavUltimaActividad = efjavUltimaActividad; }

    public LocalDateTime getEfjavExpiracion() { return efjavExpiracion; }
    public void setEfjavExpiracion(LocalDateTime efjavExpiracion) { this.efjavExpiracion = efjavExpiracion; }

    public String getEfjavEstado() { return efjavEstado; }
    public void setEfjavEstado(String efjavEstado) { this.efjavEstado = efjavEstado; }
}
