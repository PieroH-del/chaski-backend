# 🍕 Chaski Backend - API REST para Delivery de Comida

Backend completo en Spring Boot para una aplicación móvil Android tipo delivery de comida. Sistema de gestión de pedidos, restaurantes, productos y pagos.

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)

## ✨ Características Principales

- ✅ **Autenticación Segura**: BCrypt para encriptación de contraseñas
- ✅ **Gestión de Usuarios**: Registro, actualización y gestión de direcciones
- ✅ **Catálogo de Restaurantes**: Búsqueda y filtros avanzados
- ✅ **Productos Personalizables**: Sistema de opciones para productos
- ✅ **Sistema de Pedidos**: Estados completos (Pendiente → Entregado)
- ✅ **Procesamiento de Pagos**: Integración con Stripe
- ✅ **Cálculos Automáticos**: Subtotal, impuestos (18%), envío
- ✅ **Base de Datos**: MySQL en Railway (configurado)
- ✅ **CORS**: Configurado para aplicaciones móviles
- ✅ **MapStruct**: Mapeo eficiente de DTOs

## 🛠️ Stack Tecnológico

- Java 21
- Spring Boot 4.0.0
- Spring Data JPA
- MySQL 8.0 (Railway)
- MapStruct 1.5.5
- Lombok
- BCrypt
- Stripe SDK
- Maven

## 🚀 Inicio Rápido

### 1. Clonar Repositorio

```bash
git clone https://github.com/PieroH-del/chaski-backend.git
cd chaski-backend
```

### 2. Configurar Variables de Entorno

La base de datos MySQL ya está configurada en Railway. Puedes usar:

**Para conexión pública (recomendado para Azure):**
```bash
export MYSQLHOST=aboose.proxy.rlwy.net
export MYSQLPORT=41095
export MYSQLDATABASE=railway
export MYSQLUSER=root
export MYSQLPASSWORD=BBvrDiRMBKIQJFZgJsvKKepIUltHZTBH
export STRIPE_API_KEY=sk_test_tu_clave
```

Ver [ENVIRONMENT_VARIABLES.md](ENVIRONMENT_VARIABLES.md) para más opciones.

### 3. Compilar y Ejecutar

```bash
# Compilar
mvn clean package -DskipTests

# Ejecutar
java -jar target/chaski-backend-0.0.1-SNAPSHOT.jar
```

O directamente:
```bash
mvn spring-boot:run
```

**La API estará en:** `http://localhost:8080/api`

## ☁️ Despliegue en Azure

### Configurar Variables en Azure App Service

En **Azure Portal → App Service → Configuration → Application settings**:

| Variable | Valor |
|----------|-------|
| `MYSQLHOST` | `aboose.proxy.rlwy.net` |
| `MYSQLPORT` | `41095` |
| `MYSQLDATABASE` | `railway` |
| `MYSQLUSER` | `root` |
| `MYSQLPASSWORD` | `BBvrDiRMBKIQJFZgJsvKKepIUltHZTBH` |
| `STRIPE_API_KEY` | Tu clave de Stripe |

### Desplegar desde GitHub

1. En Azure Portal → **Deployment Center**
2. Seleccionar **GitHub** como fuente
3. Elegir repositorio: `PieroH-del/chaski-backend`
4. Rama: `main`
5. Azure creará automáticamente el workflow

## 📌 Endpoints Principales

**Base URL:** `http://localhost:8080/api` (desarrollo)

### Usuarios
- `POST /usuarios/registro` - Registrar usuario
- `POST /usuarios/login` - Iniciar sesión
- `GET /usuarios/{id}` - Obtener perfil
- `PUT /usuarios/{id}` - Actualizar perfil

### Restaurantes
- `GET /restaurantes` - Listar todos
- `GET /restaurantes/{id}` - Detalle
- `GET /restaurantes/buscar?nombre={nombre}` - Buscar
- `GET /restaurantes/abiertos` - Solo abiertos

