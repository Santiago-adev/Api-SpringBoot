
package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;

/**
 * Representa un producto dentro de un pedido.
 *
 * Relaciona el pedido con el producto y registra
 * la cantidad solicitada y el precio por unidad.
 */
@Entity
@Table(name = "order_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetail {

    /** Identificador único del detalle. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Pedido al que pertenece este detalle. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    /** Producto asociado al detalle del pedido. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /** Número de unidades solicitadas del producto. */
    @Column(nullable = false)
    private Integer quantity;

    /** Precio por unidad registrado para el pedido. */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;
}
