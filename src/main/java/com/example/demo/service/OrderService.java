
package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.AddOrderItemRequest;
import com.example.demo.model.Order;
import com.example.demo.model.OrderDetail;
import com.example.demo.model.OrderDetailResponse;
import com.example.demo.model.OrderResponse;
import com.example.demo.model.OrderStatus;
import com.example.demo.model.Product;
import com.example.demo.model.UpdateOrderItemRequest;
import com.example.demo.repository.OrderDetailRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Servicio encargado de administrar pedidos, artículos e inventario.
 * Implementa las operaciones del carrito y la confirmación de pedidos.
 */
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;

    /**
     * Crea un pedido nuevo en estado CARRITO.
     *
     * @return pedido creado y guardado
     */
    public Order createOrder() {
        Order order = Order.builder()
                .status(OrderStatus.CARRITO)
                .build();

        return orderRepository.save(order);
    }

    /**
     * Obtiene todos los pedidos registrados.
     *
     * @return lista de pedidos
     */
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    /**
     * Obtiene todos los pedidos con sus detalles y subtotales.
     *
     * @return lista de pedidos con sus artículos
     */
    public List<OrderResponse> getAllOrderDetails() {
        return orderRepository.findAll()
                .stream()
                .map(order -> getOrderDetailsById(order.getId()))
                .toList();
    }

    /**
     * Busca un pedido por su identificador.
     *
     * @param id identificador del pedido
     * @return pedido encontrado
     * @throws ResourceNotFoundException si el pedido no existe
     */
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pedido no encontrado con ID: " + id));
    }

    /**
     * Consulta un pedido y calcula los subtotales de sus artículos.
     *
     * @param id identificador del pedido
     * @return pedido con sus detalles
     * @throws ResourceNotFoundException si el pedido no existe
     */
    public OrderResponse getOrderDetailsById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pedido no encontrado con ID: " + id));

        List<OrderDetailResponse> items = orderDetailRepository
                .findByOrderId(id)
                .stream()
                .map(detail -> new OrderDetailResponse(
                        detail.getProduct().getId(),
                        detail.getProduct().getName(),
                        detail.getQuantity(),
                        detail.getUnitPrice(),
                        detail.getUnitPrice().multiply(
                                BigDecimal.valueOf(detail.getQuantity()))
                ))
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getStatus(),
                order.getTotal(),
                order.getCreatedAt(),
                items
        );
    }

    /**
     * Agrega un producto al carrito y recalcula el total del pedido.
     *
     * @param orderId identificador del pedido
     * @param request identificador del producto y cantidad solicitada
     * @return pedido actualizado
     * @throws ResourceNotFoundException si no existe el pedido o el producto
     */
    @Transactional
    public Order addProductToOrder(
            Long orderId,
            AddOrderItemRequest request) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Pedido no encontrado con ID: " + orderId));

        if (order.getStatus() != OrderStatus.CARRITO) {
            throw new IllegalStateException(
                    "No se pueden modificar pedidos confirmados");
        }

        Product product = productRepository.findById(request.productId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Producto no encontrado con ID: "
                                        + request.productId()));

        Optional<OrderDetail> existingDetail =
                orderDetailRepository.findByOrderIdAndProductId(
                        orderId, product.getId());

        if (existingDetail.isPresent()) {
            OrderDetail detail = existingDetail.get();
            detail.setQuantity(
                    detail.getQuantity() + request.quantity());
            orderDetailRepository.save(detail);
        } else {
            OrderDetail detail = OrderDetail.builder()
                    .order(order)
                    .product(product)
                    .quantity(request.quantity())
                    .unitPrice(product.getPrice())
                    .build();

            orderDetailRepository.save(detail);
        }

        BigDecimal total = orderDetailRepository.findByOrderId(orderId)
                .stream()
                .map(detail -> detail.getUnitPrice().multiply(
                        BigDecimal.valueOf(detail.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotal(total);

        return orderRepository.save(order);
    }

    /**
     * Actualiza la cantidad de un producto en un pedido y recalcula el total.
     *
     * @param orderId identificador del pedido
     * @param productId identificador del producto
     * @param request nueva cantidad solicitada
     * @return pedido actualizado con sus detalles
     * @throws ResourceNotFoundException si el pedido o artículo no existe
     */
    @Transactional
    public OrderResponse updateProductQuantity(
            Long orderId,
            Long productId,
            UpdateOrderItemRequest request) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pedido no encontrado con ID: " + orderId));

        if (order.getStatus() != OrderStatus.CARRITO) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se pueden modificar pedidos confirmados");
        }

        OrderDetail detail = orderDetailRepository
                .findByOrderIdAndProductId(orderId, productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "El producto no está en este pedido"));

        detail.setQuantity(request.quantity());
        orderDetailRepository.save(detail);

        BigDecimal total = orderDetailRepository.findByOrderId(orderId)
                .stream()
                .map(item -> item.getUnitPrice().multiply(
                        BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotal(total);
        orderRepository.save(order);

        return getOrderDetailsById(orderId);
    }

    /**
     * Elimina un producto del carrito y recalcula el total.
     *
     * @param orderId identificador del pedido
     * @param productId identificador del producto que se eliminará
     * @return pedido actualizado
     * @throws ResourceNotFoundException si el pedido o artículo no existe
     */
    @Transactional
    public Order removeProductFromOrder(
            Long orderId,
            Long productId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Pedido no encontrado con ID: " + orderId));

        if (order.getStatus() != OrderStatus.CARRITO) {
            throw new IllegalStateException(
                    "No se pueden modificar pedidos confirmados");
        }

        OrderDetail detail =
                orderDetailRepository.findByOrderIdAndProductId(
                                orderId, productId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "El producto no está en este pedido"));

        orderDetailRepository.delete(detail);

        BigDecimal total = orderDetailRepository.findByOrderId(orderId)
                .stream()
                .map(item -> item.getUnitPrice().multiply(
                        BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotal(total);

        return orderRepository.save(order);
    }

    /**
     * Confirma un pedido si contiene productos y hay stock suficiente.
     * Descuenta el inventario y cambia el estado a CONFIRMADO.
     *
     * @param orderId identificador del pedido que se confirmará
     * @return pedido confirmado
     * @throws ResourceNotFoundException si el pedido o un producto no existe
     * @throws ResponseStatusException si está vacío, ya fue confirmado
     *         o no hay stock suficiente
     */
    @Transactional
    public Order confirmOrder(Long orderId) {

        Order order = orderRepository.findOrderForUpdateById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Pedido no encontrado con ID: " + orderId));

        if (order.getStatus() != OrderStatus.CARRITO) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El pedido ya fue confirmado o no se puede modificar");
        }

        List<OrderDetail> details =
                orderDetailRepository.findByOrderId(orderId);

        if (details.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se puede confirmar un pedido sin productos");
        }

        // Primero valida el stock de todos los productos.
        for (OrderDetail detail : details) {
            Product product = productRepository
                    .findProductForUpdateById(
                            detail.getProduct().getId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Producto no encontrado"));

            if (product.getStock() < detail.getQuantity()) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Stock insuficiente para el producto: "
                                + product.getName());
            }
        }

        // Después descuenta el inventario.
        for (OrderDetail detail : details) {
            Product product = productRepository
                    .findProductForUpdateById(
                            detail.getProduct().getId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Producto no encontrado"));

            product.setStock(
                    product.getStock() - detail.getQuantity());

            productRepository.save(product);
        }

        order.setStatus(OrderStatus.CONFIRMADO);

        return orderRepository.save(order);
    }
}
