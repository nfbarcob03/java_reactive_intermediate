package com.example.webfluxintro.service;

import com.example.webfluxintro.model.Producto;
import com.example.webfluxintro.model.ProductoEvento;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ProductEventListener {

    @EventListener
    public void handlerEvetProduct(ProductoEvento productoEvento) {
        Producto p = productoEvento.getProducto();
        String log = String.format(
                "AUDITORÍA: Producto %s - ID: %d, Nombre: %s, Precio: %.2f",
                productoEvento.getTipo(), p.getId(), p.getNombre(), p.getPrecio()
        );
        System.out.println(log); // o guardar en DB
    }
}
