# Universidad - Sistema de Gestión Completo

Sistema CRUD facultad y carrera con **Angular 17** frontend y **Spring Boot 3.2** backend, desarrollado siguiendo los principios **SOLID** y arquitectura por capas.

## 🚀 Inicio Rápido

```bash
# 1. Clonar el repositorio
git clone <repository-url>
cd capasbackdocker

# 2. Configurar variables de entorno
cp .env.example .env
# Editar .env con tus credenciales de base de datos

# 3. Iniciar todos los servicios con Docker
docker-compose up --build -d

# 4. Verificar que todo funcione
# Frontend: http://localhost:80
# Backend:  http://localhost:8080/api/v1/facultades
# Estado:   docker-compose ps
```

**¡Listo!** El sistema estará disponible en:
- 🌐 **Frontend**: http://localhost:80
- 🚀 **API Backend**: http://localhost:8080
- 🗄️ **Base de Datos**: localhost:5432

## 🏗️ Arquitectura del Sistema

### Arquitectura de Contenedores
El sistema utiliza **Docker Compose** con 3 servicios principales:

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │   Backend       │    │   Database      │
│   (Angular)     │    │   (Spring Boot) │    │   (PostgreSQL)  │
│                 │    │                 │    │                 │
│   Port: 80      │───▶│   Port: 8080    │───▶│   Port: 5432    │
│   Nginx         │    │   Tomcat        │    │   PostgreSQL 15 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

**Servicios Docker:**
- `universidad-frontend`: Angular 17 + Nginx (puerto 80)
- `universidad-api`: Spring Boot 3.2 + Java 21 (puerto 8080)  
- `universidad-postgres`: PostgreSQL 15-alpine (puerto 5432)

### Backend (API REST)
```
backend-api/
├── src/main/java/com/universidad/api/
│   ├── application/          # Capa de aplicación
│   │   ├── dto/             # Data Transfer Objects
│   │   ├── mappers/         # Mappers (MapStruct)
│   │   └── services/        # Servicios de aplicación
│   ├── domain/              # Capa de dominio
│   │   ├── entities/        # Entidades JPA
│   │   └── repositories/    # Interfaces de repositorio
│   └── infrastructure/      # Capa de infraestructura
│       ├── config/          # Configuraciones
│       └── web/            # Controladores REST
└── src/main/resources/      # Recursos y configuraciones
```

### Frontend (Angular)
```
frontend/
├── src/app/
│   ├── components/          # Componentes reutilizables
│   ├── pages/              # Páginas principales
│   ├── services/           # Servicios HTTP
│   ├── models/             # Interfaces TypeScript
│   └── shared/             # Utilidades compartidas
└── src/environments/       # Configuraciones de entorno
```

## 🎯 Principios SOLID Aplicados

- **SRP**: Cada clase tiene una responsabilidad específica
- **OCP**: Extensibilidad a través de interfaces
- **LSP**: Implementaciones correctas de contratos
- **ISP**: Interfaces segregadas por función
- **DIP**: Dependencias hacia abstracciones

## 🛠️ Instalación y Uso

### Prerrequisitos
- Docker y Docker Compose
- Node.js 18+ (para desarrollo local del frontend)
- Java 21+ (para desarrollo local del backend)
- Maven 3.8+ (para desarrollo local del backend)

### 🐳 Ejecución con Docker (Recomendado)

1. **Clonar repositorio**
```bash
git clone <repository-url>
cd capasbackdocker
```

2. **Configurar variables de entorno**
```bash
# Copiar archivo de ejemplo
cp .env.example .env

# Editar las credenciales según tu entorno
# nano .env  (Linux/Mac)
# notepad .env  (Windows)
```

3. **Iniciar todos los servicios**
```bash
# Construir e iniciar todos los servicios
docker-compose up --build -d

# O simplemente iniciar (si ya están construidos)
docker-compose up -d
```

4. **Verificar que todos los servicios estén corriendo**
```bash
# Ver estado de contenedores
docker-compose ps

# Deberías ver algo como:
# universidad-api        ... Up X minutes (healthy)     0.0.0.0:8080->8080/tcp
# universidad-frontend   ... Up X minutes (healthy)     0.0.0.0:80->80/tcp  
# universidad-postgres   ... Up X minutes (healthy)     0.0.0.0:5432->5432/tcp

# Ver logs si hay problemas
docker-compose logs universidad-api
docker-compose logs universidad-frontend
docker-compose logs universidad-postgres
```

3. **Verificar servicios**
```bash
# Frontend (Angular + Nginx)
curl http://localhost:80
# o abrir en navegador: http://localhost:80

# Backend API
curl http://localhost:8080/api/v1/facultades
# o curl http://localhost:8080/actuator/health

# Base de datos PostgreSQL
# Conectar en: localhost:5432
# Usuario: universidad_user
# Password: universidad_password
# Base de datos: universidad_db
```

### 💻 Desarrollo Local

#### Backend
1. **Iniciar PostgreSQL**
```bash
docker-compose up postgres-db -d
```

2. **Configurar aplicación**
```bash
# Copiar configuración local
cp backend-api/src/main/resources/application.properties backend-api/src/main/resources/application-local.properties
# Editar configuración según necesidades
```

