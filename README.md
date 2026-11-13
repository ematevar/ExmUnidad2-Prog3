# Sistema Integral de Gestión de Biblioteca y Trámites Académicos
**Universidad Privada de Tacna - Escuela Profesional de Ingeniería de Sistemas**  
**Asignatura:** Programación III | Examen Práctico Integrador de Unidad II  
**Estudiante:** Emanuel Felipe José Atencio Vargas (`efjav` - Cód. 2024079964)  
**Tecnologías:** Java 21, Spring Boot, Spring Security, Spring Data JPA, Thymeleaf, MySQL, NetBeans  

---

## Documentación Oficial del Examen
- **Informe Técnico Completo en PDF:** [Informe_Examen_Unidad_II_EFJAV.pdf](docs/Informe_Examen_Unidad_II_EFJAV.pdf) (Compilado con carátula oficial UPT, Anexo C y diagramas PlantUML).
- **Declaración Obligatoria de Elementos (Anexo C):** [ANEXO_C_DECLARACION_ELEMENTOS.md](ANEXO_C_DECLARACION_ELEMENTOS.md)

---

## 1. Guía de Ejecución y Puesta en Marcha

### Prerrequisitos
- **Java JDK 21** instalado y configurado en el sistema / NetBeans.
- **MySQL Server** activo en el puerto `3306` (usuario `root`, sin contraseña).

### Base de Datos
La base de datos `db_biblioteca_tramites` se encuentra inicializada con todas las tablas y datos de prueba. Si requiere reiniciar los datos, puede ejecutar:
```sql
SOURCE src/main/resources/schema.sql;
SOURCE src/main/resources/data.sql;
```

### Ejecución
- **Desde Apache NetBeans:** Abrir la carpeta del proyecto `exmunidad2-prog3` y presionar el botón **Run Project** (`F6`).
- **Desde Terminal:**
  ```bash
  .\gradlew.bat bootRun
  ```
- **URL de Acceso:** [http://localhost:8080/login](http://localhost:8080/login)

---

## 2. Usuarios y Roles de Prueba (Contraseña General: `123456`)

| Rol | Código | Correo | Equipo Asignado |
| :--- | :--- | :--- | :--- |
| **Estudiante** | `20210001` | estudiante@ematevar.com | PC-EXTERNA-01 |
| **Biblioteca** | `BIB001` | biblioteca@ematevar.com | PC-BIB-01 |
| **Decano Facultad** | `DEC001` | decano@ematevar.com | PC-DEC-01 |
| **Director EPIS** | `EPI001` | epis@ematevar.com | PC-EPI-01 |
| **Cobranzas** | `COB001` | cobranzas@ematevar.com | PC-COB-01 |
| **Administrador** | `ADM001` | admin@ematevar.com | PC-ADM-01 |

---

## 3. Guía de Sustentación Paso a Paso de las 20 Pruebas Funcionales Obligatorias

| N.° | Caso de Prueba | Procedimiento en Pantalla | Resultado Esperado |
| :---: | :--- | :--- | :--- |
| **01** | Login correcto con usuario de MySQL | Ingresar con `20210001` y `123456`. Luego ingresar el código 2FA mostrado en pantalla. | Acceso concedido al Portal del Estudiante. |
| **02** | Contraseña incorrecta | Ingresar con `20210001` y clave errónea (ej. `000000`). | Deniega acceso y muestra mensaje de error. |
| **2FA** | Verificación 2FA correcta e incorrecta | En el paso 2FA, ingresar un código incorrecto (ej. `111111`) y luego el correcto. | Rechaza código inválido; solo con código válido crea la sesión. |
| **03** | Mismo usuario intenta ingresar desde otra PC (Sesión Única) | Con la sesión abierta en un navegador, abrir ventana de incógnito o segundo navegador e ingresar con el mismo usuario. | **Bloquea el segundo acceso** e informa al usuario la existencia de sesión activa. |
| **04** | Estudiante consulta el portal 24/7 | Ingresar como estudiante en cualquier momento. | El portal permite visualizar catálogo, préstamos e historial sin restricción de horario. |
| **05** | Solicitud de préstamo fuera de horario | Cambiar el reloj o simular hora fuera del rango 08:00–20:00. | Bloquea la solicitud de préstamo con advertencia horaria del servidor. |
| **06** | Préstamo sin una de las firmas | En la pestaña de Biblioteca, intentar confirmar préstamo dejando un canvas de firma vacío. | Deniega la confirmación exigiendo las 2 firmas obligatorias. |
| **07** | Devolución con foto/observación | En Devolución, seleccionar estado 'Con Observaciones / Daño' e ingresar descripción. | Registra la devolución guardando la observación e incidencias. |
| **08** | Devolución con retraso | En Devolución, ingresar 3 días de retraso simulados. | Calcula y registra sanción de **6 días de suspensión** ($2 \times 3$) según Res. Nº 246-2023-UPT-CU. |
| **09** | Trámite sin PDF o sin firma | Intentar presentar un trámite sin adjuntar archivo o sin firmar en el canvas. | El sistema rechaza el envío. |
| **10** | PDF que no corresponde al formato | Adjuntar un archivo que no tenga cabecera PDF válida o formato institucional. | Rechaza y explica el motivo del formato no válido. |
| **11** | Decano consulta desde PC externa | En el selector superior, cambiar a `PC-EXTERNA-01` e ir a la pestaña Oficinas. | Muestra la bandeja de Facultad en modo solo consulta (botones de acción deshabilitados). |
| **12** | Decano atiende desde PC asignada | Cambiar el selector superior a `PC-DEC-01`. | Se habilitan los botones Aprobar, Rechazar y Derivar a EPIS. |
| **13** | Facultad deriva a EPIS | Con `PC-DEC-01`, hacer clic en **Derivar EPIS**. | El expediente pasa a la bandeja de EPIS y se registra el movimiento. |
| **14** | EPIS devuelve observado a Facultad | Con `PC-EPI-01`, hacer clic en **Observar** o **Responder a Facultad**. | El expediente regresa a Facultad con motivo registrado en el historial. |
| **15** | EPIS recibe trámite directo y observa | Presentar trámite con destino EPIS y atenderlo con `PC-EPI-01`. | Devuelve con observación conservando el historial. |
| **16** | Cobranzas atiende trámite | Con `PC-COB-01`, atender trámite dirigido a Cobranzas y aprobar/rechazar. | Registra decisión, respuesta y firma. |
| **17** | Trámite concluido | Aprobar un trámite en su instancia final. | Pasa automáticamente a la pestaña **Culminados** y sale de bandejas activas. |
| **18** | Filtro de Culminados por fechas | En la pestaña Culminados, ingresar fecha inicial y final y pulsar **Filtrar**. | Muestra únicamente los expedientes finalizados en el rango especificado. |
| **19** | Recordatorio de vencimiento | En la pestaña Notificaciones, pulsar **Ejecutar Barrido de Recordatorios**. | Identifica préstamos por vencer y genera avisos internos y correos institucionales. |
| **20** | Estudiante abre notificación | En la pestaña Notificaciones, hacer clic en **Marcar Leída**. | Actualiza el estado a leída y registra fecha y hora de lectura. |

---

## 4. Arquitectura Hexagonal y Declaración de Elementos
El detalle formal de clases, métodos, funciones y convenciones prefijadas con `efjav` se encuentra documentado en [ANEXO_C_DECLARACION_ELEMENTOS.md](ANEXO_C_DECLARACION_ELEMENTOS.md).
