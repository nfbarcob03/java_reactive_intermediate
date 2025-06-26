package com.ejemplo.model;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import io.swagger.v3.oas.annotations.media.Schema;

@Table("ordenes")
@Schema(description = "Entidad que representa una orden de un cliente")
public class Orden {
    @Id
    @Schema(description = "Identificador único de la orden", example = "1")
    private Long id;

    @NotBlank(message = "La descripción es obligatoria")
    @Schema(description = "Descripción de la orden", example = "Compra de producto X")
    private String descripcion;

    @Schema(description = "ID del cliente asociado a la orden", example = "10")
    private Long clienteId;

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
}
