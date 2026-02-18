# testePratico

API Spring Boot para upload e processamento de arquivos (com acompanhamento de progresso e notificacoes via WebSocket).

## Stack

- Java 21
- Spring Boot 3.2.3 (Web, Security, Data JPA, Validation, Actuator, WebSocket)
- H2 (em memoria)
- OpenAPI/Swagger (springdoc)
- Maven Wrapper (`mvnw`, `mvnw.cmd`)

## Requisitos

- Java 21+
- (Opcional) Docker / Docker Compose

## Como rodar (local)

1. Subir a aplicacao:

```powershell
.\mvnw.cmd spring-boot:run
```

2. Acessos:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api-docs`
- Healthcheck: `http://localhost:8080/actuator/health`
- H2 Console: `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:filedb`
  - User: `sa`
  - Password: (vazio)

## Build e execucao do JAR

```powershell
.\mvnw.cmd clean package
java -jar .\target\testePratico-0.0.1-SNAPSHOT.jar
```

## Autenticacao (Bearer Token)

A API usa um token simples via header `Authorization: Bearer <token>`.

Tokens configurados em `src/main/resources/application.yml`:

- `token-envio-123` -> ROLE_ENVIO
- `token-consulta-456` -> ROLE_CONSULTA

## Endpoints principais

Base URL: `http://localhost:8080`

- `POST /api/upload` (ROLE_ENVIO)
- `GET /api/progress/{fileId}` (ROLE_ENVIO ou ROLE_CONSULTA)
- `GET /api/result/{fileId}` (ROLE_CONSULTA)

Exemplos (Windows PowerShell com curl):

```powershell
# Upload (ENVIO)
curl -X POST `
  -H "Authorization: Bearer token-envio-123" `
  -F "file=@C:\\caminho\\arquivo.txt" `
  http://localhost:8080/api/upload

# Progresso (ENVIO ou CONSULTA)
curl -H "Authorization: Bearer token-consulta-456" http://localhost:8080/api/progress/<fileId>

# Resultado (CONSULTA)
curl -H "Authorization: Bearer token-consulta-456" http://localhost:8080/api/result/<fileId>
```

## WebSocket (notificacoes)

- Endpoint SockJS/STOMP: `ws://localhost:8080/ws`
- Topicos:
  - `/topic/progress/{fileId}`
  - `/topic/complete/{fileId}`
  - `/topic/error/{fileId}`

## Docker

Build da imagem (observacao: o arquivo no repo se chama `dockerfile`):

```powershell
docker build -f dockerfile -t testepratico:local .
docker run --rm -p 8080:8080 testepratico:local
```

Docker Compose:

Existe um compose em `src/main/resources/ docker-compose.yml` (note o espaco no nome do arquivo). Para usar como esta:

```powershell
docker compose -f "src/main/resources/ docker-compose.yml" up --build
```

Se o build falhar por referencia a `Dockerfile`, ajuste a linha `dockerfile: Dockerfile` para `dockerfile: dockerfile` (ou renomeie o arquivo `dockerfile` para `Dockerfile`).

Deivid L. Mendonça
Contato	Informação
LinkedIn: https://www.linkedin.com/in/delm/
Email: mendonca.deivid@outlook.com
GitHub: https://github.com/uniom3
