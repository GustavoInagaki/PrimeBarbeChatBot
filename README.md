# 💈 PrimeBarbeChatBot

API de agendamento para barbearia desenvolvida em Java com Spring Boot, com simulação de chatbot e controle inteligente de horários.

---

## 🚀 Sobre o projeto

O **PrimeBarbeChatBot** é uma API REST que permite:

* Cadastro de clientes
* Criação de agendamentos
* Controle de conflitos de horário
* Consulta de horários disponíveis
* Simulação de chatbot para interação com o usuário

O projeto foi desenvolvido com foco em **boas práticas de backend**, arquitetura em camadas e regras de negócio reais.

---

## 🧠 Funcionalidades

### 👤 Cliente

* Criar cliente
* Listar clientes

### 📅 Agendamento

* Criar agendamento
* Listar agendamentos
* Validação de conflito de horário

### ⏰ Horários disponíveis

* Consulta de horários livres por data

### 🤖 Chatbot (em evolução)

* Simulação de atendimento via texto
* Sugestão de horários disponíveis

---

## 🛠️ Tecnologias utilizadas

* Java 17+
* Spring Boot
* Spring Data JPA
* Hibernate
* Banco H2 (em memória)
* Maven

---

## 📁 Estrutura do projeto

```
controller   → Endpoints da API  
service      → Regras de negócio  
repository   → Acesso ao banco  
model        → Entidades  
chatbot      → Simulação de atendimento  
```

---

## 🔥 Endpoints principais

### 👤 Clientes

**Criar cliente**

```
POST /clientes
```

```json
{
  "nome": "Gustavo",
  "telefone": "99999-9999"
}
```

---

**Listar clientes**

```
GET /clientes
```

---

### 📅 Agendamentos

**Criar agendamento**

```
POST /agendamentos
```

```json
{
  "dataHora": "2026-04-20T14:00:00",
  "servico": "Corte de cabelo",
  "cliente": {
    "id": 1
  }
}
```

---

**Listar agendamentos**

```
GET /agendamentos
```

---

### ⏰ Horários disponíveis

```
GET /agendamentos/disponiveis?data=2026-04-20
```

Retorna os horários livres para a data informada.

---

## 🧠 Regras de negócio

* Não é permitido dois agendamentos no mesmo horário
* Cada agendamento está vinculado a um cliente
* Os horários disponíveis são calculados dinamicamente

---

## 🎯 Objetivo do projeto

Este projeto foi desenvolvido com o objetivo de:

* Praticar desenvolvimento backend com Java
* Aplicar conceitos de APIs REST
* Trabalhar com banco de dados e ORM
* Simular um sistema real de agendamento

---

## 🚀 Próximas melhorias

* Melhorar tratamento de erros (HTTP 400/404)
* Implementar chatbot mais inteligente
* Permitir agendamento direto via chatbot
* Integração com banco de dados MySQL
* Criar interface frontend

---

## 👨‍💻 Autor

Desenvolvido por **Gustavo Inagaki**
