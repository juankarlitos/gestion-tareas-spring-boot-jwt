package com.tareas.gestion.service;

import com.tareas.gestion.dto.TareaRequestDTOs;
import com.tareas.gestion.dto.TareaResponseDTOs;
import java.util.List;

public interface TareaService {
    TareaResponseDTOs crearTarea(String username, TareaRequestDTOs dto);
    List<TareaResponseDTOs> listarTareas(String username);
    TareaResponseDTOs actualizarTarea(Long id, String username, TareaRequestDTOs dto);
    void eliminarTarea(Long id, String username);
}