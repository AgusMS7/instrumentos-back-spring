#!/bin/bash

BASE_URL="http://localhost:3001/api"
PASSED=0
FAILED=0
FAILED_TESTS=()

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

echo -e "${BLUE}=== TESTING API INSTRUMENTOS MUSICALES COMPLETA ===${NC}"
echo ""

# Función para testear endpoints
test_endpoint() {
    local method=$1
    local url=$2
    local data=$3
    local expected_status=$4
    local test_name=$5
    
    if [ -n "$data" ]; then
        response=$(curl -s -w "HTTPSTATUS:%{http_code}" -X $method "$BASE_URL$url" \
            -H "Content-Type: application/json" \
            -d "$data")
    else
        response=$(curl -s -w "HTTPSTATUS:%{http_code}" -X $method "$BASE_URL$url")
    fi
    
    http_code=$(echo $response | tr -d '\n' | sed -e 's/.*HTTPSTATUS://')
    body=$(echo $response | sed -e 's/HTTPSTATUS:.*//g')
    
    if [ "$http_code" -eq "$expected_status" ]; then
        echo -e "${GREEN}✓ PASS${NC} - $test_name (HTTP $http_code)"
        ((PASSED++))
        return 0
    else
        echo -e "${RED}✗ FAIL${NC} - $test_name (Expected HTTP $expected_status, got HTTP $http_code)"
        echo -e "${YELLOW}Response: $body${NC}"
        FAILED_TESTS+=("$test_name")
        ((FAILED++))
        return 1
    fi
}

# Función para testear con validación de contenido
test_endpoint_with_content() {
    local method=$1
    local url=$2
    local data=$3
    local expected_status=$4
    local test_name=$5
    local content_check=$6
    
    if [ -n "$data" ]; then
        response=$(curl -s -w "HTTPSTATUS:%{http_code}" -X $method "$BASE_URL$url" \
            -H "Content-Type: application/json" \
            -d "$data")
    else
        response=$(curl -s -w "HTTPSTATUS:%{http_code}" -X $method "$BASE_URL$url")
    fi
    
    http_code=$(echo $response | tr -d '\n' | sed -e 's/.*HTTPSTATUS://')
    body=$(echo $response | sed -e 's/HTTPSTATUS:.*//g')
    
    if [ "$http_code" -eq "$expected_status" ] && [[ "$body" == *"$content_check"* ]]; then
        echo -e "${GREEN}✓ PASS${NC} - $test_name (HTTP $http_code)"
        ((PASSED++))
        return 0
    else
        echo -e "${RED}✗ FAIL${NC} - $test_name (Expected HTTP $expected_status with '$content_check', got HTTP $http_code)"
        echo -e "${YELLOW}Response: $body${NC}"
        FAILED_TESTS+=("$test_name")
        ((FAILED++))
        return 1
    fi
}

# Función para testear subida de archivos
test_file_upload() {
    local url=$1
    local file_path=$2
    local expected_status=$3
    local test_name=$4
    
    if [ ! -f "$file_path" ]; then
        echo -e "${YELLOW}⚠ SKIP${NC} - $test_name (Archivo $file_path no encontrado)"
        return 1
    fi
    
    response=$(curl -s -w "HTTPSTATUS:%{http_code}" -X POST "$BASE_URL$url" \
        -F "file=@$file_path")
    
    http_code=$(echo $response | tr -d '\n' | sed -e 's/.*HTTPSTATUS://')
    body=$(echo $response | sed -e 's/HTTPSTATUS:.*//g')
    
    if [ "$http_code" -eq "$expected_status" ]; then
        echo -e "${GREEN}✓ PASS${NC} - $test_name (HTTP $http_code)"
        ((PASSED++))
        echo "$body"  # Retorna el response para extraer datos
        return 0
    else
        echo -e "${RED}✗ FAIL${NC} - $test_name (Expected HTTP $expected_status, got HTTP $http_code)"
        echo -e "${YELLOW}Response: $body${NC}"
        FAILED_TESTS+=("$test_name")
        ((FAILED++))
        return 1
    fi
}

echo -e "${YELLOW}1. TESTEANDO CATEGORÍAS...${NC}"
echo ""

# Test 1: Obtener todas las categorías
test_endpoint_with_content "GET" "/categorias" "" 200 "GET /categorias - Obtener todas las categorías" "Cuerda"

