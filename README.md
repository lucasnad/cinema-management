# Sistema de Gestão de Cinema

## Descrição do problema

O sistema tem como objetivo gerenciar a programação de um cinema, permitindo o cadastro de filmes e a criação de sessões associadas a esses filmes.  
Ele representa a base para um sistema de venda de ingressos, focando na organização interna do domínio e nas regras de negócio relacionadas às sessões.
O projeto foi desenvolvido com foco em arquitetura de software, priorizando separação de responsabilidades, modularidade e baixo acoplamento.

---

## Objetivo do sistema

O sistema permite:
- Cadastrar filmes
- Consultar filmes cadastrados
- Criar sessões para filmes
- Listar sessões disponíveis
- Vender ingressos para sessões
- Impedir venda de assentos duplicados para a mesma sessão
- Impedir venda de ingressos para sessões já iniciadas
- O foco está na modelagem correta do domínio e na organização arquitetural, não em funcionalidades completas de negócio (como pagamento).

---

## Estilo arquitetural adotado

O sistema foi desenvolvido como um **monólito modular**, utilizando:
- Separação por domínios de negócio (Movies, Sessions, Tickets)
- Conceitos inspirados em DDD (modelo de domínio rico e separação por contexto)
- Arquitetura em camadas com **Ports and Adapters (Arquitetura Hexagonal)**

Cada módulo possui:
- Camada de domínio (regras de negócio)
- Camada de aplicação (casos de uso)
- Portas (interfaces)
- Adapters (REST e persistência)

---

## Diagrama da arquitetura da aplicação

```
                    +---------------------------+
                    |       REST Controllers    |
                    |      (Movies / Sessions)  |
                    +------------+--------------+
                                 |
                                 v
                    +---------------------------+
                    |       Use Cases           |
                    |---------------------------|
                    | Movies:                   |
                    |  - CreateMovieUseCase     |
                    |  - GetMovieUseCase        |
                    |  - ListMoviesUseCase      |
                    |                           |
                    | Sessions:                 |
                    |  - CreateSessionUseCase   |
                    |  - ListSessionsUseCase    |
                    |                           |
                    | Tickets:                  |
                    |  - SellTicketUseCase      |
                    +------------+--------------+
                                 |
                                 v
                    +---------------------------+
                    |        Domain Layer       |
                    |---------------------------|
                    | Movie                     |
                    | Session                   |
                    | Ticket                    |
                    +------------+--------------+
                                 |
                                 v
                    +---------------------------+
                    |    Ports (Interfaces)     |
                    |---------------------------|
                    | MovieRepositoryPort       |
                    | SessionRepositoryPort     |
                    | MovieQueryPort            |
                    | TicketRepositoryPort      |
                    | SessionQueryPort          |
                    +------------+--------------+
                                 |
                                 v
                    +---------------------------+
                    |    Adapters (Outbound)    |
                    |---------------------------|
                    | JPA Repositories (H2)     |
                    +------------+--------------+
                                 |
                                 v
                    +---------------------------+
                    |       H2 Database         |
                    +---------------------------+

    Comunicação interna entre módulos (dentro do monólito):
    CreateSessionUseCase  --(MovieQueryPort)-->  GetMovieUseCase
    SellTicketUseCase --(SessionQueryPort)--> GetSessionUseCase
    (ligação feita via Bean de configuração do Spring)
```

---

## Infraestrutura — Nginx e Docker

### Diagrama da infraestrutura

```
    ┌──────────-┐        ┌──────────────────┐        ┌───────────────────┐
    │  Cliente  │──:80──▶│  Nginx (reverse  │──:8080▶│  Spring Boot API  │
    │ (browser/ │──:443─▶│  proxy + SSL)    │        │  (cinema-api)     │
    │  curl)    │        │                  │        │  H2 em memória    │
    └──────────-┘        └──────────────────┘        └───────────────────┘
                               │
                      docker-compose.yml
                      rede: cinema-network
```

