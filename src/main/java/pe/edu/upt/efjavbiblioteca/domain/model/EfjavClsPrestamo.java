package pe.edu.upt.efjavbiblioteca.domain.model;

import java.time.LocalDateTime;

public class EfjavClsPrestamo {
    private Long efjavId;
    private String efjavCodigo;
    private Long efjavLibroId;
    private String efjavLibroTitulo;
    private Long efjavEstudianteId;
    private String efjavEstudianteNombre;
    private Long efjavBibliotecarioId;
    private LocalDateTime efjavFechaSolicitud;
    private LocalDateTime efjavFechaEntrega;
    private LocalDateTime efjavFechaLimiteDevolucion;
    private Long efjavFirmaEstudianteId;
    private Long efjavFirmaBibliotecarioId;
    private EfjavEnuEstadoPrestamo efjavEstado;

    public EfjavClsPrestamo() {
        this.efjavFechaSolicitud = LocalDateTime.now();
        this.efjavEstado = EfjavEnuEstadoPrestamo.SOLICITADO;
    }

    // Getters y Setters
    public Long getEfjavId() { return efjavId; }
    public void setEfjavId(Long efjavId) { this.efjavId = efjavId; }

    public String getEfjavCodigo() { return efjavCodigo; }
    public void setEfjavCodigo(String efjavCodigo) { this.efjavCodigo = efjavCodigo; }

    public Long getEfjavLibroId() { return efjavLibroId; }
    public void setEfjavLibroId(Long efjavLibroId) { this.efjavLibroId = efjavLibroId; }

    public String getEfjavLibroTitulo() { return efjavLibroTitulo; }
    public void setEfjavLibroTitulo(String efjavLibroTitulo) { this.efjavLibroTitulo = efjavLibroTitulo; }

    public Long getEfjavEstudianteId() { return efjavEstudianteId; }
    public void setEfjavEstudianteId(Long efjavEstudianteId) { this.efjavEstudianteId = efjavEstudianteId; }

    public String getEfjavEstudianteNombre() { return efjavEstudianteNombre; }
    public void setEfjavEstudianteNombre(String efjavEstudianteNombre) { this.efjavEstudianteNombre = efjavEstudianteNombre; }

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

    public EfjavEnuEstadoPrestamo getEfjavEstado() { return efjavEstado; }
    public void setEfjavEstado(EfjavEnuEstadoPrestamo efjavEstado) { this.efjavEstado = efjavEstado; }
}
