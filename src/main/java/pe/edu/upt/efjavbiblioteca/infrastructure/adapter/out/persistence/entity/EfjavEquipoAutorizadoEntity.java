package pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "efjav_equipos_autorizados")
public class EfjavEquipoAutorizadoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "efjav_id")
    private Long efjavId;

    @Column(name = "efjav_identificador_equipo", nullable = false, unique = true, length = 100)
    private String efjavIdentificadorEquipo;

    @Column(name = "efjav_nombre_equipo", nullable = false, length = 100)
    private String efjavNombreEquipo;

    @Column(name = "efjav_oficina", nullable = false, length = 100)
    private String efjavOficina;

    @Column(name = "efjav_ip_asignada", length = 45)
    private String efjavIpAsignada;

    @Column(name = "efjav_usuario_responsable_id")
    private Long efjavUsuarioResponsableId;

    @Column(name = "efjav_estado", length = 20)
    private String efjavEstado;

    @Column(name = "efjav_fecha_enrolamiento")
    private LocalDateTime efjavFechaEnrolamiento;

    public EfjavEquipoAutorizadoEntity() {}

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
