package com.tareas.gestion.service;

import com.tareas.gestion.dto.TareaRequestDTOs;
import com.tareas.gestion.dto.TareaResponseDTOs;
import com.tareas.gestion.entity.EstadoTarea;
import com.tareas.gestion.entity.Tarea;
import com.tareas.gestion.entity.Usuario;
import com.tareas.gestion.repository.EstadoTareaRepository;
import com.tareas.gestion.repository.TareaRepository;
import com.tareas.gestion.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TareaServiceImpl implements TareaService {

    private final TareaRepository tareaRepository;
    private final UsuarioRepository usuarioRepository;
    private final EstadoTareaRepository estadoTareaRepository;

    public TareaServiceImpl(TareaRepository tareaRepository, UsuarioRepository usuarioRepository, EstadoTareaRepository estadoTareaRepository) {
        this.tareaRepository = tareaRepository;
        this.usuarioRepository = usuarioRepository;
        this.estadoTareaRepository = estadoTareaRepository;
    }
    @Override
    public TareaResponseDTOs crearTarea(String username, TareaRequestDTOs dto) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(()-> new RuntimeException("Usuario no encontrado"));
        EstadoTarea estado = estadoTareaRepository.findById(dto.getEstadoId())
                .orElseThrow(() -> new RuntimeException("Estado no valido"));

        Tarea tarea = new Tarea();
        tarea.setTitulo(dto.getTitulo());
        tarea.setDescripcion(dto.getDescripcion());
        tarea.setUsuario(usuario);
        tarea.setEstado(estado);

        Tarea guardada = tareaRepository.save(tarea);
        return new TareaResponseDTOs(
                guardada.getId(),
                guardada.getTitulo(),
                guardada.getDescripcion(),
                guardada.getEstado().getNombre(),
                guardada.getUsuario().getUsername()
        );
    }
    @Override
    public List<TareaResponseDTOs> listarTareas(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return  tareaRepository.findByUsuarioId(usuario.getId()).stream()
                .map(t-> new TareaResponseDTOs(
                        t.getId(),
                        t.getTitulo(),
                        t.getDescripcion(),
                        t.getEstado().getNombre(),
                        t.getUsuario().getUsername()))
                .toList();
    }
    @Override
    public TareaResponseDTOs actualizarTarea(Long id, String username, TareaRequestDTOs dto) {
        Tarea tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

        if (!tarea.getUsuario().getUsername().equals(username)){
            throw new RuntimeException("No autorizado");
        }
        EstadoTarea estado = estadoTareaRepository.findById(dto.getEstadoId())
                .orElseThrow(() -> new RuntimeException("Estado invalido"));

        tarea.setTitulo(dto.getTitulo());
        tarea.setDescripcion(dto.getDescripcion());
        tarea.setEstado(estado);

        Tarea actualizada = tareaRepository.save(tarea);

        return new TareaResponseDTOs(
                actualizada.getId(),
                actualizada.getTitulo(),
                actualizada.getDescripcion(),
                actualizada.getEstado().getNombre(),
                actualizada.getUsuario().getUsername()
        );
    }
    @Override
    public void eliminarTarea(Long id, String username) {
        Tarea tarea = tareaRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Tarea no encontrada"));

        if(!tarea.getUsuario().getUsername().equals(username)){
            throw new RuntimeException("No autorizado");
        }
        tareaRepository.delete(tarea);
    }
}