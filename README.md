# API REST de Tienda — Spring Boot y PostgreSQL

## 1. Descripción del proyecto

Este proyecto consiste en una API REST desarrollada con Spring Boot para administrar productos, pedidos e inventario de una tienda.

La aplicación permite realizar operaciones CRUD sobre productos, crear pedidos, agregar o eliminar productos del carrito, actualizar cantidades, calcular totales y confirmar pedidos.

La información se almacena en una base de datos PostgreSQL mediante Spring Data JPA e Hibernate.

## 2. Tecnologías utilizadas

- Java 26
- Spring Boot 4.1.1
- Spring Web
- Spring Data JPA
- PostgreSQL 17
- Maven
- Lombok
- Jakarta Validation
- Postman para las pruebas de la API
- IntelliJ IDEA como entorno de desarrollo

## 3. Configuración de la base de datos

La base de datos utilizada se llama `tiendaya`.

Configuración local utilizada durante el desarrollo:

- Motor: PostgreSQL
- Puerto: 5433
- Base de datos: `tiendaya`
- Esquema: `public`

Para ejecutar el proyecto en otro equipo, se deben configurar las credenciales de PostgreSQL en el archivo `application.properties`.

No se deben publicar contraseñas reales en este documento.

## 4. Módulo de productos

La API permite:

- Consultar todos los productos.
- Consultar un producto por su ID.
- Crear productos.
- Actualizar productos.
- Eliminar productos.
- Administrar el stock disponible.

### Endpoints

| Método HTTP | Endpoint             | Descripción                   |
| ----------- | -------------------- | ----------------------------- |
| GET         | `/api/products`      | Consultar todos los productos |
| GET         | `/api/products/{id}` | Consultar un producto         |
| POST        | `/api/products`      | Crear un producto             |
| PUT         | `/api/products/{id}` | Actualizar un producto        |
| DELETE      | `/api/products/{id}` | Eliminar un producto          |

### Ejemplo de creación de producto

Solicitud: `POST /api/products`

```json
{
  "name": "Teclado",
  "price": 85000.00,
  "stock": 15
}
```

El sistema valida que el nombre no esté vacío, que el precio sea mayor que cero y que el stock no sea negativo.

## 5. Módulo de pedidos

Los pedidos tienen dos estados:

- `CARRITO`: el pedido todavía se puede modificar.
- `CONFIRMADO`: el pedido ya fue confirmado.

La API permite crear pedidos, consultar sus detalles, agregar productos, actualizar cantidades, eliminar productos y confirmar pedidos.

### Endpoints

| Método HTTP | Endpoint                                  | Descripción                           |
| ----------- | ----------------------------------------- | ------------------------------------- |
| POST        | `/api/orders`                             | Crear un pedido                       |
| GET         | `/api/orders`                             | Consultar todos los pedidos           |
| GET         | `/api/orders/{id}`                        | Consultar un pedido con sus detalles  |
| POST        | `/api/orders/{orderId}/items`             | Agregar un producto al pedido         |
| PUT         | `/api/orders/{orderId}/items/{productId}` | Actualizar la cantidad de un producto |
| DELETE      | `/api/orders/{orderId}/items/{productId}` | Eliminar un producto del pedido       |
| POST        | `/api/orders/{orderId}/confirm`           | Confirmar un pedido                   |

### Ejemplo para agregar un producto

Solicitud: `POST /api/orders/1/items`

```json
{
  "productId": 3,
  "quantity": 2
}
```

El sistema calcula el subtotal de cada producto y actualiza el total del pedido.

## 6. Reglas de negocio

- No se permiten nombres de productos vacíos.
- Los precios deben ser mayores que cero.
- El stock no puede ser negativo.
- Las cantidades de los artículos de un pedido deben ser al menos uno.
- Los pedidos confirmados no se pueden modificar.
- No se pueden confirmar pedidos vacíos.
- Antes de confirmar un pedido, se verifica que exista suficiente inventario.
- Si el stock es insuficiente, la confirmación se rechaza y el inventario no se descuenta.
- El inventario se descuenta al confirmar correctamente un pedido.
- Un pedido no puede confirmarse dos veces.

## 7. Manejo de errores

La API utiliza respuestas HTTP para comunicar el resultado de las operaciones.

| Código HTTP     | Significado                                                 |
| --------------- | ----------------------------------------------------------- |
| 200 OK          | Operación realizada correctamente                           |
| 201 Created     | Recurso creado                                              |
| 204 No Content  | Eliminación realizada sin contenido de respuesta            |
| 400 Bad Request | Datos de entrada inválidos                                  |
| 404 Not Found   | Producto o pedido inexistente                               |
| 409 Conflict    | Conflicto con una regla de negocio, como stock insuficiente |

Los errores se devuelven en formato JSON mediante el manejador global de excepciones.

## 8. Pruebas realizadas

Durante el desarrollo se realizaron pruebas con Postman para comprobar:

- Creación, consulta, actualización y eliminación de productos.
- Validación de nombres, precios y stock.
- Creación y consulta de pedidos.
- Agregado, actualización y eliminación de artículos.
- Cálculo de subtotales y totales.
- Confirmación de pedidos.
- Rechazo de pedidos con inventario insuficiente.
- Conservación del stock cuando una confirmación falla.
- Prevención de confirmaciones duplicadas.
- Manejo de errores para recursos inexistentes.

## 9. Generación de documentación Javadoc

El proyecto utiliza Javadoc para generar documentación técnica de las clases y métodos Java. Esta documentación facilita la comprensión y el mantenimiento del código fuente.

### 1. Generar la documentación

Desde la terminal de PowerShell, ubicarse en la carpeta raíz del proyecto, donde se encuentra el archivo `pom.xml`, y ejecutar:

```powershell
.\mvnw.cmd javadoc:javadoc
```

Al finalizar, Maven debe mostrar el mensaje `BUILD SUCCESS`, indicando que la documentación se generó correctamente.

### 2. Ubicación de los archivos

Los archivos HTML generados se encuentran en:

```text
target/reports/apidocs/
```

El archivo principal de acceso es `index.html`.

### 3. Abrir la documentación

En PowerShell, ejecutar:

```powershell
start .\target\reports\apidocs\index.html
```

Se abrirá la documentación en el navegador, desde donde se pueden consultar las clases, los paquetes y los métodos documentados del proyecto.

**Nota:** La documentación se genera dentro de la carpeta `target`, por lo que se puede volver a crear ejecutando el comando de Maven cuando se realicen cambios en el código fuente.


## 10. Autoría

- Autor(es): Oscar Alejandro Gomez Santiago
- Programa académico: Tecnologia en desarrollo de software
- Institución: Universidad Minuto de dios UNIMINUTO
- Asignatura: Electiva CP1
- Fecha: 19/10/2026
