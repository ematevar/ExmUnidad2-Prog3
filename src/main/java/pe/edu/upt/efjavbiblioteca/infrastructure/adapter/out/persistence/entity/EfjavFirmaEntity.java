package pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "efjav_firmas")
public class EfjavFirmaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "efjav_id")
    private Long efjavId;

    @Column(name = "efjav_autor_id", nullable = false)
    private Long efjavAutorId;

    @Column(name = "efjav_nombre_firmante", nullable = false, length = 150)
    private String efjavNombreFirmante;

    @Column(name = "efjav_rol_firmante", nullable = false, length = 50)
    private String efjavRolFirmante;

    @Column(name = "efjav_referencia_archivo", nullable = false, length = 255)
    private String efjavReferenciaArchivo;

    @Column(name = "efjav_hash_sha256", nullable = false, length = 64)
    private String efjavHashSha256;

    @Column(name = "efjav_contexto_operacion", nullable = false, length = 100)
    private String efjavContextoOperacion;

    @Column(name = "efjav_fecha_firma")
    private LocalDateTime efjavFechaFirma;

    public EfjavFirmaEntity() {
        this.efjavFechaFirma = LocalDateTime.now();
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
