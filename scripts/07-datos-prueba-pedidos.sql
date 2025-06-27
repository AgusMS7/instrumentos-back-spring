-- Conectar a la base de datos
\c instrumentosdb;

-- Insertar datos de prueba en pedido
INSERT INTO pedido (fecha_pedido, total_pedido) VALUES
('2024-01-15', 1500.00),
('2024-01-16', 2300.50),
('2024-01-17', 890.75);

-- Insertar datos de prueba en pedido_detalle
-- Pedido 1
INSERT INTO pedido_detalle (pedido_id, instrumento_id, cantidad) VALUES
(1, 1, 2),
(1, 3, 1);

-- Pedido 2
INSERT INTO pedido_detalle (pedido_id, instrumento_id, cantidad) VALUES
(2, 2, 1),
(2, 4, 3);

-- Pedido 3
INSERT INTO pedido_detalle (pedido_id, instrumento_id, cantidad) VALUES
(3, 5, 1),
(3, 1, 1);

-- Mostrar los datos insertados
SELECT 'Pedidos creados:' as info;
SELECT * FROM pedido ORDER BY id;

SELECT 'Detalles de pedidos creados:' as info;
SELECT pd.*, i.instrumento, i.marca, i.precio 
FROM pedido_detalle pd 
JOIN instrumento i ON pd.instrumento_id = i.id 
ORDER BY pd.pedido_id, pd.id;
