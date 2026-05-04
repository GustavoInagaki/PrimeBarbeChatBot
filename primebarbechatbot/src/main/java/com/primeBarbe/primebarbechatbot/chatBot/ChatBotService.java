package com.primeBarbe.primebarbechatbot.chatBot;

import com.primeBarbe.primebarbechatbot.model.Agendamento;
import com.primeBarbe.primebarbechatbot.model.Cliente;
import com.primeBarbe.primebarbechatbot.service.AgendamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class ChatBotService {

    @Autowired
    private AgendamentoService agendamentoService;

    private String formatarHorarios(List<LocalDateTime> horarios) {

        StringBuilder resposta = new StringBuilder();

        for (int i = 0; i < horarios.size(); i++) {
            resposta.append(horarios.get(i).toLocalTime());

            if (i < horarios.size() - 1) {
                resposta.append(", ");
            }
        }
        return resposta.toString();
    }

    public String responder(String mensagem){

        mensagem = mensagem.toLowerCase();

        Pattern dataParttern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
        Matcher dataMatcher = dataParttern.matcher(mensagem);

        Pattern horaPattern = Pattern.compile("\\d{2}:\\d{2}");
        Matcher horaMatcher = horaPattern.matcher(mensagem);

        if (dataMatcher.find() && horaMatcher.find()) {

            String dataTexto = dataMatcher.group();
            String horaTexto = horaMatcher.group();

            LocalDate data = LocalDate.parse(dataTexto);
            LocalTime hora = LocalTime.parse(horaTexto);

            LocalDateTime dataHora = LocalDateTime.of(data, hora);

            Agendamento agendamento = new Agendamento();
            agendamento.setDataHora(dataHora);
            agendamento.setServico("Corte via chatbot");

            Cliente cliente = new Cliente();
            cliente.setId(1L);
            agendamento.setCliente(cliente);

            try {
                agendamentoService.salvar(agendamento);
                return "Agendamento confirmado para " + dataHora + " ✅";
            } catch (Exception e) {
                List<LocalDateTime> horarios = agendamentoService.horariosDisponiveis(data);

                return "Esse horário já está ocupado.\n" +
                        "Horários disponíveis: " + formatarHorarios(horarios);
            }
        }


        LocalDate dataPeriodo = null;

        if (mensagem.contains("hoje")) {
            dataPeriodo = LocalDate.now();
        } else if (mensagem.contains("amanha") || mensagem.contains("amanhã")) {
            dataPeriodo = LocalDate.now().plusDays(1);
        }

        String periodo = null;

        if (mensagem.contains("tarde")) {
            periodo = "tarde";
        } else if (
                mensagem.contains("manha") || mensagem.contains("manhã")
        ) {
            periodo = "manha";
        }

        if (dataPeriodo != null && periodo != null) {

            List<LocalDateTime> horarios = agendamentoService.horariosDisponiveis(dataPeriodo);

            boolean isManha = "manha".equals(periodo);

            List<LocalDateTime> filtrados = horarios.stream()
                    .filter(h -> {
                        int horaPeriodo = h.getHour();

                        if (isManha) {
                            return horaPeriodo >= 9 && horaPeriodo <= 12;
                        } else {
                            return horaPeriodo >= 13 && horaPeriodo <= 18;
                        }
                    })
                    .toList();

            String periodoTexto = periodo.equals("manha") ? "de manhã" : "à tarde";

            return "Beleza! Tenho esses horários disponíveis " +
                    (dataPeriodo.equals(LocalDate.now().plusDays(1)) ? "amanhã " : "") +
                    periodoTexto + ":\n" +
                    formatarHorarios(filtrados) ;
        }


        return "Desculpa, não entendi. Tente algo como: 'Quero agendar dia 2026-04-20 às 00:00'";
    }

}
