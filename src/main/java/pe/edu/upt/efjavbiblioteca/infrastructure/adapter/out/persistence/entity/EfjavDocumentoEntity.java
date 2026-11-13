package pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "efjav_documentos")
public class EfjavDocumentoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "efjav_id")
    private Long efjavId;

    @Column(name = "efjav_tramite_id", nullable = false)
    private Long efjavTramiteId;

    @Column(name = "efjav_nombre_original", nullable = false, length = 255)
    private String efjavNombreOriginal;

    @Column(name = "efjav_ruta_almacenamiento", nullable = false, length = 255)
    private String efjavRutaAlmacenamiento;

    @Column(name = "efjav_tipo_documento", nullable = false, length = 50)
    private String efjavTipoDocumento; // SOLICITUD_PDF, RESPUESTA_PDF, ANEXO

    @Column(name = "efjav_version")
    private Integer efjavVersion;

    @Column(name = "efjav_hash_sha256", nullable = false, length = 64)
    private String efjavHashSha256;

    @Column(name = "efjav_tamanio_bytes", nullable = false)
    private Long efjavTamanioBytes;

    @Column(name = "efjav_fecha_subida")
    private LocalDateTime efjavFechaSubida;

    public EfjavDocumentoEntity() {
        this.efjavFechaSubida = LocalDateTime.now();
        this.efjavVersion = 1;
    }

    // Getters y Setters
    public Long getEfjavId() { return efjavId; }
    public void setEfjavId(Long efjavId) { this.efjavId = efjavId; }

    public Long getEfjavTramiteId() { return efjavTramiteId; }
    public void setEfjavTramiteId(Long efjavTramiteId) { this.efjavTramiteId = efjavTramiteId; }

    public String getEfjavNombreOriginal() { return efjavNombreOriginal; }
    public void setEfjavNombreOriginal(String efjavNombreOriginal) { this.efjavNombreOriginal = efjavNombreOriginal; }

    public String getEfjavRutaAlmacenamiento() { return efjavRutaAlmacenamiento; }
    public void setEfjavRutaAlmacenamiento(String efjavRutaAlmacenamiento) { this.efjavRutaAlmacenamiento = efjavRutaAlmacenamiento; }

    public String getEfjavTipoDocumento() { return efjavTipoDocumento; }
    public void setEfjavTipoDocumento(String efjavTipoDocumento) { this.efjavTipoDocumento = efjavTipoDocumento; }

    public Integer getEfjavVersion() { return efjavVersion; }
    public void setEfjavVersion(Integer efjavVersion) { this.efjavVersion = efjavVersion; }

    public String getEfjavHashSha256() { return efjavHashSha256; }
    public void setEfjavHashSha256(String efjavHashSha256) { this.efjavHashSha256 = efjavHashSha256; }

    public Long getEfjavTamanioBytes() { return efjavTamanioBytes; }
    public void setEfjavTamanioBytes(Long efjavTamanioBytes) { this.efjavTamanioBytes = efjavTamanioBytes; }

    public LocalDateTime getEfjavFechaSubida() { return efjavFechaSubida; }
    public void setEfjavFechaSubida(LocalDateTime efjavFechaSubida) { this.efjavFechaSubida = efjavFechaSubida; }
}
