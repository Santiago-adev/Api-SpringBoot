
package com.example.demo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Representa la respuesta de un pedido devuelta por la API REST.
 *
 * Incluye la información general del pedido y la lista
 * de productos que contiene.
 *
 * @param id identificador único del pedido
 * @param status estado actual del pedido
 * @param total valor total del pedido
 * @param createdAt fecha y hora de creación del pedido
 * @param items lista de detalles de los productos del pedido
 */
public record OrderResponse(
        Long id,
        OrderStatus status,
        BigDecimal total,
        LocalDateTime createdAt,
        List<OrderDetailResponse> items
) {}