3. **Ejecutar aplicación**
```bash
cd backend-api
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

#### Frontend
1. **Instalar dependencias**
```bash
cd frontend
npm install
```

2. **Ejecutar en modo desarrollo**
```bash
npm start
# Disponible en http://localhost:4200
```

## 📚 API Endpoints

### Facultades

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/facultades` | Listar facultades |
| GET | `/api/v1/facultades/{id}` | Obtener facultad por ID |
| POST | `/api/v1/facultades` | Crear nueva facultad |
| PUT | `/api/v1/facultades/{id}` | Actualizar facultad |
| DELETE | `/api/v1/facultades/{id}` | Eliminar facultad |
| GET | `/api/v1/facultades/buscar/nombre/{nombre}` | Buscar por nombre |
| GET | `/api/v1/facultades/buscar/decano?decano={nombre}` | Buscar por decano |

### Carreras

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/carreras` | Listar carreras |
| GET | `/api/v1/carreras/{id}` | Obtener carrera por ID |
| GET | `/api/v1/carreras/facultad/{facultadId}` | Carreras por facultad |
| POST | `/api/v1/carreras` | Crear nueva carrera |
| PUT | `/api/v1/carreras/{id}` | Actualizar carrera |
| DELETE | `/api/v1/carreras/{id}` | Eliminar carrera |
| GET | `/api/v1/carreras/buscar/nombre/{nombre}` | Buscar por nombre |
| GET | `/api/v1/carreras/buscar/duracion/{semestres}` | Buscar por duración |

## 🔧 Configuración

### Variables de Entorno

El proyecto utiliza un archivo `.env` para la configuración. Copia `.env.example` y personaliza según tu entorno:

```bash
cp .env.example .env
```

**Variables principales:**

| Variable | Descripción | Valor por Defecto |
|----------|-------------|-------------------|
| `POSTGRES_DB` | Nombre de la base de datos | `universidad_db` |
| `POSTGRES_USER` | Usuario de PostgreSQL | `universidad_user` |
| `POSTGRES_PASSWORD` | Contraseña de PostgreSQL | `universidad_password` |
| `SPRING_PROFILES_ACTIVE` | Perfil activo de Spring | `docker` |
| `API_PORT` | Puerto del backend | `8080` |
| `FRONTEND_PORT` | Puerto del frontend | `80` |
| `DATABASE_PORT` | Puerto de PostgreSQL | `5432` |

### Perfiles de Spring

- **`default`**: Desarrollo local con PostgreSQL local
- **`docker`**: Contenedores Docker
- **`test`**: Pruebas con H2 en memoria

## 🔧 Resolución de Problemas

### CORS Issues
Si experimentas problemas de CORS:
```bash
# Verificar headers CORS
curl -H "Origin: http://localhost:4200" \
  -H "Access-Control-Request-Method: GET" \
  -H "Access-Control-Request-Headers: X-Requested-With" \
  -X OPTIONS \
  http://localhost:8080/api/v1/facultades
```

### Docker Issues
```bash
# Limpiar cache de Docker si hay problemas
docker system prune -a --volumes -f

# Reconstruir desde cero
docker-compose down
docker-compose up --build -d

# Ver logs detallados
docker-compose logs -f universidad-api
```

### Base de Datos
```bash
# Conectar directamente a PostgreSQL
docker exec -it universidad-postgres psql -U universidad_user -d universidad_db

# Verificar tablas
\dt

# Ver datos
SELECT * FROM facultades;
```

## 🔒 Seguridad

### Variables de Entorno
- ✅ **No commitear** el archivo `.env` al repositorio
- ✅ **Usar contraseñas seguras** en producción
- ✅ **Rotar credenciales** regularmente
- ✅ **Revisar** el archivo `.gitignore` incluye `.env`

### Buenas Prácticas
```bash
# Verificar que .env no esté en git
git status

# Generar contraseñas seguras
openssl rand -base64 32

# Cambiar credenciales por defecto
# Editar .env con valores únicos para tu entorno
```

## 📈 Monitoring

- **Health Check**: `http://localhost:8080/actuator/health`
- **Metrics**: `http://localhost:8080/actuator/metrics`
- **Info**: `http://localhost:8080/actuator/info`

## 🔗 URLs Importantes

### Servicios Principales
- **🌐 Frontend (Angular)**: http://localhost:80
  - Interfaz de usuario completa con Angular 17
  - Gestionado por Nginx como proxy reverso
  
- **🚀 Backend API**: http://localhost:8080
  - API REST base: http://localhost:8080/api/v1
  - Health check: http://localhost:8080/actuator/health
  - Documentación API: http://localhost:8080/swagger-ui.html
  
- **🗄️ Base de Datos PostgreSQL**: localhost:5432
  - Usuario: `universidad_user`
  - Contraseña: `universidad_password`
  - Base de datos: `universidad_db`

### Endpoints de Prueba
```bash
# Verificar frontend
curl http://localhost:80

# Verificar backend
curl http://localhost:8080/actuator/health

# Listar facultades
curl http://localhost:8080/api/v1/facultades

# Crear facultad (ejemplo)
curl -X POST http://localhost:8080/api/v1/facultades \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Ingeniería","descripcion":"Facultad de Ingeniería","ubicacion":"Campus Norte","decano":"Dr. Juan Pérez"}'
```

## 📝 Licencia

Este proyecto está bajo la licencia MIT. Ver `LICENSE` para más detalles.

## 📞 Contacto

- Email: jordy.amaro@gmail.com
