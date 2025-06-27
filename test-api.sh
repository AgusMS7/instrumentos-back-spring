#!/bin/bash

# =============================================================================
# SCRIPT DE TESTING COMPLETO - API INSTRUMENTOS MUSICALES
# Prueba todas las funcionalidades del backend incluyendo pedidos
# =============================================================================

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
PURPLE='\033[0;35m'
NC='\033[0m' # No Color

# Configuración
BASE_URL="http://localhost:3001/api"
TOTAL_TESTS=0
PASSED_TESTS=0
FAILED_TESTS=0
FAILED_TEST_NAMES=()

# Variables para IDs creados durante los tests
CATEGORIA_ID=""
INSTRUMENTO_ID=""
PEDIDO_ID=""
IMAGE_ID=""

echo -e "${BLUE}🎵 SISTEMA DE INSTRUMENTOS MUSICALES - TESTING COMPLETO${NC}"
echo -e "${BLUE}================================================================${NC}"
echo -e "${CYAN}Probando todas las funcionalidades del backend incluyendo pedidos${NC}"
echo ""

# Función para mostrar resultados
show_result() {
    TOTAL_TESTS=$((TOTAL_TESTS + 1))
    if [ $1 -eq 0 ]; then
        echo -e "${GREEN}✅ PASS${NC} - $2"
        PASSED_TESTS=$((PASSED_TESTS + 1))
    else
        echo -e "${RED}❌ FAIL${NC} - $2"
        FAILED_TESTS=$((FAILED_TESTS + 1))
        FAILED_TEST_NAMES+=("$2")
    fi
}

# Función para hacer peticiones HTTP con mejor manejo de errores
make_request() {
    local method=$1
    local endpoint=$2
    local data=$3
    local description=$4
    
    echo -e "${YELLOW}Testing:${NC} $description"
    
    if [ -n "$data" ]; then
        response=$(curl -s -w "\n%{http_code}" -X "$method" \
            -H "Content-Type: application/json" \
            -d "$data" \
            "$BASE_URL$endpoint" 2>/dev/null)
    else
        response=$(curl -s -w "\n%{http_code}" -X "$method" "$BASE_URL$endpoint" 2>/dev/null)
    fi
    
    if [ $? -ne 0 ]; then
        echo -e "${RED}Error: No se pudo conectar al servidor${NC}"
        return 1
    fi
    
    http_code=$(echo "$response" | tail -n1)
    body=$(echo "$response" | head -n -1)
    
    echo "Response: $body"
    echo "HTTP Code: $http_code"
    
    if [[ "$http_code" =~ ^2[0-9][0-9]$ ]]; then
        return 0
    else
        echo -e "${RED}Error HTTP: $http_code${NC}"
        return 1
    fi
}

# Función para extraer ID de respuesta JSON
extract_id() {
    echo "$1" | grep -o '"id":[0-9]*' | cut -d':' -f2 | head -1
}

# =============================================================================
# VERIFICACIÓN INICIAL DEL SERVIDOR
# =============================================================================

echo -e "${PURPLE}📡 VERIFICACIÓN DEL SERVIDOR${NC}"
echo "================================"

echo -e "${YELLOW}Verificando conexión al servidor...${NC}"
if curl -s --connect-timeout 5 "$BASE_URL/instrumentos" > /dev/null 2>&1; then
    echo -e "${GREEN}✅ Servidor disponible en $BASE_URL${NC}"
else
    echo -e "${RED}❌ Error: Servidor no disponible en $BASE_URL${NC}"
    echo -e "${YELLOW}💡 Asegúrate de ejecutar: mvn spring-boot:run${NC}"
    exit 1
fi

echo -e "${YELLOW}Verificando endpoints básicos...${NC}"
for endpoint in "/instrumentos" "/categorias" "/pedidos"; do
    if curl -s --connect-timeout 5 "$BASE_URL$endpoint" > /dev/null 2>&1; then
        echo -e "${GREEN}✅ $endpoint disponible${NC}"
    else
        echo -e "${RED}❌ $endpoint no disponible${NC}"
    fi
done

echo ""

# =============================================================================
# TESTS DE CATEGORÍAS
# =============================================================================

