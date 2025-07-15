package com.tareas.gestion.security;

import com.tareas.gestion.entity.Usuario;
import com.tareas.gestion.repository.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.stream.Collectors;

/*
esta clase es clave en el proceso de autenticación con Spring Security, porque se encarga de cargar
los datos del usuario desde la base de datos cuando se intenta iniciar sesión

 */

@Service//Se marca con @Service para que Spring la detecte como un componente de servicio.
public class MyUserDetailsService implements UserDetailsService {//Implementa UserDetailsService,
    // que es una interfaz de Spring Security.
    //Esto obliga a implementar el método loadUserByUsername, que es el que usa Spring Security para autenticar.

    private final UsuarioRepository usuarioRepository;//Inyeccion de dependencia

    public MyUserDetailsService(UsuarioRepository usuarioRepository){

        this.usuarioRepository = usuarioRepository;
    }//Se inyecta el repositorio de usuarios (UsuarioRepository) para poder buscar el usuario por su username.

    //Este método es llamado automáticamente por Spring Security cuando un usuario intenta autenticarse.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{

        //Busca al usuario en la base de datos usando el username.
        Usuario usuario = usuarioRepository.findByUsername(username)
                //Si no lo encuentra, lanza una excepción.
                .orElseThrow(()-> new UsernameNotFoundException("Usuario no encontrado"));

        return User.builder()//Aquí estás creando un objeto User de Spring Security, que implementa la interfaz UserDetails
                .username(usuario.getUsername())//el nombre del usuario.
                .password(usuario.getPassword())//la contraseña encriptada (BCrypt).
                .authorities(
                        usuario.getRoles().stream()
                                .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getNombre()))
                                .collect(Collectors.toList())
                )
               // .roles("USER")//asigna el rol "USER".
                .build();
    }
}

//Explicacion
//MyUserDetailsService es la clase encargada de cargar los datos del usuario desde la base de datos cuando
// Spring Security intenta autenticar. Implementa UserDetailsService, que es un contrato de Spring.
// Usa un repositorio para buscar el usuario por su nombre, y si lo encuentra, retorna un objeto
// User con el username, la contraseña encriptada y los roles. Este objeto es el que Spring usa
// internamente para verificar si las credenciales son correctas