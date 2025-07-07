--- Estados de Tareas
INSERT INTO estado_tarea (id, nombre) VALUES (1, 'Pendiente');
INSERT INTO estado_tarea (id, nombre) VALUES (2, 'En Progreso');
INSERT INTO estado_tarea (id, nombre) VALUES (3, 'Completada');

-- Usuario de prueba(La contraseña se sobreescribira luego con la encriptada via seguridad)
INSERT INTO usuario (id, username, password) VALUES (1, 'admin', '$2a$10$psfKqeo05raa3/w4DPO6GeD3Zyi.3b5.dcdzWs5ucDExP86oJKsfO');