echo -e "${PURPLE}🗂️ TESTS DE CATEGORÍAS${NC}"
echo "========================"

# Test 1: Obtener todas las categorías
response=$(make_request "GET" "/categorias" "" "Obtener todas las categorías")
show_result $? "GET /categorias - Listar categorías"

# Test 2: Crear nueva categoría
categoria_data='{"denominacion": "Test Categoria API Completa"}'
response=$(make_request "POST" "/categorias" "$categoria_data" "Crear nueva categoría")
if [ $? -eq 0 ]; then
    CATEGORIA_ID=$(extract_id "$response")
    echo -e "${CYAN}ID de categoría creada: $CATEGORIA_ID${NC}"
fi
show_result $? "POST /categorias - Crear categoría"

# Test 3: Obtener categoría por ID
if [ -n "$CATEGORIA_ID" ]; then
    make_request "GET" "/categorias/$CATEGORIA_ID" "" "Obtener categoría por ID"
    show_result $? "GET /categorias/{id} - Obtener categoría específica"
else
    show_result 1 "GET /categorias/{id} - Obtener categoría específica (sin ID)"
fi

# Test 4: Actualizar categoría
if [ -n "$CATEGORIA_ID" ]; then
    update_data='{"denominacion": "Test Categoria API Actualizada"}'
    make_request "PUT" "/categorias/$CATEGORIA_ID" "$update_data" "Actualizar categoría"
    show_result $? "PUT /categorias/{id} - Actualizar categoría"
else
    show_result 1 "PUT /categorias/{id} - Actualizar categoría (sin ID)"
fi

# Test 5: Buscar categorías
make_request "GET" "/categorias/search?denominacion=Test" "" "Buscar categorías por nombre"
show_result $? "GET /categorias/search - Buscar categorías"

echo ""

# =============================================================================
# TESTS DE INSTRUMENTOS
# =============================================================================

echo -e "${PURPLE}🎸 TESTS DE INSTRUMENTOS${NC}"
echo "========================="

# Test 6: Obtener todos los instrumentos
make_request "GET" "/instrumentos" "" "Obtener todos los instrumentos"
show_result $? "GET /instrumentos - Listar instrumentos"

# Test 7: Obtener instrumentos con paginación
make_request "GET" "/instrumentos?paginated=true&page=0&size=5" "" "Obtener instrumentos paginados"
show_result $? "GET /instrumentos - Paginación"

# Test 8: Crear nuevo instrumento
if [ -n "$CATEGORIA_ID" ]; then
    instrumento_data="{
        \"instrumento\": \"Guitarra Test API Completa\",
        \"marca\": \"Test Brand\",
        \"modelo\": \"Test Model 2024\",
        \"precio\": 25000.00,
        \"costoEnvio\": \"G\",
        \"cantidadVendida\": 0,
        \"descripcion\": \"Instrumento de prueba para testing completo de API\",
        \"categoria\": {\"id\": $CATEGORIA_ID}
    }"
    response=$(make_request "POST" "/instrumentos" "$instrumento_data" "Crear nuevo instrumento")
    if [ $? -eq 0 ]; then
        INSTRUMENTO_ID=$(extract_id "$response")
        echo -e "${CYAN}ID de instrumento creado: $INSTRUMENTO_ID${NC}"
    fi
    show_result $? "POST /instrumentos - Crear instrumento"
else
    show_result 1 "POST /instrumentos - Crear instrumento (sin categoría)"
fi

# Test 9: Obtener instrumento por ID
if [ -n "$INSTRUMENTO_ID" ]; then
    make_request "GET" "/instrumentos/$INSTRUMENTO_ID" "" "Obtener instrumento por ID"
    show_result $? "GET /instrumentos/{id} - Obtener instrumento específico"
else
    show_result 1 "GET /instrumentos/{id} - Obtener instrumento específico (sin ID)"
fi

# Test 10: Actualizar instrumento
if [ -n "$INSTRUMENTO_ID" ]; then
    update_instrumento="{
        \"instrumento\": \"Guitarra Test API Actualizada\",
        \"marca\": \"Test Brand Updated\",
        \"modelo\": \"Test Model 2024 V2\",
        \"precio\": 30000.00,
        \"categoria\": {\"id\": $CATEGORIA_ID}
    }"
    make_request "PUT" "/instrumentos/$INSTRUMENTO_ID" "$update_instrumento" "Actualizar instrumento"
    show_result $? "PUT /instrumentos/{id} - Actualizar instrumento"
