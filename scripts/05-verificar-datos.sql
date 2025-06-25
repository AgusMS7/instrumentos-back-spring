-- Script para verificar que los datos se insertaron correctamente
-- Ejecutar este script conectado a la base de datos InstrumentosDB

-- Verificar categorías
SELECT 'CATEGORÍAS:' as tipo;
SELECT id, denominacion FROM categoria_instrumento ORDER BY id;

-- Verificar instrumentos por categoría
SELECT 'INSTRUMENTOS POR CATEGORÍA:' as tipo;
SELECT 
    c.denominacion as categoria,
    COUNT(i.id) as cantidad_instrumentos
FROM categoria_instrumento c
LEFT JOIN instrumento i ON c.id = i.idcategoria
GROUP BY c.id, c.denominacion
ORDER BY c.id;

-- Verificar algunos instrumentos
SELECT 'MUESTRA DE INSTRUMENTOS:' as tipo;
SELECT 
    i.id,
    i.instrumento,
    i.marca,
    i.precio,
    c.denominacion as categoria
FROM instrumento i
JOIN categoria_instrumento c ON i.idcategoria = c.id
ORDER BY i.id
LIMIT 10;

-- Estadísticas generales
SELECT 'ESTADÍSTICAS:' as tipo;
SELECT 
    COUNT(*) as total_instrumentos,
    AVG(precio) as precio_promedio,
    MIN(precio) as precio_minimo,
    MAX(precio) as precio_maximo,
    SUM(cantidadvendida) as total_vendidos
FROM instrumento;
