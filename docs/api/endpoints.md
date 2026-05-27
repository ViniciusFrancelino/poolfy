# PoolFy API

## Base URL

Ao iniciar a aplicação:

```text
http://localhost:8080
```

---

## Como testar no Postman

Crie uma collection no Postman usando a base URL:

```text
http://localhost:8080
```

### Ordem recomendada de testes

1. cadastrar usuário
2. fazer login
3. cadastrar marca
4. cadastrar produto
5. cadastrar piscina
6. cadastrar agenda
7. registrar entrada de estoque
8. calcular dosagem
9. registrar manutenção
10. consultar dashboard, alertas e listas

---

# Endpoints

## 1. Auth

### POST `/api/auth/register`

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

### POST `/api/auth/login`

```json
{
  "email": "vinicius@email.com",
  "password": "123456",
  "rememberSession": true
}
```

### POST `/api/auth/forgot-password`

```json
{
  "email": "vinicius@email.com"
}
```

---

## 2. Usuário

### GET `/api/users/1/profile`

Sem body.

### PUT `/api/users/1/profile`

```json
{
  "firstName": "Vinicius",
  "lastName": "Francelino",
  "email": "vinicius@email.com",
  "password": "novaSenha123"
}
```

### GET `/api/users/1/preferences`

Sem body.

### PUT `/api/users/1/preferences`

```json
{
  "preferredUserType": "PROFESSIONAL",
  "preferredExperienceLevel": "ADVANCED"
}
```

---

## 3. Dashboard

### GET `/api/dashboard/1`

Sem body.

---

## 4. Marcas

### GET `/api/brands?userId=1`

Sem body.

### POST `/api/brands`

```json
{
  "userId": 1,
  "brandName": "Hth"
}
```

---

## 5. Produtos

### GET `/api/products?userId=1`

Sem body.

### GET `/api/products/1`

Sem body.

### POST `/api/products`

```json
{
  "userId": 1,
  "brandId": 1,
  "name": "Cloro Granulado",
  "productType": "CHLORINE",
  "dosagePerM3": 4.5,
  "dosageUnit": "G",
  "description": "Cloro para tratamento semanal",
  "lowStockAlertEnabled": true,
  "lowStockThreshold": 1000
}
```

### PUT `/api/products/1`

```json
{
  "userId": 1,
  "brandId": 1,
  "name": "Cloro Granulado Premium",
  "productType": "CHLORINE",
  "dosagePerM3": 5.0,
  "dosageUnit": "G",
  "description": "Versão atualizada do produto",
  "lowStockAlertEnabled": true,
  "lowStockThreshold": 800
}
```

### DELETE `/api/products/1`

Sem body.

---

## 6. Piscinas

### GET `/api/pools?userId=1`

Sem body.

### GET `/api/pools/1`

Sem body.

### GET `/api/pools/1/details`

Sem body.

### POST `/api/pools`

```json
{
  "userId": 1,
  "name": "Piscina Casa",
  "widthM": 4.0,
  "lengthM": 8.0,
  "depthM": 1.5,
  "shape": "RECTANGULAR",
  "notes": "Piscina principal da residência",
  "nextMaintenanceDate": "2026-04-20"
}
```

### PUT `/api/pools/1`

```json
{
  "userId": 1,
  "name": "Piscina Casa Atualizada",
  "widthM": 4.0,
  "lengthM": 8.5,
  "depthM": 1.6,
  "shape": "RECTANGULAR",
  "notes": "Piscina com borda reformada",
  "nextMaintenanceDate": "2026-04-25"
}
```

### DELETE `/api/pools/1`

Sem body.

---

## 7. Agenda de manutenção

### GET `/api/schedules/pool/1`

Sem body.

### POST `/api/schedules`

```json
{
  "poolId": 1,
  "userId": 1,
  "scheduleType": "AUTOMATIC",
  "frequencyDays": 7,
  "nextScheduledDate": "2026-04-21",
  "active": true,
  "notes": "Manutenção semanal automática"
}
```

### PUT `/api/schedules/1`

```json
{
  "poolId": 1,
  "userId": 1,
  "scheduleType": "MANUAL",
  "frequencyDays": 15,
  "nextScheduledDate": "2026-04-30",
  "active": true,
  "notes": "Agenda ajustada manualmente"
}
```

