#!/bin/bash

# =============================================
# Script para gestionar Universidad API
# =============================================

set -e

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Funciones de utilidad
print_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Función para mostrar ayuda
show_help() {
    echo "Universidad API - Script de gestión"
    echo ""
    echo "Uso: $0 [COMANDO]"
    echo ""
    echo "Comandos disponibles:"
    echo "  start         Iniciar todos los servicios"
    echo "  start-dev     Iniciar servicios en modo desarrollo (incluye PgAdmin)"
    echo "  stop          Detener todos los servicios"
    echo "  restart       Reiniciar todos los servicios"
    echo "  logs          Mostrar logs de la API"
    echo "  logs-db       Mostrar logs de PostgreSQL"
    echo "  logs-frontend Mostrar logs del Frontend"
    echo "  build         Construir imagen de la API"
    echo "  clean         Limpiar containers y volúmenes"
    echo "  status        Mostrar estado de los servicios"
    echo "  test          Ejecutar pruebas"
    echo "  health        Verificar health check de la API"
    echo "  help          Mostrar esta ayuda"
    echo ""
}

# Verificar que Docker esté ejecutándose
check_docker() {
    if ! docker info > /dev/null 2>&1; then
        print_error "Docker no está ejecutándose. Por favor inicia Docker."
        exit 1
    fi
}

# Iniciar servicios
start_services() {
    print_info "Iniciando servicios de Universidad API..."
    check_docker
    docker-compose up -d
    print_success "Servicios iniciados correctamente"
    print_info "Frontend disponible en: http://localhost:80"
    print_info "API disponible en: http://localhost:8080"
    print_info "Swagger UI en: http://localhost:8080/swagger-ui.html"
}

# Iniciar servicios en modo desarrollo
start_dev() {
    print_info "Iniciando servicios en modo desarrollo..."
    check_docker
    docker-compose --profile development up -d
    print_success "Servicios de desarrollo iniciados"
    print_info "Frontend disponible en: http://localhost:80"
    print_info "API disponible en: http://localhost:8080"
    print_info "Swagger UI en: http://localhost:8080/swagger-ui.html"
    print_info "PgAdmin en: http://localhost:5050"
}

# Detener servicios
stop_services() {
    print_info "Deteniendo servicios..."
    docker-compose down
    print_success "Servicios detenidos"
}

# Reiniciar servicios
restart_services() {
    print_info "Reiniciando servicios..."
    stop_services
    start_services
}

# Mostrar logs de la API
show_logs() {
    print_info "Mostrando logs de la API..."
    docker-compose logs -f universidad-api
}

# Mostrar logs del frontend
show_frontend_logs() {
    print_info "Mostrando logs del Frontend..."
    docker-compose logs -f universidad-frontend
}

# Mostrar logs de la base de datos
show_db_logs() {
    print_info "Mostrando logs de PostgreSQL..."
    docker-compose logs -f postgres-db
}

# Construir imagen
build_image() {
    print_info "Construyendo imagen de la API..."
    docker-compose build universidad-api
    print_success "Imagen construida correctamente"
}

# Limpiar sistema
clean_system() {
    print_warning "Esto eliminará todos los containers y volúmenes. ¿Continuar? (y/N)"
    read -r response
    if [[ "$response" =~ ^([yY][eE][sS]|[yY])$ ]]; then
        print_info "Limpiando sistema..."
        docker-compose down -v --remove-orphans
        docker system prune -f
        print_success "Sistema limpiado"
    else
        print_info "Operación cancelada"
    fi
}

# Mostrar estado de servicios
show_status() {
    print_info "Estado de los servicios:"
    docker-compose ps
}

# Ejecutar pruebas
run_tests() {
    print_info "Ejecutando pruebas..."
    cd backend-api
    ./mvnw test
    print_success "Pruebas completadas"
}

# Verificar health check
check_health() {
    print_info "Verificando health check de la API..."
    
    # Esperar hasta que la API esté disponible
    max_attempts=30
    attempt=1
    
    while [ $attempt -le $max_attempts ]; do
        if curl -f -s http://localhost:8080/actuator/health > /dev/null 2>&1; then
            print_success "API está funcionando correctamente"
            curl -s http://localhost:8080/actuator/health | jq '.' 2>/dev/null || curl -s http://localhost:8080/actuator/health
            return 0
        fi
        
        print_info "Intento $attempt/$max_attempts - Esperando que la API esté disponible..."
        sleep 5
        ((attempt++))
    done
    
    print_error "La API no está respondiendo después de $max_attempts intentos"
    return 1
}

# Procesamiento de argumentos
case "${1:-help}" in
    "start")
        start_services
        ;;
    "start-dev")
        start_dev
        ;;
    "stop")
        stop_services
        ;;
    "restart")
        restart_services
        ;;
    "logs")
        show_logs
        ;;
    "logs-db")
        show_db_logs
        ;;
    "logs-frontend")
        show_frontend_logs
        ;;
    "build")
        build_image
        ;;
    "clean")
        clean_system
        ;;
    "status")
        show_status
        ;;
    "test")
        run_tests
        ;;
    "health")
        check_health
        ;;
    "help"|*)
        show_help
        ;;
esac