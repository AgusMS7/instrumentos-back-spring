# 📘 API Documentation - Sistema de Instrumentos Musicales

## 📌 Información General

- **Base URL:** [http://localhost:3001/api](http://localhost:3001/api)
- **Versión:** 1.0
- **Framework:** Spring Boot 3.2.0
- **Base de Datos:** PostgreSQL
- **Puerto:** 3001


---

## 🎸 ENDPOINTS DE INSTRUMENTOS

### 🔹 Obtener todos los instrumentos (con paginación)

`GET /api/instrumentos?page=0&size=10&sort=id,asc`

**Parámetros de consulta:**

- `page` (opcional): Número de página (default: 0)
- `size` (opcional): Tamaño de página (default: 10)
- `sort` (opcional): Campo y dirección de ordenamiento


**Respuesta ejemplo:**

```json
{
  "content": [
    {
      "id": 1,
      "instrumento": "Mandolina Instrumento Musical Stagg Sunburst",
      "marca": "Stagg",
      "modelo": "M20",
      "imagen": "nro10.jpg",
      "precio": 2450.00,
      "costoEnvio": "G",
      "cantidadVendida": 28,
      "descripcion": "Excelente mandolina...",
      "idCategoria": 1,
      "categoriaDenominacion": "Cuerda"
    }
  ],
  "totalElements": 30,
  "totalPages": 3,
  "size": 10,
  "number": 0
}
```

### 🔹 Obtener instrumento por ID

`GET /api/instrumentos/{id}`

**Respuesta exitosa:**

```json
{
  "success": true,
  "message": "Instrumento obtenido exitosamente",
  "data": {
    "id": 1,
    "instrumento": "Mandolina Instrumento Musical Stagg Sunburst",
    "marca": "Stagg",
    "modelo": "M20",
    "imagen": "nro10.jpg",
    "precio": 2450.00,
    "costoEnvio": "G",
    "cantidadVendida": 28,
    "descripcion": "Excelente mandolina...",
    "idCategoria": 1,
    "categoriaDenominacion": "Cuerda"
  }
}
```

### 🔹 Crear nuevo instrumento

`POST /api/instrumentos`**Header:** `Content-Type: application/json`

**Body ejemplo:**

```json
{
  "instrumento": "Guitarra Acustica",
  "marca": "Yamaha",
  "modelo": "FG800",
  "imagen": "guitarra.jpg",
  "precio": 15000.00,
  "costoEnvio": "G",
  "cantidadVendida": 0,
  "descripcion": "Guitarra acustica de calidad",
  "categoria": {
    "id": 1
  }
}
```

**Respuesta exitosa:**

```json
{
  "success": true,
  "message": "Instrumento creado exitosamente",
  "data": {
    "id": 31,
    "instrumento": "Guitarra Acustica",
    "marca": "Yamaha",
    "modelo": "FG800",
    "imagen": "guitarra.jpg",
    "precio": 15000.00,
    "costoEnvio": "G",
    "cantidadVendida": 0,
    "descripcion": "Guitarra acustica de calidad",
    "idCategoria": 1,
    "categoriaDenominacion": "Cuerda"
  }
}
```

### 🔹 Actualizar instrumento

`PUT /api/instrumentos/{id}`**Header:** `Content-Type: application/json`

**Body ejemplo:**

```json
{
  "instrumento": "Guitarra Acustica Modificada",
  "marca": "Yamaha",
  "modelo": "FG800",
  "precio": 16000.00,
  "categoria": {
    "id": 1
  }
}
```

### 🔹 Eliminar instrumento

`DELETE /api/instrumentos/{id}`

**Respuesta exitosa:**

```json
{
  "success": true,
  "message": "Instrumento eliminado exitosamente",
  "data": null
}
```

### 🔹 Filtrar por categoría

`GET /api/instrumentos/categoria/{categoriaId}`

**Respuesta ejemplo:**

```json
{
  "success": true,
  "message": "Instrumentos obtenidos exitosamente",
  "data": [
    {
      "id": 1,
      "instrumento": "Mandolina Instrumento Musical Stagg Sunburst",
      "marca": "Stagg",
      "modelo": "M20",
      "imagen": "nro10.jpg",
      "precio": 2450.00,
      "costoEnvio": "G",
      "cantidadVendida": 28,
      "descripcion": "Excelente mandolina...",
      "idCategoria": 1,
      "categoriaDenominacion": "Cuerda"
    }
  ]
}
```

### 🔹 Buscar instrumentos

`GET /api/instrumentos/buscar?nombre=guitarra`

**Respuesta ejemplo:**

```json
{
  "success": true,
  "message": "Instrumentos encontrados exitosamente",
  "data": [
    {
      "id": 5,
      "instrumento": "Guitarra Clasica Criolla",
      "marca": "Valencia",
      "modelo": "VC104",
      "imagen": "nro5.jpg",
      "precio": 8500.00,
      "costoEnvio": "G",
      "cantidadVendida": 15,
      "descripcion": "Guitarra clasica ideal para principiantes",
      "idCategoria": 1,
      "categoriaDenominacion": "Cuerda"
    }
  ]
}
```

---

## 🗂️ ENDPOINTS DE CATEGORÍAS

### 🔹 Obtener todas las categorías

`GET /api/categorias`

**Respuesta ejemplo:**

```json
{
  "success": true,
  "message": "Categorías obtenidas exitosamente",
  "data": [
    { "id": 1, "denominacion": "Cuerda" },
    { "id": 2, "denominacion": "Viento" },
    { "id": 3, "denominacion": "Percusion" },
    { "id": 4, "denominacion": "Teclado" },
    { "id": 5, "denominacion": "Electronico" }
  ]
}
```

### 🔹 Obtener categoría por ID

`GET /api/categorias/{id}`

**Respuesta exitosa:**

```json
{
  "success": true,
  "message": "Categoría obtenida exitosamente",
  "data": {
    "id": 1,
    "denominacion": "Cuerda"
  }
}
```

### 🔹 Crear nueva categoría

`POST /api/categorias`**Header:** `Content-Type: application/json`

**Body ejemplo:**

```json
{ "denominacion": "Nueva Categoria" }
```

**Respuesta exitosa:**

```json
{
  "success": true,
  "message": "Categoría creada exitosamente",
  "data": {
    "id": 6,
    "denominacion": "Nueva Categoria"
  }
}
```

### 🔹 Actualizar categoría

`PUT /api/categorias/{id}`**Header:** `Content-Type: application/json`

**Body ejemplo:**

```json
{ "denominacion": "Categoria Actualizada" }
```

### 🔹 Eliminar categoría

`DELETE /api/categorias/{id}`

**Respuesta exitosa:**

```json
{
  "success": true,
  "message": "Categoría eliminada exitosamente",
  "data": null
}
```

---

## 🖼️ ENDPOINTS DE IMÁGENES

### 🔹 Subir imagen para instrumento

`POST /api/images/upload/{instrumentoId}`**Header:** `Content-Type: multipart/form-data`

**Form Data:**

- `file`: Archivo de imagen (JPG, PNG, GIF)
- `altText` (opcional): Texto alternativo para la imagen
- `isPrimary` (opcional): Si es imagen principal (true/false)


**Respuesta exitosa:**

```json
{
  "success": true,
  "message": "Imagen subida exitosamente",
  "data": {
    "id": 1,
    "instrumentoId": 5,
    "imageUrl": "a1b2c3d4-e5f6-7890-abcd-ef1234567890.jpg",
    "altText": "Guitarra Clasica",
    "isPrimary": false,
    "createdAt": "2024-01-15T10:30:00"
  }
}
```

### 🔹 Obtener imágenes de un instrumento

`GET /api/images/instrumento/{instrumentoId}`

**Respuesta ejemplo:**

```json
{
  "success": true,
  "message": "Imágenes obtenidas exitosamente",
  "data": [
    {
      "id": 1,
      "instrumentoId": 5,
      "imageUrl": "a1b2c3d4-e5f6-7890-abcd-ef1234567890.jpg",
      "altText": "Guitarra Clasica",
      "isPrimary": true,
      "createdAt": "2024-01-15T10:30:00"
    }
  ]
}
```

### 🔹 Obtener imagen principal de un instrumento

`GET /api/images/instrumento/{instrumentoId}/primary`

**Respuesta exitosa:**

```json
{
  "success": true,
  "message": "Imagen principal obtenida exitosamente",
  "data": {
    "id": 1,
    "instrumentoId": 5,
    "imageUrl": "a1b2c3d4-e5f6-7890-abcd-ef1234567890.jpg",
    "altText": "Guitarra Clasica",
    "isPrimary": true,
    "createdAt": "2024-01-15T10:30:00"
  }
}
```

### 🔹 Obtener archivo de imagen

`GET /api/images/{filename}`

**Respuesta:** Archivo binario de imagen con headers apropiados

### 🔹 Eliminar imagen

`DELETE /api/images/{imageId}`

**Respuesta exitosa:**

```json
{
  "success": true,
  "message": "Imagen eliminada exitosamente",
  "data": null
}
```

### 🔹 Establecer imagen como principal

`PUT /api/images/{imageId}/primary`

**Respuesta exitosa:**

```json
{
  "success": true,
  "message": "Imagen establecida como principal exitosamente",
  "data": {
    "id": 1,
    "instrumentoId": 5,
    "imageUrl": "a1b2c3d4-e5f6-7890-abcd-ef1234567890.jpg",
    "altText": "Guitarra Clasica",
    "isPrimary": true,
    "createdAt": "2024-01-15T10:30:00"
  }
}
```

---

## 🌐 CONFIGURACIÓN CORS

El backend está configurado para aceptar peticiones desde:

- [http://localhost:3000](http://localhost:3000) (React dev server)
- [http://localhost:5173](http://localhost:5173) (Vite dev server)
- [http://127.0.0.1:3000](http://127.0.0.1:3000)
- [http://127.0.0.1:5173](http://127.0.0.1:5173)


**Métodos permitidos:** GET, POST, PUT, DELETE, OPTIONS**Headers permitidos:** Content-Type, Authorization, X-Requested-With

---

## 🚨 MANEJO DE ERRORES

**Códigos de Estado HTTP:**

- `200 OK`: Operación exitosa
- `201 Created`: Recurso creado exitosamente
- `400 Bad Request`: Error en los datos enviados
- `404 Not Found`: Recurso no encontrado
- `409 Conflict`: Conflicto (ej: categoría duplicada)
- `500 Internal Server Error`: Error interno del servidor


**Formato de respuesta de error:**

```json
{
  "success": false,
  "message": "Descripcion del error",
  "data": null
}
```

**Formato de respuesta exitosa:**

```json
{
  "success": true,
  "message": "Operacion exitosa",
  "data": { /* datos del recurso */ }
}
```

**Errores comunes:**

1. **Instrumento no encontrado (404):**


```json
{
  "success": false,
  "message": "Instrumento no encontrado",
  "data": null
}
```

2. **Categoría no encontrada (404):**


```json
{
  "success": false,
  "message": "Categoría no encontrada",
  "data": null
}
```

3. **Datos inválidos (400):**


```json
{
  "success": false,
  "message": "El precio debe ser mayor a 0",
  "data": null
}
```

4. **Categoría duplicada (409):**


```json
{
  "success": false,
  "message": "Ya existe una categoría con esa denominación",
  "data": null
}
```

---

## ✅ VALIDACIONES

### Instrumento:

- `instrumento`: Requerido, no vacío, máximo 255 caracteres
- `marca`: Requerido, no vacío, máximo 100 caracteres
- `modelo`: Requerido, no vacío, máximo 100 caracteres
- `precio`: Requerido, mayor a 0, máximo 2 decimales
- `costoEnvio`: Requerido, valores válidos: "G" (Gratis), "P" (Pago)
- `cantidadVendida`: Mínimo 0
- `descripcion`: Opcional, máximo 1000 caracteres
- `categoria`: Debe existir en la base de datos


### Categoría:

- `denominacion`: Requerido, no vacío, único, máximo 100 caracteres


### Imagen:

- `file`: Requerido, formatos permitidos: JPG, JPEG, PNG, GIF
- `altText`: Opcional, máximo 255 caracteres
- `isPrimary`: Opcional, boolean
- Tamaño máximo: 10MB


---

## 🛠️ CONFIGURACIÓN DE BASE DE DATOS

**Archivo:** `application.properties`

```plaintext
# Configuración de base de datos
spring.datasource.url=jdbc:postgresql://127.0.0.1:5432/InstrumentosDB
spring.datasource.username=postgres
spring.datasource.password=admin123
spring.datasource.driver-class-name=org.postgresql.Driver

# Configuración JPA/Hibernate
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Configuración del servidor
server.port=3001

# Configuración de archivos
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Configuración de logging
logging.level.com.instrumentos=DEBUG
logging.level.org.springframework.web=DEBUG
```

**Estructura de tablas:**

```sql
-- Tabla categorias
CREATE TABLE categorias (
    id SERIAL PRIMARY KEY,
    denominacion VARCHAR(100) NOT NULL UNIQUE
);

-- Tabla instrumentos
CREATE TABLE instrumentos (
    id SERIAL PRIMARY KEY,
    instrumento VARCHAR(255) NOT NULL,
    marca VARCHAR(100) NOT NULL,
    modelo VARCHAR(100) NOT NULL,
    imagen VARCHAR(255),
    precio DECIMAL(10,2) NOT NULL CHECK (precio > 0),
    costo_envio CHAR(1) NOT NULL CHECK (costo_envio IN ('G', 'P')),
    cantidad_vendida INTEGER DEFAULT 0 CHECK (cantidad_vendida >= 0),
    descripcion TEXT,
    id_categoria INTEGER NOT NULL,
    FOREIGN KEY (id_categoria) REFERENCES categorias(id) ON DELETE RESTRICT
);

-- Tabla product_images
CREATE TABLE product_images (
    id SERIAL PRIMARY KEY,
    instrumento_id INTEGER NOT NULL,
    image_url VARCHAR(500) NOT NULL,
    alt_text VARCHAR(255),
    is_primary BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (instrumento_id) REFERENCES instrumentos(id) ON DELETE CASCADE
);
```

---

## 🧪 INSTALACIÓN Y EJECUCIÓN

**Prerrequisitos:**

1. Java 17 o superior
2. PostgreSQL 12 o superior
3. Maven 3.6 o superior


**Pasos de instalación:**

```shellscript
# 1. Clonar el repositorio
git clone [url-del-repositorio]
cd instrumentos-back-spring

# 2. Configurar la base de datos
psql -U postgres -f scripts/01-crear-bd.sql
psql -U postgres -d InstrumentosDB -f scripts/02-crear-tablas.sql
psql -U postgres -d InstrumentosDB -f scripts/03-insertar-datos-json.sql
psql -U postgres -d InstrumentosDB -f scripts/04-datos-adicionales.sql
psql -U postgres -d InstrumentosDB -f scripts/06-crear-tabla-imagenes.sql

# 3. Compilar el proyecto
mvn clean compile

# 4. Ejecutar la aplicación
mvn spring-boot:run

# 5. Verificar funcionamiento
curl http://localhost:3001/api/instrumentos
```

**Verificar instalación:**

```shellscript
# Verificar Java
java -version

# Verificar Maven
mvn -version

# Verificar PostgreSQL
psql --version

# Probar conexión a BD
psql -U postgres -d InstrumentosDB -c "SELECT COUNT(*) FROM instrumentos;"
```

---

## 🔄 INTEGRACIÓN CON FRONTEND REACT

### Configuración de Axios (`api.js`)

```javascript
import axios from 'axios';

const API_BASE_URL = 'http://localhost:3001/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Interceptor para manejo de errores
api.interceptors.response.use(
  (response) => response,
  (error) => {
    console.error('API Error:', error.response?.data || error.message);
    return Promise.reject(error);
  }
);

export default api;
```

### Servicios recomendados:

#### `instrumentoService.js`

```javascript
import api from './api';

export const instrumentoService = {
  // Obtener todos los instrumentos con paginación
  getAll: async (page = 0, size = 10, sort = 'id,asc') => {
    const response = await api.get(`/instrumentos?page=${page}&size=${size}&sort=${sort}`);
    return response.data;
  },

  // Obtener instrumento por ID
  getById: async (id) => {
    const response = await api.get(`/instrumentos/${id}`);
    return response.data;
  },

  // Crear nuevo instrumento
  create: async (instrumento) => {
    const response = await api.post('/instrumentos', instrumento);
    return response.data;
  },

  // Actualizar instrumento
  update: async (id, instrumento) => {
    const response = await api.put(`/instrumentos/${id}`, instrumento);
    return response.data;
  },

  // Eliminar instrumento
  delete: async (id) => {
    const response = await api.delete(`/instrumentos/${id}`);
    return response.data;
  },

  // Filtrar por categoría
  getByCategoria: async (categoriaId) => {
    const response = await api.get(`/instrumentos/categoria/${categoriaId}`);
    return response.data;
  },

  // Buscar por nombre
  search: async (nombre) => {
    const response = await api.get(`/instrumentos/buscar?nombre=${encodeURIComponent(nombre)}`);
    return response.data;
  }
};
```

#### `categoriaService.js`

```javascript
import api from './api';

export const categoriaService = {
  // Obtener todas las categorías
  getAll: async () => {
    const response = await api.get('/categorias');
    return response.data;
  },

  // Obtener categoría por ID
  getById: async (id) => {
    const response = await api.get(`/categorias/${id}`);
    return response.data;
  },

  // Crear nueva categoría
  create: async (categoria) => {
    const response = await api.post('/categorias', categoria);
    return response.data;
  },

  // Actualizar categoría
  update: async (id, categoria) => {
    const response = await api.put(`/categorias/${id}`, categoria);
    return response.data;
  },

  // Eliminar categoría
  delete: async (id) => {
    const response = await api.delete(`/categorias/${id}`);
    return response.data;
  }
};
```

#### `imageService.js`

```javascript
import api from './api';

export const imageService = {
  // Subir imagen
  upload: async (instrumentoId, file, altText = '', isPrimary = false) => {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('altText', altText);
    formData.append('isPrimary', isPrimary);

    const response = await api.post(`/images/upload/${instrumentoId}`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    return response.data;
  },

  // Obtener imágenes de instrumento
  getByInstrumento: async (instrumentoId) => {
    const response = await api.get(`/images/instrumento/${instrumentoId}`);
    return response.data;
  },

  // Obtener imagen principal
  getPrimary: async (instrumentoId) => {
    const response = await api.get(`/images/instrumento/${instrumentoId}/primary`);
    return response.data;
  },

  // Eliminar imagen
  delete: async (imageId) => {
    const response = await api.delete(`/images/${imageId}`);
    return response.data;
  },

  // Establecer como principal
  setPrimary: async (imageId) => {
    const response = await api.put(`/images/${imageId}/primary`);
    return response.data;
  },

  // Obtener URL de imagen
  getImageUrl: (filename) => {
    return `http://localhost:3001/api/images/${filename}`;
  }
};
```

### Hooks personalizados:

#### `useInstrumentos.js`

```javascript
import { useState, useEffect } from 'react';
import { instrumentoService } from '../services/instrumentoService';

export const useInstrumentos = (page = 0, size = 10) => {
  const [instrumentos, setInstrumentos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  const fetchInstrumentos = async () => {
    try {
      setLoading(true);
      const response = await instrumentoService.getAll(page, size);
      setInstrumentos(response.content);
      setTotalPages(response.totalPages);
      setTotalElements(response.totalElements);
      setError(null);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchInstrumentos();
  }, [page, size]);

  return {
    instrumentos,
    loading,
    error,
    totalPages,
    totalElements,
    refetch: fetchInstrumentos
  };
};
```

#### `useCategorias.js`

```javascript
import { useState, useEffect } from 'react';
import { categoriaService } from '../services/categoriaService';

export const useCategorias = () => {
  const [categorias, setCategorias] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchCategorias = async () => {
    try {
      setLoading(true);
      const response = await categoriaService.getAll();
      setCategorias(response.data);
      setError(null);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCategorias();
  }, []);

  return {
    categorias,
    loading,
    error,
    refetch: fetchCategorias
  };
};
```

---

## 🧱 ESTRUCTURA FRONTEND RECOMENDADA

```plaintext
src/
├── components/
│   ├── common/
│   │   ├── Loading.jsx
│   │   ├── ErrorMessage.jsx
│   │   └── Pagination.jsx
│   ├── instrumentos/
│   │   ├── InstrumentosGrid.jsx
│   │   ├── InstrumentoCard.jsx
│   │   ├── InstrumentoForm.jsx
│   │   ├── InstrumentoDetail.jsx
│   │   └── InstrumentoSearch.jsx
│   ├── categorias/
│   │   ├── CategoriaCombo.jsx
│   │   ├── CategoriaFilter.jsx
│   │   └── CategoriaForm.jsx
│   └── images/
│       ├── ImageUpload.jsx
│       ├── ImageGallery.jsx
│       └── ImagePreview.jsx
├── services/
│   ├── api.js
│   ├── instrumentoService.js
│   ├── categoriaService.js
│   └── imageService.js
├── hooks/
│   ├── useInstrumentos.js
│   ├── useCategorias.js
│   └── useImages.js
├── pages/
│   ├── Home.jsx
│   ├── InstrumentosPage.jsx
│   ├── InstrumentoDetailPage.jsx
│   └── AdminPage.jsx
└── utils/
    ├── constants.js
    ├── formatters.js
    └── validators.js
```

---

## 🧪 TESTING AUTOMATIZADO

**Script de testing incluido:** `test-api.sh`

```shellscript
# Ejecutar todos los tests
bash test-api.sh

# Tests incluidos:
✅ CRUD de categorías (5 tests)
✅ CRUD de instrumentos (6 tests)  
✅ Sistema de imágenes (3 tests)
✅ Validación de errores (3 tests)
✅ Total: 17 tests automatizados

# Resultados esperados:
- ✅ 17/17 tests pasando
- ⏱️ Tiempo de ejecución: ~30 segundos
- 📊 Cobertura completa de endpoints
```

**Tests específicos:**

1. **Categorías:**

1. GET /categorias (obtener todas)
2. GET /categorias/id (obtener por ID)
3. POST /categorias (crear nueva)
4. PUT /categorias/id (actualizar)
5. DELETE /categorias/id (eliminar)



2. **Instrumentos:**

1. GET /instrumentos (obtener todos con paginación)
2. GET /instrumentos/id (obtener por ID)
3. POST /instrumentos (crear nuevo)
4. PUT /instrumentos/id (actualizar)
5. GET /instrumentos/categoria/id (filtrar por categoría)
6. GET /instrumentos/buscar (buscar por nombre)



3. **Imágenes:**

1. POST /images/upload/instrumentoId (subir imagen)
2. GET /images/instrumento/id (obtener imágenes)
3. GET /images/instrumento/id/primary (imagen principal)



4. **Validaciones:**

1. Error 404 para recursos inexistentes
2. Validación de datos requeridos
3. Manejo de errores de servidor





---

## ✅ FUNCIONALIDADES IMPLEMENTADAS

### ✅ **CRUD Completo:**

- **Instrumentos:** Crear, leer, actualizar, eliminar
- **Categorías:** Crear, leer, actualizar, eliminar
- **Imágenes:** Subir, obtener, eliminar, establecer principal


### ✅ **Funcionalidades Avanzadas:**

- **Paginación:** Soporte completo con Spring Data
- **Ordenamiento:** Por cualquier campo (id, precio, marca, etc.)
- **Filtrado:** Por categoría con endpoint dedicado
- **Búsqueda:** Por nombre de instrumento (case-insensitive)
- **Validaciones:** Datos requeridos y formatos correctos
- **Manejo de errores:** Respuestas estructuradas y códigos HTTP apropiados


### ✅ **Sistema de Imágenes:**

- **Subida de archivos:** Soporte para JPG, PNG, GIF
- **Almacenamiento:** Archivos guardados con nombres únicos (UUID)
- **Metadatos:** Alt text, imagen principal, fecha de creación
- **Gestión:** Eliminar imágenes, establecer principal
- **Cascada:** Eliminación automática al borrar instrumento


### ✅ **Configuración y Seguridad:**

- **CORS:** Configurado para desarrollo frontend
- **Validaciones:** Anotaciones JPA y validaciones personalizadas
- **Transacciones:** Manejo automático con @Transactional
- **Logging:** Configurado para debugging y monitoreo


### ✅ **Base de Datos:**

- **Relaciones:** Foreign keys con integridad referencial
- **Constraints:** Validaciones a nivel de BD
- **Índices:** Optimización de consultas frecuentes
- **Datos de prueba:** Scripts de inicialización incluidos


---

## 🧪 EJEMPLOS CON `curl`

### **Instrumentos:**

```shellscript
# Obtener todos los instrumentos (paginado)
curl -X GET "http://localhost:3001/api/instrumentos?page=0&size=5&sort=precio,desc" \
  -H "Content-Type: application/json"

# Obtener instrumento específico
curl -X GET "http://localhost:3001/api/instrumentos/1" \
  -H "Content-Type: application/json"

# Crear nuevo instrumento
curl -X POST "http://localhost:3001/api/instrumentos" \
  -H "Content-Type: application/json" \
  -d '{
    "instrumento": "Guitarra Electrica Fender",
    "marca": "Fender",
    "modelo": "Stratocaster",
    "imagen": "fender-strat.jpg",
    "precio": 25000.00,
    "costoEnvio": "G",
    "cantidadVendida": 0,
    "descripcion": "Guitarra electrica profesional con pastillas single coil",
    "categoria": { "id": 1 }
  }'

# Actualizar instrumento
curl -X PUT "http://localhost:3001/api/instrumentos/1" \
  -H "Content-Type: application/json" \
  -d '{
    "instrumento": "Guitarra Electrica Fender Modificada",
    "marca": "Fender",
    "modelo": "Stratocaster Deluxe",
    "precio": 27000.00,
    "categoria": { "id": 1 }
  }'

# Eliminar instrumento
curl -X DELETE "http://localhost:3001/api/instrumentos/1" \
  -H "Content-Type: application/json"

# Filtrar por categoría
curl -X GET "http://localhost:3001/api/instrumentos/categoria/1" \
  -H "Content-Type: application/json"

# Buscar por nombre
curl -X GET "http://localhost:3001/api/instrumentos/buscar?nombre=guitarra" \
  -H "Content-Type: application/json"
```

### **Categorías:**

```shellscript
# Obtener todas las categorías
curl -X GET "http://localhost:3001/api/categorias" \
  -H "Content-Type: application/json"

# Obtener categoría específica
curl -X GET "http://localhost:3001/api/categorias/1" \
  -H "Content-Type: application/json"

# Crear nueva categoría
curl -X POST "http://localhost:3001/api/categorias" \
  -H "Content-Type: application/json" \
  -d '{ "denominacion": "Instrumentos Digitales" }'

# Actualizar categoría
curl -X PUT "http://localhost:3001/api/categorias/1" \
  -H "Content-Type: application/json" \
  -d '{ "denominacion": "Instrumentos de Cuerda" }'

# Eliminar categoría
curl -X DELETE "http://localhost:3001/api/categorias/1" \
  -H "Content-Type: application/json"
```

### **Imágenes:**

```shellscript
# Subir imagen para instrumento
curl -X POST "http://localhost:3001/api/images/upload/1" \
  -F "file=@/path/to/image.jpg" \
  -F "altText=Guitarra Fender Stratocaster" \
  -F "isPrimary=true"

# Obtener imágenes de instrumento
curl -X GET "http://localhost:3001/api/images/instrumento/1" \
  -H "Content-Type: application/json"

# Obtener imagen principal
curl -X GET "http://localhost:3001/api/images/instrumento/1/primary" \
  -H "Content-Type: application/json"

# Obtener archivo de imagen
curl -X GET "http://localhost:3001/api/images/a1b2c3d4-e5f6-7890-abcd-ef1234567890.jpg" \
  --output imagen.jpg

# Eliminar imagen
curl -X DELETE "http://localhost:3001/api/images/1" \
  -H "Content-Type: application/json"

# Establecer imagen como principal
curl -X PUT "http://localhost:3001/api/images/1/primary" \
  -H "Content-Type: application/json"
```

---

## 🔧 CONFIGURACIÓN AVANZADA

### **Variables de entorno (opcional):**

```shellscript
# Configuración de base de datos
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=InstrumentosDB
export DB_USER=postgres
export DB_PASSWORD=admin123

# Configuración del servidor
export SERVER_PORT=3001

# Configuración de archivos
export UPLOAD_DIR=./uploads
export MAX_FILE_SIZE=10MB
```

### **Perfiles de Spring:**

```plaintext
# application-dev.properties (desarrollo)
spring.jpa.show-sql=true
logging.level.com.instrumentos=DEBUG

# application-prod.properties (producción)
spring.jpa.show-sql=false
logging.level.com.instrumentos=INFO
server.error.include-stacktrace=never
```

### **Configuración de seguridad (opcional):**

```java
// Para implementar autenticación JWT
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // Configuración de seguridad
}
```

---

## 📊 MONITOREO Y LOGS

### **Endpoints de monitoreo (Actuator):**

```shellscript
# Salud de la aplicación
curl http://localhost:3001/actuator/health

# Métricas
curl http://localhost:3001/actuator/metrics

# Información de la aplicación
curl http://localhost:3001/actuator/info
```

### **Logs importantes:**

```shellscript
# Ver logs en tiempo real
tail -f logs/spring.log

# Filtrar errores
grep ERROR logs/spring.log

# Filtrar por endpoint específico
grep "/api/instrumentos" logs/spring.log
```

---

## 🚀 DEPLOYMENT

### **Construcción para producción:**

```shellscript
# Compilar y empaquetar
mvn clean package -DskipTests

# Ejecutar JAR
java -jar target/instrumentos-back-spring-1.0.0.jar

# Con perfil de producción
java -jar -Dspring.profiles.active=prod target/instrumentos-back-spring-1.0.0.jar
```

### **Docker (opcional):**

```dockerfile
FROM openjdk:17-jdk-slim
COPY target/instrumentos-back-spring-1.0.0.jar app.jar
EXPOSE 3001
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

---

## 📞 SOPORTE Y TROUBLESHOOTING

### **Problemas comunes:**

1. **Error de conexión a BD:**

1. Verificar que PostgreSQL esté ejecutándose
2. Confirmar credenciales en application.properties
3. Verificar que la BD InstrumentosDB exista



2. **Error CORS:**

1. Verificar configuración en WebConfig.java
2. Confirmar que el frontend esté en puerto permitido



3. **Error de subida de archivos:**

1. Verificar permisos de escritura en directorio uploads/
2. Confirmar tamaño máximo de archivo



4. **Tests fallando:**

1. Verificar que la BD tenga datos de prueba
2. Confirmar que el servidor esté ejecutándose en puerto 3001





### **Comandos de diagnóstico:**

```shellscript
# Verificar puerto en uso
netstat -tulpn | grep 3001

# Verificar conexión a BD
psql -U postgres -d InstrumentosDB -c "SELECT COUNT(*) FROM instrumentos;"

# Verificar logs de error
tail -n 50 logs/spring.log | grep ERROR

# Probar endpoint básico
curl -I http://localhost:3001/api/instrumentos
```

---

## 📚 RECURSOS ADICIONALES

### **Documentación oficial:**

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)


### **Herramientas recomendadas:**

- **Postman:** Para testing manual de API
- **DBeaver:** Para gestión de base de datos
- **IntelliJ IDEA:** IDE recomendado para Spring Boot
- **pgAdmin:** Interfaz web para PostgreSQL


---

**Desarrollado para:**Laboratorio de Computación 4 – UTN FRM TUP**Estudiante:** Agustín Sandoval**Fecha:** Enero 2024**Versión:** 1.0.0

---

## 📋 CHANGELOG

### **v1.0.0 (Enero 2024)**

- ✅ Implementación inicial del sistema
- ✅ CRUD completo de instrumentos y categorías
- ✅ Sistema de imágenes con upload
- ✅ Paginación y filtrado
- ✅ Validaciones y manejo de errores
- ✅ Testing automatizado
- ✅ Documentación completa


### **Próximas versiones:**

- 🔄 Autenticación y autorización
- 🔄 Cache con Redis
- 🔄 Búsqueda avanzada con Elasticsearch
- 🔄 Notificaciones en tiempo real
- 🔄 API versioning
- 🔄 Métricas y monitoreo avanzado


To configure the generation, complete these steps