
package com.example.demo.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * Representa un producto disponible para la venta en la tienda.
 *
 * Contiene la información básica del producto: identificador,
 * nombre, precio y cantidad disponible en el inventario.
 */
@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    /** Identificador único del producto. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre del producto. */
    @Column(nullable = false, length = 100)
    private String name;

    /** Precio de venta del producto. */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /** Cantidad de unidades disponibles en el inventario. */
    @Column(nullable = false)
    @Builder.Default
    private Integer stock = 0;
}