O **Nginx** atua como ponto de entrada único do sistema, recebendo todas as requisições dos clientes e encaminhando-as para a API Spring Boot. Ambos os serviços rodam em containers Docker orquestrados via `docker-compose.yml` em uma rede bridge isolada.

### Funcionalidades implementadas no Nginx

| #  | Funcionalidade | Descrição |
|----|---------------|-----------|
| 1  | **Proxy Reverso** | Encaminha todas as requisições para a API Spring Boot (porta 8080 interna) |
| 2  | **HTTPS/SSL** | Certificado self-signed gerado automaticamente no build do container (TLSv1.2 e TLSv1.3) |
| 3  | **Redirect HTTP → HTTPS** | Requisições na porta 80 são redirecionadas (301) para HTTPS na porta 443 |
| 4  | **Rate Limiting** | 5 req/s por IP, burst de 10, retorna HTTP 429 ao exceder |
| 5  | **Compressão Gzip** | Comprime respostas `application/json` e `text/plain` |
| 6  | **Cache de respostas** | GET/HEAD cacheados por 10s; requisições com `Authorization` bypass o cache |
| 7  | **Headers de segurança** | `X-Content-Type-Options: nosniff`, `X-Frame-Options: DENY`, `X-XSS-Protection: 1; mode=block` |
| 8  | **Limite de payload** | Body máximo de 1MB; retorna HTTP 413 se exceder |
| 9  | **Páginas de erro customizadas** | HTML customizado para 404 e 50x |
| 10 | **Logs estruturados** | Formato: IP, método, URI, status, tempo de resposta do upstream, status do cache |
| 11 | **Logs no stdout/stderr** | Visíveis via `docker logs cinema-nginx` |

### Docker

#### Dockerfile da API (`Dockerfile`)
Multi-stage build:
1. **Estágio de build**: usa `maven:3.9-eclipse-temurin-25` para compilar o projeto
2. **Estágio de runtime**: usa `eclipse-temurin:25-jre` (imagem leve, apenas JRE)

#### Dockerfile do Nginx (`nginx/Dockerfile`)
- Baseado em `nginx:alpine`
- Copia o `nginx.conf` e as páginas de erro customizadas
- Gera certificado SSL self-signed automaticamente via `openssl`

#### docker-compose.yml
- **api**: container Spring Boot, porta 8080 exposta apenas internamente
- **nginx**: container Nginx, portas 80 e 443 expostas para o host
- Rede bridge `cinema-network` para comunicação interna
- Nginx depende (`depends_on`) da API para garantir ordem de inicialização

---

## Principais decisões arquiteturais

### 1. Monólito modular
Foi adotado um monólito modular para manter o sistema em um único processo, reduzindo complexidade de infraestrutura e comunicação distribuída. Essa abordagem permite focar na separação de responsabilidades entre módulos e na qualidade da arquitetura interna.

### 2. Separação por domínios
Os módulos **movies**, **sessions** e **tickets** representam contextos distintos do domínio do cinema. Essa divisão reduz acoplamento, melhora a coesão e permite evolução independente de partes do sistema.

### 3. Ports and Adapters (Arquitetura Hexagonal)
A lógica de negócio foi isolada da infraestrutura por meio de interfaces (ports) e implementações externas (adapters). Isso garante baixo acoplamento com frameworks e facilita testes, manutenção e futuras mudanças tecnológicas.

### 4. Modelo de domínio rico
As entidades de domínio concentram regras e validações essenciais, garantindo que objetos nunca existam em estado inválido. Isso reforça a centralização das regras de negócio na camada de domínio, evitando lógica espalhada pela aplicação.

### 5. Nginx como proxy reverso
O Nginx foi adotado como gateway da aplicação para centralizar funcionalidades cross-cutting (SSL, rate limiting, cache, headers de segurança) fora da aplicação Java. Isso mantém a API focada em regras de negócio e permite configurar segurança e performance na camada de infraestrutura.

