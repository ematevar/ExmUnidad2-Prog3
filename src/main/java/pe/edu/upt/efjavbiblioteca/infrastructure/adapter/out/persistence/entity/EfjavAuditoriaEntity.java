package pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "efjav_auditoria")
public class EfjavAuditoriaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "efjav_id")
    private Long efjavId;

    @Column(name = "efjav_usuario_codigo", nullable = false, length = 50)
    private String efjavUsuarioCodigo;

    @Column(name = "efjav_operacion", nullable = false, length = 100)
    private String efjavOperacion;

    @Column(name = "efjav_entidad", nullable = false, length = 100)
    private String efjavEntidad;

    @Column(name = "efjav_detalle", columnDefinition = "TEXT")
    private String efjavDetalle;

    @Column(name = "efjav_ip", length = 45)
    private String efjavIp;

    @Column(name = "efjav_equipo", length = 100)
    private String efjavEquipo;

    @Column(name = "efjav_resultado", nullable = false, length = 50)
    private String efjavResultado;

    @Column(name = "efjav_fecha_hora")
    private LocalDateTime efjavFechaHora;

    public EfjavAuditoriaEntity() {
        this.efjavFechaHora = LocalDateTime.now();
    }

    public EfjavAuditoriaEntity(String efjavUsuarioCodigo, String efjavOperacion, String efjavEntidad,
                                String efjavDetalle, String efjavIp, String efjavEquipo, String efjavResultado) {
        this();
        this.efjavUsuarioCodigo = efjavUsuarioCodigo;
        this.efjavOperacion = efjavOperacion;
        this.efjavEntidad = efjavEntidad;
        this.efjavDetalle = efjavDetalle;
        this.efjavIp = efjavIp;
        this.efjavEquipo = efjavEquipo;
        this.efjavResultado = efjavResultado;
    }

    // Getters y Setters
    public Long getEfjavId() { return efjavId; }
    public void setEfjavId(Long efjavId) { this.efjavId = efjavId; }

    public String getEfjavUsuarioCodigo() { return efjavUsuarioCodigo; }
    public void setEfjavUsuarioCodigo(String efjavUsuarioCodigo) { this.efjavUsuarioCodigo = efjavUsuarioCodigo; }

    public String getEfjavOperacion() { return efjavOperacion; }
    public void setEfjavOperacion(String efjavOperacion) { this.efjavOperacion = efjavOperacion; }

    public String getEfjavEntidad() { return efjavEntidad; }
    public void setEfjavEntidad(String efjavEntidad) { this.efjavEntidad = efjavEntidad; }

    public String getEfjavDetalle() { return efjavDetalle; }
    public void setEfjavDetalle(String efjavDetalle) { this.efjavDetalle = efjavDetalle; }

    public String getEfjavIp() { return efjavIp; }
    public void setEfjavIp(String efjavIp) { this.efjavIp = efjavIp; }

    public String getEfjavEquipo() { return efjavEquipo; }
    public void setEfjavEquipo(String efjavEquipo) { this.efjavEquipo = efjavEquipo; }

    public String getEfjavResultado() { return efjavResultado; }
    public void setEfjavResultado(String efjavResultado) { this.efjavResultado = efjavResultado; }

    public LocalDateTime getEfjavFechaHora() { return efjavFechaHora; }
    public void setEfjavFechaHora(LocalDateTime efjavFechaHora) { this.efjavFechaHora = efjavFechaHora; }
}
