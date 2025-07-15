package com.tareas.gestion.repository;

import com.tareas.gestion.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long > {

    // Busca un usuario por su username, devolviendo un Optional para manejar la ausencia del usuario de forma segura.
    Optional<Usuario> findByUsername(String username);
    //Este método busca un usuario en la base de datos cuyo username coincida con el que se pasa como parámetro.
}
//Porque puede que ese username no exista en la base de datos. Entonces
//Si lo encuentra : retorna Optional con el Usuario adentro.
//Si no lo encuentra : retorna Optional.empty().
