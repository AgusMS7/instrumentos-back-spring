-- Conectar a la base de datos
\c instrumentosdb;

-- Verificar que las tablas existen
SELECT 'Verificando tablas de pedidos...' as info;
\dt pedido*;

-- Contar registros
SELECT 'Total de pedidos:' as info, COUNT(*) as total FROM pedido;
SELECT 'Total de detalles:' as info, COUNT(*) as total FROM pedido_detalle;

-- Mostrar pedidos con sus detalles
SELECT 'Pedidos con detalles:' as info;
SELECT 
    p.id as pedido_id,
    p.fecha_pedido,
    p.total_pedido,
    pd.cantidad,
    i.instrumento,
    i.marca,
    i.precio,
    (pd.cantidad * i.precio) as subtotal
FROM pedido p
JOIN pedido_detalle pd ON p.id = pd.pedido_id
JOIN instrumento i ON pd.instrumento_id = i.id
ORDER BY p.id, pd.id;

-- Verificar totales calculados
SELECT 'Verificación de totales:' as info;
SELECT 
    p.id,
    p.total_pedido as total_registrado,
    SUM(pd.cantidad * i.precio) as total_calculado,
    CASE 
        WHEN p.total_pedido = SUM(pd.cantidad * i.precio) THEN 'OK'
        ELSE 'ERROR'
    END as verificacion
FROM pedido p
JOIN pedido_detalle pd ON p.id = pd.pedido_id
JOIN instrumento i ON pd.instrumento_id = i.id
GROUP BY p.id, p.total_pedido
ORDER BY p.id;

-- Verificar integridad de datos
SELECT 'Verificando integridad de datos...' AS paso;

-- Verificar que todos los pedidos tienen al menos un detalle
SELECT 
    p.id as pedido_sin_detalles
FROM pedido p
LEFT JOIN pedido_detalle pd ON p.id = pd.pedido_id
WHERE pd.id IS NULL;

-- Verificar que todos los detalles referencian instrumentos existentes
SELECT 
    pd.id as detalle_id,
    pd.instrumento_id as instrumento_inexistente
FROM pedido_detalle pd
LEFT JOIN instrumento i ON pd.instrumento_id = i.id
WHERE i.id IS NULL;

-- Mostrar estadísticas generales
SELECT 'Estadísticas generales...' AS paso;

SELECT 
    COUNT(DISTINCT p.id) as total_pedidos,
    COUNT(pd.id) as total_items_pedidos,
    SUM(p.total_pedido) as valor_total_pedidos,
    AVG(p.total_pedido) as promedio_por_pedido,
    MIN(p.fecha_pedido) as fecha_primer_pedido,
    MAX(p.fecha_pedido) as fecha_ultimo_pedido
FROM pedido p
LEFT JOIN pedido_detalle pd ON p.id = pd.pedido_id;

-- Instrumentos más pedidos
SELECT 'Instrumentos más pedidos...' AS paso;

SELECT 
    i.instrumento,
    i.marca,
    SUM(pd.cantidad) as total_vendido,
    COUNT(pd.id) as veces_pedido
FROM instrumento i
INNER JOIN pedido_detalle pd ON i.id = pd.instrumento_id
GROUP BY i.id, i.instrumento, i.marca
ORDER BY total_vendido DESC
LIMIT 5;

SELECT 'Verificación completada exitosamente' AS resultado;
