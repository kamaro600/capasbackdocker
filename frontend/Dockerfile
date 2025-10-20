# ================================
# Multi-stage build para Angular
# ================================

# Etapa 1: Build stage
FROM node:18-alpine AS builder

# Instalar Angular CLI globalmente
RUN npm install -g @angular/cli@17

# Establecer directorio de trabajo
WORKDIR /app

# Copiar archivos de configuración
COPY package*.json ./

# Instalar dependencias
RUN npm install

# Copiar código fuente
COPY . .

# Construir aplicación para producción
RUN ng build --configuration production

# ================================
# Etapa 2: Runtime stage con Nginx
# ================================
FROM nginx:alpine

# Copiar configuración personalizada de Nginx
COPY nginx.conf /etc/nginx/nginx.conf

# Copiar archivos de build desde la etapa anterior
COPY --from=builder /app/dist/universidad-frontend /usr/share/nginx/html

# Puerto expuesto
EXPOSE 80

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:80 || exit 1

# Comando para ejecutar Nginx
CMD ["nginx", "-g", "daemon off;"]