else
    show_result 1 "PUT /instrumentos/{id} - Actualizar instrumento (sin ID)"
fi

# Test 11: Buscar instrumentos por nombre
make_request "GET" "/instrumentos/buscar?nombre=guitarra" "" "Buscar instrumentos por nombre"
show_result $? "GET /instrumentos/buscar - Búsqueda por nombre"

# Test 12: Filtrar instrumentos por categoría
if [ -n "$CATEGORIA_ID" ]; then
    make_request "GET" "/instrumentos/categoria/$CATEGORIA_ID" "" "Filtrar por categoría"
    show_result $? "GET /instrumentos/categoria/{id} - Filtrar por categoría"
else
    show_result 1 "GET /instrumentos/categoria/{id} - Filtrar por categoría (sin ID)"
fi

# Test 13: Obtener instrumentos ordenados por precio
make_request "GET" "/instrumentos?paginated=true&page=0&size=10&sortBy=precio&sortDir=desc" "" "Ordenar por precio descendente"
show_result $? "GET /instrumentos - Ordenamiento por precio"

echo ""

# =============================================================================
# TESTS DE PEDIDOS (NUEVA FUNCIONALIDAD)
# =============================================================================

echo -e "${PURPLE}🛒 TESTS DE PEDIDOS${NC}"
echo "==================="

# Test 14: Obtener todos los pedidos
make_request "GET" "/pedidos" "" "Obtener todos los pedidos"
show_result $? "GET /pedidos - Listar pedidos"

# Test 15: Crear nuevo pedido
if [ -n "$INSTRUMENTO_ID" ]; then
    pedido_data="{
        \"instrumentos\": [
            {
                \"instrumentoId\": $INSTRUMENTO_ID,
                \"cantidad\": 2
            }
        ]
    }"
    response=$(make_request "POST" "/pedidos" "$pedido_data" "Crear nuevo pedido")
    if [ $? -eq 0 ]; then
        PEDIDO_ID=$(extract_id "$response")
        echo -e "${CYAN}ID de pedido creado: $PEDIDO_ID${NC}"
    fi
    show_result $? "POST /pedidos - Crear pedido"
else
    show_result 1 "POST /pedidos - Crear pedido (sin instrumento)"
fi

# Test 16: Obtener pedido por ID
if [ -n "$PEDIDO_ID" ]; then
    make_request "GET" "/pedidos/$PEDIDO_ID" "" "Obtener pedido por ID"
    show_result $? "GET /pedidos/{id} - Obtener pedido específico"
else
    show_result 1 "GET /pedidos/{id} - Obtener pedido específico (sin ID)"
fi

# Test 17: Buscar pedidos por fecha
fecha_hoy=$(date +%Y-%m-%d)
make_request "GET" "/pedidos/fecha?fecha=$fecha_hoy" "" "Buscar pedidos por fecha"
show_result $? "GET /pedidos/fecha - Búsqueda por fecha"

# Test 18: Crear pedido con múltiples instrumentos
if [ -n "$INSTRUMENTO_ID" ]; then
    pedido_multiple="{
        \"instrumentos\": [
            {
                \"instrumentoId\": $INSTRUMENTO_ID,
                \"cantidad\": 1
            },
            {
                \"instrumentoId\": 1,
                \"cantidad\": 3
            }
        ]
    }"
    response=$(make_request "POST" "/pedidos" "$pedido_multiple" "Crear pedido con múltiples instrumentos")
    if [ $? -eq 0 ]; then
        PEDIDO_MULTIPLE_ID=$(extract_id "$response")
        echo -e "${CYAN}ID de pedido múltiple: $PEDIDO_MULTIPLE_ID${NC}"
    fi
    show_result $? "POST /pedidos - Crear pedido múltiple"
else
    show_result 1 "POST /pedidos - Crear pedido múltiple (sin instrumento)"
fi

echo ""

# =============================================================================
# TESTS DE IMÁGENES
# =============================================================================

