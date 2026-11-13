package pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "efjav_prestamos")
public class EfjavPrestamoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "efjav_id")
    private Long efjavId;

    @Column(name = "efjav_codigo", nullable = false, unique = true, length = 50)
    private String efjavCodigo;

    @Column(name = "efjav_libro_id", nullable = false)
    private Long efjavLibroId;

    @Column(name = "efjav_estudiante_id", nullable = false)
    private Long efjavEstudianteId;

    @Column(name = "efjav_bibliotecario_id")
    private Long efjavBibliotecarioId;

    @Column(name = "efjav_fecha_solicitud")
    private LocalDateTime efjavFechaSolicitud;

    @Column(name = "efjav_fecha_entrega")
    private LocalDateTime efjavFechaEntrega;

    @Column(name = "efjav_fecha_limite_devolucion", nullable = false)
    private LocalDateTime efjavFechaLimiteDevolucion;

    @Column(name = "efjav_firma_estudiante_id")
    private Long efjavFirmaEstudianteId;

    @Column(name = "efjav_firma_bibliotecario_id")
    private Long efjavFirmaBibliotecarioId;

    @Column(name = "efjav_estado", length = 30)
    private String efjavEstado; // SOLICITADO, ACTIVO, DEVUELTO, VENCIDO, RECHAZADO

    public EfjavPrestamoEntity() {}

    // Getters y Setters
    public Long getEfjavId() { return efjavId; }
    public void setEfjavId(Long efjavId) { this.efjavId = efjavId; }

    public String getEfjavCodigo() { return efjavCodigo; }
    public void setEfjavCodigo(String efjavCodigo) { this.efjavCodigo = efjavCodigo; }

    public Long getEfjavLibroId() { return efjavLibroId; }
    public void setEfjavLibroId(Long efjavLibroId) { this.efjavLibroId = efjavLibroId; }

    public Long getEfjavEstudianteId() { return efjavEstudianteId; }
    public void setEfjavEstudianteId(Long efjavEstudianteId) { this.efjavEstudianteId = efjavEstudianteId; }

    public Long getEfjavBibliotecarioId() { return efjavBibliotecarioId; }
    public void setEfjavBibliotecarioId(Long efjavBibliotecarioId) { this.efjavBibliotecarioId = efjavBibliotecarioId; }

    public LocalDateTime getEfjavFechaSolicitud() { return efjavFechaSolicitud; }
    public void setEfjavFechaSolicitud(LocalDateTime efjavFechaSolicitud) { this.efjavFechaSolicitud = efjavFechaSolicitud; }

    public LocalDateTime getEfjavFechaEntrega() { return efjavFechaEntrega; }
    public void setEfjavFechaEntrega(LocalDateTime efjavFechaEntrega) { this.efjavFechaEntrega = efjavFechaEntrega; }

    public LocalDateTime getEfjavFechaLimiteDevolucion() { return efjavFechaLimiteDevolucion; }
    public void setEfjavFechaLimiteDevolucion(LocalDateTime efjavFechaLimiteDevolucion) { this.efjavFechaLimiteDevolucion = efjavFechaLimiteDevolucion; }

    public Long getEfjavFirmaEstudianteId() { return efjavFirmaEstudianteId; }
    public void setEfjavFirmaEstudianteId(Long efjavFirmaEstudianteId) { this.efjavFirmaEstudianteId = efjavFirmaEstudianteId; }

    public Long getEfjavFirmaBibliotecarioId() { return efjavFirmaBibliotecarioId; }
    public void setEfjavFirmaBibliotecarioId(Long efjavFirmaBibliotecarioId) { this.efjavFirmaBibliotecarioId = efjavFirmaBibliotecarioId; }

    public String getEfjavEstado() { return efjavEstado; }
    public void setEfjavEstado(String efjavEstado) { this.efjavEstado = efjavEstado; }
}
