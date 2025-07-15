-- Insertar roles
INSERT INTO roles (id, nombre) VALUES (1, 'ADMIN');
INSERT INTO roles (id, nombre) VALUES (2, 'USER');

-- Usuario de prueba(La contraseña se sobreescribira luego con la encriptada via seguridad)
INSERT INTO usuario (id, username, password) VALUES (1, 'admin', '$2a$10$psfKqeo05raa3/w4DPO6GeD3Zyi.3b5.dcdzWs5ucDExP86oJKsfO');
-- Usuario normal con rol USER
INSERT INTO usuario (id, username, password) VALUES (2, 'user', '$2a$12$GLe1KaApPVLsmAxEwJ3xteOpa8rgSg7yVQVZOsZtBy22NEAJNV9iq');

-- Relación usuario-rol (tabla intermedia)
INSERT INTO usuario_rol (usuario_id, rol_id) VALUES (1, 1); -- admin -> ADMIN
INSERT INTO usuario_rol (usuario_id, rol_id) VALUES (2, 2); -- user -> USER


--- Estados de Tareas
INSERT INTO estado_tarea (id, nombre) VALUES (1, 'Pendiente');
INSERT INTO estado_tarea (id, nombre) VALUES (2, 'En Progreso');
INSERT INTO estado_tarea (id, nombre) VALUES (3, 'Completada');