### 6. Docker e Docker Compose
A containerização garante reprodutibilidade do ambiente e simplifica o deploy. O Docker Compose orquestra os dois serviços (API + Nginx) com uma rede isolada, facilitando a comunicação interna sem expor a API diretamente.

---

## Alternativas consideradas

| Alternativa | Motivo para não usar |
|-------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| SOA (Service-Oriented Architecture) | Exigiria comunicação entre serviços separados, aumentando a complexidade de implantação e integração, o que foge do escopo de um projeto individual. Do ponto de vista operacional seria muito complexo pra resolver um problema simples. |
| Microserviços | Introduz complexidade operacional (deploy, rede, resiliência) desnecessária para o tamanho do sistema |
| Arquitetura em camadas tradicional | Aumenta o acoplamento entre regras de negócio e infraestrutura, dificultando testes e evolução do domínio (controller → service → repository → banco). Classe service acaba concentrando demais funcionalidade e acopla muito o sistema |
| CRUD anêmico | É quando a regra fica toda em serviços. Isso deixa as entidades sem comportamento. Da forma que fiz o domínio tem força e independência. |

---

## Impacto das decisões

As escolhas arquiteturais resultaram em:
- Código mais organizado e modular
- Maior facilidade de testes unitários
- Possibilidade de migrar partes do sistema para serviços independentes no futuro
- Separação clara entre regras de negócio e infraestrutura
- Segurança, cache e rate limiting centralizados no Nginx (fora da aplicação)
- Ambiente reproduzível via Docker

---

## Principais regras de negócio implementadas

- Um ingresso não pode ser vendido para uma sessão que já iniciou
- Um mesmo assento não pode ser vendido duas vezes para a mesma sessão
- Uma sessão só pode ser criada para um filme existente
- Entidades do domínio nunca são criadas em estado inválido

---

## Autenticação (JWT)

O sistema utiliza autenticação via **JWT (JSON Web Token)**:
- `POST /auth/login?username=<nome>` — gera e retorna um token JWT
- Endpoints protegidos exigem o header `Authorization: Bearer <token>`
- `GET /movies` é o único endpoint público (não exige token)

---

## Instruções para execução

### Executando com Docker Compose (recomendado)

#### Requisitos
- Docker e Docker Compose instalados

#### Passo a passo

```bash
# 1. Na raiz do projeto (onde está o docker-compose.yml)
cd cinema-management

# 2. Subir os containers (build + execução)
docker compose up --build

# 3. A aplicação estará disponível em:
#    - https://localhost       (Nginx com SSL — certificado self-signed)
#    - http://localhost        (redireciona para HTTPS automaticamente)
#
# Nota: como o certificado é self-signed, use a flag -k no curl
#       ou aceite o aviso no navegador.
```

Para parar os containers:
```bash
docker compose down
```

### Executando localmente (sem Docker)

#### Requisitos
- Java 25 instalado
- Variável de ambiente `JAVA_HOME` apontando para o JDK 25
- O banco H2 é iniciado automaticamente em memória

#### Via terminal (Windows/PowerShell)
```bash
cd cinema-management
.\mvnw spring-boot:run
```

#### Via IDE (IntelliJ IDEA)
1. Abra o projeto como projeto Maven (File > Open...)
2. Configure o JDK 25 em File > Project Structure > Project SDK
3. Execute `CinemaManagementApplication.java`

#### Via IDE (VS Code)
1. Instale "Language Support for Java" e "Spring Boot Extension Pack"
2. Execute `CinemaManagementApplication.java` pelo botão Run

---

## Endpoints disponíveis

| Método | Rota | Autenticação | Descrição |
|--------|------|:------------:|-----------|
| `POST` | `/auth/login?username=<nome>` | ❌ | Gera token JWT |
| `GET` | `/movies` | ❌ | Lista filmes |
| `GET` | `/movies/{id}` | ✅ | Busca filme por ID |
| `POST` | `/movies` | ✅ | Cadastra filme |
| `GET` | `/sessions` | ✅ | Lista sessões |
| `POST` | `/sessions` | ✅ | Cria sessão |
| `GET` | `/tickets` | ✅ | Lista ingressos |
| `POST` | `/tickets` | ✅ | Compra ingresso |

