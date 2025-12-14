# Variables de Entorno - Chaski Backend

Este documento describe las variables de entorno necesarias para ejecutar la aplicación.

## 🗄️ Base de Datos MySQL (Azure/Railway)

Basado en la configuración actual de Railway/Azure:

```bash
# URL completa de conexión (opción 1 - recomendada)
MYSQL_URL=mysql://root:BBvrDiRMBKIQJFZgJsvKKepIUltHZTBH@mysql.railway.internal:3306/railway

# O usando variables separadas (opción 2)
MYSQLHOST=mysql.railway.internal
MYSQLPORT=3306
MYSQLDATABASE=railway
MYSQLUSER=root
MYSQLPASSWORD=BBvrDiRMBKIQJFZgJsvKKepIUltHZTBH

# URL pública (si usas desde fuera de Railway)
MYSQL_PUBLIC_URL=mysql://root:BBvrDiRMBKIQJFZgJsvKKepIUltHZTBH@aboose.proxy.rlwy.net:41095/railway
```

## 💳 Stripe (Pagos)

```bash
# Clave de API de Stripe (TEST o LIVE)
STRIPE_API_KEY=sk_test_tu_clave_de_stripe_aqui
# O para producción:
STRIPE_API_KEY=sk_live_tu_clave_de_stripe_aqui
```

## 🚀 Configuración para Azure App Service

### En Azure Portal → Configuration → Application settings

Agregar las siguientes variables:

| Nombre | Valor | Descripción |
|--------|-------|-------------|
| `MYSQL_URL` | `jdbc:mysql://mysql.railway.internal:3306/railway?useSSL=true&requireSSL=false&serverTimezone=UTC` | URL JDBC completa |
| `MYSQLHOST` | `mysql.railway.internal` | Host de MySQL |
| `MYSQLPORT` | `3306` | Puerto de MySQL |
| `MYSQLDATABASE` | `railway` | Nombre de la base de datos |
| `MYSQLUSER` | `root` | Usuario de MySQL |
| `MYSQLPASSWORD` | `BBvrDiRMBKIQJFZgJsvKKepIUltHZTBH` | Contraseña de MySQL |
| `STRIPE_API_KEY` | `sk_test_...` o `sk_live_...` | API Key de Stripe |

### Configuración con URL Pública (Fuera de Railway)

Si despliegas en Azure y necesitas conectarte a la base de datos de Railway desde fuera:

```
MYSQL_URL=jdbc:mysql://aboose.proxy.rlwy.net:41095/railway?useSSL=true&requireSSL=false&serverTimezone=UTC
MYSQLHOST=aboose.proxy.rlwy.net
MYSQLPORT=41095
MYSQLDATABASE=railway
MYSQLUSER=root
MYSQLPASSWORD=BBvrDiRMBKIQJFZgJsvKKepIUltHZTBH
```

## 🖥️ Configuración Local (Desarrollo)

### Windows (PowerShell):
```powershell
$env:MYSQLHOST="aboose.proxy.rlwy.net"
$env:MYSQLPORT="41095"
$env:MYSQLDATABASE="railway"
$env:MYSQLUSER="root"
$env:MYSQLPASSWORD="BBvrDiRMBKIQJFZgJsvKKepIUltHZTBH"
$env:STRIPE_API_KEY="sk_test_tu_clave"
```

### Linux/Mac (Bash):
```bash
export MYSQLHOST="aboose.proxy.rlwy.net"
export MYSQLPORT="41095"
export MYSQLDATABASE="railway"
export MYSQLUSER="root"
export MYSQLPASSWORD="BBvrDiRMBKIQJFZgJsvKKepIUltHZTBH"
export STRIPE_API_KEY="sk_test_tu_clave"
```

### Archivo .env (NO SUBIR A GIT):
```env
MYSQLHOST=aboose.proxy.rlwy.net
MYSQLPORT=41095
MYSQLDATABASE=railway
MYSQLUSER=root
MYSQLPASSWORD=BBvrDiRMBKIQJFZgJsvKKepIUltHZTBH
STRIPE_API_KEY=sk_test_tu_clave
```

## 🔒 Seguridad

⚠️ **IMPORTANTE:**
- NUNCA subir contraseñas al repositorio
- Rotar las contraseñas periódicamente
- Usar variables de entorno en todos los ambientes
- En producción, usar siempre conexiones SSL/TLS

## 🧪 Variables para Testing

Los tests usan H2 en memoria, no necesitan estas variables. Ver `src/test/resources/application.properties`.

## 📝 Notas

1. La configuración actual usa Railway como proveedor de base de datos MySQL
2. Hay dos URLs disponibles:
   - **Interna (Railway)**: `mysql.railway.internal:3306` (solo dentro de Railway)
   - **Pública**: `aboose.proxy.rlwy.net:41095` (desde cualquier lugar)
3. Para Azure App Service, usa la URL pública
4. La aplicación está configurada para funcionar con ambas opciones automáticamente

## 🔄 Migración a Azure MySQL

Si decides migrar a Azure Database for MySQL en el futuro:

```
MYSQLHOST=tu-servidor.mysql.database.azure.com
MYSQLPORT=3306
MYSQLDATABASE=chaski_db
MYSQLUSER=tu-usuario@tu-servidor
MYSQLPASSWORD=tu-contraseña
```

Y actualizar la URL:
```
MYSQL_URL=jdbc:mysql://tu-servidor.mysql.database.azure.com:3306/chaski_db?useSSL=true&requireSSL=true&serverTimezone=UTC
```

