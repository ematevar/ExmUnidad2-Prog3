package pe.edu.upt.efjavbiblioteca.domain.model;

import java.time.LocalDateTime;

public class EfjavClsFirma {
    private Long efjavId;
    private Long efjavAutorId;
    private String efjavNombreFirmante;
    private String efjavRolFirmante;
    private String efjavReferenciaArchivo;
    private String efjavHashSha256;
    private String efjavContextoOperacion;
    private LocalDateTime efjavFechaFirma;

    public EfjavClsFirma() {
        this.efjavFechaFirma = LocalDateTime.now();
    }

    public EfjavClsFirma(Long efjavAutorId, String efjavNombreFirmante, String efjavRolFirmante, 
                         String efjavReferenciaArchivo, String efjavHashSha256, String efjavContextoOperacion) {
        this();
        this.efjavAutorId = efjavAutorId;
        this.efjavNombreFirmante = efjavNombreFirmante;
        this.efjavRolFirmante = efjavRolFirmante;
        this.efjavReferenciaArchivo = efjavReferenciaArchivo;
        this.efjavHashSha256 = efjavHashSha256;
        this.efjavContextoOperacion = efjavContextoOperacion;
    }

    // Getters y Setters
    public Long getEfjavId() { return efjavId; }
    public void setEfjavId(Long efjavId) { this.efjavId = efjavId; }

    public Long getEfjavAutorId() { return efjavAutorId; }
    public void setEfjavAutorId(Long efjavAutorId) { this.efjavAutorId = efjavAutorId; }

    public String getEfjavNombreFirmante() { return efjavNombreFirmante; }
    public void setEfjavNombreFirmante(String efjavNombreFirmante) { this.efjavNombreFirmante = efjavNombreFirmante; }

    public String getEfjavRolFirmante() { return efjavRolFirmante; }
    public void setEfjavRolFirmante(String efjavRolFirmante) { this.efjavRolFirmante = efjavRolFirmante; }

    public String getEfjavReferenciaArchivo() { return efjavReferenciaArchivo; }
    public void setEfjavReferenciaArchivo(String efjavReferenciaArchivo) { this.efjavReferenciaArchivo = efjavReferenciaArchivo; }

    public String getEfjavHashSha256() { return efjavHashSha256; }
    public void setEfjavHashSha256(String efjavHashSha256) { this.efjavHashSha256 = efjavHashSha256; }

    public String getEfjavContextoOperacion() { return efjavContextoOperacion; }
    public void setEfjavContextoOperacion(String efjavContextoOperacion) { this.efjavContextoOperacion = efjavContextoOperacion; }

    public LocalDateTime getEfjavFechaFirma() { return efjavFechaFirma; }
    public void setEfjavFechaFirma(LocalDateTime efjavFechaFirma) { this.efjavFechaFirma = efjavFechaFirma; }
}