---

## Testando os requisitos — Comandos e evidências

> **Pré-requisito**: suba os containers com `docker compose up --build` e aguarde a mensagem de inicialização.  
> Todos os comandos usam `-k` para aceitar o certificado self-signed.

---

### 1. Proxy Reverso

Verifica que o Nginx encaminha a requisição para a API Spring Boot.

```bash
curl -k https://localhost/movies
```

**Esperado**: resposta JSON vinda da API (lista de filmes, possivelmente `[]` se vazia).

---

### 2. HTTPS / SSL (Certificado Self-Signed)

```bash
curl -kv https://localhost/movies 2>&1 | grep -E "SSL|TLS|subject|issuer"
```

**Esperado**: informações do handshake TLS mostrando o certificado self-signed:
```
* SSL connection using TLSv1.3
* Server certificate:
*  subject: C=BR; ST=SP; L=SaoPaulo; O=CinemaManagement; CN=localhost
```

---

### 3. Redirect HTTP → HTTPS

```bash
curl -v http://localhost/movies 2>&1 | grep -E "< HTTP|Location"
```

**Esperado**:
```
< HTTP/1.1 301 Moved Permanently
< Location: https://localhost/movies
```

---

### 4. Rate Limiting (5 req/s + burst 10)

Usando o script de teste incluído no projeto:
```bash
bash test-rate-limit.sh
```

Ou manualmente, disparando 20 requisições simultâneas:
```bash
for i in $(seq 1 20); do curl -k -s -o /dev/null -w "Req $i: HTTP %{http_code}\n" https://localhost/movies & done; wait
```

**Esperado**: as primeiras ~11 requisições passam (HTTP 200 ou 401), as demais retornam **HTTP 429** (Too Many Requests).

---

### 5. Compressão Gzip

```bash
curl -k -H "Accept-Encoding: gzip" -I https://localhost/movies
```

**Esperado**: header `Content-Encoding: gzip` na resposta.

---

### 6. Cache de Respostas (GET)

**Primeira requisição (MISS):**
```bash
curl -k -I https://localhost/movies 2>&1 | grep X-Cache-Status
```
**Esperado**: `X-Cache-Status: MISS`

**Segunda requisição (HIT):**
```bash
curl -k -I https://localhost/movies 2>&1 | grep X-Cache-Status
```
**Esperado**: `X-Cache-Status: HIT`

**Com token JWT (BYPASS — requisições autenticadas não usam cache):**
```bash
TOKEN=$(curl -k -s -X POST "https://localhost/auth/login?username=admin")
curl -k -I -H "Authorization: Bearer $TOKEN" https://localhost/movies 2>&1 | grep X-Cache-Status
```
**Esperado**: `X-Cache-Status: BYPASS`

---

### 7. Headers de Segurança

```bash
curl -kI https://localhost/movies 2>&1 | grep -E "X-Content-Type|X-Frame|X-XSS"
```

**Esperado**:
```
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 1; mode=block
```

---

### 8. Limite de Payload (1MB)

Gera um payload maior que 1MB e envia via POST:
```bash
# Gera arquivo de ~2MB
dd if=/dev/zero bs=1M count=2 2>/dev/null | base64 > /tmp/big_payload.txt

curl -k -X POST https://localhost/movies \
  -H "Content-Type: application/json" \
  -d @/tmp/big_payload.txt \
  -w "\nHTTP Code: %{http_code}\n"
```

**Esperado**: **HTTP 413** (Request Entity Too Large).

---

### 9. Páginas de Erro Customizadas

**Erro 404 (rota inexistente):**
```bash
curl -k https://localhost/rota-inexistente
```
**Esperado**: HTML customizado com `<h1>404</h1>` e "Página não encontrada".

**Erro 500 (endpoint de teste):**
```bash
curl -k https://localhost/test/500
```
**Esperado**: HTML customizado com `<h1>50x</h1>` e "Erro interno do servidor".