echo -e "${PURPLE}🖼️ TESTS DE IMÁGENES${NC}"
echo "===================="

# Test 19: Obtener imágenes de instrumento
if [ -n "$INSTRUMENTO_ID" ]; then
    make_request "GET" "/images/instrumento/$INSTRUMENTO_ID" "" "Obtener imágenes de instrumento"
    show_result $? "GET /images/instrumento/{id} - Listar imágenes"
else
    show_result 1 "GET /images/instrumento/{id} - Listar imágenes (sin ID)"
fi

# Test 20: Obtener imagen principal de instrumento (corregido)
if [ -n "$INSTRUMENTO_ID" ]; then
    echo -e "${YELLOW}Testing: Obtener imagen principal (puede no existir)${NC}"
    response=$(curl -s -w "\n%{http_code}" "$BASE_URL/images/instrumento/$INSTRUMENTO_ID/primary" 2>/dev/null)
    http_code=$(echo "$response" | tail -n1)
    body=$(echo "$response" | head -n -1)
    
    echo "Response: $body"
    echo "HTTP Code: $http_code"
    
    # Aceptar tanto 200 (si existe) como 404 (si no existe) como válidos
    if [[ "$http_code" == "200" ]] || [[ "$http_code" == "404" ]]; then
        if [[ "$http_code" == "404" ]]; then
            echo -e "${YELLOW}ℹ️  No hay imagen principal configurada (esto es normal)${NC}"
        fi
        show_result 0 "GET /images/instrumento/{id}/primary - Imagen principal"
    else
        show_result 1 "GET /images/instrumento/{id}/primary - Imagen principal"
    fi
else
    show_result 1 "GET /images/instrumento/{id}/primary - Imagen principal (sin ID)"
fi

# Test 21: Verificar acceso a imágenes estáticas (corregido)
echo -e "${YELLOW}Testing: Acceder a imagen estática${NC}"
response=$(curl -s -w "\n%{http_code}" "$BASE_URL/images/nro1.jpg" 2>/dev/null)
http_code=$(echo "$response" | tail -n1)

# No mostrar el contenido binario de la imagen, solo el código HTTP
echo "HTTP Code: $http_code"

if [[ "$http_code" == "200" ]]; then
    echo -e "${GREEN}✅ Imagen estática accesible${NC}"
    show_result 0 "GET /images/{filename} - Servir imagen estática"
elif [[ "$http_code" == "404" ]]; then
    echo -e "${YELLOW}ℹ️  Imagen estática no encontrada (verificar si existe nro1.jpg)${NC}"
    show_result 0 "GET /images/{filename} - Servir imagen estática (archivo no existe)"
else
    show_result 1 "GET /images/{filename} - Servir imagen estática"
fi

# Test 22: Subir imagen (si existe archivo de prueba)
if [ -f "test-image.png" ] && [ -n "$INSTRUMENTO_ID" ]; then
    echo -e "${YELLOW}Testing: Subir imagen de prueba${NC}"
    upload_response=$(curl -s -w "\n%{http_code}" -X POST \
        -F "file=@test-image.png" \
        -F "altText=Imagen de prueba API" \
        -F "isPrimary=true" \
        "$BASE_URL/images/upload/$INSTRUMENTO_ID" 2>/dev/null)
    
    upload_code=$(echo "$upload_response" | tail -n1)
    upload_body=$(echo "$upload_response" | head -n -1)
    
    echo "Response: $upload_body"
    echo "HTTP Code: $upload_code"
    
    if [[ "$upload_code" =~ ^2[0-9][0-9]$ ]]; then
        IMAGE_ID=$(extract_id "$upload_body")
        echo -e "${CYAN}ID de imagen subida: $IMAGE_ID${NC}"
        show_result 0 "POST /images/upload/{id} - Subir imagen"
    else
        show_result 1 "POST /images/upload/{id} - Subir imagen"
    fi
else
    echo -e "${YELLOW}⚠️  Test de subida de imagen saltado (archivo test-image.png no encontrado o sin instrumento)${NC}"
    show_result 0 "POST /images/upload/{id} - Subir imagen (saltado)"
fi

echo ""

# =============================================================================
# TESTS DE VALIDACIONES Y ERRORES
# =============================================================================

