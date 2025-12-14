# API Documentation - Chaski Backend

## Resumen del Proyecto

Chaski Backend es una API REST completa para una aplicación de delivery de comida, desarrollada con Spring Boot, que incluye gestión de usuarios, restaurantes, productos, pedidos y pagos con integración de Stripe.

## Arquitectura del Sistema

```
┌─────────────────┐
│  Android App    │
└────────┬────────┘
         │ HTTP/JSON
         ▼
┌─────────────────────────────────────────┐
│         Spring Boot REST API             │
│  ┌─────────────────────────────────┐   │
│  │       Controllers               │   │
│  │  (REST Endpoints + CORS)        │   │
│  └──────────────┬──────────────────┘   │
│                 │                        │
│  ┌──────────────▼──────────────────┐   │
│  │         Services                │   │
│  │  (Business Logic + BCrypt)      │   │
│  └──────────────┬──────────────────┘   │
│                 │                        │
│  ┌──────────────▼──────────────────┐   │
│  │       Repositories              │   │
│  │     (Spring Data JPA)           │   │
│  └──────────────┬──────────────────┘   │
│                 │                        │
│  ┌──────────────▼──────────────────┐   │
│  │       Mappers (MapStruct)       │   │
│  │      Entities ↔ DTOs            │   │
│  └─────────────────────────────────┘   │
└─────────────────────────────────────────┘
         │                    │
         ▼                    ▼
┌─────────────────┐  ┌─────────────────┐
│  MySQL Database │  │   Stripe API    │
└─────────────────┘  └─────────────────┘
```

## Modelo de Datos

### Entidades Principales

1. **Usuario** - Información de clientes
2. **Direccion** - Direcciones de entrega
3. **Restaurante** - Datos de restaurantes
4. **Categoria** - Categorías de restaurantes
5. **Producto** - Menú de productos
6. **GrupoOpciones** - Grupos de personalización
7. **Opcion** - Opciones individuales
8. **Pedido** - Órdenes realizadas
9. **DetallePedido** - Items del pedido
10. **OpcionDetallePedido** - Opciones seleccionadas
11. **Pago** - Información de pagos

### Relaciones

```
Usuario 1──N Direccion
Usuario 1──N Pedido

Restaurante N──N Categoria
Restaurante 1──N Producto
Restaurante 1──N Pedido

Producto 1──N GrupoOpciones
GrupoOpciones 1──N Opcion

Pedido 1──N DetallePedido
Pedido 1──1 Pago

DetallePedido N──1 Producto
DetallePedido 1──N OpcionDetallePedido
OpcionDetallePedido N──1 Opcion
```

## API Endpoints

### 1. Autenticación y Usuarios

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/usuarios/registro` | Registrar nuevo usuario |
| POST | `/api/usuarios/login` | Iniciar sesión |
| GET | `/api/usuarios/{id}` | Obtener usuario por ID |
| PUT | `/api/usuarios/{id}` | Actualizar perfil |
| POST | `/api/usuarios/validar-credenciales` | Validar email/password |

**Ejemplo - Registro:**
```json
POST /api/usuarios/registro
{
  "nombre": "Juan Pérez",
  "email": "juan@example.com",
  "password": "password123",
  "telefono": "987654321"
}

Response 201 Created:
{
  "id": 1,
  "nombre": "Juan Pérez",
  "email": "juan@example.com",
  "telefono": "987654321",
  "fechaRegistro": "2025-01-14T10:00:00",
  "activo": true
}
```

### 2. Direcciones

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/direcciones` | Crear dirección |
| GET | `/api/direcciones/usuario/{usuarioId}` | Listar direcciones de usuario |
| GET | `/api/direcciones/{id}` | Obtener dirección por ID |
| PUT | `/api/direcciones/{id}` | Actualizar dirección |
| DELETE | `/api/direcciones/{id}` | Eliminar dirección |

### 3. Restaurantes

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/restaurantes` | Listar todos |
| GET | `/api/restaurantes/{id}` | Obtener por ID |
| GET | `/api/restaurantes/buscar?nombre={nombre}` | Buscar por nombre |
| GET | `/api/restaurantes/filtrar/disponibilidad?estaAbierto={bool}` | Filtrar por apertura |
| GET | `/api/restaurantes/filtrar/categoria/{categoriaId}` | Filtrar por categoría |
| GET | `/api/restaurantes/filtrar/calificacion?calificacionMinima={decimal}` | Filtrar por rating |
| GET | `/api/restaurantes/filtrar/tiempo-espera?tiempoMaximo={int}` | Filtrar por tiempo |

### 4. Productos

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/productos/restaurante/{restauranteId}` | Productos de restaurante |
| GET | `/api/productos/restaurante/{restauranteId}/disponibles` | Solo disponibles |
| GET | `/api/productos/{id}` | Detalle con opciones |

