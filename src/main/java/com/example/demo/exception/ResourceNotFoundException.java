
package com.example.demo.exception;

/**
 * Excepción que indica que un recurso solicitado no existe.
 *
 * Se utiliza, por ejemplo, cuando no se encuentra un producto
 * o un pedido con el identificador proporcionado.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Crea una excepción con un mensaje descriptivo.
     *
     * @param message descripción del recurso que no se encontró
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
