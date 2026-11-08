# Cafeta TiPum — Backend (Spring Boot)

Backend de la tienda online **Cafeta TiPum** (Evaluación Parcial 3 — Desarrollo Fullstack II).

API REST con Spring Boot, Spring Data JPA, Spring Security (JWT) y documentación con Swagger/OpenAPI.

## Tecnologías

- Java 17, Spring Boot 3.3, Maven 3.9
- MySQL 8 (producción) / H2 en memoria (perfil `h2` para demo)
- Spring Security + JWT (jjwt 0.12) + BCrypt
- springdoc-openapi (Swagger UI)

## Requisitos

- JDK 17 y Maven 3.9 (o Docker).
- MySQL 8 (opcional si usas el perfil `h2`).

## Cómo ejecutar

### Opción A — Con Docker (MySQL + backend)

```bash
docker compose up --build
```

### Opción B — Manual (MySQL local)

1. Crea la base de datos `tipum` (usuario `tipum` / contraseña `tipum123`).
2. Ejecuta:

```bash
mvn spring-boot:run
```

### Opción C — Perfil H2 (demo sin MySQL)

```bash
java -jar target/tipum-backend-1.0.0.jar --spring.profiles.active=h2
```

El backend queda disponible en `http://localhost:8080`.
Swagger UI: `http://localhost:8080/swagger-ui.html`.

## Configuración

Las variables de entorno principales son:

| Variable | Default | Descripción |
|----------|---------|-------------|
| `DB_HOST` | localhost | Host de la base de datos |
| `DB_PORT` | 3306 | Puerto de MySQL |
| `DB_NAME` | tipum | Nombre de la base de datos |
| `DB_USER` | tipum | Usuario |
| `DB_PASSWORD` | tipum123 | Contraseña |
| `JWT_SECRET` | (valor por defecto) | Clave de firma del token JWT |

## Usuarios de prueba (seed)

| Rol | Correo | Contraseña |
|-----|--------|-----------|
| Administrador | admin@duoc.cl | admin123 |
| Vendedor | vendedor@duoc.cl | vendedor123 |
| Cliente | juan.perez@duoc.cl | cliente123 |
| Cliente | m.gonzalez@gmail.com | cliente123 |

## Estructura

```
src/main/java/com/tipum/
├── config/        # SecurityConfig, OpenApiConfig (Swagger), DataInitializer (seed)
├── controller/    # Controladores REST (v1 y v2)
├── dto/           # Records de request/response
├── entity/        # Entidades JPA (Usuario, Producto, Orden, DetalleOrden)
├── exception/     # Manejo global de errores
├── repository/    # Repositorios Spring Data JPA
├── security/      # JwtService, JwtAuthenticationFilter
└── service/       # Lógica de negocio
```