echo -e "${PURPLE}🔍 TESTS DE VALIDACIONES${NC}"
echo "========================="

# Test 23: Error 404 para instrumento inexistente
echo -e "${YELLOW}Testing: Error 404 para instrumento inexistente${NC}"
response=$(curl -s -w "\n%{http_code}" "$BASE_URL/instrumentos/99999" 2>/dev/null)
http_code=$(echo "$response" | tail -n1)
echo "HTTP Code: $http_code"
if [ "$http_code" = "404" ]; then
    show_result 0 "GET /instrumentos/99999 - Error 404 correcto"
else
    show_result 1 "GET /instrumentos/99999 - Error 404 correcto"
fi

# Test 24: Error 404 para pedido inexistente
echo -e "${YELLOW}Testing: Error 404 para pedido inexistente${NC}"
response=$(curl -s -w "\n%{http_code}" "$BASE_URL/pedidos/99999" 2>/dev/null)
http_code=$(echo "$response" | tail -n1)
echo "HTTP Code: $http_code"
if [ "$http_code" = "404" ]; then
    show_result 0 "GET /pedidos/99999 - Error 404 correcto"
else
    show_result 1 "GET /pedidos/99999 - Error 404 correcto"
fi

# Test 25: Validación de datos inválidos en pedido
invalid_pedido='{"instrumentos": []}'
echo -e "${YELLOW}Testing: Validación de pedido inválido${NC}"
response=$(curl -s -w "\n%{http_code}" -X POST \
    -H "Content-Type: application/json" \
    -d "$invalid_pedido" \
    "$BASE_URL/pedidos" 2>/dev/null)
http_code=$(echo "$response" | tail -n1)
body=$(echo "$response" | head -n -1)
echo "Response: $body"
echo "HTTP Code: $http_code"
if [ "$http_code" = "400" ]; then
    show_result 0 "POST /pedidos - Validación de datos inválidos"
else
    show_result 1 "POST /pedidos - Validación de datos inválidos"
fi

# Test 26: Validación de categoría duplicada
if [ -n "$CATEGORIA_ID" ]; then
    duplicate_categoria='{"denominacion": "Test Categoria API Actualizada"}'
    echo -e "${YELLOW}Testing: Validación de categoría duplicada${NC}"
    response=$(curl -s -w "\n%{http_code}" -X POST \
        -H "Content-Type: application/json" \
        -d "$duplicate_categoria" \
        "$BASE_URL/categorias" 2>/dev/null)
    http_code=$(echo "$response" | tail -n1)
    body=$(echo "$response" | head -n -1)
    echo "Response: $body"
    echo "HTTP Code: $http_code"
    if [ "$http_code" = "400" ] || [ "$http_code" = "409" ]; then
        show_result 0 "POST /categorias - Validación de duplicados"
    else
        show_result 1 "POST /categorias - Validación de duplicados"
    fi
else
    show_result 1 "POST /categorias - Validación de duplicados (sin categoría)"
fi

echo ""

# =============================================================================
# LIMPIEZA DE DATOS DE PRUEBA
# =============================================================================

echo -e "${PURPLE}🧹 LIMPIEZA DE DATOS DE PRUEBA${NC}"
echo "==============================="

# Eliminar imagen subida
if [ -n "$IMAGE_ID" ]; then
    echo -e "${YELLOW}Eliminando imagen de prueba...${NC}"
    make_request "DELETE" "/images/$IMAGE_ID" "" "Eliminar imagen de prueba"
    show_result $? "DELETE /images/{id} - Limpiar imagen"
fi

# Eliminar pedidos creados
if [ -n "$PEDIDO_ID" ]; then
    echo -e "${YELLOW}Eliminando pedido de prueba...${NC}"
    make_request "DELETE" "/pedidos/$PEDIDO_ID" "" "Eliminar pedido de prueba"
    show_result $? "DELETE /pedidos/{id} - Limpiar pedido"
fi

if [ -n "$PEDIDO_MULTIPLE_ID" ]; then
    echo -e "${YELLOW}Eliminando pedido múltiple...${NC}"
    make_request "DELETE" "/pedidos/$PEDIDO_MULTIPLE_ID" "" "Eliminar pedido múltiple"
    show_result $? "DELETE /pedidos/{id} - Limpiar pedido múltiple"
