
package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Product;
import com.example.demo.model.ProductRequest;
import com.example.demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio encargado de gestionar los productos de la tienda.
 * Contiene la lógica de consulta, creación, actualización y eliminación.
 */
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * Obtiene todos los productos registrados.
     *
     * @return lista de productos
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Busca un producto por su identificador.
     *
     * @param id identificador del producto
     * @return producto encontrado
     * @throws ResourceNotFoundException si el producto no existe
     */
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Producto no encontrado con ID: " + id
                        )
                );
    }

    /**
     * Crea un producto con los datos recibidos.
     *
     * @param request datos del producto que se va a crear
     * @return producto guardado en la base de datos
     */
    public Product createProduct(ProductRequest request) {
        Product product = Product.builder()
                .name(request.name())
                .price(request.price())
                .stock(request.stock())
                .build();

        return productRepository.save(product);
    }

    /**
     * Actualiza el nombre, precio y stock de un producto existente.
     *
     * @param id identificador del producto que se actualizará
     * @param request nuevos datos del producto
     * @return producto actualizado
     * @throws ResourceNotFoundException si el producto no existe
     */
    public Product updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Producto no encontrado con ID: " + id
                        )
                );

        product.setName(request.name());
        product.setPrice(request.price());
        product.setStock(request.stock());

        return productRepository.save(product);
    }

    /**
     * Elimina un producto existente.
     *
     * @param id identificador del producto que se eliminará
     * @throws ResourceNotFoundException si el producto no existe
     */
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Producto no encontrado con ID: " + id
            );
        }

        productRepository.deleteById(id);
    }
}
