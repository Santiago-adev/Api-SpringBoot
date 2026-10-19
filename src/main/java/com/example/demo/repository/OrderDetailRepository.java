
package com.example.demo.repository;

import com.example.demo.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio encargado de las operaciones de persistencia
 * de los detalles de los pedidos.
 *
 * Permite consultar los productos incluidos en un pedido
 * y localizar un producto específico dentro de él.
 */
public interface OrderDetailRepository
        extends JpaRepository<OrderDetail, Long> {

    /**
     * Busca todos los detalles asociados a un pedido.
     *
     * @param orderId identificador del pedido
     * @return lista de detalles encontrados
     */
    List<OrderDetail> findByOrderId(Long orderId);

    /**
     * Busca el detalle de un producto específico dentro de un pedido.
     *
     * @param orderId identificador del pedido
     * @param productId identificador del producto
     * @return detalle encontrado, o vacío si no existe
     */
    Optional<OrderDetail> findByOrderIdAndProductId(
            Long orderId,
            Long productId
    );
}