**Ejemplo - Detalle de Producto:**
```json
GET /api/productos/1

Response 200 OK:
{
  "id": 1,
  "restauranteId": 1,
  "nombre": "Whopper",
  "descripcion": "Hamburguesa clásica",
  "precio": 15.90,
  "disponible": true,
  "gruposOpciones": [
    {
      "id": 1,
      "nombre": "Elige tu bebida",
      "esObligatorio": true,
      "seleccionMinima": 1,
      "seleccionMaxima": 1,
      "opciones": [
        {
          "id": 1,
          "nombre": "Coca Cola",
          "precioExtra": 0.00
        }
      ]
    }
  ]
}
```

### 5. Pedidos

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/pedidos` | Crear pedido |
| GET | `/api/pedidos/usuario/{usuarioId}` | Historial de usuario |
| GET | `/api/pedidos/{id}` | Detalle de pedido |
| PUT | `/api/pedidos/{id}/estado?estado={EstadoPedido}` | Actualizar estado |
| PUT | `/api/pedidos/{id}/cancelar` | Cancelar pedido |
| GET | `/api/pedidos/estado/{estado}` | Filtrar por estado |

**Estados Posibles:**
- `PENDIENTE_PAGO`
- `CONFIRMADO_TIENDA`
- `EN_PREPARACION`
- `LISTO_PARA_RECOGER`
- `EN_CAMINO`
- `ENTREGADO`
- `CANCELADO`

**Ejemplo - Crear Pedido:**
```json
POST /api/pedidos
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
        { "opcionId": 1 },
        { "opcionId": 5 }
      ]
    }
  ]
}

Response 201 Created:
{
  "id": 1,
  "subtotalProductos": 31.80,
  "costoEnvio": 5.00,
  "impuestos": 5.72,
  "totalFinal": 42.52,
  "estado": "PENDIENTE_PAGO",
  "fechaCreacion": "2025-01-14T11:00:00"
}
```

### 6. Pagos

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/pagos` | Crear pago |
| POST | `/api/pagos/{id}/confirmar` | Confirmar pago |
| POST | `/api/pagos/{id}/marcar-fallido` | Marcar como fallido |
| GET | `/api/pagos/pedido/{pedidoId}` | Obtener pago de pedido |
| GET | `/api/pagos/{id}/client-secret` | Client secret para Stripe |
| POST | `/api/pagos/webhook/stripe` | Webhook de Stripe |

**Métodos de Pago:**
- `TARJETA_CREDITO`
- `TARJETA_DEBITO`
- `YAPE`
- `EFECTIVO`

**Estados de Pago:**
- `PENDIENTE`
- `COMPLETADO`
- `FALLIDO`
- `REEMBOLSADO`

**Ejemplo - Crear Pago con Tarjeta:**
```json
POST /api/pagos
{
  "pedidoId": 1,
  "monto": 42.52,
  "metodo": "TARJETA_CREDITO"
}

Response 201 Created:
{
  "id": 1,
  "pedidoId": 1,
  "monto": 42.52,
  "metodo": "TARJETA_CREDITO",
  "estado": "PENDIENTE",
  "referenciaPasarela": "pi_3xxxxxxx"
}
```

## Códigos de Respuesta HTTP

| Código | Significado |
|--------|-------------|
| 200 | OK - Operación exitosa |
| 201 | Created - Recurso creado |
| 204 | No Content - Eliminación exitosa |
| 400 | Bad Request - Datos inválidos |
| 404 | Not Found - Recurso no encontrado |
| 500 | Internal Server Error - Error del servidor |

## Formato de Errores

```json
{
  "status": 400,
  "message": "El email ya está registrado",
  "timestamp": "2025-01-14T11:00:00"
}
```

**Errores de Validación:**
```json
{
  "status": 400,
  "errors": {
    "email": "Formato de email inválido",
    "password": "La contraseña es obligatoria"
  },
  "timestamp": "2025-01-14T11:00:00"
}
```

## Características de Seguridad

