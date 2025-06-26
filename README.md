# 🎵 Sistema de Instrumentos Musicales - Backend API

Una API REST completa desarrollada con **Spring Boot 3** para la gestión de instrumentos musicales, categorías e imágenes.

## 📋 Características Principales

- ✅ **CRUD completo** de instrumentos y categorías
- ✅ **Sistema de imágenes** con upload y gestión
- ✅ **Paginación y filtrado** avanzado
- ✅ **Búsqueda** por nombre de instrumento
- ✅ **Validaciones** robustas de datos
- ✅ **Manejo de errores** estructurado
- ✅ **Logging** detallado para debugging
- ✅ **CORS** configurado para desarrollo frontend
- ✅ **Base de datos PostgreSQL** con relaciones optimizadas

## 🛠️ Tecnologías Utilizadas

- **Java 17+**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **PostgreSQL 12+**
- **Maven 3.6+**
- **Hibernate**
- **Jackson** para serialización JSON
- **SLF4J + Logback** para logging

## 🚀 Instalación y Configuración

### Prerrequisitos

\`\`\`bash
# Verificar Java
java -version  # Debe ser 17 o superior

# Verificar Maven
mvn -version   # Debe ser 3.6 o superior

# Verificar PostgreSQL
psql --version # Debe ser 12 o superior
\`\`\`

### 1. Configurar Base de Datos

\`\`\`bash
# Crear base de datos
psql -U postgres -f scripts/01-crear-bd.sql

# Crear tablas
psql -U postgres -d InstrumentosDB -f scripts/02-crear-tablas.sql

# Insertar datos iniciales
psql -U postgres -d InstrumentosDB -f scripts/03-insertar-datos-json.sql
psql -U postgres -d InstrumentosDB -f scripts/04-datos-adicionales.sql

# Verificar instalación
psql -U postgres -d InstrumentosDB -c "SELECT COUNT(*) FROM instrumentos;"
\`\`\`

### 2. Configurar Aplicación

\`\`\`bash
# Clonar repositorio
git clone <url-del-repositorio>
cd instrumentos-back-spring

# Configurar application.properties (si es necesario)
# Las credenciales por defecto son:
# - Usuario: postgres
# - Contraseña: admin123
# - Puerto: 5432
# - Base de datos: InstrumentosDB
\`\`\`

### 3. Compilar y Ejecutar

\`\`\`bash
# Compilar proyecto
mvn clean compile

# Ejecutar aplicación
mvn spring-boot:run

# O ejecutar JAR compilado
mvn clean package
java -jar target/instrumentos-back-spring-1.0.0.jar
\`\`\`

### 4. Verificar Funcionamiento

\`\`\`bash
# Probar endpoint básico
curl http://localhost:3001/api/instrumentos

# Verificar salud de la aplicación
curl http://localhost:3001/actuator/health
\`\`\`

## 📡 Endpoints de la API

### Base URL
\`\`\`
http://localhost:3001/api
\`\`\`

### 🎸 Instrumentos

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/instrumentos` | Obtener todos los instrumentos |
| GET | `/instrumentos?paginated=true&page=0&size=10` | Obtener con paginación |
| GET | `/instrumentos/{id}` | Obtener instrumento por ID |
| POST | `/instrumentos` | Crear nuevo instrumento |
| PUT | `/instrumentos/{id}` | Actualizar instrumento |
| DELETE | `/instrumentos/{id}` | Eliminar instrumento |
| GET | `/instrumentos/categoria/{categoriaId}` | Filtrar por categoría |
| GET | `/instrumentos/buscar?nombre={nombre}` | Buscar por nombre |

### 🗂️ Categorías

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/categorias` | Obtener todas las categorías |
| GET | `/categorias/{id}` | Obtener categoría por ID |
| POST | `/categorias` | Crear nueva categoría |
| PUT | `/categorias/{id}` | Actualizar categoría |
| DELETE | `/categorias/{id}` | Eliminar categoría |
| GET | `/categorias/search?denominacion={nombre}` | Buscar categorías |

### 🖼️ Imágenes

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/images/upload/{instrumentoId}` | Subir imagen |
| GET | `/images/{filename}` | Obtener archivo de imagen |
| GET | `/images/instrumento/{instrumentoId}` | Obtener imágenes de instrumento |
| GET | `/images/instrumento/{instrumentoId}/primary` | Obtener imagen principal |
| PUT | `/images/{imageId}/primary` | Establecer como principal |
| DELETE | `/images/{imageId}` | Eliminar imagen |

