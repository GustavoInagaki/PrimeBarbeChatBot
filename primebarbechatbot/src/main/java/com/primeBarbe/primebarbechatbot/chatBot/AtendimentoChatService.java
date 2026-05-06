package com.primeBarbe.primebarbechatbot.chatBot;

import com.primeBarbe.primebarbechatbot.dto.AgendamentoRequestDTO;
import com.primeBarbe.primebarbechatbot.exception.BusinessException;
import com.primeBarbe.primebarbechatbot.model.Agendamento;
import com.primeBarbe.primebarbechatbot.service.AgendamentoService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AtendimentoChatService {

    private final AgendamentoService agendamentoService;

    public AtendimentoChatService(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    public String responder(String telefone, String mensagem) {
        if (mensagem == null || mensagem.isBlank()) {
            return "Me envie a data e o horario desejado para o agendamento.";
        }

        String texto = mensagem.toLowerCase();
        String telefoneResolvido = telefoneValido(telefone) ? telefone : extrairTelefone(texto);

        String respostaPrimeiroHorario = responderPrimeiroHorario(texto);
        if (respostaPrimeiroHorario != null) {
            return respostaPrimeiroHorario;
        }

        String respostaDepoisDas = responderDepoisDas(texto);
        if (respostaDepoisDas != null) {
            return respostaDepoisDas;
        }

        String respostaPeriodo = responderPorPeriodo(texto);
        if (respostaPeriodo != null) {
            return respostaPeriodo;
        }

        String respostaAgendamento = tentarAgendar(texto, telefoneResolvido);
        if (respostaAgendamento != null) {
            return respostaAgendamento;
        }

        return "Desculpa, nao entendi. Tente algo como: Quero agendar dia 2026-05-05 as 15:00.";
    }

    private String responderPrimeiroHorario(String texto) {
        if (!texto.contains("primeiro hor")) {
            return null;
        }

        LocalDate data = texto.contains("amanha") ? LocalDate.now().plusDays(1) : LocalDate.now();
        List<LocalDateTime> horarios = agendamentoService.horariosDisponiveis(data);

        if (horarios.isEmpty()) {
            return "Nao ha horarios disponiveis.";
        }

        return "O primeiro horario disponivel e: " + horarios.get(0).toLocalTime();
    }

    private String responderDepoisDas(String texto) {
        if (!texto.contains("depois das")) {
            return null;
        }

        Matcher matcher = Pattern.compile("depois das (\\d{1,2})").matcher(texto);
        if (!matcher.find()) {
            return null;
        }

        int hora = Integer.parseInt(matcher.group(1));
        if (hora < 0 || hora > 23) {
            return "Horario invalido.";
        }

        LocalDate data = texto.contains("amanha") ? LocalDate.now().plusDays(1) : LocalDate.now();
        List<LocalDateTime> filtrados = agendamentoService.horariosDisponiveis(data)
                .stream()
                .filter(h -> h.getHour() > hora)
                .toList();

        if (filtrados.isEmpty()) {
            return "Nao ha horarios disponiveis apos " + hora + "h.";
        }

        return "Tenho esses horarios disponiveis apos " + hora + "h:\n" + formatarHorarios(filtrados);
    }

    private String responderPorPeriodo(String texto) {
        LocalDate dataPeriodo = null;
        if (texto.contains("hoje")) {
            dataPeriodo = LocalDate.now();
        } else if (texto.contains("amanha")) {
            dataPeriodo = LocalDate.now().plusDays(1);
        }

        String periodo = null;
        if (texto.contains("tarde")) {
            periodo = "tarde";
        } else if (texto.contains("manha")) {
            periodo = "manha";
        }

        if (dataPeriodo == null || periodo == null) {
            return null;
        }

        boolean isManha = "manha".equals(periodo);
        List<LocalDateTime> filtrados = agendamentoService.horariosDisponiveis(dataPeriodo)
                .stream()
                .filter(h -> isManha ? h.getHour() >= 9 && h.getHour() < 12 : h.getHour() >= 13 && h.getHour() <= 18)
                .toList();

        if (filtrados.isEmpty()) {
            return "Nao ha horarios disponiveis nesse periodo.";
        }

        String periodoTexto = isManha ? "de manha" : "a tarde";
        String dataTexto = dataPeriodo.equals(LocalDate.now().plusDays(1)) ? "amanha " : "";

        return "Tenho esses horarios disponiveis " + dataTexto + periodoTexto + ":\n" + formatarHorarios(filtrados);
    }

    private String tentarAgendar(String texto, String telefone) {
        Matcher dataMatcher = Pattern.compile("\\d{4}-\\d{2}-\\d{2}").matcher(texto);
        Matcher horaMatcher = Pattern.compile("\\d{1,2}:\\d{2}").matcher(texto);

        if (!dataMatcher.find() || !horaMatcher.find()) {
            return null;
        }

        if (!telefoneValido(telefone)) {
            return "Para confirmar o agendamento, me envie seu telefone com DDD.";
        }

        LocalDate data = LocalDate.parse(dataMatcher.group());
        LocalTime hora = LocalTime.parse(horaMatcher.group());
        LocalDateTime dataHora = LocalDateTime.of(data, hora);

        AgendamentoRequestDTO request = new AgendamentoRequestDTO();
        request.setDataHora(dataHora);
        request.setServico("Corte via chatbot");
        request.setTelefoneCliente(telefone);
        request.setNomeCliente("Cliente WhatsApp");

        try {
            Agendamento agendamento = agendamentoService.salvar(request);
            return "Agendamento confirmado para " + agendamento.getDataHora() + ".";
        } catch (BusinessException ex) {
            List<LocalDateTime> sugestoes = agendamentoService.sugerirHorarios(dataHora);
            if (sugestoes.isEmpty()) {
                return ex.getMessage() + ". Nao encontrei outros horarios nesse dia.";
            }

            return ex.getMessage() + ". Podemos marcar nesses horarios proximos:\n" + formatarHorarios(sugestoes);
        }
    }

    private boolean telefoneValido(String telefone) {
        return telefone != null && telefone.replaceAll("\\D", "").length() >= 10;
    }

    private String extrairTelefone(String texto) {
        Matcher matcher = Pattern.compile("(?<!\\d)(?:\\+?55\\s*)?\\(?\\d{2}\\)?\\s*9?\\d{4}[-\\s]?\\d{4}(?!\\d)").matcher(texto);
        if (matcher.find()) {
            return matcher.group().replaceAll("\\D", "");
        }
        return null;
    }

    private String formatarHorarios(List<LocalDateTime> horarios) {
        return String.join(", ", horarios.stream()
                .map(h -> h.toLocalTime().toString())
                .toList());
    }
}
