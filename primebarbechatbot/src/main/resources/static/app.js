const bookingForm = document.querySelector("#bookingForm");
const dateInput = document.querySelector("#data");
const timeGrid = document.querySelector("#horarios");
const dateTimeInput = document.querySelector("#dataHora");
const feedback = document.querySelector("#feedback");
const submitButton = document.querySelector("#submitButton");
const chatForm = document.querySelector("#chatForm");
const chatResposta = document.querySelector("#chatResposta");

const today = new Date();
dateInput.min = today.toISOString().slice(0, 10);
dateInput.value = dateInput.min;

function setFeedback(message, type = "") {
    feedback.textContent = message;
    feedback.className = `feedback ${type}`.trim();
}

function formatTime(dateTime) {
    return dateTime.slice(11, 16);
}

async function carregarHorarios() {
    dateTimeInput.value = "";
    timeGrid.innerHTML = "";
    setFeedback("Carregando horarios...");

    try {
        const response = await fetch(`/agendamentos/disponiveis?data=${dateInput.value}`);
        if (!response.ok) {
            throw new Error("Nao foi possivel carregar horarios.");
        }

        const horarios = await response.json();
        if (horarios.length === 0) {
            setFeedback("Nao ha horarios disponiveis para essa data.", "error");
            return;
        }

        horarios.forEach((dateTime) => {
            const button = document.createElement("button");
            button.type = "button";
            button.className = "time-button";
            button.textContent = formatTime(dateTime);
            button.setAttribute("aria-pressed", "false");
            button.addEventListener("click", () => selecionarHorario(button, dateTime));
            timeGrid.appendChild(button);
        });

        setFeedback("Escolha um horario disponivel.");
    } catch (error) {
        setFeedback(error.message, "error");
    }
}

function selecionarHorario(button, dateTime) {
    document.querySelectorAll(".time-button").forEach((item) => {
        item.setAttribute("aria-pressed", "false");
    });
    button.setAttribute("aria-pressed", "true");
    dateTimeInput.value = dateTime;
    setFeedback(`Horario selecionado: ${formatTime(dateTime)}.`);
}

async function criarAgendamento(event) {
    event.preventDefault();

    if (!dateTimeInput.value) {
        setFeedback("Escolha um horario antes de confirmar.", "error");
        return;
    }

    submitButton.disabled = true;
    setFeedback("Confirmando agendamento...");

    const payload = {
        nomeCliente: document.querySelector("#nomeCliente").value,
        telefoneCliente: document.querySelector("#telefoneCliente").value,
        servico: document.querySelector("#servico").value,
        dataHora: dateTimeInput.value
    };

    try {
        const response = await fetch("/agendamentos", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(payload)
        });

        const body = await response.json();
        if (!response.ok) {
            throw new Error(body.message || body.errors?.[0]?.message || "Nao foi possivel agendar.");
        }

        await carregarHorarios();
        setFeedback(`Agendamento confirmado para ${formatTime(body.dataHora)}. Obrigado, ${body.nomeCliente}!`, "success");
    } catch (error) {
        setFeedback(error.message, "error");
    } finally {
        submitButton.disabled = false;
    }
}

async function testarChat(event) {
    event.preventDefault();
    chatResposta.textContent = "Enviando...";
    chatResposta.className = "chat-response";

    const payload = {
        telefone: document.querySelector("#chatTelefone").value,
        mensagem: document.querySelector("#chatMensagem").value
    };

    try {
        const response = await fetch("/webhooks/whatsapp/mock", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(payload)
        });

        const body = await response.json();
        if (!response.ok) {
            throw new Error(body.message || body.errors?.[0]?.message || "Nao foi possivel responder.");
        }

        chatResposta.textContent = body.resposta;
        chatResposta.className = "chat-response success";
    } catch (error) {
        chatResposta.textContent = error.message;
        chatResposta.className = "chat-response error";
    }
}

dateInput.addEventListener("change", carregarHorarios);
bookingForm.addEventListener("submit", criarAgendamento);
chatForm.addEventListener("submit", testarChat);

carregarHorarios();
