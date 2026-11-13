-- Script de creacion de base de datos y tablas
-- Sistema Integral de Gestion de Biblioteca y Tramites Academicos
-- Estudiante: Emanuel Felipe Jose Atencio Vargas (efjav)
-- Evaluacion: Examen de Unidad - Programacion III (UPT)

CREATE DATABASE IF NOT EXISTS db_biblioteca_tramites CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE db_biblioteca_tramites;

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS efjav_notificacion_lectura;
DROP TABLE IF EXISTS efjav_notificaciones;
DROP TABLE IF EXISTS efjav_observaciones;
DROP TABLE IF EXISTS efjav_documentos;
DROP TABLE IF EXISTS efjav_movimientos_tramite;
DROP TABLE IF EXISTS efjav_tramites;
DROP TABLE IF EXISTS efjav_instancias;
DROP TABLE IF EXISTS efjav_penalidades;
DROP TABLE IF EXISTS efjav_evidencias;
DROP TABLE IF EXISTS efjav_firmas;
DROP TABLE IF EXISTS efjav_devoluciones;
DROP TABLE IF EXISTS efjav_prestamos;
DROP TABLE IF EXISTS efjav_libros;
DROP TABLE IF EXISTS efjav_auditoria;
DROP TABLE IF EXISTS efjav_sesiones;
DROP TABLE IF EXISTS efjav_equipos_autorizados;
DROP TABLE IF EXISTS efjav_usuarios_roles;
DROP TABLE IF EXISTS efjav_roles;
DROP TABLE IF EXISTS efjav_usuarios;
SET FOREIGN_KEY_CHECKS = 1;

