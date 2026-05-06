package com.primeBarbe.primebarbechatbot.controller;

import com.primeBarbe.primebarbechatbot.chatBot.ChatBotService;
import com.primeBarbe.primebarbechatbot.dto.WhatsappMockRequestDTO;
import com.primeBarbe.primebarbechatbot.dto.WhatsappMockResponseDTO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhooks/whatsapp")
public class WhatsappMockController {

    private final ChatBotService chatBotService;

    public WhatsappMockController(ChatBotService chatBotService) {
        this.chatBotService = chatBotService;
    }

    @PostMapping("/mock")
    public WhatsappMockResponseDTO receberMensagemMock(@RequestBody @Valid WhatsappMockRequestDTO request) {
        String resposta = chatBotService.responder(request.getTelefone(), request.getMensagem());
        return new WhatsappMockResponseDTO(request.getTelefone(), resposta);
    }
}
