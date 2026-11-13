-- Datos de prueba e iniciales del sistema
-- Passwords hasheadas con BCrypt para '123456': $2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi

USE db_biblioteca_tramites;

-- Roles
INSERT INTO efjav_roles (efjav_id, efjav_nombre, efjav_descripcion) VALUES
(1, 'ROLE_ESTUDIANTE', 'Estudiante regular de la UPT'),
(2, 'ROLE_BIBLIOTECA', 'Jefe y personal de Biblioteca'),
(3, 'ROLE_DECANO', 'Decano de la Facultad'),
(4, 'ROLE_EPIS', 'Director de Escuela Profesional de Ingenieria de Sistemas'),
(5, 'ROLE_COBRANZAS', 'Responsable de la Oficina de Cobranzas'),
(6, 'ROLE_ADMIN', 'Administrador del Sistema');

-- Usuarios iniciales para cada rol (Password: 123456)
-- Secret TOTP base32 de prueba: JBSWY3DPEHPK3PXP (facil de configurar en Bitwarden)
INSERT INTO efjav_usuarios (efjav_id, efjav_codigo, efjav_nombres, efjav_correo, efjav_password_hash, efjav_totp_secret, efjav_totp_activo, efjav_estado) VALUES
(1, '20210001', 'Emanuel Felipe Jose Atencio Vargas', 'estudiante@ematevar.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'JBSWY3DPEHPK3PXP', TRUE, 'ACTIVO'),
(2, 'BIB001', 'Carlos Mamani Flores', 'biblioteca@ematevar.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'JBSWY3DPEHPK3PXP', TRUE, 'ACTIVO'),
(3, 'DEC001', 'Dr. Alberto Valdivia Romero', 'decano@ematevar.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'JBSWY3DPEHPK3PXP', TRUE, 'ACTIVO'),
(4, 'EPI001', 'Ing. Roberto Silva Castro', 'epis@ematevar.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'JBSWY3DPEHPK3PXP', TRUE, 'ACTIVO'),
(5, 'COB001', 'Lic. Patricia Gomez Morales', 'cobranzas@ematevar.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'JBSWY3DPEHPK3PXP', TRUE, 'ACTIVO'),
(6, 'ADM001', 'Admin General UPT', 'admin@ematevar.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'JBSWY3DPEHPK3PXP', TRUE, 'ACTIVO');

-- Asignacion de Roles
INSERT INTO efjav_usuarios_roles (efjav_usuario_id, efjav_rol_id) VALUES
(1, 1), -- Estudiante
(2, 2), -- Biblioteca
(3, 3), -- Decano
(4, 4), -- EPIS
(5, 5), -- Cobranzas
(6, 6); -- Admin

-- Equipos Autorizados Institucionales (Para control de PC asignada)
INSERT INTO efjav_equipos_autorizados (efjav_id, efjav_identificador_equipo, efjav_nombre_equipo, efjav_oficina, efjav_usuario_responsable_id, efjav_estado) VALUES
(1, 'PC-BIB-01', 'Computadora Central Biblioteca', 'BIBLIOTECA', 2, 'AUTORIZADO'),
(2, 'PC-DEC-01', 'Equipo Despacho Decano FAING', 'FACULTAD', 3, 'AUTORIZADO'),
(3, 'PC-EPI-01', 'Equipo Direccion EPIS', 'EPIS', 4, 'AUTORIZADO'),
(4, 'PC-COB-01', 'Equipo Modulo Cobranzas', 'COBRANZAS', 5, 'AUTORIZADO');

-- Instancias de Tramite
INSERT INTO efjav_instancias (efjav_id, efjav_codigo, efjav_nombre, efjav_descripcion) VALUES
(1, 'FACULTAD', 'Decanato de Facultad', 'Despacho del Decano de Facultad'),
(2, 'EPIS', 'Direccion de EPIS', 'Escuela Profesional de Ingenieria de Sistemas'),
(3, 'COBRANZAS', 'Unidad de Cobranzas', 'Gestion economica y pensiones');

-- Libros de Catalogo
INSERT INTO efjav_libros (efjav_id, efjav_codigo, efjav_titulo, efjav_autor, efjav_categoria, efjav_ejemplares_totales, efjav_ejemplares_disponibles, efjav_estado) VALUES
(1, 'LIB-001', 'Estructuras de Datos y Algoritmos en Java', 'Robert Lafore', 'Sistemas e Informatica', 3, 3, 'DISPONIBLE'),
(2, 'LIB-002', 'Ingenieria de Software: Un Enfoque Practico', 'Roger S. Pressman', 'Ingenieria de Software', 2, 2, 'DISPONIBLE'),
(3, 'LIB-003', 'Sistemas de Bases de Datos', 'Ramez Elmasri & Shamkant Navathe', 'Bases de Datos', 4, 4, 'DISPONIBLE'),
(4, 'LIB-004', 'Clean Code: A Handbook of Agile Software Craftsmanship', 'Robert C. Martin', 'Metodologias', 2, 2, 'DISPONIBLE');