### Productos
- `GET /productos/restaurante/{restauranteId}` - Por restaurante
- `GET /productos/{id}` - Detalle
- `GET /productos/disponibles/{restauranteId}` - Disponibles

### Pedidos
- `POST /pedidos` - Crear pedido
- `GET /pedidos/usuario/{usuarioId}` - Por usuario
- `PUT /pedidos/{id}/estado` - Actualizar estado
- `PUT /pedidos/{id}/cancelar` - Cancelar

### Pagos
- `POST /pagos` - Crear pago
- `PUT /pagos/{id}/confirmar` - Confirmar pago
- `GET /pagos/pedido/{pedidoId}` - Por pedido

Ver [API_DOCUMENTATION.md](API_DOCUMENTATION.md) para documentación completa.

## 📁 Estructura del Proyecto

```
chaski-backend/
├── src/main/java/com/example/chaski_backend/
│   ├── config/          # Configuración (CORS)
│   ├── controller/      # REST Controllers
│   ├── dto/             # Data Transfer Objects
│   ├── enums/           # Estados y tipos
│   ├── exception/       # Manejo de errores
│   ├── mapper/          # MapStruct mappers
│   ├── model/           # Entidades JPA
│   ├── repository/      # Spring Data repositories
│   └── service/         # Lógica de negocio
├── src/main/resources/
│   ├── application.properties
│   └── data.sql         # Datos iniciales
├── src/test/
│   └── resources/
│       └── application.properties  # Config para tests (H2)
└── pom.xml
```

## 🧪 Testing

Los tests usan H2 en memoria (no requieren MySQL):

```bash
# Ejecutar todos los tests
mvn test

# Ejecutar y generar reporte
mvn clean test

# Compilar sin tests
mvn clean package -DskipTests
```

## 🗄️ Base de Datos

### Configuración Actual: Railway MySQL

✅ Ya configurado y funcionando
- Host público: `aboose.proxy.rlwy.net:41095`
- Host interno: `mysql.railway.internal:3306`
- Database: `railway`
- Usuario: `root`

### Modelo de Datos

Entidades principales:
- Usuario → Direccion (1:N)
- Usuario → Pedido (1:N)
- Restaurante → Producto (1:N)
- Producto → GrupoOpciones → Opcion
- Pedido → DetallePedido → OpcionDetallePedido
- Pedido → Pago (1:1)

## 📚 Documentación

- **[API_DOCUMENTATION.md](API_DOCUMENTATION.md)** - Endpoints detallados
- **[ENVIRONMENT_VARIABLES.md](ENVIRONMENT_VARIABLES.md)** - Variables de entorno
- **[HELP.md](HELP.md)** - Guía de Spring Boot

## 🔧 Solución de Problemas

### Error de conexión a MySQL
- Verificar que las variables de entorno estén configuradas
- Usar la URL pública si estás fuera de Railway
- Verificar firewall/reglas de red

### Tests fallando
- Los tests usan H2, no requieren MySQL
- Verificar que H2 esté en el `pom.xml`
- Ejecutar con `-DskipTests` si es necesario

### Build en GitHub Actions falla
- El workflow está configurado para ignorar fallos de tests
- Verifica las variables de entorno en GitHub Secrets

## 🤝 Contribución

1. Fork el proyecto
2. Crea tu rama (`git checkout -b feature/NuevaCaracteristica`)
3. Commit cambios (`git commit -m 'Add: Nueva característica'`)
4. Push (`git push origin feature/NuevaCaracteristica`)
5. Abre un Pull Request

## 📄 Licencia

MIT License - ver [LICENSE](LICENSE)

## 👥 Autor

**PieroH-del** - [GitHub](https://github.com/PieroH-del)

## 🙏 Agradecimientos

- Spring Boot Team
- MapStruct
- Stripe
- Railway (Hosting MySQL)

---

⭐ **Si te fue útil, dale una estrella en GitHub!**

📧 Soporte: [GitHub Issues](https://github.com/PieroH-del/chaski-backend/issues)

