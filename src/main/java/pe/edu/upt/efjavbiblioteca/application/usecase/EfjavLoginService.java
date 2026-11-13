package pe.edu.upt.efjavbiblioteca.application.usecase;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.edu.upt.efjavbiblioteca.domain.model.EfjavClsUsuario;
import pe.edu.upt.efjavbiblioteca.domain.port.out.EfjavInfaAuditoriaRepository;
import pe.edu.upt.efjavbiblioteca.domain.port.out.EfjavInfaUsuarioRepository;
import pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.entity.EfjavSesionEntity;
import pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.repository.EfjavSesionJpaRepository;
import pe.edu.upt.efjavbiblioteca.infrastructure.security.EfjavTotpService;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EfjavLoginService {

    private final EfjavInfaUsuarioRepository efjavUsuarioRepository;
    private final EfjavInfaAuditoriaRepository efjavAuditoriaRepository;
    private final EfjavSesionJpaRepository efjavSesionJpaRepository;
    private final PasswordEncoder efjavPasswordEncoder;
    private final EfjavTotpService efjavTotpService;

    public EfjavLoginService(EfjavInfaUsuarioRepository efjavUsuarioRepository,
                             EfjavInfaAuditoriaRepository efjavAuditoriaRepository,
                             EfjavSesionJpaRepository efjavSesionJpaRepository,
                             PasswordEncoder efjavPasswordEncoder,
                             EfjavTotpService efjavTotpService) {
        this.efjavUsuarioRepository = efjavUsuarioRepository;
        this.efjavAuditoriaRepository = efjavAuditoriaRepository;
        this.efjavSesionJpaRepository = efjavSesionJpaRepository;
        this.efjavPasswordEncoder = efjavPasswordEncoder;
        this.efjavTotpService = efjavTotpService;
    }

    public EfjavClsUsuario efjavMtdValidarPrimerPaso(String usuarioOCorreo, String passwordPlano, String ip, String equipo) {
        Optional<EfjavClsUsuario> usuarioOpt = efjavUsuarioRepository.efjavMtdBuscarPorCodigo(usuarioOCorreo);
        if (usuarioOpt.isEmpty()) {
            usuarioOpt = efjavUsuarioRepository.efjavMtdBuscarPorCorreo(usuarioOCorreo);
        }

        if (usuarioOpt.isEmpty()) {
            efjavAuditoriaRepository.efjavMtdRegistrar(usuarioOCorreo, "LOGIN_PASO1", "USUARIO",
                    "Usuario no encontrado", ip, equipo, "DENEGADO");
            return null;
        }

        EfjavClsUsuario usuario = usuarioOpt.get();

        if (Boolean.TRUE.equals(usuario.getEfjavBloqueado())) {
            efjavAuditoriaRepository.efjavMtdRegistrar(usuario.getEfjavCodigo(), "LOGIN_PASO1", "USUARIO",
                    "Cuenta bloqueada por exceso de intentos", ip, equipo, "BLOQUEADO");
            return null;
        }

        if (!efjavPasswordEncoder.matches(passwordPlano, usuario.getEfjavPasswordHash())) {
            int intentos = (usuario.getEfjavIntentosFallidos() == null ? 0 : usuario.getEfjavIntentosFallidos()) + 1;
            usuario.setEfjavIntentosFallidos(intentos);
            if (intentos >= EfjavClsUsuario.EFJAV_CTE_MAX_INTENTOS_LOGIN) {
                usuario.setEfjavBloqueado(true);
            }
            efjavUsuarioRepository.efjavMtdGuardar(usuario);
            efjavAuditoriaRepository.efjavMtdRegistrar(usuario.getEfjavCodigo(), "LOGIN_PASO1", "USUARIO",
                    "Contraseña incorrecta. Intento: " + intentos, ip, equipo, "ERROR_PASSWORD");
            return null;
        }

        // Generar OTP de 6 dígitos con expiración de 5 minutos como alternativa / respaldo a TOTP
        String otpGenerado = String.format("%06d", new SecureRandom().nextInt(1_000_000));
        usuario.setEfjavOtpActual(otpGenerado);
        usuario.setEfjavOtpExpiracion(LocalDateTime.now().plusMinutes(5));
        usuario.setEfjavIntentosFallidos(0);
        efjavUsuarioRepository.efjavMtdGuardar(usuario);

        System.out.println("=================================================");
        System.out.println(" [2FA SIMULADOR] Código OTP para " + usuario.getEfjavCodigo() + " (" + usuario.getEfjavCorreo() + "): " + otpGenerado);
        System.out.println(" [2FA TOTP] Secreto Base32: " + usuario.getEfjavTotpSecret());
        System.out.println("=================================================");

        efjavAuditoriaRepository.efjavMtdRegistrar(usuario.getEfjavCodigo(), "LOGIN_PASO1", "USUARIO",
                "Primer factor validado correctamente. Solicitando 2FA", ip, equipo, "EXITOSO");

        return usuario;
    }

    public boolean efjavMtdValidarSegundoPaso(Long usuarioId, String codigo2fa, String ip, String equipo) {
        Optional<EfjavClsUsuario> opt = efjavUsuarioRepository.efjavMtdBuscarPorId(usuarioId);
        if (opt.isEmpty()) return false;

        EfjavClsUsuario usuario = opt.get();
        boolean esValido = false;

        // 1. Validar OTP generado (si está vigente)
        if (usuario.getEfjavOtpActual() != null && usuario.getEfjavOtpExpiracion() != null) {
            if (LocalDateTime.now().isBefore(usuario.getEfjavOtpExpiracion())) {
                if (usuario.getEfjavOtpActual().equals(codigo2fa.trim())) {
                    esValido = true;
                    // Invalidar código después de usarlo
                    usuario.setEfjavOtpActual(null);
                    usuario.setEfjavOtpExpiracion(null);
                }
            }
        }

        // 2. Si no fue por OTP directo, validar con TOTP RFC 6238 (Bitwarden / Google Authenticator)
        if (!esValido && usuario.getEfjavTotpSecret() != null) {
            if (efjavTotpService.efjavFncVerificarCodigo(usuario.getEfjavTotpSecret(), codigo2fa)) {
                esValido = true;
            }
        }

        if (esValido) {
            usuario.setEfjavIntentosFallidos(0);
            efjavUsuarioRepository.efjavMtdGuardar(usuario);
            efjavAuditoriaRepository.efjavMtdRegistrar(usuario.getEfjavCodigo(), "LOGIN_2FA", "USUARIO",
                    "Segundo factor validado satisfactoriamente", ip, equipo, "EXITOSO");
            return true;
        } else {
            efjavAuditoriaRepository.efjavMtdRegistrar(usuario.getEfjavCodigo(), "LOGIN_2FA", "USUARIO",
                    "Código 2FA inválido o expirado: " + codigo2fa, ip, equipo, "RECHAZADO");
            return false;
        }
    }

    public boolean efjavFncVerificarSesionUnica(Long usuarioId, String identificadorEquipoActual) {
        List<EfjavSesionEntity> activas = efjavSesionJpaRepository.findByEfjavUsuarioIdAndEfjavEstado(usuarioId, "ACTIVA");
        if (activas.isEmpty()) return true;

        for (EfjavSesionEntity sesion : activas) {
            if (sesion.getEfjavExpiracion().isAfter(LocalDateTime.now())) {
                // Si la sesión activa pertenece a otro equipo/token, bloquear el segundo acceso
                if (identificadorEquipoActual == null || !identificadorEquipoActual.equals(sesion.getEfjavIdentificadorEquipo())) {
                    return false; // Bloquea segundo acceso
                }
            } else {
                sesion.setEfjavEstado("EXPIRADA");
                efjavSesionJpaRepository.save(sesion);
            }
        }
        return true;
    }

    public EfjavSesionEntity efjavMtdRegistrarSesion(Long usuarioId, String ip, String equipo, String userAgent) {
        // Expirar cualquier sesión anterior si era del mismo equipo
        List<EfjavSesionEntity> previas = efjavSesionJpaRepository.findByEfjavUsuarioIdAndEfjavEstado(usuarioId, "ACTIVA");
        for (EfjavSesionEntity s : previas) {
            s.setEfjavEstado("CERRADA");
            efjavSesionJpaRepository.save(s);
        }

        EfjavSesionEntity sesion = new EfjavSesionEntity();
        sesion.setEfjavUsuarioId(usuarioId);
        sesion.setEfjavSessionToken(UUID.randomUUID().toString());
        sesion.setEfjavIp(ip != null ? ip : "127.0.0.1");
        sesion.setEfjavIdentificadorEquipo(equipo != null ? equipo : "PC-GENERICA");
        sesion.setEfjavUserAgent(userAgent);
        sesion.setEfjavInicio(LocalDateTime.now());
        sesion.setEfjavUltimaActividad(LocalDateTime.now());
        sesion.setEfjavExpiracion(LocalDateTime.now().plusHours(2));
        sesion.setEfjavEstado("ACTIVA");
        return efjavSesionJpaRepository.save(sesion);
    }
}
