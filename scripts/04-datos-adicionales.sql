-- Agregar más instrumentos para tener una base de datos más completa
-- Ejecutar este script conectado a la base de datos InstrumentosDB

INSERT INTO instrumento (instrumento, marca, modelo, imagen, precio, costoenvio, cantidadvendida, descripcion, idcategoria) VALUES
-- Instrumentos de Cuerda
('Guitarra Acústica Yamaha', 'Yamaha', 'FG800', 'guitarra-yamaha.jpg', 25000.00, 'G', 15, 'Guitarra acústica de calidad profesional con tapa de abeto sólido', 1),
('Guitarra Eléctrica Fender', 'Fender', 'Stratocaster', 'fender-strat.jpg', 45000.00, 'G', 8, 'Guitarra eléctrica clásica con pastillas single coil', 1),
('Bajo Eléctrico Fender', 'Fender', 'Precision Bass', 'fender-bass.jpg', 38000.00, 'G', 5, 'Bajo eléctrico de 4 cuerdas con sonido potente', 1),
('Violín Stentor', 'Stentor', 'Student I', 'stentor-violin.jpg', 18000.00, '500', 12, 'Violín 4/4 para estudiantes con arco y estuche incluidos', 1),
('Guitarra Clásica Criolla', 'Gracia', 'M5', 'criolla-gracia.jpg', 8500.00, '400', 25, 'Guitarra criolla de estudio con cuerdas de nylon', 1),

-- Instrumentos de Viento
('Saxofón Alto Yamaha', 'Yamaha', 'YAS-280', 'yamaha-sax.jpg', 95000.00, 'G', 2, 'Saxofón alto en Mi bemol, ideal para estudiantes avanzados', 2),
('Trompeta Bach', 'Bach', 'TR300H2', 'bach-trumpet.jpg', 42000.00, 'G', 4, 'Trompeta en Si bemol con acabado lacado dorado', 2),
('Flauta Traversa Pearl', 'Pearl', 'PF-525E', 'pearl-flute.jpg', 32000.00, '300', 9, 'Flauta traversa plateada con mecanismo de platos abiertos', 2),
('Clarinete Buffet', 'Buffet', 'E11', 'buffet-clarinet.jpg', 55000.00, 'G', 3, 'Clarinete en Si bemol de madera de granadilla', 2),
('Armónica Hohner', 'Hohner', 'Special 20', 'hohner-harmonica.jpg', 3500.00, '200', 18, 'Armónica diatónica en Do mayor, ideal para blues', 2),

-- Instrumentos de Percusión
('Batería Pearl Export', 'Pearl', 'Export Series', 'pearl-drums.jpg', 85000.00, 'G', 3, 'Batería completa de 5 piezas con platillos incluidos', 3),
('Cajón Peruano', 'Meinl', 'Headliner', 'meinl-cajon.jpg', 12000.00, '600', 8, 'Cajón peruano con snares internos y sonido profesional', 3),
('Bongos LP', 'Latin Percussion', 'Aspire', 'lp-bongos.jpg', 15000.00, '500', 6, 'Bongos de madera con parches naturales', 3),
('Platillos Zildjian', 'Zildjian', 'A Custom', 'zildjian-cymbals.jpg', 28000.00, 'G', 4, 'Set de platillos profesionales Hi-Hat 14" + Crash 16"', 3),

-- Instrumentos de Teclado
('Piano Digital Casio', 'Casio', 'CDP-S110', 'casio-piano.jpg', 55000.00, 'G', 7, 'Piano digital de 88 teclas con sonidos realistas', 4),
('Teclado Yamaha', 'Yamaha', 'PSR-E373', 'yamaha-keyboard.jpg', 35000.00, 'G', 12, 'Teclado de 61 teclas con 622 voces y 205 estilos', 4),
('Piano Acústico Vertical', 'Kawai', 'K-15E', 'kawai-upright.jpg', 180000.00, 'G', 1, 'Piano acústico vertical de 88 teclas con acabado negro', 4),

-- Instrumentos Electrónicos
('Sintetizador Roland', 'Roland', 'JUNO-DS61', 'roland-synth.jpg', 75000.00, 'G', 6, 'Sintetizador de 61 teclas con múltiples sonidos y efectos', 5),
('Controlador MIDI', 'Akai', 'MPK Mini', 'akai-mpk.jpg', 18000.00, '400', 10, 'Controlador MIDI de 25 teclas con pads y controles', 5),
('Interfaz de Audio', 'Focusrite', 'Scarlett 2i2', 'focusrite-2i2.jpg', 22000.00, '350', 14, 'Interfaz de audio USB de 2 entradas y 2 salidas', 5);
