package pe.edu.upt.efjavbiblioteca.infrastructure.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import pe.edu.upt.efjavbiblioteca.domain.model.EfjavClsEquipoAutorizado;
import pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.entity.EfjavEquipoAutorizadoEntity;
import pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.repository.EfjavEquipoAutorizadoJpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
public class EfjavDispositivoSecurityService {

    public static final String EFJAV_DEVICE_COOKIE_NAME = "EFJAV_DEVICE_TOKEN";
    private final EfjavEquipoAutorizadoJpaRepository efjavEquipoAutorizadoJpaRepository;

    public EfjavDispositivoSecurityService(EfjavEquipoAutorizadoJpaRepository efjavEquipoAutorizadoJpaRepository) {
        this.efjavEquipoAutorizadoJpaRepository = efjavEquipoAutorizadoJpaRepository;
    }

    /**
     * Obtiene el identificador criptográfico del equipo actual desde una cookie segura HttpOnly.
     * Si no existe, genera uno nuevo persistente para este navegador/equipo físico.
     */
    public String efjavMtdObtenerOGenerarTokenEquipo(HttpServletRequest request, HttpServletResponse response) {
        if (request.getCookies() != null) {
            for (Cookie c : request.getCookies()) {
                if (EFJAV_DEVICE_COOKIE_NAME.equals(c.getName()) && c.getValue() != null && !c.getValue().trim().isEmpty()) {
                    return c.getValue().trim();
                }
            }
        }

        // Generar nuevo token único de enrolamiento para esta máquina
        String nuevoToken = "PC-" + UUID.randomUUID().toString();
        Cookie deviceCookie = new Cookie(EFJAV_DEVICE_COOKIE_NAME, nuevoToken);
        deviceCookie.setPath("/");
        deviceCookie.setMaxAge(365 * 24 * 60 * 60); // 1 año de persistencia
        deviceCookie.setHttpOnly(true);
        response.addCookie(deviceCookie);
        return nuevoToken;
    }

    /**
     * Verifica en la base de datos si el token del equipo físico está enrolado y autorizado para la oficina requerida.
     */
    public boolean efjavFncEsEquipoAutorizadoParaOficina(String deviceToken, String oficinaRequerida) {
        if (deviceToken == null || oficinaRequerida == null) return false;
        Optional<EfjavEquipoAutorizadoEntity> opt = efjavEquipoAutorizadoJpaRepository.findByEfjavIdentificadorEquipo(deviceToken);
        if (opt.isEmpty()) return false;
        EfjavEquipoAutorizadoEntity equipo = opt.get();
        return "AUTORIZADO".equalsIgnoreCase(equipo.getEfjavEstado()) &&
                oficinaRequerida.equalsIgnoreCase(equipo.getEfjavOficina());
    }

    public Optional<EfjavEquipoAutorizadoEntity> efjavMtdBuscarEquipo(String deviceToken) {
        if (deviceToken == null) return Optional.empty();
        return efjavEquipoAutorizadoJpaRepository.findByEfjavIdentificadorEquipo(deviceToken);
    }

    /**
     * Enrolar formalmente un equipo físico asociándolo a una oficina y responsable
     */
    public EfjavEquipoAutorizadoEntity efjavMtdEnrolarEquipo(String deviceToken, String nombreEquipo, String oficina, Long usuarioId, String ip) {
        EfjavEquipoAutorizadoEntity entity = efjavEquipoAutorizadoJpaRepository.findByEfjavIdentificadorEquipo(deviceToken)
                .orElse(new EfjavEquipoAutorizadoEntity());

        entity.setEfjavIdentificadorEquipo(deviceToken);
        entity.setEfjavNombreEquipo(nombreEquipo);
        entity.setEfjavOficina(oficina);
        entity.setEfjavUsuarioResponsableId(usuarioId);
        entity.setEfjavIpAsignada(ip);
        entity.setEfjavEstado("AUTORIZADO");
        entity.setEfjavFechaEnrolamiento(LocalDateTime.now());

        return efjavEquipoAutorizadoJpaRepository.save(entity);
    }

    /**
     * Desenrolar / Revocar autorización de un equipo físico
     */
    public void efjavMtdRevocarEquipo(String deviceToken) {
        efjavEquipoAutorizadoJpaRepository.findByEfjavIdentificadorEquipo(deviceToken).ifPresent(e -> {
            e.setEfjavEstado("REVOCADO");
            efjavEquipoAutorizadoJpaRepository.save(e);
        });
    }
}
