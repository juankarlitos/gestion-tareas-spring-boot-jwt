package com.tareas.gestion.util;

import com.tareas.gestion.security.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
/*
Esta clase intercepta cada solicitud HTTP, revisa si hay un token JWT y,
 si es válido, autentica al usuario en el contexto de Spring Security.

 */
@Component//Spring la detecta automáticamente como un Bean.
public class JwtFilter extends OncePerRequestFilter {//asegura que el filtro se ejecute una sola vez por cada solicitud HTTP.

    private final JwtUtil jwtUtil;// clase que extrae el username del token y valida si el token es correcto.
    private final MyUserDetailsService userDetailsService;//se encarga de cargar los datos del usuario desde la base (o en memoria).

    public JwtFilter(JwtUtil jwtUtil, MyUserDetailsService userDetailsService) {//Constructor donde inyectamos los dos beans anteriores.
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }


    @Override//Este método se ejecuta en cada solicitud HTTP.
    protected  void doFilterInternal(HttpServletRequest request,
                                     HttpServletResponse response,
                                     FilterChain filterChain) throws ServletException, IOException {//cierre
        //*si estás accediendo al login o a la consola H2, no se requiere token y se deja pasar directamente.
        String path = request.getServletPath();

        if (path.equals("/api/auth/login") || path.startsWith("/h2-console")){
            filterChain.doFilter(request, response);
            return;// se coloco para salir del metodo inmediatamente, ya que si no haces el return
            //la ejecución continuaría más abajo y trataría de verificar el token JWT...
            // ¡aunque no debería hacerlo en esas rutas públicas!
            //filterChain.doFilter(request, response); deja pasar la petición.
            //return; interrumpe el resto del filtro y evita que se ejecute la validación JWT
            //Así protegemos rutas privadas, pero permites acceso abierto al login y la consola H2.
        }//*cierre

        final String authHeader = request.getHeader("Authorization");//Obtiene el header Authorization del request,
        // donde debería venir el JWT.

        if (authHeader != null && authHeader.startsWith("Bearer ")){//verifica que el header exista y empiece con
            // "Bearer ", que es el formato estándar.

            try {
                String token = authHeader.substring(7);
                String username = jwtUtil.extractUsername(token);// + Quita la palabra "Bearer " (7 caracteres) y extrae el token puro.
                //Luego extrae el username desde el JWT.

                //Si el token tiene un username válido y aún no hay usuario autenticado en el contexto de Spring, continúa
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                    //Busca al usuario usando su username con tu clase MyUserDetailsService.
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    if (jwtUtil.isTokenValid(token)){//Si el token es válido (firmado correctamente y no expirado)//

                        //*Crea una autenticación completa del usuario, con sus roles.
                        //Y la establece en el contexto de seguridad de Spring, lo que significa que desde ahora en
                        // adelante la solicitud está autenticada.
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }//*cierre
                }
            } catch (Exception e){
                System.out.println("Error en JwtFilter : " + e.getMessage());//Captura cualquier error para que no caiga
                // la aplicación si algo sale mal.
            }
        }
        filterChain.doFilter(request, response);//Deja continuar con el resto de filtros o el controlador si todo está bien.
    }
}
//Explicacion

//Implementé un filtro personalizado JwtFilter que intercepta todas las peticiones HTTP y revisa si el header de autorización
//tiene un token JWT. Si el token existe y es válido, extraigo el username y cargo el usuario con sus roles. Luego creo un
// UsernamePasswordAuthenticationToken y lo coloco en el SecurityContext. Esto permite que Spring sepa quién es el
// usuario autenticado y qué permisos tiene, sin necesidad de sesión, ya que es un sistema stateless.”