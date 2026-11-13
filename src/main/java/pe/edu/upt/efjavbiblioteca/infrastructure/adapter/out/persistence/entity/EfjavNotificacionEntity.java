package pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "efjav_notificaciones")
public class EfjavNotificacionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "efjav_id")
    private Long efjavId;

    @Column(name = "efjav_destinatario_id", nullable = false)
    private Long efjavDestinatarioId;

    @Column(name = "efjav_canal", nullable = false, length = 30)
    private String efjavCanal;

    @Column(name = "efjav_titulo", nullable = false, length = 200)
    private String efjavTitulo;

    @Column(name = "efjav_contenido", nullable = false, columnDefinition = "TEXT")
    private String efjavContenido;

    @Column(name = "efjav_tipo", nullable = false, length = 50)
    private String efjavTipo;

    @Column(name = "efjav_estado_envio", length = 30)
    private String efjavEstadoEnvio;

    @Column(name = "efjav_leida")
    private Boolean efjavLeida;

    @Column(name = "efjav_fecha_creacion")
    private LocalDateTime efjavFechaCreacion;

    public EfjavNotificacionEntity() {
        this.efjavFechaCreacion = LocalDateTime.now();
        this.efjavLeida = false;
        this.efjavEstadoEnvio = "ENVIADO";
    }

    // Getters y Setters
    public Long getEfjavId() { return efjavId; }
    public void setEfjavId(Long efjavId) { this.efjavId = efjavId; }

    public Long getEfjavDestinatarioId() { return efjavDestinatarioId; }
    public void setEfjavDestinatarioId(Long efjavDestinatarioId) { this.efjavDestinatarioId = efjavDestinatarioId; }

    public String getEfjavCanal() { return efjavCanal; }
    public void setEfjavCanal(String efjavCanal) { this.efjavCanal = efjavCanal; }

    public String getEfjavTitulo() { return efjavTitulo; }
    public void setEfjavTitulo(String efjavTitulo) { this.efjavTitulo = efjavTitulo; }

    public String getEfjavContenido() { return efjavContenido; }
    public void setEfjavContenido(String efjavContenido) { this.efjavContenido = efjavContenido; }

    public String getEfjavTipo() { return efjavTipo; }
    public void setEfjavTipo(String efjavTipo) { this.efjavTipo = efjavTipo; }

    public String getEfjavEstadoEnvio() { return efjavEstadoEnvio; }
    public void setEfjavEstadoEnvio(String efjavEstadoEnvio) { this.efjavEstadoEnvio = efjavEstadoEnvio; }

    public Boolean getEfjavLeida() { return efjavLeida; }
    public void setEfjavLeida(Boolean efjavLeida) { this.efjavLeida = efjavLeida; }

    public LocalDateTime getEfjavFechaCreacion() { return efjavFechaCreacion; }
    public void setEfjavFechaCreacion(LocalDateTime efjavFechaCreacion) { this.efjavFechaCreacion = efjavFechaCreacion; }
}
