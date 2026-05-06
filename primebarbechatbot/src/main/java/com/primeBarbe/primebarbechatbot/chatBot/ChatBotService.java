package com.primeBarbe.primebarbechatbot.chatBot;

import org.springframework.stereotype.Component;

@Component
public class ChatBotService {

    private final AtendimentoChatService atendimentoChatService;

    public ChatBotService(AtendimentoChatService atendimentoChatService) {
        this.atendimentoChatService = atendimentoChatService;
    }

    public String responder(String mensagem) {
        return atendimentoChatService.responder(null, mensagem);
    }

    public String responder(String telefone, String mensagem) {
        return atendimentoChatService.responder(telefone, mensagem);
    }
}
