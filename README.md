# API Biblioteca

API REST de biblioteca (autores, livros, usuários e empréstimos) feita com Spring Boot 4, Java 25, JPA/H2 e autenticação JWT.

## Como rodar

```bash
./mvnw spring-boot:run
```

A API sobe em `http://localhost:8080`. O console do H2 fica em `/h2-console` (JDBC URL: `jdbc:h2:mem:biblioteca`, usuário `sa`, sem senha).

Um admin é criado automaticamente: `admin@biblioteca.com` / `admin123` (só para desenvolvimento; troque por variáveis de ambiente `ADMIN_EMAIL`/`ADMIN_SENHA`).

## Endpoints

| Método | Rota | Quem acessa |
|---|---|---|
| POST | `/auth/registrar` | público |
| POST | `/auth/login` | público |
| GET | `/auth/me` | logado |
| GET | `/livros`, `/livros/{id}`, `/livros/autor/{id}` | público |
| GET | `/autores`, `/autores/{id}` | público |
| POST/DELETE | `/livros`, `/autores` | ADMIN |
| GET/DELETE | `/usuarios` | ADMIN |
| POST | `/emprestimos` (`{"livroId": 1}`) | logado |
| GET | `/emprestimos/meus` | logado |
| GET | `/emprestimos`, `/emprestimos/{id}` | ADMIN |
| PUT | `/emprestimos/{id}/devolver` | ADMIN |

### Testando na mão

```bash
# 1. login como admin -> copie o "token" da resposta
curl -X POST localhost:8080/auth/login -H "Content-Type: application/json" \
  -d '{"email":"admin@biblioteca.com","senha":"admin123"}'

# 2. use o token no header Authorization
curl -X POST localhost:8080/autores -H "Authorization: Bearer SEU_TOKEN" \
  -H "Content-Type: application/json" -d '{"nome":"Machado de Assis","nacionalidade":"Brasileiro"}'
```

---

# Guia de estudo: JWT

## O problema

HTTP não tem memória: cada requisição chega "sozinha". Como o servidor sabe que a requisição de agora vem da mesma pessoa que fez login há 5 minutos?

- **Sessão (jeito antigo):** o servidor guarda "sessão 123 = Maria" na memória e manda um cookie com `123`. Funciona, mas o servidor precisa lembrar de todo mundo — difícil de escalar para vários servidores.
- **Token (JWT):** o servidor entrega um "crachá" assinado dizendo "esta é a Maria, id 5, papel USER, válido até 15h". O cliente manda o crachá em toda requisição. O servidor **não guarda nada**: só confere a assinatura. Isso é o que chamamos de *stateless*.

## Anatomia de um JWT

Um JWT tem 3 partes em Base64, separadas por ponto: `HEADER.PAYLOAD.ASSINATURA`

```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1Iiwicm9sZXMiOlsiVVNFUiJdLCJleHAiOjE3...}.Xk3f9...
```

1. **Header** — o algoritmo: `{"alg": "HS256"}`
2. **Payload (claims)** — os dados: `{"sub": "5", "email": "maria@...", "roles": ["USER"], "exp": 1790000000}`
3. **Assinatura** — `HMAC-SHA256(header + "." + payload, chaveSecreta)`

Cole um token em [jwt.io](https://jwt.io) e veja: **qualquer um consegue ler o payload**. JWT não é criptografado, é *assinado*. Por isso nunca coloque senha ou dados sensíveis nas claims.

**Por que ninguém consegue falsificar?** Se um atacante trocar `"roles":["USER"]` por `"roles":["ADMIN"]`, a assinatura deixa de bater, porque ele não tem a chave secreta para recalcular. O servidor rejeita com 401.

## O fluxo neste projeto

```
Cliente                                   API
  | POST /auth/login {email, senha}  -->   |  AuthService: busca usuário, confere BCrypt
  |                                        |  TokenService: monta claims e assina
  | <-- {"token": "eyJ...", "expiraEm"}    |
  |                                        |
  | GET /auth/me                           |
  | Authorization: Bearer eyJ...     -->   |  Filtro do Spring Security (oauth2ResourceServer):
  |                                        |   1. extrai o token do header
  |                                        |   2. JwtDecoder confere assinatura e "exp"
  |                                        |   3. converte "roles" em ROLE_USER/ROLE_ADMIN
  |                                        |   4. confere as regras do SecurityConfig
  | <-- 200 / 401 (token ruim) / 403 (sem permissão)
```

## Onde está cada peça no código

| Arquivo | Papel |
|---|---|
| `security/SecurityConfig.java` | Regras de acesso por rota, `PasswordEncoder` (BCrypt), `JwtEncoder`/`JwtDecoder`, mapeamento de `roles` |
| `security/TokenService.java` | Gera e assina o token no login |
| `service/AuthService.java` | Registro (salva hash da senha) e login (confere senha) |
| `controller/AuthController.java` | Endpoints `/auth/*` |
| `controller/EmprestimoController.java` | Usa `@AuthenticationPrincipal Jwt` para saber quem está logado |
| `security/AdminInicializador.java` | Cria o admin ao subir a aplicação |

## Conceitos importantes

- **401 vs 403:** 401 = "não sei quem você é" (sem token, token inválido/expirado). 403 = "sei quem você é, mas você não pode" (USER tentando rota de ADMIN).
- **BCrypt:** a senha nunca é salva em texto puro. `passwordEncoder.encode("123456")` gera um hash diferente a cada vez (usa *salt*), e `matches()` confere. Nem quem tem acesso ao banco descobre a senha.
- **Nunca confie no cliente para identidade:** antes, `POST /emprestimos` recebia `usuarioId` no corpo — qualquer um poderia pegar livro em nome de outro. Agora o id vem do `subject` do token, que o cliente não consegue forjar.
- **Mensagem de erro genérica no login:** "Email ou senha inválidos" para os dois casos, para não revelar quais emails existem.
- **CSRF desabilitado:** CSRF explora cookies que o navegador envia sozinho. Como o token vai no header `Authorization` (e não em cookie), esse ataque não se aplica.
- **Chave secreta:** HS256 exige ≥ 32 bytes. Em produção, nunca deixe a chave no código: use a variável de ambiente `JWT_SECRET`.
- **Limitação do JWT:** não dá para "invalidar" um token antes de expirar (o servidor não guarda estado). Por isso a validade é curta (60 min). Sistemas reais usam *refresh tokens* para renovar sem pedir senha de novo.

## Próximos passos para praticar

1. Implementar *refresh token* (`POST /auth/refresh`).
2. Deixar o ADMIN criar empréstimo para outro usuário.
3. Retornar JSON no padrão `ErroResponse` também nos erros 401/403 (dica: `AuthenticationEntryPoint` e `AccessDeniedHandler`).
4. Paginação em `GET /livros` com `Pageable`.
5. Trocar H2 por PostgreSQL com Docker.
