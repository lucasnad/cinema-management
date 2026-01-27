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

O foco está na modelagem correta do domínio e na organização arquitetural, não em funcionalidades completas de negócio (como pagamento ou autenticação).

---

## Estilo arquitetural adotado

O sistema foi desenvolvido como um **monólito modular**, utilizando:

- Separação por domínios de negócio (Movies, Sessions, Tickets)
- Conceitos inspirados em DDD (modelo de domínio rico e separação por contexto)
- Arquitetura em camadas com **Ports and Adapters (Hexagonal Architecture)**

Cada módulo possui:

- Camada de domínio (regras de negócio)
- Camada de aplicação (casos de uso)
- Portas (interfaces)
- Adapters (REST e persistência)

---

## Diagrama da arquitetura

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
                    +------------+--------------+
                                 |
                                 v
                    +---------------------------+
                    |        Domain Layer       |
                    |---------------------------|
                    | Movie                    |
                    | Session                  |
                    +------------+--------------+
                                 |
                                 v
                    +---------------------------+
                    |    Ports (Interfaces)     |
                    |---------------------------|
                    | MovieRepositoryPort       |
                    | SessionRepositoryPort     |
                    | MovieQueryPort            |
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

        ---------------------------------------------------------

        Comunicação interna entre módulos (dentro do monólito):

        CreateSessionUseCase  --(MovieQueryPort)-->  GetMovieUseCase
        (ligação feita via Bean de configuração do Spring)



---

## Principais decisões arquiteturais

### 1. Monólito modular
Foi escolhido um monólito modular em vez de microserviços para reduzir complexidade operacional e focar na organização interna do código.

### 2. Separação por domínios
Os módulos **movies** e **sessions** representam contextos distintos do domínio do cinema, reduzindo acoplamento e facilitando evolução futura.

### 3. Ports and Adapters
A comunicação entre camadas ocorre via interfaces (ports), permitindo trocar implementações de infraestrutura sem impactar o domínio.

### 4. Modelo de domínio rico
As entidades possuem validações e métodos de criação, garantindo que objetos nunca existam em estado inválido.

---

## Alternativas consideradas

//TODO

## Impacto das decisões

As escolhas arquiteturais resultaram em:

- Código mais organizado e modular
- Maior facilidade de testes unitários
- Possibilidade de migrar partes do sistema para serviços independentes no futuro
- Separação clara entre regras de negócio e infraestrutura

---

## Instruções para execução

1. Clonar o repositório
2. Executar a aplicação Spring Boot (classe CinemaManagementSystemApplication) ou rodar mvn spring-boot:run na raiz do projeto.
3. O banco H2 é iniciado automaticamente em memória

### Endpoints disponíveis

**Movies**
- POST `/movies`
- GET `/movies`
- GET `/movies/{id}`

**Sessions**
- POST `/sessions`
- GET `/sessions`

---

## Autor

Lucas Oliveira Nadier