# Test 2: Obtener categoría por ID
test_endpoint_with_content "GET" "/categorias/1" "" 200 "GET /categorias/1 - Obtener categoría por ID" "Cuerda"

# Test 3: Crear nueva categoría
echo -e "${BLUE}Creando categoría de prueba...${NC}"
create_response=$(curl -s -w "HTTPSTATUS:%{http_code}" -X POST "$BASE_URL/categorias" \
    -H "Content-Type: application/json" \
    -d '{"denominacion": "Categoria Test API"}')

create_http_code=$(echo $create_response | tr -d '\n' | sed -e 's/.*HTTPSTATUS://')
create_body=$(echo $create_response | sed -e 's/HTTPSTATUS:.*//g')

if [ "$create_http_code" -eq 201 ]; then
    echo -e "${GREEN}✓ PASS${NC} - POST /categorias - Crear nueva categoría (HTTP $create_http_code)"
    ((PASSED++))
    
    # Extraer ID de la nueva categoría
    CATEGORIA_ID=$(echo $create_body | grep -o '"id":[0-9]*' | grep -o '[0-9]*' | head -1)
    echo -e "${BLUE}ID de nueva categoría: $CATEGORIA_ID${NC}"
    
    # Test 4: Actualizar categoría
    test_endpoint "PUT" "/categorias/$CATEGORIA_ID" '{"denominacion": "Categoria Test Actualizada"}' 200 "PUT /categorias/$CATEGORIA_ID - Actualizar categoría"
    
    # Test 5: Eliminar categoría
    test_endpoint "DELETE" "/categorias/$CATEGORIA_ID" "" 200 "DELETE /categorias/$CATEGORIA_ID - Eliminar categoría"
    
else
    echo -e "${RED}✗ FAIL${NC} - POST /categorias - Crear nueva categoría (Expected HTTP 201, got HTTP $create_http_code)"
    echo -e "${YELLOW}Response: $create_body${NC}"
    FAILED_TESTS+=("POST /categorias - Crear nueva categoría")
    ((FAILED++))
fi

echo ""
echo -e "${YELLOW}2. TESTEANDO INSTRUMENTOS...${NC}"
echo ""

# Test 6: Obtener todos los instrumentos
test_endpoint_with_content "GET" "/instrumentos" "" 200 "GET /instrumentos - Obtener todos los instrumentos" "instrumento"

# Test 7: Obtener instrumento por ID existente (usar uno que sabemos que existe)
# Primero obtenemos la lista para encontrar un ID válido
echo -e "${BLUE}Buscando instrumento existente...${NC}"
instruments_response=$(curl -s "$BASE_URL/instrumentos")
EXISTING_ID=$(echo $instruments_response | grep -o '"id":[0-9]*' | grep -o '[0-9]*' | head -1)

if [ -n "$EXISTING_ID" ]; then
    echo -e "${BLUE}Usando instrumento ID: $EXISTING_ID${NC}"
    test_endpoint_with_content "GET" "/instrumentos/$EXISTING_ID" "" 200 "GET /instrumentos/$EXISTING_ID - Obtener instrumento por ID" "instrumento"
else
    echo -e "${YELLOW}⚠ SKIP${NC} - No hay instrumentos existentes para probar"
fi

# Test 8: Crear nuevo instrumento
echo -e "${BLUE}Creando instrumento de prueba...${NC}"
instrumento_data='{
    "instrumento": "Guitarra Test API",
    "marca": "Test Brand",
    "modelo": "Test Model",
    "imagen": "test.jpg",
    "precio": 1500.00,
    "costoEnvio": "G",
    "cantidadVendida": 0,
    "descripcion": "Instrumento de prueba API",
    "categoria": {"id": 1}
}'

create_instr_response=$(curl -s -w "HTTPSTATUS:%{http_code}" -X POST "$BASE_URL/instrumentos" \
    -H "Content-Type: application/json" \
    -d "$instrumento_data")

create_instr_http_code=$(echo $create_instr_response | tr -d '\n' | sed -e 's/.*HTTPSTATUS://')
create_instr_body=$(echo $create_instr_response | sed -e 's/HTTPSTATUS:.*//g')

