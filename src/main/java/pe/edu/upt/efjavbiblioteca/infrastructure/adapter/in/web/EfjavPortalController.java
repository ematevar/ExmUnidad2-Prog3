package pe.edu.upt.efjavbiblioteca.infrastructure.adapter.in.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.edu.upt.efjavbiblioteca.application.usecase.EfjavBibliotecaService;
import pe.edu.upt.efjavbiblioteca.application.usecase.EfjavRecordatorioService;
import pe.edu.upt.efjavbiblioteca.application.usecase.EfjavTramiteService;
import pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.entity.*;
import pe.edu.upt.efjavbiblioteca.infrastructure.adapter.out.persistence.repository.EfjavNotificacionJpaRepository;
import pe.edu.upt.efjavbiblioteca.infrastructure.config.EfjavTimeService;
import pe.edu.upt.efjavbiblioteca.infrastructure.security.EfjavDispositivoSecurityService;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Controller
public class EfjavPortalController {

    private final EfjavBibliotecaService efjavBibliotecaService;
    private final EfjavTramiteService efjavTramiteService;
    private final EfjavRecordatorioService efjavRecordatorioService;
    private final EfjavNotificacionJpaRepository efjavNotificacionJpaRepository;
    private final EfjavTimeService efjavTimeService;
    private final EfjavDispositivoSecurityService efjavDispositivoSecurityService;

    public EfjavPortalController(EfjavBibliotecaService efjavBibliotecaService,
                                 EfjavTramiteService efjavTramiteService,
                                 EfjavRecordatorioService efjavRecordatorioService,
                                 EfjavNotificacionJpaRepository efjavNotificacionJpaRepository,
                                 EfjavTimeService efjavTimeService,
                                 EfjavDispositivoSecurityService efjavDispositivoSecurityService) {
        this.efjavBibliotecaService = efjavBibliotecaService;
        this.efjavTramiteService = efjavTramiteService;
        this.efjavRecordatorioService = efjavRecordatorioService;
        this.efjavNotificacionJpaRepository = efjavNotificacionJpaRepository;
        this.efjavTimeService = efjavTimeService;
        this.efjavDispositivoSecurityService = efjavDispositivoSecurityService;
    }

    private boolean validarSesion(HttpSession session) {
        return session.getAttribute("AUTH_USER_ID") != null;
    }

    @GetMapping("/")
    public String index(HttpSession session) {
        if (!validarSesion(session)) return "redirect:/login";
        return "redirect:/portal";
    }

