# ============================================
# ETAPA 1: BUILD (Compilación)
# ============================================
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /build

# Copiar el wrapper y archivos de configuración de Gradle
COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle.kts settings.gradle ./

# Dar permisos de ejecución al wrapper (Linux los pierde al copiar desde Windows)
RUN chmod +x ./gradlew

# Descargar dependencias sin compilar el código aún
RUN ./gradlew dependencies --no-daemon

# Copiar el código fuente del proyecto
COPY src ./src

# Compilar la aplicación omitiendo los tests para acelerar el despliegue
RUN ./gradlew build -x test --no-daemon

# ============================================
# ETAPA 2: RUNTIME (Ejecución ligera)
# ============================================
FROM eclipse-temurin:17-jre-alpine AS runtime

RUN addgroup -S spring && adduser -S spring -G spring
WORKDIR /app

COPY --from=builder /build/build/libs/*.jar app.jar

RUN chown spring:spring app.jar
USER spring:spring

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/api/actuator/health || exit 1

ENV SPRING_PROFILES_ACTIVE=prod

ENTRYPOINT ["java", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-Xms256m", \
    "-Xmx512m", \
    "-jar", \
    "app.jar"]