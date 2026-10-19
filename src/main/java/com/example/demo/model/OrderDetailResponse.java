
package com.example.demo.model;

import java.math.BigDecimal;

/**
 * Representa los datos de un producto incluido en un pedido.
 *
 * Contiene la identificación del producto, su nombre,
 * la cantidad solicitada, el precio unitario y el subtotal.
 *
 * @param productId identificador del producto
 * @param productName nombre del producto
 * @param quantity cantidad de unidades solicitadas
 * @param unitPrice precio por unidad del producto
 * @param subtotal valor total de las unidades de este producto
 */
public record OrderDetailResponse(
        Long productId,
        String productName,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {}
