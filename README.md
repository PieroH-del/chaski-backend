# Chaski Backend - API REST para Delivery de Comida

Backend completo en Spring Boot para una aplicación móvil Android tipo delivery de comida.

## 🚀 Características

- ✅ Sistema de autenticación con BCrypt (sin JWT)
- ✅ Gestión completa de usuarios y perfiles
- ✅ CRUD de direcciones de entrega
- ✅ Catálogo de restaurantes con filtros múltiples
- ✅ Gestión de productos con opciones personalizables
- ✅ Sistema completo de pedidos con estados
- ✅ Integración con Stripe para pagos
- ✅ CORS configurado para Android
- ✅ MapStruct para mapeo de DTOs
- ✅ Validaciones con Bean Validation
- ✅ Manejo global de excepciones

## 🛠️ Tecnologías

- **Java 21**
- **Spring Boot 4.0.0**
- **Spring Data JPA**
- **MySQL**
- **MapStruct 1.5.5**
- **Lombok**
- **BCrypt (Spring Security Crypto)**
- **Stripe Java SDK**
- **Maven**

## 📋 Requisitos Previos

- JDK 21 o superior
- MySQL 8.0 o superior
- Maven 3.6 o superior
- Cuenta de Stripe (para pagos)

## ⚙️ Configuración

### 1. Base de Datos

Crear la base de datos MySQL:

```sql
CREATE DATABASE chaski_db;
```

### 2. Configuración de application.properties

Editar `src/main/resources/application.properties`:

```properties
# Configurar credenciales de MySQL
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña

# Configurar API Key de Stripe
stripe.api.key=tu_stripe_secret_key
```

### 3. Compilar el Proyecto

```bash
mvn clean install
```

### 4. Ejecutar la Aplicación

```bash
mvn spring-boot:run
```

La API estará disponible en: `http://localhost:8080`

## 📚 Endpoints Principales

### Autenticación y Usuarios

#### Registro de Usuario
```http
POST /api/usuarios/registro
Content-Type: application/json

{
  "nombre": "Juan Pérez",
  "email": "juan@example.com",
  "password": "password123",
  "telefono": "987654321"
}
```

#### Login
```http
POST /api/usuarios/login
Content-Type: application/json

{
  "email": "juan@example.com",
  "password": "password123"
}
```

#### Actualizar Perfil
```http
PUT /api/usuarios/{id}
Content-Type: application/json

{
  "nombre": "Juan Carlos Pérez",
  "telefono": "987654321",
  "imagenPerfilUrl": "https://example.com/foto.jpg"
}
```

### Direcciones

#### Crear Dirección
```http
POST /api/direcciones
Content-Type: application/json

{
  "usuarioId": 1,
  "etiqueta": "Casa",
  "direccionCompleta": "Av. Lima 123, Dept 401",
  "referencia": "Edificio blanco",
  "latitud": -12.046374,
  "longitud": -77.042793,
  "esPredeterminada": true
}
```

#### Obtener Direcciones de Usuario
```http
GET /api/direcciones/usuario/{usuarioId}
```

### Restaurantes

#### Listar Todos
```http
GET /api/restaurantes
```

#### Buscar por Nombre
```http
GET /api/restaurantes/buscar?nombre=burger
```

#### Filtrar por Categoría
```http
GET /api/restaurantes/filtrar/categoria/{categoriaId}
```

#### Filtrar por Disponibilidad
```http
GET /api/restaurantes/filtrar/disponibilidad?estaAbierto=true
```

#### Filtrar por Calificación
```http
GET /api/restaurantes/filtrar/calificacion?calificacionMinima=4.0
```

#### Filtrar por Tiempo de Espera
```http
GET /api/restaurantes/filtrar/tiempo-espera?tiempoMaximo=30
```

### Productos

#### Listar Productos de Restaurante
```http
GET /api/productos/restaurante/{restauranteId}
```

#### Listar Solo Disponibles
```http
GET /api/productos/restaurante/{restauranteId}/disponibles
```

#### Detalle de Producto
```http
GET /api/productos/{id}
```

### Pedidos

#### Crear Pedido
```http
POST /api/pedidos
Content-Type: application/json

{
  "usuarioId": 1,
  "restauranteId": 1,
  "direccionEntregaId": 1,
  "notasInstrucciones": "Sin cebolla",
  "detalles": [
    {
      "productoId": 1,
      "cantidad": 2,
      "opciones": [
        {
          "opcionId": 1
        },
        {
          "opcionId": 5
        }
      ]
    }
  ]
}
```

