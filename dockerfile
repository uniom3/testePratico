# === ESTÁGIO 1: BUILD ===
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

# Copiar arquivos de configuração primeiro (aproveita cache do Docker)
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Baixar dependências (cache layer)
RUN mvn dependency:go-offline -B

# Copiar código fonte
COPY src src

# Compilar e gerar JAR
RUN mvn clean package -DskipTests

# === ESTÁGIO 2: EXECUÇÃO ===
FROM eclipse-temurin:21-jre-jammy

# Argumentos de build
ARG APP_VERSION=1.0.0
ARG BUILD_DATE

# Labels para documentação
LABEL org.opencontainers.image.title="File Processor Hexagonal"
LABEL org.opencontainers.image.description="Arquitetura Hexagonal para processamento de arquivos"
LABEL org.opencontainers.image.version=${APP_VERSION}
LABEL org.opencontainers.image.created=${BUILD_DATE}
LABEL org.opencontainers.image.authors="Seu Nome <seu.email@exemplo.com>"

# Criar usuário não-root para segurança
RUN addgroup --system --gid 1001 appuser && \
    adduser --system --uid 1001 --gid 1001 appuser

# Diretório da aplicação
WORKDIR /app

# Copiar JAR do estágio de build
COPY --from=builder --chown=appuser:appuser /app/target/*.jar app.jar

# Criar diretório para arquivos temporários
RUN mkdir -p /tmp/file-processing && \
    chown -R appuser:appuser /tmp/file-processing

# Configurações de JVM otimizadas para container
ENV JAVA_OPTS="\
    -XX:+UseContainerSupport \
    -XX:MaxRAMPercentage=75.0 \
    -XX:+UseG1GC \
    -XX:+ParallelRefProcEnabled \
    -XX:+HeapDumpOnOutOfMemoryError \
    -XX:HeapDumpPath=/tmp/heap-dump.hprof \
    -Djava.security.egd=file:/dev/./urandom \
    -Dfile.encoding=UTF-8"

# Healthcheck para monitoramento
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Expor porta da aplicação
EXPOSE 8080

# Mudar para usuário não-root
USER appuser

# Entrypoint
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]