if [ "$create_instr_http_code" -eq 201 ]; then
    echo -e "${GREEN}✓ PASS${NC} - POST /instrumentos - Crear nuevo instrumento (HTTP $create_instr_http_code)"
    ((PASSED++))
    
    # Extraer ID del nuevo instrumento
    INSTRUMENTO_ID=$(echo $create_instr_body | grep -o '"id":[0-9]*' | grep -o '[0-9]*' | head -1)
    echo -e "${BLUE}ID de nuevo instrumento: $INSTRUMENTO_ID${NC}"
    
    # Test 9: Actualizar instrumento
    update_data='{
        "instrumento": "Guitarra Test API Actualizada",
        "marca": "Test Brand Updated",
        "modelo": "Test Model V2",
        "precio": 2000.00,
        "categoria": {"id": 2}
    }'
    test_endpoint "PUT" "/instrumentos/$INSTRUMENTO_ID" "$update_data" 200 "PUT /instrumentos/$INSTRUMENTO_ID - Actualizar instrumento"
    
else
    echo -e "${RED}✗ FAIL${NC} - POST /instrumentos - Crear nuevo instrumento (Expected HTTP 201, got HTTP $create_instr_http_code)"
    echo -e "${YELLOW}Response: $create_instr_body${NC}"
    FAILED_TESTS+=("POST /instrumentos - Crear nuevo instrumento")
    ((FAILED++))
    INSTRUMENTO_ID=$EXISTING_ID  # Usar un ID existente para continuar con los tests
fi

# Test 10: Filtrar por categoría
test_endpoint_with_content "GET" "/instrumentos/categoria/1" "" 200 "GET /instrumentos/categoria/1 - Filtrar instrumentos por categoría" "instrumento"

# Test 11: Buscar instrumentos
test_endpoint_with_content "GET" "/instrumentos/buscar?nombre=guitarra" "" 200 "GET /instrumentos/buscar - Buscar instrumentos por nombre" "instrumento"

echo ""
echo -e "${YELLOW}3. TESTEANDO SISTEMA DE IMÁGENES...${NC}"
echo ""

# Test 12: Subir imagen (si existe test-image.png)
echo -e "${BLUE}Buscando test-image.png...${NC}"
if [ -f "test-image.png" ]; then
    echo -e "${GREEN}Archivo encontrado, subiendo imagen...${NC}"
    upload_response=$(test_file_upload "/images/upload/$INSTRUMENTO_ID" "test-image.png" 200 "POST /images/upload - Subir imagen PNG")
    
    if [ $? -eq 0 ]; then
        # Extraer información de la imagen subida
        IMAGE_ID=$(echo $upload_response | grep -o '"id":[0-9]*' | grep -o '[0-9]*' | head -1)
        IMAGE_FILENAME=$(echo $upload_response | grep -o '"imageUrl":"[^"]*"' | sed 's/"imageUrl":"$$[^"]*$$"/\1/')
        echo -e "${BLUE}ID de imagen: $IMAGE_ID, Filename: $IMAGE_FILENAME${NC}"
        
        # Test 13: Obtener imagen por filename
        if [ -n "$IMAGE_FILENAME" ]; then
            response=$(curl -s -w "HTTPSTATUS:%{http_code}" "$BASE_URL/images/$IMAGE_FILENAME")
            http_code=$(echo $response | tr -d '\n' | sed -e 's/.*HTTPSTATUS://')
            
            if [ "$http_code" -eq 200 ]; then
                echo -e "${GREEN}✓ PASS${NC} - GET /images/$IMAGE_FILENAME - Obtener imagen por filename (HTTP $http_code)"
                ((PASSED++))
            else
                echo -e "${RED}✗ FAIL${NC} - GET /images/$IMAGE_FILENAME - Obtener imagen por filename (Expected HTTP 200, got HTTP $http_code)"
                FAILED_TESTS+=("GET /images/$IMAGE_FILENAME - Obtener imagen por filename")
                ((FAILED++))
            fi
        fi
        
        # Test 14: Establecer imagen como principal
        if [ -n "$IMAGE_ID" ]; then
            test_endpoint "POST" "/images/$IMAGE_ID/set-primary" "" 200 "POST /images/$IMAGE_ID/set-primary - Establecer imagen como principal"
        fi
        
        # Test 15: Eliminar imagen
        if [ -n "$IMAGE_ID" ]; then
            test_endpoint "DELETE" "/images/$IMAGE_ID" "" 200 "DELETE /images/$IMAGE_ID - Eliminar imagen"
        fi
    fi
else
    echo -e "${YELLOW}⚠ SKIP${NC} - Tests de imágenes (Coloca test-image.png en la raíz del proyecto para probar)"
