package pe.edu.upt.efjavbiblioteca.infrastructure.adapter.in.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pe.edu.upt.efjavbiblioteca.application.usecase.EfjavLoginService;
import pe.edu.upt.efjavbiblioteca.domain.model.EfjavClsUsuario;
import pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.entity.EfjavEquipoAutorizadoEntity;
import pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.entity.EfjavSesionEntity;
import pe.edu.upt.efjavbiblioteca.infrastructure.security.EfjavDispositivoSecurityService;

import java.util.Optional;

@Controller
public class EfjavLoginController {

    private final EfjavLoginService efjavLoginService;
    private final EfjavDispositivoSecurityService efjavDispositivoSecurityService;

    public EfjavLoginController(EfjavLoginService efjavLoginService,
                                EfjavDispositivoSecurityService efjavDispositivoSecurityService) {
        this.efjavLoginService = efjavLoginService;
        this.efjavDispositivoSecurityService = efjavDispositivoSecurityService;
    }

    @GetMapping("/login")
    public String efjavMtdMostrarLogin(@RequestParam(value = "error", required = false) String error,
                                       @RequestParam(value = "logout", required = false) String logout,
                                       @RequestParam(value = "bloqueado", required = false) String bloqueado,
                                       HttpServletRequest request,
                                       HttpServletResponse response,
                                       Model model) {
        String deviceToken = efjavDispositivoSecurityService.efjavMtdObtenerOGenerarTokenEquipo(request, response);
        Optional<EfjavEquipoAutorizadoEntity> equipoOpt = efjavDispositivoSecurityService.efjavMtdBuscarEquipo(deviceToken);

        model.addAttribute("deviceToken", deviceToken);
        model.addAttribute("equipoEnrolado", equipoOpt.orElse(null));

        if (error != null) {
            model.addAttribute("errorMsg", "Usuario o contraseña incorrectos.");
        }
        if (logout != null) {
            model.addAttribute("infoMsg", "Sesión cerrada correctamente.");
        }
        if (bloqueado != null) {
            model.addAttribute("errorMsg", "ACCESO DENEGADO: Ya existe una sesión activa para este usuario en otro equipo (Política de Sesión Única).");
        }
        return "login";
    }

    @PostMapping("/login")
    public String efjavMtdProcesarPrimerPaso(@RequestParam("usuario") String usuario,
                                             @RequestParam("password") String password,
                                             HttpServletRequest request,
                                             HttpServletResponse response,
                                             HttpSession session,
                                             Model model) {
        String deviceToken = efjavDispositivoSecurityService.efjavMtdObtenerOGenerarTokenEquipo(request, response);
        String ip = request.getRemoteAddr();

        EfjavClsUsuario usuarioAuth = efjavLoginService.efjavMtdValidarPrimerPaso(usuario, password, ip, deviceToken);

        if (usuarioAuth == null) {
            model.addAttribute("deviceToken", deviceToken);
            model.addAttribute("errorMsg", "Credenciales inválidas o cuenta bloqueada.");
            return "login";
        }

        // Guardar temporalmente en sesión para el segundo paso (2FA)
        session.setAttribute("PENDING_2FA_USER_ID", usuarioAuth.getEfjavId());
        session.setAttribute("PENDING_2FA_USER_CODIGO", usuarioAuth.getEfjavCodigo());
        session.setAttribute("PENDING_2FA_USER_NOMBRE", usuarioAuth.getEfjavNombres());
        session.setAttribute("PENDING_2FA_EQUIPO", deviceToken);
        session.setAttribute("PENDING_2FA_OTP_DEBUG", usuarioAuth.getEfjavOtpActual());
        session.setAttribute("PENDING_2FA_TOTP_SECRET", usuarioAuth.getEfjavTotpSecret());

        return "redirect:/login-2fa";
    }

    @GetMapping("/login-2fa")
    public String efjavMtdMostrar2FA(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("PENDING_2FA_USER_ID");
        if (userId == null) {
            return "redirect:/login";
        }

        model.addAttribute("codigoUsuario", session.getAttribute("PENDING_2FA_USER_CODIGO"));
        model.addAttribute("nombreUsuario", session.getAttribute("PENDING_2FA_USER_NOMBRE"));
        model.addAttribute("deviceToken", session.getAttribute("PENDING_2FA_EQUIPO"));
        model.addAttribute("otpDebug", session.getAttribute("PENDING_2FA_OTP_DEBUG"));
        model.addAttribute("totpSecret", session.getAttribute("PENDING_2FA_TOTP_SECRET"));

        return "login-2fa";
    }

    @PostMapping("/login-2fa")
    public String efjavMtdProcesarSegundoPaso(@RequestParam("codigo2fa") String codigo2fa,
                                              HttpServletRequest request,
                                              HttpServletResponse response,
                                              HttpSession session,
                                              Model model) {
        Long userId = (Long) session.getAttribute("PENDING_2FA_USER_ID");
        if (userId == null) {
            return "redirect:/login";
        }

        String deviceToken = efjavDispositivoSecurityService.efjavMtdObtenerOGenerarTokenEquipo(request, response);
        String ip = request.getRemoteAddr();

        boolean valido = efjavLoginService.efjavMtdValidarSegundoPaso(userId, codigo2fa, ip, deviceToken);

        if (!valido) {
            model.addAttribute("errorMsg", "Código 2FA incorrecto o expirado.");
            model.addAttribute("codigoUsuario", session.getAttribute("PENDING_2FA_USER_CODIGO"));
            model.addAttribute("nombreUsuario", session.getAttribute("PENDING_2FA_USER_NOMBRE"));
            model.addAttribute("deviceToken", deviceToken);
            model.addAttribute("otpDebug", session.getAttribute("PENDING_2FA_OTP_DEBUG"));
            model.addAttribute("totpSecret", session.getAttribute("PENDING_2FA_TOTP_SECRET"));
            return "login-2fa";
        }

        // RN01: Verificar política de sesión única basada en equipo real
        boolean sesionPermitida = efjavLoginService.efjavFncVerificarSesionUnica(userId, deviceToken);
        if (!sesionPermitida) {
            session.invalidate();
            return "redirect:/login?bloqueado=true";
        }

        // Registrar sesión activa
        EfjavSesionEntity sesionEntity = efjavLoginService.efjavMtdRegistrarSesion(userId, ip, deviceToken, request.getHeader("User-Agent"));

        // Limpiar temporales y establecer usuario autenticado
        session.removeAttribute("PENDING_2FA_USER_ID");
        session.removeAttribute("PENDING_2FA_OTP_DEBUG");

        session.setAttribute("AUTH_USER_ID", userId);
        session.setAttribute("AUTH_USER_CODIGO", session.getAttribute("PENDING_2FA_USER_CODIGO"));
        session.setAttribute("AUTH_USER_NOMBRE", session.getAttribute("PENDING_2FA_USER_NOMBRE"));
        session.setAttribute("AUTH_EQUIPO_ID", deviceToken);
        session.setAttribute("AUTH_SESSION_TOKEN", sesionEntity.getEfjavSessionToken());

        return "redirect:/portal";
    }

    @GetMapping("/logout")
    public String efjavMtdLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout=true";
    }
}
