# MS College API

API para gestión de estudiantes desarrollado con Spring Boot WebFlux y PostgreSQL.

## Características

- **Spring Boot WebFlux**: Aplicación reactiva no bloqueante
- **PostgreSQL con R2DBC**: Base de datos relacional con driver reactivo
- **Docker & Docker Compose**: Containerización completa
- **OpenAPI/Swagger**: Documentación automática del API
- **Validación**: Bean Validation para validación de datos
- **Logging**: Log4j2 con logging asíncrono
- **Testing**: Tests unitarios con Mockito y WebTestClient

## Inicio Rápido

### Con Docker (Recomendado)

```bash
# Iniciar todos los servicios
docker-compose up --build

# La aplicación estará disponible en http://localhost:8080
```

### Sin Docker

```bash
# Instalar dependencias y ejecutar
./gradlew bootRun

# La aplicación estará disponible en http://localhost:8080
```

## Documentación del API

### Swagger UI

- **URL**: http://localhost:8080/swagger-ui.html
- **Descripción**: Interfaz gráfica para explorar y probar los endpoints

### OpenAPI JSON

- **URL**: http://localhost:8080/api-docs
- **Descripción**: Especificación OpenAPI en formato JSON

## Endpoints Disponibles

### Estudiantes

- `GET /api/students` - Listar todos los estudiantes
- `POST /api/students` - Crear un nuevo estudiante

## Configuración

### Variables de Entorno

```bash
SPRING_R2DBC_URL=r2dbc:postgresql://localhost:5432/db_cc_college
SPRING_R2DBC_USERNAME=postgres
SPRING_R2DBC_PASSWORD=postgres
```

### Base de Datos

- **Host**: localhost:5432
- **Database**: db_cc_college
- **Usuario**: postgres
- **Contraseña**: postgres

## Testing

```bash
# Ejecutar todos los tests
./gradlew test

# Ejecutar tests específicos
./gradlew test --tests StudentServiceTest
./gradlew test --tests StudentControllerTest
```

## Estructura del Proyecto

```plaintext
src/
├── main/java/com/scotiabank/cc/mscollegeapi/
│   ├── config/          # Configuraciones (OpenAPI, Aspect)
│   ├── controller/      # Controladores REST
│   ├── dto/             # Data Transfer Objects
│   ├── entity/          # Entidades JPA
│   ├── enums/           # Enumeraciones
│   ├── exception/       # Excepciones personalizadas
│   ├── repository/      # Repositorios de datos
│   └── service/         # Lógica de negocio
└── test/                # Tests unitarios e integración
```