---

### 10. Logs Estruturados

```bash
docker logs cinema-nginx 2>&1 | tail -5
```

**Esperado**: logs no formato estruturado:
```
IP: 172.18.0.1 | Metódo: GET | URI: /movies | Status: 200 | Tempo de resposta: 0.045s | Cache: HIT
```

---

### 11. Autenticação JWT (fluxo completo)

**Login (obter token):**
```bash
TOKEN=$(curl -k -s -X POST "https://localhost/auth/login?username=admin")
echo $TOKEN
```

**Acessar endpoint protegido com token:**
```bash
curl -k -H "Authorization: Bearer $TOKEN" https://localhost/sessions
```
**Esperado**: resposta JSON (lista de sessões).

**Acessar endpoint protegido sem token:**
```bash
curl -k https://localhost/sessions
```
**Esperado**: **HTTP 401** (Unauthorized).

---

### 12. Fluxo completo (criar filme → criar sessão → comprar ingresso)

```bash
# 1. Login
TOKEN=$(curl -k -s -X POST "https://localhost/auth/login?username=admin")

# 2. Criar filme
curl -k -X POST https://localhost/movies \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"title": "Interestelar", "duration": 169}'

# 3. Criar sessão para o filme (ajuste o movieId conforme retorno)
curl -k -X POST https://localhost/sessions \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"movieId": 1, "room": "Sala 1", "startsAt": "2026-12-31T20:00:00"}'

# 4. Comprar ingresso (ajuste o sessionId conforme retorno)
curl -k -X POST https://localhost/tickets \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"sessionId": 1, "seat": "A1", "customerName": "Lucas"}'

# 5. Listar ingressos
curl -k -H "Authorization: Bearer $TOKEN" https://localhost/tickets
```

---

## Arquivo `nginx.conf` comentado

O arquivo `nginx/nginx.conf` está **inteiramente comentado** linha a linha, explicando cada diretiva e sua função. Os comentários cobrem:

- `worker_processes` e `worker_connections`
- Formato de log estruturado (`log_format`)
- Rate limiting (`limit_req_zone`, `limit_req`)
- Compressão Gzip (`gzip`, `gzip_types`, `gzip_vary`)
- Cache de proxy (`proxy_cache_path`, `proxy_cache`, `proxy_cache_valid`)
- Redirect HTTP → HTTPS
- SSL/TLS (`ssl_certificate`, `ssl_protocols`, `ssl_ciphers`)
- Limite de payload (`client_max_body_size`)
- Headers de segurança (`X-Content-Type-Options`, `X-Frame-Options`, `X-XSS-Protection`)
- Proxy pass e headers de repasse (`proxy_set_header`)
- Páginas de erro customizadas (`error_page`)

---

## Estrutura do projeto

```
cinema-management/
├── docker-compose.yml          # Orquestração dos containers
├── Dockerfile                  # Build da API Spring Boot (multi-stage)
├── mvnw / mvnw.cmd             # Maven wrapper
├── pom.xml                     # Dependências do projeto
├── test-rate-limit.sh          # Script de teste de rate limiting
├── docs/
│   └── requirements.md         # Requisitos do sistema
├── nginx/
│   ├── Dockerfile              # Build do container Nginx
│   ├── nginx.conf              # Configuração completa (comentada)
│   ├── error_pages/
│   │   ├── 404.html            # Página de erro 404 customizada
│   │   └── 50x.html            # Página de erro 50x customizada
│   └── logs/
│       ├── access.log
│       └── error.log
└── src/
    └── main/
        └── java/org/example/cinemamanagement/
            ├── CinemaManagementApplication.java
            ├── infrastructure/security/   # JWT + Spring Security
            ├── movies/                    # Módulo de filmes
            ├── sessions/                  # Módulo de sessões
            ├── tickets/                   # Módulo de ingressos
            └── shared/                    # Exceções e handlers globais
```

---

## Autor

Lucas Oliveira Nadier
