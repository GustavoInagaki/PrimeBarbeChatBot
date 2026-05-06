package com.primeBarbe.primebarbechatbot.dto;

public class WhatsappMockResponseDTO {

    private String telefone;
    private String resposta;

    public WhatsappMockResponseDTO(String telefone, String resposta) {
        this.telefone = telefone;
        this.resposta = resposta;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getResposta() {
        return resposta;
    }
}