    @GetMapping("/portal")
    public String portal(@RequestParam(value = "fechaInicioFiltro", required = false) String fechaInicioFiltro,
                         @RequestParam(value = "fechaFinFiltro", required = false) String fechaFinFiltro,
                         @RequestParam(value = "criterioFiltro", required = false) String criterioFiltro,
                         HttpServletRequest request,
                         HttpServletResponse response,
                         HttpSession session, Model model) {
        if (!validarSesion(session)) return "redirect:/login";

        Long userId = (Long) session.getAttribute("AUTH_USER_ID");
        String userCodigo = (String) session.getAttribute("AUTH_USER_CODIGO");
        String userNombre = (String) session.getAttribute("AUTH_USER_NOMBRE");
        @SuppressWarnings("unchecked")
        List<String> roles = (List<String>) session.getAttribute("AUTH_USER_ROLES");
        boolean esEstudiante = roles != null && roles.contains("ROLE_ESTUDIANTE");
        boolean esBiblioteca = roles != null && roles.contains("ROLE_BIBLIOTECA");
        boolean esDecano = roles != null && roles.contains("ROLE_DECANO");
        boolean esEpis = roles != null && roles.contains("ROLE_EPIS");
        boolean esCobranzas = roles != null && roles.contains("ROLE_COBRANZAS");
        boolean esAdmin = roles != null && roles.contains("ROLE_ADMIN");
        
        // Obtener el token real de la máquina física
        String deviceToken = efjavDispositivoSecurityService.efjavMtdObtenerOGenerarTokenEquipo(request, response);
        session.setAttribute("AUTH_EQUIPO_ID", deviceToken);
        Optional<EfjavEquipoAutorizadoEntity> equipoEnroladoOpt = efjavDispositivoSecurityService.efjavMtdBuscarEquipo(deviceToken);

        model.addAttribute("userCodigo", userCodigo);
        model.addAttribute("userNombre", userNombre);
        model.addAttribute("userRoles", roles);
        model.addAttribute("esEstudiante", esEstudiante);
        model.addAttribute("esBiblioteca", esBiblioteca);
        model.addAttribute("esDecano", esDecano);
        model.addAttribute("esEpis", esEpis);
        model.addAttribute("esCobranzas", esCobranzas);
        model.addAttribute("esAdmin", esAdmin);
        model.addAttribute("deviceToken", deviceToken);
        model.addAttribute("equipoEnrolado", equipoEnroladoOpt.orElse(null));
        model.addAttribute("esAutorizadoBiblioteca", efjavDispositivoSecurityService.efjavFncEsEquipoAutorizadoParaOficina(deviceToken, "BIBLIOTECA"));
        model.addAttribute("esAutorizadoFacultad", efjavDispositivoSecurityService.efjavFncEsEquipoAutorizadoParaOficina(deviceToken, "FACULTAD"));
        model.addAttribute("esAutorizadoEpis", efjavDispositivoSecurityService.efjavFncEsEquipoAutorizadoParaOficina(deviceToken, "EPIS"));
        model.addAttribute("esAutorizadoCobranzas", efjavDispositivoSecurityService.efjavFncEsEquipoAutorizadoParaOficina(deviceToken, "COBRANZAS"));
        model.addAttribute("fechaServidor", efjavTimeService.efjavFncObtenerFechaHoraServidor());

        // Verificaciones de penalidad y notificaciones
        Optional<EfjavPenalidadEntity> penalidad = efjavBibliotecaService.efjavFncVerificarPenalidadVigente(userId);
        model.addAttribute("penalidadVigente", penalidad.orElse(null));

        List<EfjavNotificacionEntity> notificaciones = efjavNotificacionJpaRepository.findByEfjavDestinatarioIdOrderByEfjavFechaCreacionDesc(userId);
        model.addAttribute("notificaciones", notificaciones);
        model.addAttribute("noLeidasCount", efjavNotificacionJpaRepository.countByEfjavDestinatarioIdAndEfjavLeidaFalse(userId));

        // Catálogo y préstamos
        model.addAttribute("libros", efjavBibliotecaService.efjavMtdListarLibros());
        model.addAttribute("misPrestamos", efjavBibliotecaService.efjavMtdListarPrestamosEstudiante(userId));
        model.addAttribute("todosPrestamos", efjavBibliotecaService.efjavMtdListarTodosLosPrestamos());

        // Trámites
        model.addAttribute("misTramites", efjavTramiteService.efjavMtdListarPorEstudiante(userId));
        
        // Bandejas según oficina/instancia
        model.addAttribute("bandejaFacultad", efjavTramiteService.efjavMtdListarPorUbicacion(1L));
        model.addAttribute("bandejaEpis", efjavTramiteService.efjavMtdListarPorUbicacion(2L));
        model.addAttribute("bandejaCobranzas", efjavTramiteService.efjavMtdListarPorUbicacion(3L));

        // Culminados (Filtro por fechas - Numeral 6.14 y Prueba 18)
        LocalDateTime inicio = LocalDateTime.now().minusDays(30);
        LocalDateTime fin = LocalDateTime.now().plusDays(1);
        if (fechaInicioFiltro != null && !fechaInicioFiltro.isEmpty()) {
            inicio = LocalDate.parse(fechaInicioFiltro).atStartOfDay();
        }
        if (fechaFinFiltro != null && !fechaFinFiltro.isEmpty()) {
            fin = LocalDate.parse(fechaFinFiltro).atTime(LocalTime.MAX);
        }
        model.addAttribute("fechaInicioFiltro", fechaInicioFiltro);
        model.addAttribute("fechaFinFiltro", fechaFinFiltro);
        model.addAttribute("culminados", efjavTramiteService.efjavMtdListarCulminados(inicio, fin));

        return "portal";
    }

