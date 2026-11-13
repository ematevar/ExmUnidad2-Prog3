package pe.edu.upt.efjavbiblioteca.infrastructure.storage;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.upt.efjavbiblioteca.domain.model.EfjavClsFirma;
import pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.entity.EfjavFirmaEntity;
import pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.repository.EfjavFirmaJpaRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Component
public class EfjavStorageService {

    @Value("${efjav.storage.location:./uploads}")
    private String efjavStorageLocation;

    private final EfjavFirmaJpaRepository efjavFirmaJpaRepository;

    public EfjavStorageService(EfjavFirmaJpaRepository efjavFirmaJpaRepository) {
        this.efjavFirmaJpaRepository = efjavFirmaJpaRepository;
    }

    public EfjavFirmaEntity efjavMtdGuardarFirmaBase64(Long autorId, String nombreFirmante, String rolFirmante, 
                                                      String base64Data, String contexto) {
        try {
            Path dirPath = Paths.get(efjavStorageLocation, "firmas");
            Files.createDirectories(dirPath);

            String pureBase64 = base64Data != null ? base64Data.trim() : "";
            if (pureBase64.contains(",")) {
                pureBase64 = pureBase64.split(",")[1].trim();
            }

            // Normalizar saltos de línea y espacios
            pureBase64 = pureBase64.replaceAll("\\s+", "");

            byte[] bytes;
            // Si no es un Base64 válido (ejemplo cuando se envía texto plano por omisión en formularios), usar sus bytes
            try {
                // Intentar decodificar URL-safe o estándar
                if (pureBase64.contains("-") || pureBase64.contains("_")) {
                    bytes = Base64.getUrlDecoder().decode(pureBase64);
                } else {
                    bytes = Base64.getDecoder().decode(pureBase64);
                }
            } catch (IllegalArgumentException e) {
                // Fallback robusto: Si venía un texto simulado como 'FIRMA_DIGITALIZADA_DECANATO_BASE64'
                bytes = pureBase64.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            }
            String hashSha256 = DigestUtils.sha256Hex(bytes);

            String fileName = "firma_" + UUID.randomUUID() + ".png";
            Path filePath = dirPath.resolve(fileName);
            Files.write(filePath, bytes);

            EfjavFirmaEntity firma = new EfjavFirmaEntity();
            firma.setEfjavAutorId(autorId);
            firma.setEfjavNombreFirmante(nombreFirmante);
            firma.setEfjavRolFirmante(rolFirmante);
            firma.setEfjavReferenciaArchivo(filePath.toString());
            firma.setEfjavHashSha256(hashSha256);
            firma.setEfjavContextoOperacion(contexto);
            firma.setEfjavFechaFirma(LocalDateTime.now());

            return efjavFirmaJpaRepository.save(firma);
        } catch (IOException e) {
            throw new RuntimeException("Error al almacenar la firma digitalizada", e);
        }
    }

    public String efjavMtdGuardarArchivoPdf(MultipartFile file, String subcarpeta) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("El archivo es obligatorio.");
        }

        // Validación de tipo MIME y extensión
        String contentType = file.getContentType();
        String originalFilename = file.getOriginalFilename();
        if (contentType == null || !contentType.equalsIgnoreCase("application/pdf") 
                || originalFilename == null || !originalFilename.toLowerCase().endsWith(".pdf")) {
            throw new IllegalArgumentException("Formato no válido: Debe ser un documento PDF.");
        }

        // Validación de cabecera mágica (%PDF-)
        byte[] bytes = file.getBytes();
        if (bytes.length < 5 || bytes[0] != '%' || bytes[1] != 'P' || bytes[2] != 'D' || bytes[3] != 'F' || bytes[4] != '-') {
            throw new IllegalArgumentException("El archivo no corresponde a un formato PDF institucional válido.");
        }

        Path dirPath = Paths.get(efjavStorageLocation, subcarpeta);
        Files.createDirectories(dirPath);

        String fileName = "doc_" + UUID.randomUUID() + ".pdf";
        Path filePath = dirPath.resolve(fileName);
        Files.write(filePath, bytes);

        return filePath.toString();
    }
}
