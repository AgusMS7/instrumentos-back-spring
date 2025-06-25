-- Crear la base de datos InstrumentosDB
-- Ejecutar este script como superusuario de PostgreSQL

DROP DATABASE IF EXISTS "InstrumentosDB";

CREATE DATABASE "InstrumentosDB"
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'Spanish_Argentina.1252'
    LC_CTYPE = 'Spanish_Argentina.1252'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

COMMENT ON DATABASE "InstrumentosDB"
    IS 'Base de datos para la tienda de instrumentos musicales Sandoval';
