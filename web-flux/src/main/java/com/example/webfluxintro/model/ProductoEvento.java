package com.example.webfluxintro.model;

public class ProductoEvento {
    private final String tipo; // "CREADO" o "ACTUALIZADO"
    private final Producto producto;
 
    public ProductoEvento(String tipo, Producto producto) {
        this.tipo = tipo;
        this.producto = producto;
    }
 
    public String getTipo() { return tipo; }
    public Producto getProducto() { return producto; }
}