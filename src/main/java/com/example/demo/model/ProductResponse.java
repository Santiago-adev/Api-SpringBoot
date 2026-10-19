
package com.example.demo.model;

import java.math.BigDecimal;

/**
 * Representa los datos de un producto que devuelve la API REST.
 *
 * @param id identificador único del producto
 * @param name nombre del producto
 * @param price precio de venta del producto
 * @param stock cantidad disponible en el inventario
 */
public record ProductResponse(
        Long id,
        String name,
        BigDecimal price,
        Integer stock
) {

    /**
     * Convierte una entidad Product en un objeto de respuesta.
     *
     * @param p producto que se va a convertir
     * @return respuesta con los datos del producto
     */
    public static ProductResponse from(Product p) {
        return new ProductResponse(
                p.getId(),
                p.getName(),
                p.getPrice(),
                p.getStock()
        );
    }
}
