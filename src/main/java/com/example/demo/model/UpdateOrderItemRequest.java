
package com.example.demo.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Representa los datos necesarios para actualizar la cantidad
 * de un producto dentro de un pedido.
 *
 * @param quantity nueva cantidad de unidades del producto;
 *                  debe ser como mínimo 1
 */
public record UpdateOrderItemRequest(

        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 1, message = "La cantidad debe ser al menos 1")
        Integer quantity

) {}