    // Ejecutar recordatorios de vencimiento bajo demanda (Prueba 19)
    @GetMapping("/recordatorios/ejecutar")
    public String ejecutarRecordatorios(RedirectAttributes redirectAttributes) {
        int generados = efjavRecordatorioService.efjavMtdEjecutarBarridoRecordatorios();
        redirectAttributes.addFlashAttribute("successMsg", "Barrido de recordatorios ejecutado: " + generados + " notificaciones generadas.");
        return "redirect:/portal";
    }

    // Ver historial completo de un trámite
    @GetMapping("/tramites/historial/{id}")
    @ResponseBody
    public List<EfjavMovimientoTramiteEntity> verHistorialTramite(@PathVariable("id") Long id) {
        return efjavTramiteService.efjavMtdObtenerHistorial(id);
    }

    // Descargar/Visualizar PDF de un trámite (Prueba 10 y Numeral 6.14)
    @GetMapping("/tramites/documento/{tramiteId}")
    public ResponseEntity<Resource> descargarDocumento(@PathVariable("tramiteId") Long tramiteId) {
        List<EfjavDocumentoEntity> docs = efjavTramiteService.efjavMtdObtenerDocumentos(tramiteId);
        if (docs.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        EfjavDocumentoEntity doc = docs.get(0);
        File file = new File(doc.getEfjavRutaAlmacenamiento());
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }
        Resource resource = new FileSystemResource(file);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + doc.getEfjavNombreOriginal() + "\"")
                .body(resource);
    }

    // Enrolamiento o desvinculación real del equipo físico actual
    @PostMapping("/dispositivos/enrolar")
    public String enrolarEquipoActual(@RequestParam("oficina") String oficina,
                                      @RequestParam("nombreEquipo") String nombreEquipo,
                                      HttpServletRequest request,
                                      HttpServletResponse response,
                                      HttpSession session,
                                      RedirectAttributes redirectAttributes) {
        if (!validarSesion(session)) return "redirect:/login";
        Long userId = (Long) session.getAttribute("AUTH_USER_ID");
        String deviceToken = efjavDispositivoSecurityService.efjavMtdObtenerOGenerarTokenEquipo(request, response);

        efjavDispositivoSecurityService.efjavMtdEnrolarEquipo(deviceToken, nombreEquipo, oficina, userId, request.getRemoteAddr());
        redirectAttributes.addFlashAttribute("successMsg", "Equipo físico enrolado y autorizado satisfactoriamente para la oficina: " + oficina);
        return "redirect:/portal";
    }

    @PostMapping("/dispositivos/revocar")
    public String revocarEquipoActual(HttpServletRequest request,
                                     HttpServletResponse response,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {
        if (!validarSesion(session)) return "redirect:/login";
        String deviceToken = efjavDispositivoSecurityService.efjavMtdObtenerOGenerarTokenEquipo(request, response);
        efjavDispositivoSecurityService.efjavMtdRevocarEquipo(deviceToken);
        redirectAttributes.addFlashAttribute("successMsg", "Autorización revocada. Esta computadora ahora figura como PC No Autorizada (Externa).");
        return "redirect:/portal";
    }

    // Solicitar préstamo
    @PostMapping("/biblioteca/solicitar")
    public String solicitarPrestamo(@RequestParam("libroId") Long libroId,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        if (!validarSesion(session)) return "redirect:/login";
        Long userId = (Long) session.getAttribute("AUTH_USER_ID");
        String userCodigo = (String) session.getAttribute("AUTH_USER_CODIGO");
        String equipo = (String) session.getAttribute("AUTH_EQUIPO_ID");

        try {
            efjavBibliotecaService.efjavMtdSolicitarPrestamo(userId, userCodigo, libroId, "127.0.0.1", equipo);
            redirectAttributes.addFlashAttribute("successMsg", "Solicitud de préstamo registrada exitosamente dentro del horario.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/portal";
    }

    // Confirmar préstamo con 2 firmas (Biblioteca)
    @PostMapping("/biblioteca/confirmar")
    public String confirmarPrestamo(@RequestParam("prestamoId") Long prestamoId,
                                    @RequestParam("firmaEstudiante") String firmaEstudiante,
                                    @RequestParam("firmaBibliotecario") String firmaBibliotecario,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        if (!validarSesion(session)) return "redirect:/login";
        Long userId = (Long) session.getAttribute("AUTH_USER_ID");
        String userCodigo = (String) session.getAttribute("AUTH_USER_CODIGO");
        String equipo = (String) session.getAttribute("AUTH_EQUIPO_ID");

        try {
            efjavBibliotecaService.efjavMtdConfirmarPrestamoConDosFirmas(prestamoId, userId, userCodigo, firmaEstudiante, firmaBibliotecario, "127.0.0.1", equipo);
            redirectAttributes.addFlashAttribute("successMsg", "Préstamo confirmado y entregado con ambas firmas registradas.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/portal";
    }

    // Registrar devolución con 2 firmas y cálculo de penalidad
    @PostMapping("/biblioteca/devolver")
    public String registrarDevolucion(@RequestParam("prestamoId") Long prestamoId,
                                      @RequestParam("firmaEstudiante") String firmaEstudiante,
                                      @RequestParam("firmaBibliotecario") String firmaBibliotecario,
                                      @RequestParam("estadoFisico") String estadoFisico,
                                      @RequestParam(value = "observacion", required = false) String observacion,
                                      @RequestParam(value = "diasRetrasoManual", required = false) Integer diasRetrasoManual,
                                      HttpSession session,
                                      RedirectAttributes redirectAttributes) {
        if (!validarSesion(session)) return "redirect:/login";
        Long userId = (Long) session.getAttribute("AUTH_USER_ID");
        String userCodigo = (String) session.getAttribute("AUTH_USER_CODIGO");
        String equipo = (String) session.getAttribute("AUTH_EQUIPO_ID");

        try {
            efjavBibliotecaService.efjavMtdRegistrarDevolucion(prestamoId, userId, userCodigo, firmaEstudiante, firmaBibliotecario,
                    estadoFisico, observacion, diasRetrasoManual, "127.0.0.1", equipo);
            redirectAttributes.addFlashAttribute("successMsg", "Devolución registrada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/portal";
    }

    // Registrar nuevo trámite
    @PostMapping("/tramites/registrar")
    public String registrarTramite(@RequestParam("tipoTramite") String tipoTramite,
                                   @RequestParam("asunto") String asunto,
                                   @RequestParam("instanciaDestinoId") Long instanciaDestinoId,
                                   @RequestParam("documentoPdf") MultipartFile documentoPdf,
                                   @RequestParam("firmaEstudiante") String firmaEstudiante,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        if (!validarSesion(session)) return "redirect:/login";
        Long userId = (Long) session.getAttribute("AUTH_USER_ID");
        String userCodigo = (String) session.getAttribute("AUTH_USER_CODIGO");
        String equipo = (String) session.getAttribute("AUTH_EQUIPO_ID");

        try {
            EfjavTramiteEntity tramite = efjavTramiteService.efjavMtdRegistrarTramite(
                    userId, userCodigo, tipoTramite, asunto, instanciaDestinoId, documentoPdf, firmaEstudiante, "127.0.0.1", equipo
            );
            redirectAttributes.addFlashAttribute("successMsg", "Trámite enviado exitosamente. Código de seguimiento: " + tramite.getEfjavCodigoSeguimiento());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/portal";
    }

    // Subsanar trámite por el estudiante
    @PostMapping("/tramites/subsanar")
    public String subsanarTramite(@RequestParam("tramiteId") Long tramiteId,
                                  @RequestParam("respuestaSubsanacion") String respuestaSubsanacion,
                                  @RequestParam(value = "nuevoDocumentoPdf", required = false) MultipartFile nuevoDocumentoPdf,
                                  @RequestParam("firmaEstudianteSubsanacion") String firmaEstudianteSubsanacion,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        if (!validarSesion(session)) return "redirect:/login";
        Long userId = (Long) session.getAttribute("AUTH_USER_ID");
        String userCodigo = (String) session.getAttribute("AUTH_USER_CODIGO");
        String equipo = (String) session.getAttribute("AUTH_EQUIPO_ID");

        try {
            efjavTramiteService.efjavMtdSubsanarTramite(tramiteId, userId, userCodigo, respuestaSubsanacion, nuevoDocumentoPdf, firmaEstudianteSubsanacion, "127.0.0.1", equipo);
            redirectAttributes.addFlashAttribute("successMsg", "Subsanación enviada exitosamente. El expediente ha vuelto a la oficina correspondiente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/portal";
    }

    // Atender trámite en Facultad
    @PostMapping("/tramites/atender-facultad")
    public String atenderFacultad(@RequestParam("tramiteId") Long tramiteId,
                                  @RequestParam("accion") String accion,
                                  @RequestParam(value = "observacion", required = false) String observacion,
                                  @RequestParam("firmaDecano") String firmaDecano,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        if (!validarSesion(session)) return "redirect:/login";
        Long userId = (Long) session.getAttribute("AUTH_USER_ID");
        String userCodigo = (String) session.getAttribute("AUTH_USER_CODIGO");
        String equipo = (String) session.getAttribute("AUTH_EQUIPO_ID");

        try {
            efjavTramiteService.efjavMtdAtenderEnFacultad(tramiteId, userId, userCodigo, accion, observacion, firmaDecano, "127.0.0.1", equipo);
            redirectAttributes.addFlashAttribute("successMsg", "Expediente procesado en Facultad exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/portal";
    }

    // Atender trámite en EPIS
    @PostMapping("/tramites/atender-epis")
    public String atenderEpis(@RequestParam("tramiteId") Long tramiteId,
                              @RequestParam("accion") String accion,
                              @RequestParam(value = "observacion", required = false) String observacion,
                              @RequestParam("firmaDirector") String firmaDirector,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        if (!validarSesion(session)) return "redirect:/login";
        Long userId = (Long) session.getAttribute("AUTH_USER_ID");
        String userCodigo = (String) session.getAttribute("AUTH_USER_CODIGO");
        String equipo = (String) session.getAttribute("AUTH_EQUIPO_ID");

        try {
            efjavTramiteService.efjavMtdAtenderEnEpis(tramiteId, userId, userCodigo, accion, observacion, firmaDirector, "127.0.0.1", equipo);
            redirectAttributes.addFlashAttribute("successMsg", "Expediente procesado en EPIS exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/portal";
    }

    // Atender trámite en Cobranzas
    @PostMapping("/tramites/atender-cobranzas")
    public String atenderCobranzas(@RequestParam("tramiteId") Long tramiteId,
                                   @RequestParam("accion") String accion,
                                   @RequestParam(value = "observacion", required = false) String observacion,
                                   @RequestParam("firmaResponsable") String firmaResponsable,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        if (!validarSesion(session)) return "redirect:/login";
        Long userId = (Long) session.getAttribute("AUTH_USER_ID");
        String userCodigo = (String) session.getAttribute("AUTH_USER_CODIGO");
        String equipo = (String) session.getAttribute("AUTH_EQUIPO_ID");

        try {
            efjavTramiteService.efjavMtdAtenderEnCobranzas(tramiteId, userId, userCodigo, accion, observacion, firmaResponsable, "127.0.0.1", equipo);
            redirectAttributes.addFlashAttribute("successMsg", "Expediente atendido en Cobranzas exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/portal";
    }

    // Marcar notificación leída
    @GetMapping("/notificaciones/marcar-leida/{id}")
    public String marcarNotificacionLeida(@PathVariable("id") Long id) {
        efjavNotificacionJpaRepository.findById(id).ifPresent(n -> {
            n.setEfjavLeida(true);
            efjavNotificacionJpaRepository.save(n);
        });
        return "redirect:/portal";
    }
}
