# Gestión de Tareas - Spring Boot + JWT
Este proyecto es una API RESTful desarrollada con Java 17 y Spring Boot 3.5 que permite gestionar tareas con autenticación y autorización segura basada en JWT (JSON Web Token). 
Se implementan dos roles de usuario: ADMIN y USER, y las contraseñas están encriptadas con BCrypt en la base de datos H2 embebida.

# Tecnologías usadas
Java 17

Spring Boot 3.5.x   
Spring Security   
JWT (JSON Web Token)  
H2 Database (modo en memoria)    
Swagger UI (Springdoc OpenAPI)   
Maven   
Seguridad con JWT  
Login genera un token JWT firmado.  
Se usa Bearer Token en el header Authorization para autenticar.    
Rutas protegidas por roles: ROLE_ADMIN y ROLE_USER.   
Contraseñas no se almacenan en texto plano, se encriptan con BCrypt.   
Usuarios habilitados (Base de datos H2)    
Username	Contraseña	Rol   
admin	12345	ADMIN   
user	12345	USER   

## Endpoints de Autenticación

## Login

POST /api/auth/login
Request JSON:

{ 
"username": "admin",
"password": "12345"
}

Respuesta:
{ 
"jwt": "eyJhbGciOiJIUzI1NiIsInR..." 
}

El token devuelto debe usarse como Bearer Token en los headers:

Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI...

## Endpoints para Tareas

## Crear Tarea

POST /api/tareas Requiere: Bearer Token 
Body:

{

"titulo": "Estudiar Spring Boot",
"descripcion": "Repasar seguridad y JWT"
} 

Roles permitidos: ADMIN, USER

## Listar todas las tareas

GET /api/tareas 

Requiere: Bearer Token 

Roles permitidos: ADMIN, USER

## Obtener tarea por ID

GET /api/tareas/{id} 

Requiere: Bearer Token 

Roles permitidos: ADMIN, USER

## Actualizar tarea

PUT /api/tareas/{id}

Requiere: Bearer Token

Body:

{
"titulo": "Título actualizado", 
"descripcion": "Descripción actualizada" 
}
Roles permitidos: ADMIN, USER

## Eliminar tarea

DELETE /api/tareas/{id} 

Requiere: Bearer Token Roles permitidos: ADMIN, USER

Swagger UI Puedes probar todos los endpoints visualmente desde:

http://localhost:9097/swagger-ui.html (No olvides presionar "Authorize" y pegar tu token JWT como Bearer )

API-DOCS

http://localhost:9097/v3/api-docs

## Base de datos H2 (modo dev) 

Acceso a consola H2: 

http://localhost:9097/h2-console

Parámetros:

JDBC URL: jdbc:h2:mem:tareasdb

Username: sa 
Password: (vacío)

## Estructura del proyecto

src/ ├── controller/
# Controladores REST ├── dto/

# Clases DTO para requests y responses ├── entity/

# Entidades JPA ├── repository/ # Repositorios Spring Data JPA ├── security/ 

# Configuración JWT y filtros de seguridad ├── service/ 

# Interfaces y lógica de negocio └── config/ 

# Configuración general


# Autor
Juan Inostroza Chávez GitHub: juankarlitos](https://github.com/juankarlitos

# Licencia
Este proyecto es de uso libre para fines educativos y profesionales. ¡Contribuciones y estrellas son bienvenidas!
