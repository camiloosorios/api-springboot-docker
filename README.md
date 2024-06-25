# Spring Boot API con Docker

Este proyecto es una aplicación Spring Boot con una API REST para gestionar productos. La aplicación está configurada para ser ejecutada en contenedores Docker tanto en entornos de desarrollo como de producción. Utiliza PostgreSQL como base de datos.

## Requisitos

- Docker
- Docker Compose

## Configuración del Proyecto

### Estructura del Proyecto

- `src/main/java`: Código fuente de la aplicación.
- `src/main/resources`: Archivos de configuración.
- `Dockerfile`: Definición de la imagen Docker para producción.
- `docker-compose.dev.yml`: Configuración de Docker Compose para desarrollo.
- `docker-compose.prod.yml`: Configuración de Docker Compose para producción.
- `target`: Directorio donde se genera el archivo `.jar` de la aplicación.
- `openapi.yml`: Especificación OpenAPI para la documentación de la API.

### Configuración de Docker Compose

#### Entorno de Desarrollo
Para ejecutar la aplicación en un entorno de desarrollo, usa el siguiente comando:
```
docker-compose -f docker-compose.dev.yml up
```

#### Entorno de Producción
Para ejecutar la aplicación en un entorno de desarrollo, usa el siguiente comando:
```
docker-compose -f docker-compose.prod.yml up --build
```

## Documentación de la API

La API está documentada utilizando OpenAPI 3.0.1. Puedes encontrar la especificación en el archivo `openapi.yml`. Aquí tienes una descripción básica de los endpoints disponibles:

- `GET /api/products`: Obtiene una lista de productos.
- `POST /api/products`: Crea un nuevo producto.
- `GET /api/products/{id}`: Obtiene un producto por ID.
- `PUT /api/products/{id}`: Actualiza un producto existente.
- `PATCH /api/products/{id}`: Actualiza la disponibilidad de un producto.
- `DELETE /api/products/{id}`: Elimina un producto por ID.

### Ejemplos de Respuestas

#### GET /api/products

```json
[
  {
    "id": 1,
    "name": "Producto 1",
    "description": "Descripción del producto 1",
    "price": 100.0,
    "available": true
  },
  {
    "id": 2,
    "name": "Producto 2",
    "description": "Descripción del producto 2",
    "price": 150.0,
    "available": false
  }
]
```
#### POST /api/products
```json
{
  "message": "Product created successfully"
}
```
#### Errores comunes

- `400 Bad Request`:  Solicitud incorrecta.
- `404 Not Found`  :  Producto no encontrado.
- `500 Internal Server Error`:  Error interno del servidor.

## Documentación de Swagger
Puedes acceder a la documentación de la API utilizando Swagger UI:

- `Entorno de Desarrollo:` http://localhost:8080/swagger-ui/index.html
- `Entorno de Producción:` URL_PROD/swagger-ui/index.html

## Contribuir
Si deseas contribuir a este proyecto, por favor abre un issue o envía un pull request.

## Licencia
Este proyecto está licenciado bajo la Licencia Apache 2.0 - ver el archivo LICENSE para más detalles.
