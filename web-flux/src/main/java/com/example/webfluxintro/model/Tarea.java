package com.example.webfluxintro.model;

import org.springframework.data.annotation.Id;

import org.springframework.data.relational.core.mapping.Table; // Importante usar esta

import java.util.UUID;

@Table("tareas") // Mapea esta clase a la tabla "tareas"

public class Tarea {



    @Id // Marca este campo como la clave primaria

    private String id;

    private String descripcion;

    private boolean completada;



    // Constructor para crear nuevas tareas (sin ID explícito, se genera aquí)

    public Tarea(String descripcion) {

        this.id = UUID.randomUUID().toString(); // Genera un ID único

        this.descripcion = descripcion;

        this.completada = false;

    }



    // Constructor para cuando el ID ya existe (ej. al leer de BD o DTO de entrada)

    public Tarea(String id, String descripcion, boolean completada) {

        this.id = id;

        this.descripcion = descripcion;

        this.completada = completada;

    }



    // Constructor vacío es a menudo requerido por frameworks de (de)serialización

    public Tarea() {

    }



    // Getters y Setters (asegúrate que todos los campos los tengan)

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