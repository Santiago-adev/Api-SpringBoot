
package com.example.demo.repository;

import com.example.demo.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import java.util.Optional;

/**
 * Repositorio encargado de las operaciones de persistencia
 * de los pedidos de la tienda.
 *
 * Extiende JpaRepository para disponer de las operaciones
 * CRUD proporcionadas por Spring Data JPA.
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Busca un pedido por su identificador y aplica un bloqueo
     * de escritura durante la transacción.
     *
     * Ayuda a evitar que varias transacciones confirmen
     * simultáneamente el mismo pedido.
     *
     * @param id identificador del pedido
     * @return pedido encontrado, o vacío si no existe
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Order> findOrderForUpdateById(Long id);
}