#### Historial de Pedidos de Usuario
```http
GET /api/pedidos/usuario/{usuarioId}
```

#### Detalle de Pedido
```http
GET /api/pedidos/{id}
```

#### Actualizar Estado de Pedido
```http
PUT /api/pedidos/{id}/estado?estado=EN_PREPARACION
```

#### Cancelar Pedido
```http
PUT /api/pedidos/{id}/cancelar
```

### Pagos

#### Crear Pago
```http
POST /api/pagos
Content-Type: application/json

{
  "pedidoId": 1,
  "monto": 45.50,
  "metodo": "TARJETA_CREDITO"
}
```

#### Confirmar Pago
```http
POST /api/pagos/{id}/confirmar
```

#### Obtener Client Secret (para Stripe)
```http
GET /api/pagos/{id}/client-secret
```

#### Webhook de Stripe
```http
POST /api/pagos/webhook/stripe
```

## 🔄 Estados del Pedido

Los pedidos siguen este flujo de estados:

1. **PENDIENTE_PAGO** - Pedido creado, esperando pago
2. **CONFIRMADO_TIENDA** - Pago confirmado, tienda notificada
3. **EN_PREPARACION** - Restaurante preparando el pedido
4. **LISTO_PARA_RECOGER** - Pedido listo para ser recogido por delivery
5. **EN_CAMINO** - Delivery en camino al cliente
6. **ENTREGADO** - Pedido entregado exitosamente
7. **CANCELADO** - Pedido cancelado (posible en cualquier momento antes de EN_CAMINO)

## 💳 Métodos de Pago

- `TARJETA_CREDITO`
- `TARJETA_DEBITO`
- `YAPE`
- `EFECTIVO`

## 🔐 Autenticación Simple

Este backend utiliza autenticación simple basada en validación de credenciales:

1. El usuario se registra con email y contraseña
2. La contraseña se hashea con BCrypt
3. En el login, se validan las credenciales y se devuelven los datos del usuario
4. Para endpoints protegidos, se puede validar email/password en cada request usando:

```http
POST /api/usuarios/validar-credenciales
Content-Type: application/json

{
  "email": "usuario@example.com",
  "password": "password123"
}
```

## 📊 Base de Datos

El esquema incluye las siguientes tablas:

- `usuarios` - Información de usuarios
- `direcciones` - Direcciones de entrega
- `restaurantes` - Datos de restaurantes
- `categorias` - Categorías de restaurantes
- `restaurante_categorias` - Relación muchos a muchos
- `productos` - Menú de productos
- `grupos_opciones` - Grupos de personalización
- `opciones` - Opciones individuales de personalización
- `pedidos` - Pedidos realizados
- `detalles_pedido` - Ítems del pedido
- `opciones_detalle_pedido` - Opciones seleccionadas por ítem
- `pagos` - Información de pagos

## 🧪 Datos de Prueba

El archivo `data.sql` incluye datos de ejemplo:

- Usuario: `juan@example.com` / `password123`
- 4 Restaurantes con productos
- 6 Categorías
- Productos con opciones personalizables

## 🌐 CORS

El backend tiene CORS configurado para permitir peticiones desde cualquier origen (`*`), ideal para desarrollo. Para producción, se recomienda especificar los orígenes permitidos.

## 📦 Estructura del Proyecto

```
src/main/java/com/example/chaski_backend/
├── config/          # Configuraciones (CORS)
├── controller/      # Controladores REST
├── dto/            # Data Transfer Objects
├── enums/          # Enumeraciones
├── exception/      # Manejo de excepciones
├── mapper/         # Mappers de MapStruct
├── model/          # Entidades JPA
├── repository/     # Repositorios de datos
└── service/        # Lógica de negocio
```

## 🔨 Compilación para Producción

```bash
mvn clean package -DskipTests
java -jar target/chaski-backend-0.0.1-SNAPSHOT.jar
```

## 📝 Notas Importantes

1. **Stripe**: Asegúrate de usar tu clave de API real de Stripe en producción
2. **Seguridad**: Para producción, considera implementar JWT o Spring Security completo
3. **Base de Datos**: Cambia `spring.jpa.hibernate.ddl-auto` a `validate` en producción
4. **CORS**: Restringe los orígenes permitidos en producción

## 🤝 Contribución

Este es un proyecto académico. Para mejoras:

1. Fork del proyecto
2. Crear rama feature (`git checkout -b feature/AmazingFeature`)
3. Commit cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir Pull Request

## 📄 Licencia

Proyecto académico - uso educativo

## 👨‍💻 Autor

Desarrollado como proyecto de Chaski Backend API

