# Sistema de Gestão de Estacionamentos - Estapar

Sistema desenvolvido para gerenciar estacionamentos com controle de vagas, entrada/saída de veículos e faturamento.

## Tecnologias Utilizadas

- **Java** - Linguagem principal
- **Spring Boot 3.5** - Framework web
- **Spring Data JPA** - Persistência de dados
- **PostgreSQL** - Banco de dados
- **Flyway** - Migração de banco de dados
- **Docker & Docker Compose** - Containerização

## Funcionalidades

### Regras de Negócio Implementadas

1. **Preço Dinâmico por Lotação:**
    - < 25% lotação: 10% desconto
    - 25-50% lotação: preço normal
    - 50-75% lotação: 10% aumento
    - 75-100% lotação: 25% aumento

2. **Controle de Lotação:**
    - Setor com 100% de lotação é fechado para novos veículos

### Endpoints da API

#### Webhook (Recebe eventos do simulador)
- `POST /webhook` - Recebe eventos de entrada, estacionamento e saída

#### Consultas
- `POST /plate-status` - Status de um veículo por placa
- `POST /spot-status` - Status de uma vaga por coordenadas
- `GET /revenue` - Faturamento por data e setor

### Swagger UI
- Acesse a documentação da API em [http://localhost:3003/swagger-ui.html](http://localhost:3003/swagger-ui.html)

## Como Executar

### Pré-requisitos
- Docker e Docker Compose
- Java 21+ e Maven (para desenvolvimento local)

### Execução com Docker Compose

Execute o ambiente completo:
```bash
docker compose up -d
```
### Execução Local

1. Inicie o Postgres:
```bash
docker compose up postgres -d
```

2. Inicie o simulador:
```bash
docker compose up garage-simulator -d
```
 
3. Execute a aplicação:
```bash
mvn spring-boot:run
```
ou execute a classe `ParkingManagementApplication.java` na sua IDE.

## Estrutura do Projeto

- **Controller**: Endpoints REST e webhook
- **Service**: Lógica de negócio
- **External**: Integração com o simulador e outros serviços externos
- **Repository**: Acesso a dados
- **Model**: Entidades e DTOs
- **Exception**: Exceções personalizadas e tratamento de erros
- **Config**: Configurações da aplicação

## Testes

Execute os testes com:
```bash
mvn test
```

## Fluxo de Funcionamento

1. **Inicialização**: Sistema busca configuração da garagem via API do simulador
2. **Entrada**: Veículo entra na garagem (evento ENTRY)
3. **Estacionamento**: Veículo estaciona em vaga específica (evento PARKED)
    - Preço dinâmico é calculado baseado na lotação atual
4. **Saída**: Veículo sai da garagem (evento EXIT)
    - Preço final é calculado baseado no tempo estacionado
    - Receita é registrada
