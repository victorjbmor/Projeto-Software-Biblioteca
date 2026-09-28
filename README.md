# API Biblioteca

API REST para gestão de biblioteca — cadastro de autores, livros e usuários, controle de empréstimos e devoluções — com autenticação JWT e controle de acesso por papéis.

![Java](https://img.shields.io/badge/Java-25-orange) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1-6DB33F) ![Spring Security](https://img.shields.io/badge/Spring%20Security-JWT-6DB33F) ![Testes](https://img.shields.io/badge/testes-16%20passando-brightgreen)

## Tecnologias

- **Java 25** e **Spring Boot 4.1**
- **Spring Web MVC** — API REST
- **Spring Data JPA / Hibernate** — persistência
- **Spring Security + OAuth2 Resource Server** — autenticação JWT (HS256) e autorização por papéis
- **Bean Validation** — validação de entrada
- **H2** — banco em memória
- **JUnit 5, Mockito e MockMvc** — testes unitários e de integração
- **Maven**

## Funcionalidades

- CRUD de autores, livros e usuários
- Empréstimo de livros com prazo de devolução de 14 dias
- Regras de negócio: um livro não pode ser emprestado duas vezes ao mesmo tempo, um empréstimo não pode ser devolvido duas vezes, e ISBN e e-mail são únicos
- Cadastro e login com senha armazenada como hash **BCrypt**
- Autenticação **stateless** via token JWT
- Dois papéis de acesso: **USER** (consulta o acervo e faz empréstimos) e **ADMIN** (gerencia o acervo, os usuários e as devoluções)
- Respostas de erro padronizadas em JSON (400, 401, 403, 404, 409)

## Arquitetura

Organizada em camadas, cada uma com uma responsabilidade:

```
controller/   → recebe requisições HTTP e converte entre DTO e entidade
service/      → regras de negócio e controle transacional (@Transactional)
repository/   → acesso a dados com Spring Data JPA
model/        → entidades JPA com restrições de integridade
dto/          → contratos de entrada e saída da API (Java records)
security/     → configuração do Spring Security, emissão e validação de JWT
exception/    → exceções de domínio e tratamento global (@RestControllerAdvice)
```

### Decisões técnicas

- **A identidade do usuário vem do token.** A requisição de empréstimo não recebe o id do usuário no corpo: ele é lido do `subject` do JWT, o que impede que alguém crie empréstimos em nome de outra pessoa.
- **DTOs separados das entidades.** A API nunca expõe entidades JPA nem o hash da senha.
- **Regras de domínio dentro da entidade.** A validação de devolução fica em `Emprestimo.devolver()`, de modo que o objeto nunca chega a um estado inválido.
- **Login com mensagem de erro genérica.** A resposta é a mesma para e-mail inexistente e senha errada, evitando que se descubra quais e-mails estão cadastrados.
- **Segredos por variável de ambiente.** A chave do JWT e as credenciais do admin vêm de `JWT_SECRET`, `ADMIN_EMAIL` e `ADMIN_SENHA`.

## Como executar

Pré-requisito: JDK 25.

```bash
./mvnw spring-boot:run
```

A API sobe em `http://localhost:8080`. Em ambiente de desenvolvimento, um administrador é criado automaticamente (`admin@biblioteca.com` / `admin123`).

Para rodar os testes:

```bash
./mvnw test
```

## Endpoints

| Método | Rota | Acesso |
|---|---|---|
| POST | `/auth/registrar` | público |
| POST | `/auth/login` | público |
| GET | `/auth/me` | autenticado |
| GET | `/livros`, `/livros/{id}`, `/livros/autor/{id}` | público |
| GET | `/autores`, `/autores/{id}` | público |
| POST, DELETE | `/livros`, `/autores` | ADMIN |
| GET, DELETE | `/usuarios` | ADMIN |
| POST | `/emprestimos` | autenticado |
| GET | `/emprestimos/meus` | autenticado |
| GET | `/emprestimos`, `/emprestimos/{id}` | ADMIN |
| PUT | `/emprestimos/{id}/devolver` | ADMIN |

### Exemplo de uso

```bash
# Login: a resposta traz o token
curl -X POST localhost:8080/auth/login -H "Content-Type: application/json" \
  -d '{"email":"admin@biblioteca.com","senha":"admin123"}'
```

```json
{ "token": "eyJhbGciOiJIUzI1NiJ9...", "tipo": "Bearer", "expiraEm": "2026-09-28T18:45:58Z" }
```

```bash
# Requisição autenticada
curl -X POST localhost:8080/autores -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"nome":"Machado de Assis","nacionalidade":"Brasileiro"}'
```

Exemplo de erro de regra de negócio:

```json
{ "status": 409, "error": "Conflict", "message": "Livro 'Dom Casmurro' ja esta emprestado" }
```

## Testes

- **Unitários** (JUnit 5 + Mockito): regras de negócio dos services, como prazo de devolução, livro já emprestado, devolução duplicada e recurso inexistente
- **Integração** (MockMvc + contexto Spring completo): fluxo de registro, login e acesso autenticado; respostas 401 para requisição sem token ou com token inválido; resposta 403 para usuário sem permissão

## Próximos passos

- Refresh token
- Paginação e filtros nas listagens
- PostgreSQL com Docker e migrações com Flyway
- Documentação OpenAPI/Swagger

## Autor

**Victor** — [github.com/victorjbmor](https://github.com/victorjbmor)
