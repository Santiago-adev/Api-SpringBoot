
package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

/**
 * Centraliza el manejo de excepciones de la API REST.
 *
 * Transforma los errores de validación, los recursos inexistentes
 * y las excepciones con estado HTTP en respuestas JSON.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Gestiona los errores de validación de los datos recibidos.
     *
     * @param ex excepción generada cuando falla una validación
     * @return mapa con los campos que presentan errores y sus mensajes
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errores = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errores.put(error.getField(), error.getDefaultMessage());
        });

        return errores;
    }

    /**
     * Gestiona los errores que ocurren cuando no existe un recurso.
     *
     * @param ex excepción que contiene el mensaje del error
     * @return mapa con el mensaje del recurso no encontrado
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleResourceNotFound(
            ResourceNotFoundException ex) {

        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());

        return error;
    }

    /**
     * Gestiona excepciones que contienen un estado HTTP específico.
     *
     * @param ex excepción con el estado HTTP y la descripción del error
     * @return respuesta HTTP con el estado correspondiente y el mensaje
     */
    @ExceptionHandler(ResponseStatusException.class)
    public org.springframework.http.ResponseEntity<Map<String, String>>
    handleResponseStatusException(ResponseStatusException ex) {

        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getReason());

        return org.springframework.http.ResponseEntity
                .status(ex.getStatusCode())
                .body(error);
    }
}
