
# TokioMarine — Spring Boot Test (Java 11)

## 🧭 Visão Geral
API com **registro & login (JWT)** e **transferências** com cálculo de taxa.  
Cada usuário recebe um **accountNumber** (10 dígitos). Só é possível transferir **da sua própria conta** para outra conta válida.

**Stack:** Spring Boot 2.7.x (Java 11), Spring Security, JPA/Hibernate, H2, Lombok, JJWT, springdoc-openapi.

---

## ⚙️ Requisitos
- **Java 11**
- **Maven 3.8+**
- (Opcional) `curl` e `jq` para testes via terminal

---

## 🔧 Configuração
Crie/edite `src/main/resources/application.properties`:

```properties
# H2 / JPA
spring.datasource.url=jdbc:h2:mem:transfers;DB_CLOSE_DELAY=-1;MODE=PostgreSQL
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.h2.console.enabled=true
spring.h2.console.path=/h2

# JWT (chave Base64 com >= 32 bytes)
app.jwt.secret=B6RsrQutaqWnm8v/p5A1xVE6UGp50gJuBA73k2Ao5xs=
app.jwt.expiration-ms=7200000
```

> Em produção, prefira variáveis de ambiente: `APP_JWT_SECRET` e `APP_JWT_EXPIRATION_MS`.

---

## ▶️ Como Rodar
```bash
mvn -U clean package
java -jar target/*.jar
```
- H2 Console: `http://localhost:8009/h2`  
  JDBC: `jdbc:h2:mem:transfers` | usuário: `sa` | senha: `admin`

---

## 🧱 Estrutura de Pastas (resumo)
```
com.leoosato.project.TokioMarine
 ├─ config/               # SecurityConfig, OpenApiConfig
 ├─ model/                # User, Transfer
 │   └─ dto/              # SignupRequestDTO, UserResponseDTO, LoginRequestDTO, AuthResponseDTO, Transfer DTOs
 ├─ repository/           # UserRepository, TransferRepository
 ├─ security/             # JwtUtil, JwtAuthFilter, UserDetailsServiceImpl
 ├─ service/              # UserService, TransferService
 ├─ controller/           # AuthController, TransferController
 └─ web/exception/        # BusinessException (+ ApiExceptionHandler opcional)
```

---

## 🔐 Autenticação (JWT)
Fluxo:
1. **POST `/auth/register`** cria usuário e gera `accountNumber`.
2. **POST `/auth/login`** retorna `token` JWT.
3. Use `Authorization: Bearer <token>` nos endpoints protegidos.
4. **GET `/auth/me`** retorna dados do usuário logado (inclui `accountNumber`).

---

## 📡 Endpoints

### Auth
| Método | Caminho          | Body (JSON)                         | Descrição |
|-------:|------------------|-------------------------------------|-----------|
| POST   | `/auth/register` | `{ "username","email","password" }` | Cadastra usuário (gera `accountNumber`) |
| POST   | `/auth/login`    | `{ "username","password" }`         | Retorna `{ token, tokenType }` |
| GET    | `/auth/me`       | — (JWT)                             | Perfil do usuário logado + `accountNumber` |

### Transfers
| Método | Caminho            | Body (JSON)                                                                 | Descrição |
|-------:|--------------------|-----------------------------------------------------------------------------|-----------|
| POST   | `/api/transfers`   | `{ "accountFrom","accountTo","amount","transferDate(YYYY-MM-DD)" }` (JWT)  | Agenda transferência |
| GET    | `/api/transfers`   | — (JWT)                                                                     | Lista transferências |
| GET    | `/api/transfers/{id}` | — (JWT)                                                                  | Busca transferência por id |

**Regras**
- `accountFrom` **deve ser** o `accountNumber` do usuário autenticado.
- `accountTo` precisa existir (pertencer a outro usuário).
- `transferDate` não pode ser no passado.

**Taxas (fee)**
- `0 dias`: **R$ 3,00** + **2,5%**
- `1–10`: **R$ 12,00**
- `11–20`: **8,2%**
- `21–30`: **6,9%**
- `31–40`: **4,7%**
- `41–50`: **1,7%**
- `>50`: **inválido**

---

## 📘 OpenAPI / Swagger

### Dependências (Maven)
```xml
<dependency>
  <groupId>org.springdoc</groupId>
  <artifactId>springdoc-openapi-ui</artifactId>
  <version>1.7.0</version>
</dependency>
<dependency>
  <groupId>org.springdoc</groupId>
  <artifactId>springdoc-openapi-security</artifactId>
  <version>1.7.0</version>
</dependency>
```

### Security (liberar docs)
Na `SecurityConfig`, adicione:
```java
.antMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
```

### Config (exemplo)
```java
@Bean
public OpenAPI apiInfo() {
  return new OpenAPI()
    .info(new Info().title("TokioMarine API").version("v1.0.0"))
    .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
    .components(new Components().addSecuritySchemes("bearerAuth",
        new SecurityScheme().name("bearerAuth").type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
}
```

### Acesso
- **Swagger UI:** `http://localhost:8009/swagger-ui.html` (ou `/docs` se configurado)
- **OpenAPI JSON:** `http://localhost:8009/v3/api-docs`

### Screenshots
<img width="1469" height="562" alt="image" src="https://github.com/user-attachments/assets/bdef4885-f27f-4f62-b0a6-8c217bb5c7f2" />

<img width="1483" height="561" alt="image" src="https://github.com/user-attachments/assets/452635dd-58e8-4a9f-ab38-d21d25f44f89" />


---

## 🧪 Testes Rápidos (cURL)

### Registrar usuários
```bash
curl --location 'http://localhost:8009/auth/register' \
--header 'Content-Type: application/json' \
--data '{
  "username": "ana",
  "email": "ana@example.com",
  "password": "senhaAna"
}'

curl --location 'http://localhost:8009/auth/register' \
--header 'Content-Type: application/json' \
--data '{
  "username": "bruno",
  "email": "bruno@example.com",
  "password": "senhaBruno"
}'
```

### Login e pegar token
```bash
curl --location 'http://localhost:8009/auth/login' \
--header 'Content-Type: application/json' \
--data '{
  "username": "ana",
  "password": "senhaAna"
}'
```

### Ver meu perfil (para obter `accountNumber`)
```bash
curl --location 'http://localhost:8009/auth/me' \
--header 'Authorization: Bearer <TOKEN>'
```

### Criar transferência
```bash
ACCOUNT_FROM="1234567890"   # da ANA (logada)
ACCOUNT_TO="0987654321"     # do BRUNO

curl -i -X POST http://localhost:8009/api/transfers   -H "Authorization: Bearer $TOKEN"   -H "Content-Type: application/json"   -d "{
    \"accountFrom\": \"$ACCOUNT_FROM\",
    \"accountTo\":   \"$ACCOUNT_TO\",
    \"amount\":      1000.00,
    \"transferDate\": \"2025-08-25\"
  }"
```

### Listar transferências
```bash
curl -i http://localhost:8009/api/transfers -H "Authorization: Bearer $TOKEN"
```

---
