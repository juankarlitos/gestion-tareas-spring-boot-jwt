package com.tareas.gestion.dto;

public class TareaRequestDTOs {

    private String titulo;
    private String descripcion;
    private Long estadoId;

    public TareaRequestDTOs(String titulo, String descripcion, Long estadoId) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.estadoId = estadoId;
    }
    public TareaRequestDTOs() {
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public Long getEstadoId() {
        return estadoId;
    }
    public void setEstadoId(Long estadoId) {
        this.estadoId = estadoId;
    }
}