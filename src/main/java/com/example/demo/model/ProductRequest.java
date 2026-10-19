
package com.example.demo.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;

/**
 * Representa los datos recibidos para crear o actualizar un producto.
 *
 * Incluye validaciones para garantizar que el nombre, el precio
 * y el stock cumplan las reglas establecidas.
 *
 * @param name nombre del producto, obligatorio y de máximo 100 caracteres
 * @param price precio del producto, mayor que cero y con máximo dos decimales
 * @param stock cantidad disponible, obligatoria y no negativa
 */
public record ProductRequest(

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
        String name,

        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.01", message = "El precio debe ser mayor que cero")
        @Digits(integer = 10, fraction = 2,
                message = "El precio admite hasta 10 enteros y 2 decimales")
        BigDecimal price,

        @NotNull(message = "El stock es obligatorio")
        @Min(value = 0, message = "El stock no puede ser negativo")
        Integer stock

) {}
