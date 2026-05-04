# 💈 PrimeBarbeChatBot

API de agendamento para barbearia com chatbot inteligente, desenvolvida em **Java + Spring Boot**.

---

##  Sobre o projeto

O **PrimeBarbeChatBot** é uma API que simula um sistema real de agendamento de clientes, permitindo:

* Cadastro de clientes
* Criação de agendamentos
* Controle de conflitos de horário
* Consulta de horários disponíveis
* Interação via chatbot (linguagem natural)

---

##  Funcionalidades

###  Agendamentos

* Criar agendamento
* Listar agendamentos
* Evitar conflitos de horário
* Sugerir horários disponíveis automaticamente

###  Chatbot

* Entende frases como:

    * "Quero agendar dia 2026-05-05 às 15:00"
    * "Quero agendar amanhã de manhã"
* Sugere horários disponíveis
* Confirma agendamentos

---

## ⚙ Tecnologias utilizadas

* Java 17+
* Spring Boot
* Spring Web
* Spring Data JPA
* H2 Database
* Maven

---

##  Arquitetura

* Controller → entrada da API
* Service → regras de negócio
* Repository → acesso ao banco
* DTO → controle de dados
* Exception Handler → tratamento global de erros

---

##  Diferenciais do projeto

* Uso de DTO (entrada e saída)
* Validação com `@Valid`
* Tratamento global de exceções
* API REST com status HTTP corretos
* Chatbot com interpretação de linguagem natural

---

##  Exemplos de uso

### Criar agendamento

### POST /agendamentos
```json
{
  "dataHora": "2026-05-05T15:00:00",
  "servico": "Corte de cabelo",
  "clienteId": 1
}
```
D
---

### Chatbot

```
POST /chat

Quero agendar amanhã de manhã
```

Resposta:

```
Beleza! Tenho esses horários disponíveis amanhã de manhã:
09:00, 10:00, 11:00 😄
```

---

##  Próximas melhorias

* Integração com banco MySQL
* Criação de frontend (React ou Mobile)
* Uso de IA para melhorar o chatbot
* Autenticação de usuários

---

##  Autor

Desenvolvido por **Gustavo Inagaki**
