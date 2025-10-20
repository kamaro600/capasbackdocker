-- =====================================
-- SCRIPT DE INICIALIZACIÓN DE BASE DE DATOS
-- Universidad API - PostgreSQL
-- =====================================

-- Crear extensiones necesarias
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- =====================================
-- TABLA FACULTAD
-- =====================================
CREATE TABLE IF NOT EXISTS facultad (
    facultad_id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) UNIQUE NOT NULL,
    descripcion TEXT,
    ubicacion VARCHAR(100),
    decano VARCHAR(100),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE
);

-- Comentarios para la tabla facultad
COMMENT ON TABLE facultad IS 'Tabla que almacena información de las facultades universitarias';
COMMENT ON COLUMN facultad.facultad_id IS 'Identificador único de la facultad';
COMMENT ON COLUMN facultad.nombre IS 'Nombre oficial de la facultad (único)';
COMMENT ON COLUMN facultad.descripcion IS 'Descripción detallada de la facultad';
COMMENT ON COLUMN facultad.ubicacion IS 'Ubicación física de la facultad';
COMMENT ON COLUMN facultad.decano IS 'Nombre del decano actual';
COMMENT ON COLUMN facultad.fecha_registro IS 'Fecha y hora de registro en el sistema';
COMMENT ON COLUMN facultad.activo IS 'Indica si la facultad está activa';

-- =====================================
-- TABLA CARRERA
-- =====================================
CREATE TABLE IF NOT EXISTS carrera (
    carrera_id SERIAL PRIMARY KEY,
    facultad_id INTEGER NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    duracion_semestres INTEGER NOT NULL,
    titulo_otorgado VARCHAR(100),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_facultad FOREIGN KEY (facultad_id) REFERENCES facultad(facultad_id) ON DELETE RESTRICT,
    CONSTRAINT uk_carrera_nombre UNIQUE (nombre),
    CONSTRAINT chk_duracion_semestres CHECK (duracion_semestres > 0 AND duracion_semestres <= 20)
);

-- Comentarios para la tabla carrera
COMMENT ON TABLE carrera IS 'Tabla que almacena información de las carreras universitarias';
COMMENT ON COLUMN carrera.carrera_id IS 'Identificador único de la carrera';
COMMENT ON COLUMN carrera.facultad_id IS 'Referencia a la facultad que ofrece la carrera';
COMMENT ON COLUMN carrera.nombre IS 'Nombre oficial de la carrera (único)';
COMMENT ON COLUMN carrera.descripcion IS 'Descripción detallada de la carrera';
COMMENT ON COLUMN carrera.duracion_semestres IS 'Duración de la carrera en semestres';
COMMENT ON COLUMN carrera.titulo_otorgado IS 'Título profesional que se otorga';
COMMENT ON COLUMN carrera.fecha_registro IS 'Fecha y hora de registro en el sistema';
COMMENT ON COLUMN carrera.activo IS 'Indica si la carrera está activa';

-- =====================================
-- ÍNDICES PARA OPTIMIZACIÓN
-- =====================================

-- Índices en tabla facultad
CREATE INDEX IF NOT EXISTS idx_facultad_nombre ON facultad(nombre);
CREATE INDEX IF NOT EXISTS idx_facultad_activo ON facultad(activo);
CREATE INDEX IF NOT EXISTS idx_facultad_decano ON facultad(decano);

-- Índices en tabla carrera
CREATE INDEX IF NOT EXISTS idx_carrera_nombre ON carrera(nombre);
CREATE INDEX IF NOT EXISTS idx_carrera_facultad_id ON carrera(facultad_id);
CREATE INDEX IF NOT EXISTS idx_carrera_activo ON carrera(activo);
CREATE INDEX IF NOT EXISTS idx_carrera_duracion ON carrera(duracion_semestres);
CREATE INDEX IF NOT EXISTS idx_carrera_facultad_activo ON carrera(facultad_id, activo);

-- =====================================
-- DATOS DE PRUEBA (OPCIONAL)
-- =====================================

-- Insertar facultades de ejemplo
INSERT INTO facultad (nombre, descripcion, ubicacion, decano, activo) 
VALUES 
    ('Facultad de Ingeniería', 'Facultad dedicada a las ciencias exactas y aplicadas', 'Edificio Central - Piso 3', 'Dr. Carlos Rodríguez', true),
    ('Facultad de Medicina', 'Facultad de ciencias de la salud', 'Edificio de Medicina', 'Dra. Ana García', true),
    ('Facultad de Derecho', 'Facultad de ciencias jurídicas y sociales', 'Edificio de Humanidades', 'Dr. Luis Martínez', true),
    ('Facultad de Administración', 'Facultad de ciencias económicas y administrativas', 'Edificio de Administración', 'Mg. María Fernández', true),
    ('Facultad de Educación', 'Facultad de ciencias de la educación', 'Edificio de Educación', 'Dr. Pedro Sánchez', true)
ON CONFLICT (nombre) DO NOTHING;

-- Insertar carreras de ejemplo
INSERT INTO carrera (facultad_id, nombre, descripcion, duracion_semestres, titulo_otorgado, activo)
VALUES 
    -- Carreras de Ingeniería
    (1, 'Ingeniería de Sistemas', 'Carrera enfocada en el desarrollo de software y sistemas', 10, 'Ingeniero de Sistemas', true),
    (1, 'Ingeniería Civil', 'Carrera enfocada en la construcción y diseño de infraestructuras', 10, 'Ingeniero Civil', true),
    (1, 'Ingeniería Industrial', 'Carrera enfocada en la optimización de procesos', 10, 'Ingeniero Industrial', true),
    
    -- Carreras de Medicina
    (2, 'Medicina', 'Carrera de ciencias médicas', 12, 'Médico Cirujano', true),
    (2, 'Enfermería', 'Carrera de enfermería profesional', 8, 'Enfermero Profesional', true),
    
    -- Carreras de Derecho
    (3, 'Derecho', 'Carrera de ciencias jurídicas', 10, 'Abogado', true),
    
    -- Carreras de Administración
    (4, 'Administración de Empresas', 'Carrera enfocada en la gestión empresarial', 8, 'Administrador de Empresas', true),
    (4, 'Contaduría Pública', 'Carrera enfocada en ciencias contables', 8, 'Contador Público', true),
    
    -- Carreras de Educación
    (5, 'Licenciatura en Educación Básica', 'Formación de educadores para primaria', 8, 'Licenciado en Educación Básica', true),
    (5, 'Licenciatura en Educación Infantil', 'Formación de educadores para preescolar', 8, 'Licenciado en Educación Infantil', true)
ON CONFLICT (nombre) DO NOTHING;

-- =====================================
-- MENSAJE DE CONFIRMACIÓN
-- =====================================
DO $$
BEGIN
    RAISE NOTICE 'Base de datos Universidad inicializada correctamente';
    RAISE NOTICE 'Facultades creadas: %', (SELECT COUNT(*) FROM facultad);
    RAISE NOTICE 'Carreras creadas: %', (SELECT COUNT(*) FROM carrera);
END $$;