package com.tareas.gestion.repository;

import com.tareas.gestion.entity.EstadoTarea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadoTareaRepository extends JpaRepository<EstadoTarea, Long> {

    // Busca un estado por su nombre y lo envuelve en Optional para manejar el caso en que no exista.
    Optional<EstadoTarea> findByNombre(String nombre);
}
//Se usa Optional porque puede o no existir un estado con ese nombre en la base de datos.
//Si lo encuentra → devuelve Optional con el objeto adentro.
//Si no lo encuentra → devuelve Optional.empty().
//Evita errores de NullPointerException.