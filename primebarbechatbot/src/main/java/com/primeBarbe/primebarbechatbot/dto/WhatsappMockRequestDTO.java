package com.primeBarbe.primebarbechatbot.dto;

import jakarta.validation.constraints.NotBlank;

public class WhatsappMockRequestDTO {

    @NotBlank(message = "Telefone e obrigatorio")
    private String telefone;

    @NotBlank(message = "Mensagem e obrigatoria")
    private String mensagem;

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