## 📝 Ejemplos de Uso

### Crear Instrumento

\`\`\`bash
curl -X POST "http://localhost:3001/api/instrumentos" \
  -H "Content-Type: application/json" \
  -d '{
    "instrumento": "Guitarra Acústica Yamaha",
    "marca": "Yamaha",
    "modelo": "FG800",
    "precio": 15000.00,
    "costoEnvio": "G",
    "cantidadVendida": 0,
    "descripcion": "Guitarra acústica ideal para principiantes",
    "categoria": { "id": 1 }
  }'
\`\`\`

### Subir Imagen

\`\`\`bash
curl -X POST "http://localhost:3001/api/images/upload/1" \
  -F "file=@guitarra.jpg" \
  -F "altText=Guitarra Yamaha FG800" \
  -F "isPrimary=true"
\`\`\`

### Buscar Instrumentos

\`\`\`bash
# Buscar por nombre
curl "http://localhost:3001/api/instrumentos/buscar?nombre=guitarra"

# Filtrar por categoría
curl "http://localhost:3001/api/instrumentos/categoria/1"

# Obtener con paginación
curl "http://localhost:3001/api/instrumentos?paginated=true&page=0&size=5&sortBy=precio&sortDir=desc"
\`\`\`

## 🗄️ Estructura de Base de Datos

### Tabla: categorias
\`\`\`sql
CREATE TABLE categorias (
    id SERIAL PRIMARY KEY,
    denominacion VARCHAR(100) NOT NULL UNIQUE
);
\`\`\`

### Tabla: instrumentos
\`\`\`sql
CREATE TABLE instrumentos (
    id SERIAL PRIMARY KEY,
    instrumento VARCHAR(255) NOT NULL,
    marca VARCHAR(100) NOT NULL,
    modelo VARCHAR(100) NOT NULL,
    imagen VARCHAR(255),
    precio DECIMAL(10,2) NOT NULL,
    costo_envio CHAR(1) DEFAULT 'G',
    cantidad_vendida INTEGER DEFAULT 0,
    descripcion TEXT,
    id_categoria INTEGER NOT NULL,
    FOREIGN KEY (id_categoria) REFERENCES categorias(id)
);
\`\`\`

### Tabla: product_images
\`\`\`sql
CREATE TABLE product_images (
    id SERIAL PRIMARY KEY,
    instrumento_id INTEGER NOT NULL,
    image_url VARCHAR(500) NOT NULL,
    alt_text VARCHAR(255),
    is_primary BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (instrumento_id) REFERENCES instrumentos(id) ON DELETE CASCADE
);
\`\`\`

## 📊 Formato de Respuestas

### Respuesta Exitosa
\`\`\`json
{
  "success": true,
  "message": "Operación exitosa",
  "data": {
    "id": 1,
    "instrumento": "Guitarra Acústica",
    "marca": "Yamaha",
    "precio": 15000.00,
    "categoriaDenominacion": "Cuerda"
  }
}
\`\`\`

### Respuesta de Error
\`\`\`json
{
  "success": false,
  "message": "Descripción del error",
  "data": null
}
\`\`\`

## 🔧 Configuración

### Variables de Entorno (Opcionales)
\`\`\`bash
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=InstrumentosDB
export DB_USER=postgres
export DB_PASSWORD=admin123
export SERVER_PORT=3001
\`\`\`

### Perfiles de Spring
\`\`\`bash
# Desarrollo
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Producción
java -jar app.jar --spring.profiles.active=prod
\`\`\`

## 🧪 Testing

### Ejecutar Tests Automatizados
\`\`\`bash
# Ejecutar script de testing
bash test-api.sh

# Tests incluidos:
# ✅ CRUD de categorías (5 tests)
# ✅ CRUD de instrumentos (6 tests)
# ✅ Sistema de imágenes (3 tests)
# ✅ Validaciones (3 tests)
# ✅ Total: 17 tests
\`\`\`

### Tests Manuales con curl
\`\`\`bash
# Test básico de conectividad
curl -I http://localhost:3001/api/instrumentos

# Test de creación de categoría
curl -X POST "http://localhost:3001/api/categorias" \
  -H "Content-Type: application/json" \
  -d '{"denominacion": "Test Category"}'
\`\`\`

## 📁 Estructura del Proyecto

\`\`\`
src/
├── main/
│   ├── java/com/instrumentos/
│   │   ├── config/          # Configuraciones (CORS, Web)
│   │   ├── controller/      # Controladores REST
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── exception/      # Manejo de excepciones
│   │   ├── model/          # Entidades JPA
│   │   ├── repository/     # Repositorios JPA
│   │   └── service/        # Lógica de negocio
│   └── resources/
│       ├── application.properties
│       └── static/         # Archivos estáticos
├── test/                   # Tests unitarios
├── scripts/               # Scripts SQL
├── public/images/         # Directorio de imágenes
└── target/               # Archivos compilados
\`\`\`

## 🔍 Logging y Monitoreo

### Logs de la Aplicación
\`\`\`bash
# Ver logs en tiempo real
tail -f logs/spring.log

# Filtrar errores
grep ERROR logs/spring.log

# Filtrar por endpoint
grep "/api/instrumentos" logs/spring.log
\`\`\`

### Endpoints de Monitoreo
\`\`\`bash
# Salud de la aplicación
curl http://localhost:3001/actuator/health

# Información de la aplicación
curl http://localhost:3001/actuator/info

# Métricas
curl http://localhost:3001/actuator/metrics
\`\`\`

## 🚨 Solución de Problemas

### Error de Conexión a Base de Datos
1. Verificar que PostgreSQL esté ejecutándose
2. Confirmar credenciales en `application.properties`
3. Verificar que la base de datos `InstrumentosDB` exista

### Error CORS
1. Verificar configuración en `WebConfig.java`
2. Confirmar que el frontend esté en puerto permitido (3000, 5173)

### Error de Subida de Archivos
1. Verificar permisos de escritura en `public/images/`
2. Confirmar tamaño máximo de archivo (5MB)
3. Verificar formatos permitidos (JPG, PNG, GIF, WEBP)

### Puerto en Uso
\`\`\`bash
# Verificar qué proceso usa el puerto 3001
netstat -tulpn | grep 3001

# Cambiar puerto en application.properties
server.port=3002
\`\`\`

## 🔄 Integración con Frontend

### Configuración de Axios (JavaScript)
\`\`\`javascript
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:3001/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Ejemplo de uso
const getInstrumentos = async () => {
  const response = await api.get('/instrumentos');
  return response.data;
};
\`\`\`

### URLs de Imágenes
\`\`\`javascript
// URL base para imágenes
const IMAGE_BASE_URL = 'http://localhost:3001/api/images/';

// Mostrar imagen
const imageUrl = IMAGE_BASE_URL + filename;
\`\`\`

## 📈 Rendimiento y Optimización

- **Pool de conexiones**: Configurado con HikariCP
- **Índices de base de datos**: En campos frecuentemente consultados
- **Cache de recursos estáticos**: 1 hora para imágenes
- **Compresión HTTP**: Habilitada para JSON y texto
- **Batch processing**: Para operaciones masivas en BD

## 🔐 Seguridad

- **Validación de entrada**: En todos los endpoints
- **Sanitización de archivos**: Validación de tipos y tamaños
- **Manejo seguro de errores**: Sin exposición de stack traces
- **CORS configurado**: Solo orígenes permitidos

## 📚 Documentación Adicional

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)

## 👨‍💻 Desarrollo

### Agregar Nueva Funcionalidad
1. Crear entidad en `model/`
2. Crear repositorio en `repository/`
3. Implementar servicio en `service/`
4. Crear controlador en `controller/`
5. Agregar tests correspondientes

### Convenciones de Código
- **Nombres de clases**: PascalCase
- **Nombres de métodos**: camelCase
- **Nombres de endpoints**: kebab-case
- **Logging**: Usar SLF4J con niveles apropiados

## 📄 Licencia

Este proyecto está desarrollado por Agustín Sandoval

---

**Desarrollado por:** Agustín Sandoval  
**Institución:** Universidad Tecnológica Nacional - Facultad Regional Mendoza  
**Carrera:** Tecnicatura Universitaria en Programación  
**Materia:** Laboratorio de Computación 4  
**Año:** 2025
---

## 🆘 Soporte

Para reportar problemas o solicitar ayuda:

1. **Issues del repositorio**: Para bugs y mejoras
2. **Documentación**: Revisar este README y la documentación de Spring Boot
3. **Logs**: Revisar logs de la aplicación para detalles de errores

**¡Gracias por usar el Sistema de Instrumentos Musicales! 🎵**
