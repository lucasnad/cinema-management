# Requisitos do Sistema – Gestão de Cinema

## 1. Escopo do sistema
Sistema backend para gestão de sessões e venda de ingressos de um cinema.
O sistema permite cadastrar filmes, criar sessões e realizar a compra de ingressos,
aplicando regras de negócio simples.

## 2. Entidades principais
- Movie
    - id
    - title
    - duration

- Session
    - id
    - movieId
    - room
    - startsAt

- Ticket
    - id
    - sessionId
    - seat
    - customerName

## 3. Regras de negócio
- Não é permitido vender dois ingressos para o mesmo assento na mesma sessão.
- Não é permitido vender ingressos após o início da sessão.
- A sessão deve existir para que a compra do ingresso seja realizada.
- O filme associado à sessão deve existir.

## 4. Casos de uso principais
- Cadastrar filme
- Criar sessão para um filme
- Listar sessões disponíveis
- Comprar ingresso para uma sessão

## 5. Interfaces de entrada (API REST)
- POST /movies
- POST /sessions
- GET /sessions
- POST /tickets
