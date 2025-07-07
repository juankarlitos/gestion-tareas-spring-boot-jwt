package com.tareas.gestion.controller;

import com.tareas.gestion.dto.TareaRequestDTOs;
import com.tareas.gestion.dto.TareaResponseDTOs;
import com.tareas.gestion.service.TareaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    private final TareaService tareaService;

    public TareaController(TareaService tareaService) {
        this.tareaService = tareaService;
    }
    @GetMapping
    public List<TareaResponseDTOs> listar(@AuthenticationPrincipal UserDetails user){
        return tareaService.listarTareas(user.getUsername());
    }
    @PostMapping
    public ResponseEntity<?> crea(@AuthenticationPrincipal UserDetails user,
                                  @RequestBody TareaRequestDTOs dto){

        TareaResponseDTOs respuesta =  tareaService.crearTarea(user.getUsername(), dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of(
                        "mensaje", "Tarea creada exitosamente",
                        "tarea", respuesta
                ));
    }
    @PutMapping("/{id}")
    public TareaResponseDTOs actualizar(@AuthenticationPrincipal UserDetails user,
                                        @PathVariable Long id,
                                        @RequestBody TareaRequestDTOs dto){
        return tareaService.actualizarTarea(id, user.getUsername(), dto);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@AuthenticationPrincipal UserDetails user,
                         @PathVariable Long id){
        tareaService.eliminarTarea(id, user.getUsername());
        return ResponseEntity.noContent().build();
    }
}