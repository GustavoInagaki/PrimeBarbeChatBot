package com.primeBarbe.primebarbechatbot.controller;

import com.primeBarbe.primebarbechatbot.chatBot.ChatBotService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatBotService chatBotService;

    public ChatController(ChatBotService chatBotService) {
        this.chatBotService = chatBotService;
    }

    @PostMapping
    public String conversa(@RequestBody String body) {
        if (body != null && body.trim().startsWith("{")) {
            String telefone = extrairCampoJson(body, "telefone");
            String mensagem = extrairCampoJson(body, "mensagem");
            if (mensagem == null || mensagem.isBlank()) {
                mensagem = extrairCampoJson(body, "message");
            }
            return chatBotService.responder(telefone, mensagem);
        }

        return chatBotService.responder(body);
    }

    private String extrairCampoJson(String json, String campo) {
        Matcher matcher = Pattern.compile("\"" + campo + "\"\\s*:\\s*\"([^\"]*)\"").matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
