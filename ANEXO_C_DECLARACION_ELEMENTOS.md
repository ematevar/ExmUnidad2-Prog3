# DECLARACIÓN DE ELEMENTOS DEL PROYECTO (OBLIGATORIA - ANEXO C)

**Estudiante:** Emanuel Felipe José Atencio Vargas  
**Iniciales:** `efjav`  
**Asignatura:** Programación III - Examen de Unidad  
**Proyecto:** efjavbiblioteca (Sistema Integral de Gestión de Biblioteca y Trámites Académicos)  

---

| Elemento (nombre con convención) | Tipo | Capa hexagonal | Paquete / archivo |
| :--- | :--- | :--- | :--- |
| `ClsUsuario` | Clase | Dominio | `domain/model/EfjavClsUsuario.java` |
| `InfaPrestamoRepository` | Interfaz (puerto de salida) | Dominio/Aplicación | `domain/port/out/EfjavInfaPrestamoRepository.java` |
| `ClsRegistrarPrestamoService` | Clase (caso de uso) | Aplicación | `application/usecase/EfjavBibliotecaService.java` |
| `MtdEjecutar` | Método | Aplicación | `application/usecase/EfjavBibliotecaService.java` |
| `FncCalcularDias` | Función | Aplicación | `application/usecase/EfjavBibliotecaService.java` |
| `ClsPrestamoJpaAdapter` | Clase (adaptador de salida) | Infraestructura | `infrastructure/adapter/out/persistence/adapter/EfjavUsuarioJpaAdapter.java` |
| `EnuEstadoTramite` | Enum | Dominio | `domain/model/EfjavEnuEstadoTramite.java` |
| `CteMaxIntentosLogin` | Constante | Dominio | `domain/model/EfjavClsUsuario.java` |
| `EfjavClsLibro` | Clase | Dominio | `domain/model/EfjavClsLibro.java` |
| `EfjavClsFirma` | Clase | Dominio | `domain/model/EfjavClsFirma.java` |
| `EfjavClsPrestamo` | Clase | Dominio | `domain/model/EfjavClsPrestamo.java` |
| `EfjavClsPenalidad` | Clase | Dominio | `domain/model/EfjavClsPenalidad.java` |
| `EfjavClsTramite` | Clase | Dominio | `domain/model/EfjavClsTramite.java` |
| `EfjavClsMovimientoTramite` | Clase | Dominio | `domain/model/EfjavClsMovimientoTramite.java` |
| `EfjavClsEquipoAutorizado` | Clase | Dominio | `domain/model/EfjavClsEquipoAutorizado.java` |
| `EfjavClsNotificacion` | Clase | Dominio | `domain/model/EfjavClsNotificacion.java` |
| `EfjavEnuRol` | Enum | Dominio | `domain/model/EfjavEnuRol.java` |
| `EfjavEnuEstadoPrestamo` | Enum | Dominio | `domain/model/EfjavEnuEstadoPrestamo.java` |
| `EfjavInfaUsuarioRepository` | Interfaz (puerto de salida) | Dominio/Aplicación | `domain/port/out/EfjavInfaUsuarioRepository.java` |
| `EfjavInfaTramiteRepository` | Interfaz (puerto de salida) | Dominio/Aplicación | `domain/port/out/EfjavInfaTramiteRepository.java` |
| `EfjavInfaPenalidadRepository` | Interfaz (puerto de salida) | Dominio/Aplicación | `domain/port/out/EfjavInfaPenalidadRepository.java` |
| `EfjavInfaNotificacionRepository` | Interfaz (puerto de salida) | Dominio/Aplicación | `domain/port/out/EfjavInfaNotificacionRepository.java` |
| `EfjavInfaEquipoAutorizadoRepository` | Interfaz (puerto de salida) | Dominio/Aplicación | `domain/port/out/EfjavInfaEquipoAutorizadoRepository.java` |
| `EfjavInfaFirmaRepository` | Interfaz (puerto de salida) | Dominio/Aplicación | `domain/port/out/EfjavInfaFirmaRepository.java` |
| `EfjavInfaLibroRepository` | Interfaz (puerto de salida) | Dominio/Aplicación | `domain/port/out/EfjavInfaLibroRepository.java` |
| `EfjavInfaAuditoriaRepository` | Interfaz (puerto de salida) | Dominio/Aplicación | `domain/port/out/EfjavInfaAuditoriaRepository.java` |
| `EfjavLoginService` | Clase (caso de uso) | Aplicación | `application/usecase/EfjavLoginService.java` |
| `EfjavBibliotecaService` | Clase (caso de uso) | Aplicación | `application/usecase/EfjavBibliotecaService.java` |
| `EfjavTramiteService` | Clase (caso de uso) | Aplicación | `application/usecase/EfjavTramiteService.java` |
| `EfjavUsuarioJpaAdapter` | Clase (adaptador de salida) | Infraestructura | `infrastructure/adapter/out/persistence/adapter/EfjavUsuarioJpaAdapter.java` |
| `EfjavAuditoriaJpaAdapter` | Clase (adaptador de salida) | Infraestructura | `infrastructure/adapter/out/persistence/adapter/EfjavAuditoriaJpaAdapter.java` |
| `EfjavLoginController` | Clase (adaptador de entrada) | Infraestructura | `infrastructure/adapter/in/web/EfjavLoginController.java` |
| `EfjavPortalController` | Clase (adaptador de entrada) | Infraestructura | `infrastructure/adapter/in/web/EfjavPortalController.java` |
| `EfjavTotpService` | Clase (servicio seguridad) | Infraestructura | `infrastructure/security/EfjavTotpService.java` |
| `EfjavStorageService` | Clase (servicio almacenamiento) | Infraestructura | `infrastructure/storage/EfjavStorageService.java` |
| `EfjavTimeService` | Clase (servicio horario servidor) | Infraestructura | `infrastructure/config/EfjavTimeService.java` |
