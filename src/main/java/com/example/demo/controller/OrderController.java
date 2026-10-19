
package com.example.demo.controller;

import com.example.demo.model.AddOrderItemRequest;
import com.example.demo.model.Order;
import com.example.demo.model.OrderResponse;
import com.example.demo.model.UpdateOrderItemRequest;
import com.example.demo.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST que expone las operaciones para administrar pedidos,
 * gestionar sus artículos y confirmar compras.
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * Crea un pedido nuevo en estado CARRITO.
     *
     * @return pedido creado
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order createOrder() {
        return orderService.createOrder();
    }

    /**
     * Consulta todos los pedidos con sus respectivos artículos.
     *
     * @return lista de pedidos y sus detalles
     */
    @GetMapping
    public List<OrderResponse> getAllOrders() {
        return orderService.getAllOrderDetails();
    }

    /**
     * Consulta un pedido por su identificador.
     *
     * @param id identificador del pedido
     * @return pedido con sus artículos y totales
     */
    @GetMapping("/{id}")
    public OrderResponse getOrderById(@PathVariable Long id) {
        return orderService.getOrderDetailsById(id);
    }

    /**
     * Agrega un producto a un pedido que todavía está en estado CARRITO.
     *
     * @param orderId identificador del pedido
     * @param request producto y cantidad que se agregarán
     * @return pedido actualizado
     */
    @PostMapping("/{orderId}/items")
    public Order addProductToOrder(
            @PathVariable Long orderId,
            @Valid @RequestBody AddOrderItemRequest request) {

        return orderService.addProductToOrder(orderId, request);
    }

    /**
     * Actualiza la cantidad de un producto dentro de un pedido.
     *
     * @param orderId identificador del pedido
     * @param productId identificador del producto
     * @param request nueva cantidad del producto
     * @return pedido actualizado con sus detalles
     */
    @PutMapping("/{orderId}/items/{productId}")
    public OrderResponse updateProductQuantity(
            @PathVariable Long orderId,
            @PathVariable Long productId,
            @Valid @RequestBody UpdateOrderItemRequest request) {

        return orderService.updateProductQuantity(
                orderId, productId, request);
    }

    /**
     * Elimina un producto de un pedido que todavía está en estado CARRITO.
     *
     * @param orderId identificador del pedido
     * @param productId identificador del producto que se eliminará
     * @return pedido actualizado
     */
    @DeleteMapping("/{orderId}/items/{productId}")
    public Order removeProductFromOrder(
            @PathVariable Long orderId,
            @PathVariable Long productId) {

        return orderService.removeProductFromOrder(
                orderId, productId);
    }

    /**
     * Confirma un pedido y solicita la validación y el descuento del inventario.
     *
     * @param orderId identificador del pedido que se confirmará
     * @return pedido con su estado actualizado
     */
    @PostMapping("/{orderId}/confirm")
    public Order confirmOrder(@PathVariable Long orderId) {
        return orderService.confirmOrder(orderId);
    }
}