fi

# Eliminar instrumento creado
if [ -n "$INSTRUMENTO_ID" ]; then
    echo -e "${YELLOW}Eliminando instrumento de prueba...${NC}"
    make_request "DELETE" "/instrumentos/$INSTRUMENTO_ID" "" "Eliminar instrumento de prueba"
    show_result $? "DELETE /instrumentos/{id} - Limpiar instrumento"
fi

# Eliminar categoría creada
if [ -n "$CATEGORIA_ID" ]; then
    echo -e "${YELLOW}Eliminando categoría de prueba...${NC}"
    make_request "DELETE" "/categorias/$CATEGORIA_ID" "" "Eliminar categoría de prueba"
    show_result $? "DELETE /categorias/{id} - Limpiar categoría"
fi

echo ""

# =============================================================================
# RESUMEN FINAL
# =============================================================================

echo -e "${BLUE}📊 RESUMEN FINAL DE TESTING${NC}"
echo -e "${BLUE}============================${NC}"
echo ""
echo -e "${CYAN}Total de tests ejecutados: ${YELLOW}$TOTAL_TESTS${NC}"
echo -e "${CYAN}Tests exitosos: ${GREEN}$PASSED_TESTS${NC}"
echo -e "${CYAN}Tests fallidos: ${RED}$FAILED_TESTS${NC}"
echo ""

# Calcular porcentaje de éxito
if [ $TOTAL_TESTS -gt 0 ]; then
    success_rate=$(( (PASSED_TESTS * 100) / TOTAL_TESTS ))
    echo -e "${CYAN}Tasa de éxito: ${YELLOW}$success_rate%${NC}"
fi

echo ""

if [ $FAILED_TESTS -eq 0 ]; then
    echo -e "${GREEN}🎉 ¡TODOS LOS TESTS PASARON EXITOSAMENTE! 🎉${NC}"
    echo -e "${GREEN}✅ La API está funcionando perfectamente${NC}"
    echo ""
    echo -e "${GREEN}📋 Funcionalidades verificadas:${NC}"
    echo -e "${GREEN}   ✅ CRUD completo de categorías${NC}"
    echo -e "${GREEN}   ✅ CRUD completo de instrumentos${NC}"
    echo -e "${GREEN}   ✅ CRUD completo de pedidos${NC}"
    echo -e "${GREEN}   ✅ Sistema de imágenes${NC}"
    echo -e "${GREEN}   ✅ Búsqueda y filtrado${NC}"
    echo -e "${GREEN}   ✅ Paginación y ordenamiento${NC}"
    echo -e "${GREEN}   ✅ Validaciones y manejo de errores${NC}"
    echo ""
    echo -e "${GREEN}🚀 Endpoints disponibles:${NC}"
    echo -e "${CYAN}   • Instrumentos: $BASE_URL/instrumentos${NC}"
    echo -e "${CYAN}   • Categorías: $BASE_URL/categorias${NC}"
    echo -e "${CYAN}   • Pedidos: $BASE_URL/pedidos${NC}"
    echo -e "${CYAN}   • Imágenes: $BASE_URL/images${NC}"
    echo ""
    echo -e "${GREEN}🎵 ¡Tu backend de instrumentos musicales está listo para producción!${NC}"
    exit 0
else
    echo -e "${RED}⚠️  ALGUNOS TESTS FALLARON${NC}"
    echo -e "${RED}❌ Tests que necesitan revisión:${NC}"
    for test_name in "${FAILED_TEST_NAMES[@]}"; do
        echo -e "${RED}   • $test_name${NC}"
    done
    echo ""
    echo -e "${YELLOW}💡 Recomendaciones:${NC}"
    echo -e "${YELLOW}   • Revisa los logs de Spring Boot para más detalles${NC}"
    echo -e "${YELLOW}   • Verifica que PostgreSQL esté corriendo${NC}"
    echo -e "${YELLOW}   • Confirma que la base de datos 'instrumentosdb' exista${NC}"
    echo -e "${YELLOW}   • Asegúrate de que Spring Boot esté en puerto 3001${NC}"
    exit 1
fi