-- 1. Tabla de Usuarios
CREATE TABLE efjav_usuarios (
    efjav_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    efjav_codigo VARCHAR(50) NOT NULL UNIQUE,
    efjav_nombres VARCHAR(150) NOT NULL,
    efjav_correo VARCHAR(100) NOT NULL UNIQUE,
    efjav_password_hash VARCHAR(255) NOT NULL,
    efjav_totp_secret VARCHAR(64) NULL,
    efjav_totp_activo BOOLEAN DEFAULT TRUE,
    efjav_otp_actual VARCHAR(10) NULL,
    efjav_otp_expiracion DATETIME NULL,
    efjav_intentos_fallidos INT DEFAULT 0,
    efjav_bloqueado BOOLEAN DEFAULT FALSE,
    efjav_estado VARCHAR(20) DEFAULT 'ACTIVO',
    efjav_fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 2. Tabla de Roles
CREATE TABLE efjav_roles (
    efjav_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    efjav_nombre VARCHAR(50) NOT NULL UNIQUE,
    efjav_descripcion VARCHAR(150) NOT NULL
) ENGINE=InnoDB;

-- Tabla intermedia Usuarios - Roles
CREATE TABLE efjav_usuarios_roles (
    efjav_usuario_id BIGINT NOT NULL,
    efjav_rol_id BIGINT NOT NULL,
    PRIMARY KEY (efjav_usuario_id, efjav_rol_id),
    CONSTRAINT fk_efjav_ur_usuario FOREIGN KEY (efjav_usuario_id) REFERENCES efjav_usuarios(efjav_id) ON DELETE CASCADE,
    CONSTRAINT fk_efjav_ur_rol FOREIGN KEY (efjav_rol_id) REFERENCES efjav_roles(efjav_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 3. Tabla de Equipos Autorizados (Enrolamiento de PC)
CREATE TABLE efjav_equipos_autorizados (
    efjav_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    efjav_identificador_equipo VARCHAR(100) NOT NULL UNIQUE,
    efjav_nombre_equipo VARCHAR(100) NOT NULL,
    efjav_oficina VARCHAR(100) NOT NULL,
    efjav_ip_asignada VARCHAR(45) NULL,
    efjav_usuario_responsable_id BIGINT NULL,
    efjav_estado VARCHAR(20) DEFAULT 'AUTORIZADO',
    efjav_fecha_enrolamiento DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_efjav_equipo_usuario FOREIGN KEY (efjav_usuario_responsable_id) REFERENCES efjav_usuarios(efjav_id)
) ENGINE=InnoDB;

-- 4. Tabla de Sesiones Activas (Sesion Unica)
CREATE TABLE efjav_sesiones (
    efjav_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    efjav_usuario_id BIGINT NOT NULL,
    efjav_session_token VARCHAR(255) NOT NULL UNIQUE,
    efjav_identificador_equipo VARCHAR(100) NULL,
    efjav_ip VARCHAR(45) NOT NULL,
    efjav_user_agent VARCHAR(255) NULL,
    efjav_inicio DATETIME DEFAULT CURRENT_TIMESTAMP,
    efjav_ultima_actividad DATETIME DEFAULT CURRENT_TIMESTAMP,
    efjav_expiracion DATETIME NOT NULL,
    efjav_estado VARCHAR(20) DEFAULT 'ACTIVA',
    CONSTRAINT fk_efjav_sesion_usuario FOREIGN KEY (efjav_usuario_id) REFERENCES efjav_usuarios(efjav_id)
) ENGINE=InnoDB;

-- 5. Tabla de Auditoria Inmutable
CREATE TABLE efjav_auditoria (
    efjav_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    efjav_usuario_codigo VARCHAR(50) NOT NULL,
    efjav_operacion VARCHAR(100) NOT NULL,
    efjav_entidad VARCHAR(100) NOT NULL,
    efjav_detalle TEXT NULL,
    efjav_ip VARCHAR(45) NULL,
    efjav_equipo VARCHAR(100) NULL,
    efjav_resultado VARCHAR(50) NOT NULL,
    efjav_fecha_hora DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 6. Tabla de Libros
CREATE TABLE efjav_libros (
    efjav_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    efjav_codigo VARCHAR(50) NOT NULL UNIQUE,
    efjav_titulo VARCHAR(255) NOT NULL,
    efjav_autor VARCHAR(150) NOT NULL,
    efjav_categoria VARCHAR(100) NOT NULL,
    efjav_ejemplares_totales INT NOT NULL DEFAULT 1,
    efjav_ejemplares_disponibles INT NOT NULL DEFAULT 1,
    efjav_estado VARCHAR(30) DEFAULT 'DISPONIBLE'
) ENGINE=InnoDB;

-- 7. Tabla de Firmas Digitalizadas
CREATE TABLE efjav_firmas (
    efjav_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    efjav_autor_id BIGINT NOT NULL,
    efjav_nombre_firmante VARCHAR(150) NOT NULL,
    efjav_rol_firmante VARCHAR(50) NOT NULL,
    efjav_referencia_archivo VARCHAR(255) NOT NULL,
    efjav_hash_sha256 VARCHAR(64) NOT NULL,
    efjav_contexto_operacion VARCHAR(100) NOT NULL,
    efjav_fecha_firma DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_efjav_firma_autor FOREIGN KEY (efjav_autor_id) REFERENCES efjav_usuarios(efjav_id)
) ENGINE=InnoDB;

-- 8. Tabla de Prestamos de Libros
CREATE TABLE efjav_prestamos (
    efjav_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    efjav_codigo VARCHAR(50) NOT NULL UNIQUE,
    efjav_libro_id BIGINT NOT NULL,
    efjav_estudiante_id BIGINT NOT NULL,
    efjav_bibliotecario_id BIGINT NULL,
    efjav_fecha_solicitud DATETIME DEFAULT CURRENT_TIMESTAMP,
    efjav_fecha_entrega DATETIME NULL,
    efjav_fecha_limite_devolucion DATETIME NOT NULL,
    efjav_firma_estudiante_id BIGINT NULL,
    efjav_firma_bibliotecario_id BIGINT NULL,
    efjav_estado VARCHAR(30) DEFAULT 'SOLICITADO', -- SOLICITADO, ACTIVO, DEVUELTO, VENCIDO, RECHAZADO
    CONSTRAINT fk_efjav_prestamo_libro FOREIGN KEY (efjav_libro_id) REFERENCES efjav_libros(efjav_id),
    CONSTRAINT fk_efjav_prestamo_estudiante FOREIGN KEY (efjav_estudiante_id) REFERENCES efjav_usuarios(efjav_id),
    CONSTRAINT fk_efjav_prestamo_bibliotecario FOREIGN KEY (efjav_bibliotecario_id) REFERENCES efjav_usuarios(efjav_id),
    CONSTRAINT fk_efjav_prestamo_firma_est FOREIGN KEY (efjav_firma_estudiante_id) REFERENCES efjav_firmas(efjav_id),
    CONSTRAINT fk_efjav_prestamo_firma_bib FOREIGN KEY (efjav_firma_bibliotecario_id) REFERENCES efjav_firmas(efjav_id)
) ENGINE=InnoDB;

-- 9. Tabla de Devoluciones
CREATE TABLE efjav_devoluciones (
    efjav_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    efjav_prestamo_id BIGINT NOT NULL UNIQUE,
    efjav_fecha_devolucion DATETIME DEFAULT CURRENT_TIMESTAMP,
    efjav_bibliotecario_id BIGINT NOT NULL,
    efjav_firma_estudiante_id BIGINT NOT NULL,
    efjav_firma_bibliotecario_id BIGINT NOT NULL,
    efjav_estado_fisico_libro VARCHAR(50) DEFAULT 'CONFORME', -- CONFORME, OBSERVADO, EN_REPARACION
    efjav_observaciones TEXT NULL,
    CONSTRAINT fk_efjav_dev_prestamo FOREIGN KEY (efjav_prestamo_id) REFERENCES efjav_prestamos(efjav_id),
    CONSTRAINT fk_efjav_dev_bibliotecario FOREIGN KEY (efjav_bibliotecario_id) REFERENCES efjav_usuarios(efjav_id),
    CONSTRAINT fk_efjav_dev_firma_est FOREIGN KEY (efjav_firma_estudiante_id) REFERENCES efjav_firmas(efjav_id),
    CONSTRAINT fk_efjav_dev_firma_bib FOREIGN KEY (efjav_firma_bibliotecario_id) REFERENCES efjav_firmas(efjav_id)
) ENGINE=InnoDB;

-- 10. Tabla de Evidencias (Fotos o documentos de dano)
CREATE TABLE efjav_evidencias (
    efjav_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    efjav_nombre_archivo VARCHAR(255) NOT NULL,
    efjav_ruta_almacenamiento VARCHAR(255) NOT NULL,
    efjav_tipo_mime VARCHAR(100) NOT NULL,
    efjav_tamanio_bytes BIGINT NOT NULL,
    efjav_descripcion TEXT NOT NULL,
    efjav_operacion_tipo VARCHAR(50) NOT NULL, -- DEVOLUCION, TRAMITE
    efjav_operacion_id BIGINT NOT NULL,
    efjav_fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 11. Tabla de Penalidades de Biblioteca
CREATE TABLE efjav_penalidades (
    efjav_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    efjav_prestamo_id BIGINT NOT NULL,
    efjav_estudiante_id BIGINT NOT NULL,
    efjav_fecha_vencimiento DATETIME NOT NULL,
    efjav_fecha_real_devolucion DATETIME NOT NULL,
    efjav_dias_retraso INT NOT NULL,
    efjav_dias_suspension INT NOT NULL,
    efjav_regla_aplicada VARCHAR(255) NOT NULL, -- Res. No 246-2023-UPT-CU: 2x dias demora
    efjav_fecha_inicio_suspension DATE NOT NULL,
    efjav_fecha_fin_suspension DATE NOT NULL,
    efjav_estado VARCHAR(20) DEFAULT 'VIGENTE', -- VIGENTE, CUMPLIDA, ANULADA
    efjav_fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_efjav_pen_prestamo FOREIGN KEY (efjav_prestamo_id) REFERENCES efjav_prestamos(efjav_id),
    CONSTRAINT fk_efjav_pen_estudiante FOREIGN KEY (efjav_estudiante_id) REFERENCES efjav_usuarios(efjav_id)
) ENGINE=InnoDB;

-- 12. Tabla de Instancias de Tramite
CREATE TABLE efjav_instancias (
    efjav_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    efjav_codigo VARCHAR(50) NOT NULL UNIQUE,
    efjav_nombre VARCHAR(100) NOT NULL,
    efjav_descripcion VARCHAR(200) NULL
) ENGINE=InnoDB;

-- 13. Tabla de Tramites Academicos
CREATE TABLE efjav_tramites (
    efjav_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    efjav_codigo_seguimiento VARCHAR(50) NOT NULL UNIQUE,
    efjav_estudiante_id BIGINT NOT NULL,
    efjav_tipo_tramite VARCHAR(100) NOT NULL,
    efjav_asunto VARCHAR(255) NOT NULL,
    efjav_instancia_destino_id BIGINT NOT NULL,
    efjav_ubicacion_actual_id BIGINT NOT NULL,
    efjav_estado VARCHAR(30) NOT NULL DEFAULT 'ENVIADO', -- BORRADOR, ENVIADO, RECIBIDO, EN_PROCESO, OBSERVADO, RECHAZADO, ARCHIVADO, CONCLUIDO
    efjav_fecha_envio DATETIME DEFAULT CURRENT_TIMESTAMP,
    efjav_fecha_conclusion DATETIME NULL,
    efjav_firma_estudiante_id BIGINT NOT NULL,
    CONSTRAINT fk_efjav_tram_estudiante FOREIGN KEY (efjav_estudiante_id) REFERENCES efjav_usuarios(efjav_id),
    CONSTRAINT fk_efjav_tram_destino FOREIGN KEY (efjav_instancia_destino_id) REFERENCES efjav_instancias(efjav_id),
    CONSTRAINT fk_efjav_tram_ubicacion FOREIGN KEY (efjav_ubicacion_actual_id) REFERENCES efjav_instancias(efjav_id),
    CONSTRAINT fk_efjav_tram_firma_est FOREIGN KEY (efjav_firma_estudiante_id) REFERENCES efjav_firmas(efjav_id)
) ENGINE=InnoDB;

-- 14. Tabla de Documentos Asociados a Tramites
CREATE TABLE efjav_documentos (
    efjav_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    efjav_tramite_id BIGINT NOT NULL,
    efjav_nombre_original VARCHAR(255) NOT NULL,
    efjav_ruta_almacenamiento VARCHAR(255) NOT NULL,
    efjav_tipo_documento VARCHAR(50) NOT NULL, -- SOLICITUD_PDF, RESPUESTA_PDF, ANEXO
    efjav_version INT DEFAULT 1,
    efjav_hash_sha256 VARCHAR(64) NOT NULL,
    efjav_tamanio_bytes BIGINT NOT NULL,
    efjav_fecha_subida DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_efjav_doc_tramite FOREIGN KEY (efjav_tramite_id) REFERENCES efjav_tramites(efjav_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 15. Tabla de Movimientos y Derivaciones de Tramites
CREATE TABLE efjav_movimientos_tramite (
    efjav_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    efjav_tramite_id BIGINT NOT NULL,
    efjav_instancia_origen_id BIGINT NOT NULL,
    efjav_instancia_destino_id BIGINT NOT NULL,
    efjav_responsable_id BIGINT NOT NULL,
    efjav_estado_resultante VARCHAR(30) NOT NULL,
    efjav_firma_responsable_id BIGINT NULL,
    efjav_observacion TEXT NULL,
    efjav_fecha_movimiento DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_efjav_mov_tramite FOREIGN KEY (efjav_tramite_id) REFERENCES efjav_tramites(efjav_id) ON DELETE CASCADE,
    CONSTRAINT fk_efjav_mov_origen FOREIGN KEY (efjav_instancia_origen_id) REFERENCES efjav_instancias(efjav_id),
    CONSTRAINT fk_efjav_mov_destino FOREIGN KEY (efjav_instancia_destino_id) REFERENCES efjav_instancias(efjav_id),
    CONSTRAINT fk_efjav_mov_responsable FOREIGN KEY (efjav_responsable_id) REFERENCES efjav_usuarios(efjav_id),
    CONSTRAINT fk_efjav_mov_firma FOREIGN KEY (efjav_firma_responsable_id) REFERENCES efjav_firmas(efjav_id)
) ENGINE=InnoDB;

-- 16. Tabla de Observaciones Detalladas
CREATE TABLE efjav_observaciones (
    efjav_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    efjav_tramite_id BIGINT NOT NULL,
    efjav_oficina_id BIGINT NOT NULL,
    efjav_responsable_id BIGINT NOT NULL,
    efjav_motivo VARCHAR(255) NOT NULL,
    efjav_detalle TEXT NOT NULL,
    efjav_fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_efjav_obs_tramite FOREIGN KEY (efjav_tramite_id) REFERENCES efjav_tramites(efjav_id) ON DELETE CASCADE,
    CONSTRAINT fk_efjav_obs_oficina FOREIGN KEY (efjav_oficina_id) REFERENCES efjav_instancias(efjav_id),
    CONSTRAINT fk_efjav_obs_responsable FOREIGN KEY (efjav_responsable_id) REFERENCES efjav_usuarios(efjav_id)
) ENGINE=InnoDB;

-- 17. Tabla de Notificaciones
CREATE TABLE efjav_notificaciones (
    efjav_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    efjav_destinatario_id BIGINT NOT NULL,
    efjav_canal VARCHAR(30) NOT NULL DEFAULT 'SISTEMA', -- SISTEMA, CORREO, AMBOS
    efjav_titulo VARCHAR(200) NOT NULL,
    efjav_contenido TEXT NOT NULL,
    efjav_tipo VARCHAR(50) NOT NULL, -- RECORDATORIO_VENCIMIENTO, INICIO_SESION, ESTADO_TRAMITE
    efjav_estado_envio VARCHAR(30) DEFAULT 'ENVIADO', -- ENVIADO, ENTREGADO, FALLIDO
    efjav_leida BOOLEAN DEFAULT FALSE,
    efjav_fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_efjav_notif_destinatario FOREIGN KEY (efjav_destinatario_id) REFERENCES efjav_usuarios(efjav_id)
) ENGINE=InnoDB;

-- 18. Tabla de Registro de Lectura de Notificaciones
CREATE TABLE efjav_notificacion_lectura (
    efjav_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    efjav_notificacion_id BIGINT NOT NULL UNIQUE,
    efjav_usuario_id BIGINT NOT NULL,
    efjav_fecha_lectura DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_efjav_nl_notif FOREIGN KEY (efjav_notificacion_id) REFERENCES efjav_notificaciones(efjav_id) ON DELETE CASCADE,
    CONSTRAINT fk_efjav_nl_usuario FOREIGN KEY (efjav_usuario_id) REFERENCES efjav_usuarios(efjav_id)
) ENGINE=InnoDB;
