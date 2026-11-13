package pe.edu.upt.efjavbiblioteca.domain.model;

import java.time.LocalDateTime;

public class EfjavClsNotificacion {
    private Long efjavId;
    private Long efjavDestinatarioId;
    private String efjavCanal; // SISTEMA, CORREO, AMBOS
    private String efjavTitulo;
    private String efjavContenido;
    private String efjavTipo; // RECORDATORIO_VENCIMIENTO, INICIO_SESION, ESTADO_TRAMITE
    private String efjavEstadoEnvio; // ENVIADO, ENTREGADO, FALLIDO
    private Boolean efjavLeida;
    private LocalDateTime efjavFechaCreacion;
    private LocalDateTime efjavFechaLectura;

    public EfjavClsNotificacion() {
        this.efjavCanal = "SISTEMA";
        this.efjavEstadoEnvio = "ENVIADO";
        this.efjavLeida = false;
        this.efjavFechaCreacion = LocalDateTime.now();
    }

    public void efjavMtdMarcarLeida() {
        this.efjavLeida = true;
        this.efjavFechaLectura = LocalDateTime.now();
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

    public LocalDateTime getEfjavFechaLectura() { return efjavFechaLectura; }
    public void setEfjavFechaLectura(LocalDateTime efjavFechaLectura) { this.efjavFechaLectura = efjavFechaLectura; }
}
