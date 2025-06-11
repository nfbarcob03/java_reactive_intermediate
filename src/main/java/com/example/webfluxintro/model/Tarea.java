package com.example.webfluxintro.model;

import java.util.UUID;

public class Tarea {

    private String id;

    private String descripcion;

    private boolean completada;



    // Constructor para nuevas tareas (sin ID, se generará)

    public Tarea(String descripcion) {

        this.id = UUID.randomUUID().toString(); // Genera un ID único

        this.descripcion = descripcion;

        this.completada = false;

    }



    // Constructor para deserialización o creación con ID

    public Tarea(String id, String descripcion, boolean completada) {

        this.id = id;

        this.descripcion = descripcion;

        this.completada = completada;

    }



    // Constructor vacío necesario para algunas librerías de (de)serialización como Jackson

    public Tarea() {

    }



    // Getters y Setters

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getDescripcion() { return descripcion; }

    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public boolean isCompletada() { return completada; }

    public void setCompletada(boolean completada) { this.completada = completada; }



    @Override

    public String toString() {

        return "Tarea{" +

                "id='" + id + '\'' +

                ", descripcion='" + descripcion + '\'' +

                ", completada=" + completada +

                '}';

    }

}