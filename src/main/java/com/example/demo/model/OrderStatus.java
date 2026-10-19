
package com.example.demo.model;

/**
 * Define los estados posibles de un pedido en la tienda.
 *
 * Un pedido comienza como carrito y puede pasar a confirmado
 * cuando se validan sus productos y se descuenta el inventario.
 */
public enum OrderStatus {

    /** El pedido está en preparación y puede modificarse. */
    CARRITO,

    /** El pedido fue confirmado y no puede modificarse. */
    CONFIRMADO
}
