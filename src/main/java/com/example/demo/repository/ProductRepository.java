
package com.example.demo.repository;

import com.example.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import java.util.Optional;

/**
 * Repositorio encargado de las operaciones de persistencia
 * de los productos de la tienda.
 *
 * Extiende JpaRepository para proporcionar operaciones CRUD
 * como guardar, consultar, actualizar y eliminar productos.
 */
public interface ProductRepository
        extends JpaRepository<Product, Long> {

    /**
     * Busca un producto por su identificador y aplica un bloqueo
     * de escritura durante la transacción.
     *
     * Este bloqueo ayuda a proteger el inventario cuando
     * se confirma un pedido.
     *
     * @param id identificador del producto
     * @return producto encontrado, o vacío si no existe
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Product> findProductForUpdateById(Long id);
}
