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

### 🔹 Crear nuevo instrumento
`POST /api/instrumentos`  
**Header:** `Content-Type: application/json`

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

### 🔹 Actualizar instrumento
`PUT /api/instrumentos/{id}`  
**Header:** `Content-Type: application/json`

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

### 🔹 Filtrar por categoría
`GET /api/instrumentos/categoria/{categoriaId}`

### 🔹 Buscar instrumentos
`GET /api/instrumentos/buscar?nombre=guitarra`

---

## 🗂️ ENDPOINTS DE CATEGORÍAS

### 🔹 Obtener todas las categorías
`GET /api/categorias`

**Respuesta ejemplo:**
```json
[
  { "id": 1, "denominacion": "Cuerda" },
  { "id": 2, "denominacion": "Viento" },
  { "id": 3, "denominacion": "Percusion" },
  { "id": 4, "denominacion": "Teclado" },
  { "id": 5, "denominacion": "Electronico" }
]
```

### 🔹 Obtener categoría por ID
`GET /api/categorias/{id}`

### 🔹 Crear nueva categoría
`POST /api/categorias`  
**Header:** `Content-Type: application/json`

**Body ejemplo:**
```json
{ "denominacion": "Nueva Categoria" }
```

### 🔹 Actualizar categoría
`PUT /api/categorias/{id}`  
**Header:** `Content-Type: application/json`

**Body ejemplo:**
```json
{ "denominacion": "Categoria Actualizada" }
```

### 🔹 Eliminar categoría
`DELETE /api/categorias/{id}`

---

## 🖼️ ENDPOINTS DE IMÁGENES

### 🔹 Obtener imagen
`GET /api/images/{filename}`

---

## 🌐 CONFIGURACIÓN CORS

El backend está configurado para aceptar peticiones desde:

- http://localhost:3000 (React dev server)
- http://localhost:5173 (Vite dev server)
- http://127.0.0.1:3000
- http://127.0.0.1:5173

---

## 🚨 MANEJO DE ERRORES

**Códigos de Estado HTTP:**
- `200 OK`: Operación exitosa
- `201 Created`: Recurso creado exitosamente
- `400 Bad Request`: Error en los datos enviados
- `404 Not Found`: Recurso no encontrado
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
  "data": { }
}
```

---

## ✅ VALIDACIONES

### Instrumento:
- `instrumento`: Requerido, no vacío
- `marca`: Requerido, no vacío, máximo 100 caracteres
- `modelo`: Requerido, no vacío, máximo 100 caracteres
- `precio`: Requerido, mayor a 0
- `cantidadVendida`: Mínimo 0
- `categoria`: Debe existir en la base de datos

### Categoría:
- `denominacion`: Requerido, no vacío, único, máximo 100 caracteres

---

## 🛠️ CONFIGURACIÓN DE BASE DE DATOS

**Archivo:** `application.properties`
```
spring.datasource.url=jdbc:postgresql://127.0.0.1:5432/InstrumentosDB
spring.datasource.username=postgres
spring.datasource.password=admin123
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
```

---

## 🧪 INSTALACIÓN Y EJECUCIÓN

**Prerrequisitos:**
1. Java 17 o superior
2. PostgreSQL 12 o superior
3. Maven 3.6 o superior

**Pasos de instalación:**
```bash
# 1. Clonar el repositorio
git clone [url-del-repositorio]
cd instrumentos-back-spring

# 2. Configurar la base de datos
psql -U postgres -f scripts/01-crear-bd.sql
psql -U postgres -d InstrumentosDB -f scripts/02-crear-tablas.sql
psql -U postgres -d InstrumentosDB -f scripts/03-insertar-datos-json.sql

# 3. Ejecutar la aplicación
mvn spring-boot:run

# 4. Verificar funcionamiento
curl http://localhost:3001/api/instrumentos
```

---

## 🔄 INTEGRACIÓN CON FRONTEND REACT

### Configuración de Axios (`api.js`)
```js
import axios from 'axios';

const API_BASE_URL = 'http://localhost:3001/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export default api;
```

### Ejemplos de uso:

#### Obtener instrumentos
```js
const getInstrumentos = async (page = 0, size = 10) => {
  const response = await api.get(`/instrumentos?page=${page}&size=${size}`);
  return response.data;
};
```

#### Crear instrumento
```js
const crearInstrumento = async (instrumento) => {
  const response = await api.post('/instrumentos', instrumento);
  return response.data;
};
```

#### Obtener categorías
```js
const getCategorias = async () => {
  const response = await api.get('/categorias');
  return response.data;
};
```

#### Filtrar por categoría
```js
const getInstrumentosPorCategoria = async (categoriaId) => {
  const response = await api.get(`/instrumentos/categoria/${categoriaId}`);
  return response.data;
};
```

---

## 🧱 ESTRUCTURA FRONTEND RECOMENDADA

```
src/
├── components/
│   ├── InstrumentosGrid.jsx
│   ├── InstrumentoForm.jsx
│   ├── CategoriaCombo.jsx
│   └── FiltroCategoria.jsx
├── services/
│   ├── instrumentoService.js
│   └── categoriaService.js
├── hooks/
│   ├── useInstrumentos.js
│   └── useCategorias.js
└── utils/
    └── api.js
```

---

## ✅ FUNCIONALIDADES IMPLEMENTADAS

- CRUD completo de instrumentos y categorías
- Validaciones de datos
- Manejo de errores
- Filtrado por categoría
- Paginación y ordenamiento
- Búsqueda por nombre
- Manejo de imágenes
- API REST con CORS y respuestas JSON estructuradas

---

## 🧪 EJEMPLOS CON `curl`

```bash
# Obtener todos los instrumentos
curl -X GET "http://localhost:3001/api/instrumentos" -H "Content-Type: application/json"

# Crear instrumento
curl -X POST "http://localhost:3001/api/instrumentos" -H "Content-Type: application/json" -d '{
  "instrumento": "Guitarra Electrica",
  "marca": "Fender",
  "modelo": "Stratocaster",
  "imagen": "fender.jpg",
  "precio": 25000.00,
  "costoEnvio": "G",
  "cantidadVendida": 0,
  "descripcion": "Guitarra electrica profesional",
  "categoria": { "id": 1 }
}'

# Obtener categorías
curl -X GET "http://localhost:3001/api/categorias" -H "Content-Type: application/json"

# Filtrar por categoría
curl -X GET "http://localhost:3001/api/instrumentos/categoria/1" -H "Content-Type: application/json"
```

---

## 📞 SOPORTE Y CONTACTO

Ante errores o dudas:
1. Revisar logs en consola
2. Verificar estado de la base de datos
3. Confirmar configuración CORS
4. Validar datos enviados

---

**Desarrollado para:**  
Laboratorio de Computación 4 – UTN FRM TUP  
**Estudiante:**
Agustín Sandoval
 
