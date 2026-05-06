# PrimeBarbeChatBot

Site e API de agendamento para barbearia com atendimento preparado para WhatsApp/IA em modo mock.

## Sobre o projeto

O PrimeBarbeChatBot permite que clientes marquem horarios por uma interface web responsiva ou testem um fluxo de conversa simulado. O telefone e a identidade principal do cliente, o que facilita a futura integracao com WhatsApp.

## Funcionalidades

- Cadastro e atualizacao de cliente por telefone.
- Criacao de agendamentos.
- Consulta de horarios disponiveis.
- Bloqueio de conflito de horario na aplicacao e no banco.
- Horario de funcionamento das 09:00 as 18:00, com 12:00 reservado para almoco.
- Chat deterministico para consultas e agendamentos.
- Endpoint mock para webhook de WhatsApp.
- Site responsivo servido pelo proprio Spring Boot.

## Tecnologias

- Java 21
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- H2 Database
- Bean Validation
- Maven
- HTML, CSS e JavaScript

## Como usar

Entre na pasta do projeto Spring:

```bash
cd primebarbechatbot
```

Execute a aplicacao:

```bash
./mvnw spring-boot:run
```

No Windows:

```bat
mvnw.cmd spring-boot:run
```

Depois acesse:

```text
http://localhost:8080
```

## Exemplos de API

### Criar cliente

```http
POST /clientes
Content-Type: application/json
```

```json
{
  "nome": "Gustavo",
  "telefone": "11999999999"
}
```

### Criar agendamento

```http
POST /agendamentos
Content-Type: application/json
```

```json
{
  "dataHora": "2026-05-07T15:00:00",
  "servico": "Corte de cabelo",
  "telefoneCliente": "11999999999",
  "nomeCliente": "Gustavo"
}
```

### Consultar horarios disponiveis

```http
GET /agendamentos/disponiveis?data=2026-05-07
```

### Chat simples

```http
POST /chat
Content-Type: application/json
```

```json
{
  "telefone": "11999999999",
  "mensagem": "Quero agendar dia 2026-05-07 as 15:00"
}
```

### WhatsApp mock

```http
POST /webhooks/whatsapp/mock
Content-Type: application/json
```

```json
{
  "telefone": "11999999999",
  "mensagem": "Quais horarios amanha de manha?"
}
```

## Proximos passos

- Integrar WhatsApp Cloud API.
- Trocar o chat deterministico por um provedor de IA.
- Migrar H2 para MySQL ou PostgreSQL.
- Criar painel administrativo para a barbearia.
- Adicionar escolha de barbeiro.
