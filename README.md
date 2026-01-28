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
                    |  - ListSessionsUseCase  
                    | Tickets:                  |
                    |  - SellTicketUseCase      |
                    |
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
                    |                           |
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
        SellTicketUseCase --(SessionQueryPort)--> GetSessionUseCase

        (ligação feita via Bean de configuração do Spring)
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

---
## Alternativas consideradas
Durante a definição da arquitetura, foram consideradas abordagens mais complexas, porém descartadas devido ao escopo acadêmico do projeto e ao foco na organização interna do código.

| Alternativa | Motivo para não usar                                                                                                                                                                                                                        |
|-------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| SOA (Service-Oriented Architecture) | Exigiria comunicação entre serviços separados, aumentando a complexidade de implantação e integração, o que foge do escopo de um projeto individual. Do ponto de vista operacional seria muito complexo pra resolver um problema simples.   |
| Microserviços | Introduz complexidade operacional (deploy, rede, resiliência) desnecessária para o tamanho do sistema                                                                                                                                       |
| Arquitetura em camadas tradicional | Aumenta o acoplamento entre regras de negócio e infraestrutura, dificultando testes e evolução do domínio  (controller -> service -> repository -> banco). Classe service acaba concentrando demais funcionalidade e acopla muito o sistema |
| CRUD anêmico | É quando a regra fica toda em serviços. Isso deixa as entidades sem comportamento. Da forma que fiz o dominío tem força e independência.                                                                                                    |

## Impacto das decisões
As escolhas arquiteturais resultaram em:
- Código mais organizado e modular
- Maior facilidade de testes unitários
- Possibilidade de migrar partes do sistema para serviços independentes no futuro
- Separação clara entre regras de negócio e infraestrutura
---

## Principais regras de negócio implementadas

- Um ingresso não pode ser vendido para uma sessão que já iniciou
- Um mesmo assento não pode ser vendido duas vezes para a mesma sessão
- Uma sessão só pode ser criada para um filme existente
- Entidades do domínio nunca são criadas em estado inválido

## Instruções para execução
Siga as instruções abaixo conforme seu ambiente e preferência de IDE/terminal.

#### Requisitos
- Java 25 instalado.
- Variável de ambiente `JAVA_HOME` apontando para o JDK 25.
- O banco H2 é iniciado automaticamente em memória quando a aplicação é executada.

Executando na sua IDE
#### VS Code
  1. Abra a pasta do projeto no VS Code.
  2. Instale a extensão "Language Support for Java(TM)" e o "Spring Boot Extension Pack" (opcional, mas recomendado).
  3. Abra `src/main/java/org/example/cinemamanagement/CinemaManagementApplication.java` e use o botão Run acima do método `main` ou use a paleta de comandos Java para executar a classe principal.
  4. Alternativamente, use o Spring Boot Dashboard para iniciar a aplicação.

#### IntelliJ IDEA
  1. Abra o projeto como um projeto Maven (File > Open... > selecione a pasta do projeto).
  2. Configure o JDK do projeto para Java 25 em File > Project Structure > Project SDK.
  3. Localize `src/main/java/org/example/cinemamanagement/CinemaManagementApplication.java`.
  4. Clique com o botão direito na classe -> Run 'CinemaManagementApplication' ou crie uma Run Configuration do tipo 'Application' apontando para `org.example.cinemamanagement.CinemaManagementApplication`.

#### Executando via terminal
- Windows (PowerShell, bash e CMD)
  - Para compilar e rodar diretamente com o wrapper do Maven:
```bash
# Na raiz do projeto
.\cinema-management\mvnw spring-boot:run
```
Observações
- O banco H2 utilizado pelo projeto inicia em memória, dados não são persistidos.

### Endpoints disponíveis

**Movies**
- POST `/movies`
- GET `/movies`
- GET `/movies/{id}`

**Sessions**
- POST `/sessions`
- GET `/sessions`

**Sessions**
- POST `/tickets`
- GET `/tickets`

---
## Autor
Lucas Oliveira Nadier
