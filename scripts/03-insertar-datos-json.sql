-- Insertar los datos del JSON adaptados a la estructura actual
-- Ejecutar este script conectado a la base de datos InstrumentosDB

-- Función helper para asignar categoría basada en el nombre del instrumento
CREATE OR REPLACE FUNCTION asignar_categoria(nombre_instrumento TEXT) 
RETURNS BIGINT AS $$
BEGIN
    -- Cuerda
    IF LOWER(nombre_instrumento) LIKE '%mandolina%' OR 
       LOWER(nombre_instrumento) LIKE '%guitarra%' OR 
       LOWER(nombre_instrumento) LIKE '%ukelele%' OR
       LOWER(nombre_instrumento) LIKE '%violin%' OR
       LOWER(nombre_instrumento) LIKE '%bajo%' THEN
        RETURN 1;
    -- Viento
    ELSIF LOWER(nombre_instrumento) LIKE '%flauta%' OR 
          LOWER(nombre_instrumento) LIKE '%saxo%' OR 
          LOWER(nombre_instrumento) LIKE '%trompeta%' THEN
        RETURN 2;
    -- Percusión
    ELSIF LOWER(nombre_instrumento) LIKE '%pandereta%' OR 
          LOWER(nombre_instrumento) LIKE '%triangulo%' OR 
          LOWER(nombre_instrumento) LIKE '%percusion%' OR
          LOWER(nombre_instrumento) LIKE '%bateria%' OR
          LOWER(nombre_instrumento) LIKE '%tambor%' OR
          LOWER(nombre_instrumento) LIKE '%chimes%' OR
          LOWER(nombre_instrumento) LIKE '%shekeres%' THEN
        RETURN 3;
    -- Teclado
    ELSIF LOWER(nombre_instrumento) LIKE '%piano%' OR 
          LOWER(nombre_instrumento) LIKE '%teclado%' OR 
          LOWER(nombre_instrumento) LIKE '%organo%' THEN
        RETURN 4;
    -- Electrónico
    ELSIF LOWER(nombre_instrumento) LIKE '%electronico%' OR 
          LOWER(nombre_instrumento) LIKE '%sintetizador%' THEN
        RETURN 5;
    -- Por defecto: Cuerda
    ELSE
        RETURN 1;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Insertar los instrumentos del JSON
INSERT INTO instrumento (instrumento, marca, modelo, imagen, precio, costoenvio, cantidadvendida, descripcion, idcategoria) VALUES
('Mandolina Instrumento Musical Stagg Sunburst', 'Stagg', 'M20', 'nro10.jpg', 2450.00, 'G', 28, 'Estas viendo una excelente mandolina de la marca Stagg, con un sonido muy dulce, tapa aros y fondo de tilo, y diapasón de palisandro. Es un instrumento acústico (no se enchufa) de cuerdas dobles (4 pares) con la caja ovalada y cóncava, y el mástil corto. Su utilización abarca variados ámbitos, desde rock, folk, country y ensambles experimentales.', asignar_categoria('Mandolina Instrumento Musical Stagg Sunburst')),

('Pandereta Pandero Instrumento Musical', 'DyM ventas', '32 sonajas', 'nro9.jpg', 325.00, '150', 10, '1 Pandereta - 32 sonajas metálicas. Más de 8 años vendiendo con 100 % de calificaciones POSITIVAS y clientes satisfechos !!', asignar_categoria('Pandereta Pandero Instrumento Musical')),

('Triangulo Musical 24 Cm Percusion', 'LBP', '24', 'nro8.jpg', 260.00, '250', 3, 'Triangulo Musical de 24 Centímetros De Acero. ENVIOS POR CORREO O ENCOMIENDA: Se le deberán adicionar $40 en concepto de Despacho y el Costo del envío se abonará al recibir el producto en Terminal, Sucursal OCA o Domicilio', asignar_categoria('Triangulo Musical 24 Cm Percusion')),

('Bar Chimes Lp Cortina Musical 72 Barras', 'FM', 'LATIN', 'nro7.jpg', 2250.00, 'G', 2, 'BARCHIME CORTINA MUSICAL DE 25 BARRAS LATIN CUSTOM. Emitimos factura A y B', asignar_categoria('Bar Chimes Lp Cortina Musical 72 Barras')),

('Shekeres. Instrumento. Música. Artesanía.', 'Azalea Artesanías', 'Cuentas de madera', 'nro6.jpg', 850.00, '300', 5, 'Las calabazas utilizadas para nuestras artesanías son sembradas y cosechadas por nosotros, quienes seleccionamos el mejor fruto para garantizar la calidad del producto y ofrecerle algo creativo y original.', asignar_categoria('Shekeres. Instrumento. Música. Artesanía.')),

('Antiguo Piano Aleman Con Candelabros.', 'Neumeyer', 'Stratus', 'nro3.jpg', 17000.00, '2000', 0, 'Buen dia! Sale a la venta este Piano Alemán Neumeyer con candelabros incluidos. Tiene una talla muy bonita en la madera. Una pieza de calidad.', asignar_categoria('Antiguo Piano Aleman Con Candelabros.')),

('Guitarra Ukelele Infantil Grande 60cm', 'GUITARRA', 'UKELELE', 'nro4.jpg', 500.00, 'G', 5, 'Material: Plástico smil madera 4 Cuerdas longitud: 60cm, el mejor regalo para usted, su familia y amigos, adecuado para 3-18 años de edad', asignar_categoria('Guitarra Ukelele Infantil Grande 60cm')),

('Teclado Organo Electronico Musical Instrumento 54 Teclas', 'GADNIC', 'T01', 'nro2.jpg', 2250.00, 'G', 1375, 'Organo Electrónico GADNIC T01. Display de Led. 54 Teclas. 100 Timbres / 100 Ritmos. 4 1/2 octavas. 8 Percusiones. 8 Canciones de muestra. Grabación y reproducción. Entrada para Micrófono. Salida de Audio (Auriculares / Amplificador). Vibrato. Sustain Incluye Atril Apoya partitura y Micrófono. Dimensiones: 84,5 x 32,5 x 11 cm', asignar_categoria('Teclado Organo Electronico Musical Instrumento 54 Teclas')),

('Instrumentos De Percusión Niños Set Musical Con Estuche', 'KNIGHT', 'LB17', 'nro1.jpg', 2700.00, '300', 15, 'Estas viendo un excelente y completísimo set de percusion para niños con estuche rígido, equipado con los instrumentos mas divertidos! De gran calidad y sonoridad. Ideal para jardines, escuelas primarias, musicoterapeutas o chicos que se quieran iniciar en la música de la mejor manera. Es un muy buen producto que garantiza entretenimiento en cualquier casa o reunión, ya que esta equipado para que varias personas al mismo tiempo estén tocando un instrumento.', asignar_categoria('Instrumentos De Percusión Niños Set Musical Con Estuche')),

('Batería Musical Infantil Juguete Niño 9 Piezas Palillos', 'Bateria', 'Infantil', 'nro5.jpg', 850.00, '250', 380, 'DESCRIPCIÓN: DE 1 A 3 AÑOS. EL SET INCLUYE 5 TAMBORES, PALILLOS Y EL PLATILLO TAL CUAL LAS FOTOS. SONIDOS REALISTAS Y FÁCIL DE MONTAR. MEDIDAS: 40X20X46 CM', asignar_categoria('Batería Musical Infantil Juguete Niño 9 Piezas Palillos'));

-- Eliminar la función helper
DROP FUNCTION asignar_categoria(TEXT);
