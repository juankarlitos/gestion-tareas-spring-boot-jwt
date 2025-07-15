package com.tareas.gestion.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
Esta clase se encarga de crear, validar y analizar tokens JW
 */

@Component//Spring pueda inyectarla donde se necesite, como en JwtFilter.
public class JwtUtil  {

    @Value("${jwt.secret}")
    private String SECRET_KEY;//Inyecta la clave secreta definida en el archivo application.properties

    private final long EXPIRATION_TIME = 86400000; // 1 día
    //Define que el token expira en 24 horas (86400000 milisegundos).

    //Este método genera un token JWT a partir del username y la lista de roles del usuario.
    public String generateToken(String username, List<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);//Los claims son datos personalizados que se pueden incluir en el JWT.
                                  //Aquí estás agregando los roles para usarlos luego en autorizaciones.

        return Jwts.builder()
                .setClaims(claims)//agregas los datos personalizados.
                .setSubject(username)//el username del usuario.
                .setIssuedAt(new Date())//fecha de creación del token.
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))//fecha de expiración (24h después).
                .signWith(getSignKey(), SignatureAlgorithm.HS256)//firma el token con una clave secreta y algoritmo HS256.
                .compact();// genera el token final (como un String).
    }
    public String extractUsername(String token) {//Extraer username del token
        return extractAllClaims(token).getSubject();//Extrae el subject, que es el username, del token recibido.
    }
    public boolean isTokenValid(String token) {//Validar si el token es correcto
        try {
            extractAllClaims(token);// Intenta analizar (parsear) el token.
            return true;//Si no lanza error, el token es válido.
        } catch (Exception e) {//Si lanza error (por expirado o mal firmado), retorna false.
            System.out.println("Token inválido: " + e.getMessage());
            return false;
        }
    }
    private Claims extractAllClaims(String token) {//Obtener todos los claims (datos)
        return Jwts.parserBuilder()//Aquí parseas el token usando la misma clave secreta.
                .setSigningKey(getSignKey())//Esto retorna el cuerpo (claims) del token con toda su información
                .build()
                .parseClaimsJws(token)//subject, roles, fecha, etc.
                .getBody();
    }
    private Key getSignKey() {//Obtener la clave secreta para firmar/verificar
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        //Convierte la cadena SECRET_KEY en un objeto Key, usando el algoritmo HMAC-SHA para firmar
        // o verificar el token.
    }
}

//Explicacion de la clase
//JwtUtil es la clase que se encarga de generar tokens JWT cuando un usuario se loguea correctamente.
// Utiliza una clave secreta para firmar el token con HS256, le agrega el username como subject,
// y también incluye los roles como claims personalizados. Además, puede validar si un token es correcto
// y extraer el username desde él. Esto permite que el sistema sea stateless, ya que toda la información del
// usuario viaja en el propio token.