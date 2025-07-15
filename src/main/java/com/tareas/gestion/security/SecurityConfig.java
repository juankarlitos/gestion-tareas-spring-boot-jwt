package com.tareas.gestion.security;

import com.tareas.gestion.util.JwtFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration// clase de configuracion
@EnableMethodSecurity//activamos la seguridada a nivel de metodo(permite restringir acceso en los metodos de los servicios o controladores)

public class SecurityConfig {

    private final JwtFilter jwtFilter;//inyectamos el filtro, lo lee el token, lo valida y autentica al usuario
    //se ejecuta antes de que spring decida se deja o no pasar la peticion

    public SecurityConfig(JwtFilter jwtFilter) {//constructor que inyecta la dependencia del filtro
        this.jwtFilter = jwtFilter;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //aqui configuramos la cadena de filtros de seguridad
        http
                .csrf(csrf -> csrf.disable())// Cross-Site-Request Forgery se usa en apps web con formularios
                //como mi app es RESTful y usa JWT(sin session)no necesitamos el CSRF
                .headers(headers -> headers.frameOptions().disable()) // se desactiva la proteccion x-Frame-Option para
                //para que se pueda mostrar la consola H2 embebida en el navegador

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login", "/h2-console/**").permitAll()
                        //estas rutas se usan para permitir el login y acceso a la consola H2

                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html"
                        ).permitAll()//permite el aceso publico a la documentacion Swagger
                        .anyRequest().authenticated()//Si no se permite explícitamente, necesitará JWT válido
                )
                .exceptionHandling(e ->
                        e.authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"Token inválido o ausente\"}");
                        })
                )//Si el token es inválido o no está, se devuelve una respuesta con mensaje claro.
                .sessionManagement(session -> session//JWT no necesita sesiones.
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)//Cada request debe traer su token.
                )//Stateless = no se guarda nada del usuario en el servidor.
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
                //filtro JWT se ejecuta antes del filtro estándar de login
                //quí es donde tu filtro intercepta el token, lo valida y autentica al usuario antes que Spring.

        return http.build();//Se construye y devuelve la configuración de seguridad.
    }
    @Bean
    public PasswordEncoder passwordEncoder(){//para encriptar contraseñas
        return new BCryptPasswordEncoder();//Es el encoder estándar y seguro que usa Spring.
    }                                     //Se utiliza cuando creas o validas contraseñas en login o registro.
    @Bean//Para autenticar usuarios con username y password
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        //Este AuthenticationManager se inyecta en tu servicio de autenticación.
        //Lo usas cuando alguien hace login: valida que el usuario y contraseña sean correctos.
        return config.getAuthenticationManager();
    }
}

//Explicacion de la clase
//Primero creé la clase SecurityConfig porque es donde definimos toda la lógica de seguridad de Spring Security.
// Ahí le indicamos qué rutas son públicas, cómo se comportan los errores, y que no usamos sesiones ya que
// implementamos JWT. También añadí el filtro personalizado antes del filtro de login estándar para interceptar
// el token y validar al usuario. Además, inyectamos el AuthenticationManager y un encoder BCrypt para manejar
// contraseñas de forma segura.”