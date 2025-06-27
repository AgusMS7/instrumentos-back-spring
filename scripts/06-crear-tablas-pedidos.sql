-- Conectar a la base de datos
\c instrumentosdb;

-- Crear tabla pedido
CREATE TABLE IF NOT EXISTS pedido (
    id BIGSERIAL PRIMARY KEY,
    fecha_pedido DATE NOT NULL,
    total_pedido DECIMAL(10,2) NOT NULL
);

-- Crear tabla pedido_detalle
CREATE TABLE IF NOT EXISTS pedido_detalle (
    id BIGSERIAL PRIMARY KEY,
    pedido_id BIGINT NOT NULL,
    instrumento_id BIGINT NOT NULL,
    cantidad INTEGER NOT NULL,
    FOREIGN KEY (pedido_id) REFERENCES pedido(id) ON DELETE CASCADE,
    FOREIGN KEY (instrumento_id) REFERENCES instrumento(id) ON DELETE CASCADE
);

-- Crear índices para mejorar rendimiento
CREATE INDEX IF NOT EXISTS idx_pedido_fecha ON pedido(fecha_pedido);
CREATE INDEX IF NOT EXISTS idx_pedido_detalle_pedido ON pedido_detalle(pedido_id);
CREATE INDEX IF NOT EXISTS idx_pedido_detalle_instrumento ON pedido_detalle(instrumento_id);

-- Mostrar las tablas creadas
\dt;

-- Mostrar estructura de las tablas
\d pedido;
\d pedido_detalle;
