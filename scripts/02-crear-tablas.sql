-- Crear las tablas del sistema
-- Ejecutar este script conectado a la base de datos InstrumentosDB

-- Crear tabla categoria_instrumento
CREATE TABLE IF NOT EXISTS categoria_instrumento (
    id BIGSERIAL PRIMARY KEY,
    denominacion VARCHAR(100) NOT NULL UNIQUE
);

-- Insertar categorías según el TP
INSERT INTO categoria_instrumento (denominacion) VALUES 
('Cuerda'),
('Viento'), 
('Percusión'),
('Teclado'),
('Electrónico');

-- Crear la tabla instrumento con los tipos correctos según el TP
CREATE TABLE IF NOT EXISTS instrumento (
    id BIGSERIAL PRIMARY KEY,
    instrumento VARCHAR(255) NOT NULL,
    marca VARCHAR(100) NOT NULL,
    modelo VARCHAR(100) NOT NULL,
    imagen VARCHAR(255) DEFAULT '',
    precio DECIMAL(10,2) NOT NULL,
    costoenvio VARCHAR(10) DEFAULT '0',
    cantidadvendida INTEGER DEFAULT 0,
    descripcion TEXT DEFAULT '',
    idcategoria BIGINT,
    CONSTRAINT fk_instrumento_categoria 
        FOREIGN KEY (idcategoria) REFERENCES categoria_instrumento(id)
);

-- Crear índices para mejorar el rendimiento
CREATE INDEX IF NOT EXISTS idx_instrumento_marca ON instrumento(marca);
CREATE INDEX IF NOT EXISTS idx_instrumento_precio ON instrumento(precio);
CREATE INDEX IF NOT EXISTS idx_instrumento_categoria ON instrumento(idcategoria);

-- Comentarios en las tablas
COMMENT ON TABLE categoria_instrumento IS 'Tabla que almacena las categorías de instrumentos';
COMMENT ON COLUMN categoria_instrumento.id IS 'Identificador único de la categoría';
COMMENT ON COLUMN categoria_instrumento.denominacion IS 'Nombre de la categoría';

COMMENT ON TABLE instrumento IS 'Tabla que almacena la información de los instrumentos musicales';
COMMENT ON COLUMN instrumento.id IS 'Identificador único del instrumento';
COMMENT ON COLUMN instrumento.instrumento IS 'Nombre del instrumento';
COMMENT ON COLUMN instrumento.marca IS 'Marca del instrumento';
COMMENT ON COLUMN instrumento.modelo IS 'Modelo del instrumento';
COMMENT ON COLUMN instrumento.imagen IS 'Nombre del archivo de imagen';
COMMENT ON COLUMN instrumento.precio IS 'Precio del instrumento';
COMMENT ON COLUMN instrumento.costoenvio IS 'Costo de envío (G para gratis)';
COMMENT ON COLUMN instrumento.cantidadvendida IS 'Cantidad de unidades vendidas';
COMMENT ON COLUMN instrumento.descripcion IS 'Descripción detallada del instrumento';
COMMENT ON COLUMN instrumento.idcategoria IS 'Referencia a la categoría del instrumento';