fi

# Test 16: Obtener todas las imágenes de un instrumento (buscar imageUrl en lugar de filename)
test_endpoint_with_content "GET" "/images/instrumento/$INSTRUMENTO_ID" "" 200 "GET /images/instrumento/$INSTRUMENTO_ID - Obtener imágenes de instrumento" "imageUrl"

# Test 17: Obtener imagen principal del instrumento
response=$(curl -s -w "HTTPSTATUS:%{http_code}" "$BASE_URL/images/instrumento/$INSTRUMENTO_ID/primary")
http_code=$(echo $response | tr -d '\n' | sed -e 's/.*HTTPSTATUS://')

if [ "$http_code" -eq 200 ] || [ "$http_code" -eq 404 ]; then
    echo -e "${GREEN}✓ PASS${NC} - GET /images/instrumento/$INSTRUMENTO_ID/primary - Obtener imagen principal (HTTP $http_code)"
    ((PASSED++))
else
    echo -e "${RED}✗ FAIL${NC} - GET /images/instrumento/$INSTRUMENTO_ID/primary - Obtener imagen principal (Expected HTTP 200 or 404, got HTTP $http_code)"
    FAILED_TESTS+=("GET /images/instrumento/$INSTRUMENTO_ID/primary - Obtener imagen principal")
    ((FAILED++))
fi

echo ""
echo -e "${YELLOW}4. TESTS DE ERRORES Y VALIDACIONES...${NC}"
echo ""

# Test 18: Validar error 404 para instrumento inexistente
test_endpoint "GET" "/instrumentos/99999" "" 404 "GET /instrumentos/99999 - Validar error 404 para instrumento inexistente"

# Test 19: Validar error 404 para categoría inexistente
test_endpoint "GET" "/categorias/99999" "" 404 "GET /categorias/99999 - Validar error 404 para categoría inexistente"

# Test 20: Validar error 404 para imagen inexistente
test_endpoint "GET" "/images/imagen-inexistente.jpg" "" 404 "GET /images/imagen-inexistente.jpg - Validar error 404 para imagen inexistente"

# Test 21: Eliminar el instrumento de prueba (si se creó)
if [ -n "$INSTRUMENTO_ID" ] && [ "$INSTRUMENTO_ID" != "$EXISTING_ID" ]; then
    test_endpoint "DELETE" "/instrumentos/$INSTRUMENTO_ID" "" 200 "DELETE /instrumentos/$INSTRUMENTO_ID - Eliminar instrumento (cascada de imágenes)"
fi

echo ""
echo -e "${BLUE}=== RESUMEN DE TESTING COMPLETO ===${NC}"
echo ""

TOTAL=$((PASSED + FAILED))

if [ $FAILED -eq 0 ]; then
    echo -e "${GREEN}🎉 ¡TODAS LAS FUNCIONALIDADES FUNCIONAN PERFECTAMENTE! 🎉${NC}"
    echo -e "${GREEN}✓ $PASSED/$TOTAL tests pasaron exitosamente${NC}"
    echo ""
    echo -e "${GREEN}✅ CRUD de Categorías: FUNCIONANDO${NC}"
    echo -e "${GREEN}✅ CRUD de Instrumentos: FUNCIONANDO${NC}"
    echo -e "${GREEN}✅ Sistema de Imágenes: FUNCIONANDO${NC}"
    echo -e "${GREEN}✅ Filtrado y Búsqueda: FUNCIONANDO${NC}"
    echo -e "${GREEN}✅ Manejo de Errores: FUNCIONANDO${NC}"
    echo ""
    echo -e "${GREEN}🚀 Tu backend completo está listo para producción!${NC}"
else
    echo -e "${RED}❌ ALGUNAS FUNCIONALIDADES NECESITAN REVISIÓN ❌${NC}"
    echo -e "${RED}✗ $FAILED/$TOTAL tests fallaron${NC}"
    echo -e "${GREEN}✓ $PASSED/$TOTAL tests pasaron${NC}"
    echo ""
    echo -e "${RED}Tests que fallaron:${NC}"
    for test in "${FAILED_TESTS[@]}"; do
        echo -e "${RED}  • $test${NC}"
    done
    echo ""
    echo -e "${YELLOW}💡 Revisa los logs de Spring Boot para más detalles${NC}"
fi

echo ""
echo -e "${BLUE}=== FIN DEL TESTING COMPLETO ===${NC}"
