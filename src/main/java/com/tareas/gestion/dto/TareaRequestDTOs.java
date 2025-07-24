package com.tareas.gestion.dto;

public class TareaRequestDTOs {

    private String titulo;
    private String descripcion;
    private Long estadoId;

    private int numero_hojas;

    public TareaRequestDTOs(String titulo, String descripcion, Long estadoId, int numero_hojas) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.estadoId = estadoId;
        this.numero_hojas = numero_hojas;
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

    public int getNumero_hojas() {
        return numero_hojas;
    }

    public void setNumero_hojas(int numero_hojas) {
        this.numero_hojas = numero_hojas;
    }
}