### PATCH `/api/schedules/1/deactivate`

Sem body.

---

## 8. Estoque

### GET `/api/inventory?userId=1`

Sem body.

### POST `/api/inventory/movements`

Entrada de estoque:

```json
{
  "userId": 1,
  "productId": 1,
  "movementType": "IN",
  "quantity": 5000,
  "unit": "G",
  "reason": "Compra de fornecedor",
  "maintenanceId": null
}
```

### POST `/api/inventory/movements`

Saída manual de estoque:

```json
{
  "userId": 1,
  "productId": 1,
  "movementType": "OUT",
  "quantity": 200,
  "unit": "G",
  "reason": "Uso manual fora de manutenção",
  "maintenanceId": null
}
```

---

## 9. Manutenções

### GET `/api/maintenances?userId=1`

Sem body.

### GET `/api/maintenances/pool/1`

Sem body.

### GET `/api/maintenances/search?userId=1&startDate=2026-04-01&endDate=2026-04-30`

Sem body.

### GET `/api/maintenances/calculate?poolId=1&productId=1`

Sem body.

### POST `/api/maintenances`

Exemplo com produto aplicado:

```json
{
  "userId": 1,
  "poolId": 1,
  "maintenanceDate": "2026-04-14",
  "maintenanceType": "CHEMICAL_TREATMENT",
  "description": "Aplicação de cloro e ajuste químico",
  "nextMaintenanceDate": "2026-04-21",
  "products": [
    {
      "productId": 1,
      "appliedQuantity": 216,
      "notes": "Aplicado após limpeza"
    }
  ]
}
```

### POST `/api/maintenances`

Exemplo de manutenção sem produto:

```json
{
  "userId": 1,
  "poolId": 1,
  "maintenanceDate": "2026-04-14",
  "maintenanceType": "CLEANING",
  "description": "Aspiração e limpeza física",
  "nextMaintenanceDate": "2026-04-18",
  "products": []
}
```

---

## 10. Alertas

### GET `/api/alerts?userId=1`

Sem body.

### PATCH `/api/alerts/1/read`

Sem body.

---

## Enums válidos

### `userType`

```text
PROFESSIONAL
COMMON
```

### `experienceLevel`

```text
BEGINNER
INTERMEDIATE
ADVANCED
```

### `shape`

```text
RECTANGULAR
ROUND
OVAL
IRREGULAR
```

### `scheduleType`

```text
AUTOMATIC
MANUAL
```

### `productType`

```text
CHLORINE
ALGAECIDE
CLARIFIER
PH_INCREASER
PH_REDUCER
ALUMINUM_SULFATE
OTHER
```

### `dosageUnit`

```text
G
KG
ML
L
```

### `movementType`

```text
IN
OUT
```

### `maintenanceType`

```text
CLEANING
CHEMICAL_TREATMENT
COMPLETE
```

---

## Fluxo mínimo para validação completa

1. `POST /api/auth/register`
2. `POST /api/brands`
3. `POST /api/products`
4. `POST /api/pools`
5. `POST /api/schedules`
6. `POST /api/inventory/movements`
7. `GET /api/maintenances/calculate?poolId=1&productId=1`
8. `POST /api/maintenances`
9. `GET /api/dashboard/1`
10. `GET /api/alerts?userId=1`

---

## Observações finais

- Os IDs dos exemplos assumem banco vazio e inserts iniciais começando em `1`.
- O endpoint de recuperação de senha está apenas como fluxo inicial, sem token persistido no banco.
- Para registrar manutenção com produto, é necessário já existir:
  - usuário
  - marca
  - produto
  - piscina
  - estoque disponível
- Caso use MariaDB, prefira o driver e dialect próprios do MariaDB.
- Caso use MySQL, ajuste URL, driver e dialect conforme o banco.

---

## Estrutura esperada do projeto

```text
poolfy-api/
├── pom.xml
├── README.md
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/poolfy/api/
│   │   └── resources/
│   │       └── application.yml
└── script.sql
```

---

## Uso recomendado

Este README pode ser usado para:

- subir o projeto localmente
- configurar o banco
- importar e testar no Postman
- servir como documentação inicial da API

