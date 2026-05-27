# PoolFy API

API inicial em **Java + Spring Boot**.

## Implementações

- Autenticação inicial
  - cadastro
  - login
  - solicitação de recuperação de senha
- Dashboard
  - resumo geral
  - atividades recentes
  - alertas
- Piscinas
  - listar
  - detalhar
  - cadastrar
  - editar
  - deletar
- Manutenções
  - registrar manutenção
  - listar por piscina
  - buscar por período
  - cálculo automático de produtos
- Agenda de manutenção
  - criar
  - listar por piscina
  - atualizar
  - desativar
- Produtos / Dosagem
  - CRUD de produtos
  - cadastro/listagem de marcas
- Estoque
  - visão do estoque atual
  - entrada e saída
  - baixa automática ao registrar manutenção
- Configurações
  - perfil do usuário
  - preferências
- Alertas
  - listar
  - marcar como lido

## Tecnologias

- Java 21
- Spring Boot 3
- Spring Web
- Spring Data JPA
- Spring Validation
- Spring Security
- MySQL

## Estrutura resumida

- `controller` -> endpoints REST
- `service` -> regras de negócio
- `repository` -> acesso ao banco
- `entity` -> mapeamento das tabelas do SQL
- `dto` -> entrada e saída da API
- `config` -> configuração de segurança

## Banco de dados

Use o `script.sql` que você já possui para criar a base `pool_system`.

Depois ajuste o arquivo:

- `src/main/resources/application.yml`

Exemplo:

```yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/pool_system?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Sao_Paulo
    username: root
    password: SUA_SENHA
```

## Como rodar

### 1. Pré-requisitos

- Java 21
- Maven 3.9+
- MySQL 8+

### 2. Criar banco

Execute o seu `script.sql`.

### 3. Ajustar conexão

Edite `application.yml`.

### 4. Subir a aplicação

```bash
mvn spring-boot:run
```

Ou:

```bash
mvn clean package
java -jar target/poolfy-api-0.0.1-SNAPSHOT.jar
```

## Endpoints principais

### Auth

- `POST /api/auth/register`
- `POST /api/auth/login`
- `POST /api/auth/forgot-password`

### Dashboard

- `GET /api/dashboard/{userId}`

### Usuário / Configurações

- `GET /api/users/{userId}/profile`
- `PUT /api/users/{userId}/profile`
- `GET /api/users/{userId}/preferences`
- `PUT /api/users/{userId}/preferences`

### Piscinas

- `GET /api/pools?userId=1`
- `GET /api/pools/{poolId}`
- `POST /api/pools`
- `PUT /api/pools/{poolId}`
- `DELETE /api/pools/{poolId}`

### Manutenções

- `GET /api/maintenances?userId=1`
- `GET /api/maintenances/pool/{poolId}`
- `GET /api/maintenances/search?userId=1&startDate=2026-04-01&endDate=2026-04-30`
- `POST /api/maintenances`
- `GET /api/maintenances/calculate?poolId=1&productId=2`

### Agenda

- `GET /api/schedules/pool/{poolId}`
- `POST /api/schedules`
- `PUT /api/schedules/{scheduleId}`
- `PATCH /api/schedules/{scheduleId}/deactivate`

### Produtos

- `GET /api/products?userId=1`
- `GET /api/products/{productId}`
- `POST /api/products`
- `PUT /api/products/{productId}`
- `DELETE /api/products/{productId}`

### Marcas

- `GET /api/brands?userId=1`
- `POST /api/brands`

### Estoque

- `GET /api/inventory?userId=1`
- `POST /api/inventory/movements`

### Alertas

- `GET /api/alerts?userId=1`
- `PATCH /api/alerts/{alertId}/read`

## Exemplos de payload

### Cadastro

```json
{
  "firstName": "Vinicius",
  "lastName": "Silva",
  "email": "vinicius@email.com",
  "password": "123456",
  "confirmPassword": "123456",
  "userType": "PROFESSIONAL",
  "experienceLevel": "INTERMEDIATE"
}
```

### Cadastro de piscina

```json
{
  "userId": 1,
  "name": "Piscina Casa",
  "widthM": 4.0,
  "lengthM": 8.0,
  "depthM": 1.5,
  "shape": "RECTANGULAR",
  "notes": "Área externa"
}
```

### Cadastro de produto

```json
{
  "userId": 1,
  "brandId": 1,
  "name": "Cloro Premium",
  "productType": "CHLORINE",
  "dosagePerM3": 4.5,
  "dosageUnit": "G",
  "description": "Uso semanal",
  "lowStockAlertEnabled": true,
  "lowStockThreshold": 500
}
```

### Movimento de estoque

```json
{
  "userId": 1,
  "productId": 1,
  "movementType": "IN",
  "quantity": 1000,
  "unit": "G",
  "reason": "Compra mensal"
}
```

### Registro de manutenção

```json
{
  "userId": 1,
  "poolId": 1,
  "maintenanceDate": "2026-04-14",
  "maintenanceType": "COMPLETE",
  "description": "Limpeza completa com correção química",
  "nextMaintenanceDate": "2026-04-21",
  "products": [
    {
      "productId": 1,
      "appliedQuantity": 150
    }
  ]
}
```

## Observação importante

O fluxo de `forgot-password` foi implementado apenas como endpoint inicial de solicitação, porque o banco ainda não possui suporte para token de recuperação.