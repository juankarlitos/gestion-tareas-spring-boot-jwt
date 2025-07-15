package com.tareas.gestion.controller;

import com.tareas.gestion.dto.LoginRequest;
import com.tareas.gestion.dto.LoginResponse;
import com.tareas.gestion.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.stream.Collectors;

/*
Es donde se recibe la solicitud con el usuario y la contraseña,
se verifica que sean válidos y se genera el JWT si todo está bien.
 */

@RestController//indica que todos los métodos retornan JSON directamente.
@RequestMapping("/api/auth")// define la ruta base: todos los endpoints aquí empiezan con /api/auth.
public class AuthController {
    private final AuthenticationManager authenticationManager;//realizamos la inyeccion de dependencia
    private final JwtUtil jwtUtil;//realizamos la inyeccion de dependencia

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;//bjeto de Spring que verifica usuario y contraseña.
        this.jwtUtil = jwtUtil;// clase personalizada que genera el token JWT.
    }
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request){//Recibe un LoginRequest (un DTO con username y password).
        //Retorna un LoginResponse con el token JWT.

        System.out.println("Intentanto loguear con : " + request.getUsername() + " - " + request.getPassword());
        //Aquí estás validando las credenciales usando Spring Security.
        //verificamos con MyUserDetailsService si el usuario existe y si la contraseña
        // coincide (usando BCryptPasswordEncoder).
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        //Obtener datos del usuario autenticado
        //De la autenticación, extraes el UserDetails.
        //Luego obtienes sus roles, que son necesarios para agregarlos al JWT.
        UserDetails user = (UserDetails) authentication.getPrincipal();
        List<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());//cierre

        //Usas JwtUtil para generar el token con el username y los roles.
        //Este token será enviado al frontend o a Postman para autenticarse en los siguientes endpoints.
        String token = jwtUtil.generateToken(user.getUsername(), roles);//
        System.out.println("LOGIN EXITOSO: " + user.getUsername());
        System.out.println("ROLES: " + roles);
        return new LoginResponse(token);//Devuelves una respuesta con el token JWT.
    }
}
//Explicacion
//AuthController se encarga de manejar el login de los usuarios. Expone un endpoint /api/auth/login
// que recibe las credenciales, las valida usando AuthenticationManager, y si son correctas,
// genera un token JWT usando la clase JwtUtil. Ese token incluye los roles del usuario y será usado
// para autenticación en el resto del sistema