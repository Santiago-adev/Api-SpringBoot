
package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.model.ProductRequest;
import com.example.demo.model.ProductResponse;
import com.example.demo.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST encargado de exponer las operaciones
 * de consulta, creación, actualización y eliminación de productos.
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * Obtiene todos los productos registrados.
     *
     * @return lista de productos
     */
    @GetMapping
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts()
                .stream()
                .map(ProductResponse::from)
                .toList();
    }

    /**
     * Busca un producto por su identificador.
     *
     * @param id identificador del producto
     * @return datos del producto encontrado
     */
    @GetMapping("/{id}")
    public ProductResponse getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ProductResponse.from(product);
    }

    /**
     * Registra un producto nuevo.
     *
     * @param request datos del producto que se va a registrar
     * @return producto creado
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(
            @Valid @RequestBody ProductRequest request) {
        Product savedProduct = productService.createProduct(request);
        return ProductResponse.from(savedProduct);
    }

    /**
     * Actualiza los datos de un producto existente.
     *
     * @param id identificador del producto
     * @param request nuevos datos del producto
     * @return producto actualizado
     */
    @PutMapping("/{id}")
    public ProductResponse updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        Product updatedProduct = productService.updateProduct(id, request);
        return ProductResponse.from(updatedProduct);
    }

    /**
     * Elimina un producto por su identificador.
     *
     * @param id identificador del producto que se eliminará
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
