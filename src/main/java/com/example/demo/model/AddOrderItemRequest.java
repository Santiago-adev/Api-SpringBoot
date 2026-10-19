
package com.example.demo.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Representa los datos necesarios para agregar un producto a un pedido.
 *
 * @param productId identificador del producto que se agregará
 * @param quantity cantidad de unidades que se desean agregar,
 *                 debe ser como mínimo 1
 */
public record AddOrderItemRequest(

        @NotNull(message = "El ID del producto es obligatorio")
        Long productId,

        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 1, message = "La cantidad debe ser al menos 1")
        Integer quantity

) {}
