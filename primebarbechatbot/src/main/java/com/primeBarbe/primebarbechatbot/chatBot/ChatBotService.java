package com.primeBarbe.primebarbechatbot.chatBot;


import com.primeBarbe.primebarbechatbot.service.AgendamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Component
public class ChatBotService {

    @Autowired
    private AgendamentoService agendamentoService;

    public String responder(String mensagem){
        //verifica se a mensagem contém uma data
        if(mensagem.contains("2026")){
            LocalDate data = LocalDate.parse("2026-04-20");

            List<LocalDateTime> horarios = agendamentoService.horariosDisponiveis(data);

            return "Horário disponíveis: " + horarios.toString();
        }

        return "Desculpa, não entendi. Tente algo como: Quero agendar dia 2026-04-20";
    }

}