### Hashing de Contraseñas
- BCrypt con factor de trabajo de 10
- Salt generado automáticamente
- Contraseñas nunca almacenadas en texto plano

### CORS
- Configurado para permitir peticiones desde cualquier origen
- Métodos permitidos: GET, POST, PUT, DELETE, OPTIONS
- Headers permitidos: Todos

### Validaciones
- Bean Validation (JSR-380)
- Validaciones personalizadas en servicios
- Validación de transiciones de estado
- Verificación de disponibilidad de productos

## Integración con Stripe

### Flujo de Pago con Tarjeta

1. **Cliente crea pedido** → Estado: `PENDIENTE_PAGO`
2. **Cliente crea pago** → Stripe genera PaymentIntent
3. **App obtiene client secret** → Para procesar pago en cliente
4. **Cliente confirma pago** → Stripe procesa
5. **Webhook notifica resultado** → Backend actualiza estado
6. **Pago exitoso** → Pedido cambia a `CONFIRMADO_TIENDA`

### Configuración de Webhook

```bash
URL: https://tu-dominio.com/api/pagos/webhook/stripe
Eventos: payment_intent.succeeded, payment_intent.payment_failed
```

## Cálculo de Precios

```
Subtotal = Σ((Precio Producto + Σ Precio Opciones) × Cantidad)
Impuestos = Subtotal × 0.18 (18% IGV)
Total = Subtotal + Costo Envío + Impuestos
```

## Reglas de Negocio

### Pedidos
- Solo se pueden crear pedidos de restaurantes abiertos
- Los productos deben estar disponibles
- Las opciones obligatorias deben ser seleccionadas
- No se puede cancelar un pedido en estado `EN_CAMINO` o `ENTREGADO`

### Direcciones
- Solo puede haber una dirección predeterminada por usuario
- Al marcar una como predeterminada, las demás se desmarcan

### Usuarios
- Email único
- Teléfono único (si se proporciona)
- Contraseña hasheada automáticamente

## Performance y Optimización

- **Lazy Loading**: Relaciones cargadas bajo demanda
- **DTOs**: Transferencia optimizada de datos
- **Índices**: En campos de búsqueda frecuente
- **Connection Pooling**: HikariCP por defecto

## Tecnologías Utilizadas

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| Java | 21 | Lenguaje principal |
| Spring Boot | 4.0.0 | Framework |
| Spring Data JPA | 4.0.0 | Persistencia |
| MySQL | 8.0+ | Base de datos |
| MapStruct | 1.5.5 | Mapeo de objetos |
| Lombok | Latest | Reducción de boilerplate |
| BCrypt | Latest | Hashing de contraseñas |
| Stripe Java SDK | 26.5.0 | Procesamiento de pagos |
| Maven | 3.6+ | Gestión de dependencias |

## Buenas Prácticas Implementadas

✅ Separación de responsabilidades (Controller-Service-Repository)
✅ DTOs para transferencia de datos
✅ Mappers automáticos con MapStruct
✅ Validaciones con Bean Validation
✅ Manejo centralizado de excepciones
✅ CORS configurado
✅ Logging de SQL queries
✅ Timestamps automáticos
✅ Transacciones gestionadas
✅ Código limpio y documentado

## Limitaciones Actuales

⚠️ Sin autenticación JWT (autenticación simple)
⚠️ Sin paginación en listados
⚠️ Sin caché
⚠️ Sin rate limiting
⚠️ Sin audit logging
⚠️ Stripe en modo test

## Próximas Mejoras Sugeridas

🔜 Implementar JWT para autenticación stateless
🔜 Agregar paginación y sorting a los listados
🔜 Implementar Redis para caché
🔜 Agregar notificaciones push
🔜 Sistema de calificaciones y reseñas
🔜 Chat en tiempo real
🔜 Panel de administración
🔜 Reportes y analytics
🔜 Cupones y descuentos
🔜 Programa de lealtad

## Documentación Adicional

- `README.md` - Información general del proyecto
- `INSTALLATION.md` - Guía de instalación
- `TESTING_GUIDE.md` - Guía de pruebas
- `data.sql` - Datos de ejemplo

## Soporte

Para reportar bugs o sugerir mejoras, consulta la documentación del proyecto o contacta al equipo de desarrollo.

---

**Versión:** 1.0.0  
**Última actualización:** Enero 2025  
**Estado:** ✅ Producción Ready (con limitaciones señaladas)

