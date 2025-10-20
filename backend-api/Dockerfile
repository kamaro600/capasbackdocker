# Etapa 1: Build stage
FROM eclipse-temurin:21-jdk-alpine AS builder

# Instalar Maven
RUN apk add --no-cache maven

# Establecer directorio de trabajo
WORKDIR /app

# Copiar archivos de configuración Maven
COPY pom.xml .

# Descargar dependencias (cacheable)
RUN mvn dependency:go-offline -B

# Copiar código fuente
COPY src ./src

# Compilar aplicación con optimizaciones
RUN mvn clean package -DskipTests -B && \
    java -Djarmode=layertools -jar target/*.jar extract

# ================================
# Etapa 2: Runtime stage ultra-liviana
# ================================
FROM eclipse-temurin:21-jre-alpine

# Establecer directorio de trabajo
WORKDIR /app

# Copiar dependencias y aplicación por capas (mejor cacheo)
COPY --from=builder /app/dependencies/ ./
COPY --from=builder /app/spring-boot-loader/ ./
COPY --from=builder /app/snapshot-dependencies/ ./
COPY --from=builder /app/application/ ./

# Variables de entorno optimizadas para contenedores pequeños
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75.0 \
                       -XX:+UseSerialGC \
                       -Xss256k \
                       -XX:MaxMetaspaceSize=128m \
                       -Djava.security.egd=file:/dev/./urandom \
                       -Dspring.profiles.active=docker"

# Puerto expuesto
EXPOSE 8080

# Health check usando netcat
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD nc -z localhost 8080 || exit 1

# Comando principal optimizado
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]