# empregos

Plataforma de vagas de emprego full stack: empresas publicam vagas e gerenciam skills, candidatos navegam e se candidatam. Backend em **Spring Boot** (Java 17) com autenticação JWT, e front-end em **Angular** (standalone + signals) com Tailwind CSS.

## Stack

**Backend**
- Java 17 · Spring Boot 4 (Web, Security, Data JPA, Validation, HATEOAS)
- MySQL
- JWT (jjwt) para autenticação stateless
- ModelMapper

**Frontend** (`frontend/`)
- Angular 22 (standalone components, signals, `@if`/`@for`)
- Tailwind CSS v4
- TypeScript

## Funcionalidades

- Cadastro/login com dois perfis: **empresa** (`COMPANY`) e **candidato** (`CANDIDATE`)
- Autenticação via access/refresh token (JWT), com refresh automático no front
- Listagem e busca de vagas, com filtro por tipo e nível
- Empresas: publicar, editar e excluir vagas; gerenciar skills
- Candidatos: se candidatar a vagas
- Rotas protegidas por autenticação e por perfil (guards no front, `@PreAuthorize` no back)

## Estrutura do repositório

```
empregos/
├── src/                # Backend (Spring Boot)
├── pom.xml
└── frontend/           # Frontend (Angular)
    ├── src/app/
    │   ├── core/        # models, services, guards, interceptors
    │   ├── layout/       # shell/navbar
    │   ├── features/     # auth, jobs, skills
    │   └── shared/
    └── package.json
```

## Pré-requisitos

- Java 17+
- Node.js 20+ e npm
- MySQL rodando localmente

## Rodando o backend

O banco `empregos` é criado automaticamente na primeira execução. Configuração em `src/main/resources/application.properties` (usuário/senha do MySQL, secrets de JWT, paginação).

```bash
./mvnw spring-boot:run
```

A API sobe em `http://localhost:8080`.

## Rodando o frontend

```bash
cd frontend
npm install
npm start
```

A aplicação sobe em `http://localhost:4200` e já aponta para a API local (`src/environments/environment.ts`). CORS para essa origem já está liberado no backend (`CorsConfig`).

## API

Base path: `/api`

| Método | Rota | Descrição | Acesso |
|---|---|---|---|
| POST | `/auth/register` | Cria usuário (empresa ou candidato) | Público |
| POST | `/auth/login` | Autentica e retorna tokens | Público |
| POST | `/auth/refresh` | Renova o access token | Público |
| GET | `/auth/me` | Dados do usuário autenticado | Autenticado |
| GET | `/jobs` | Lista vagas | Público |
| GET | `/jobs/{id}` | Detalhe da vaga | Público |
| POST | `/jobs` | Cria vaga | Empresa |
| PUT | `/jobs/{id}` | Atualiza vaga | Empresa dona |
| DELETE | `/jobs/{id}` | Remove vaga | Empresa dona |
| GET | `/jobs/{id}/skills` | Skills da vaga | Público |
| POST | `/jobs/{id}/apply` | Candidata-se à vaga | Candidato |
| GET | `/skills` | Lista skills (paginado) | Público |
| POST/PUT/DELETE | `/skills{/id}` | Gerencia skills | Empresa |

## Build de produção

```bash
./mvnw clean package        # backend -> target/*.jar
cd frontend && npm run build # frontend -> frontend/dist/
```
