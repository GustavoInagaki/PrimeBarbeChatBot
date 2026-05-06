package com.primeBarbe.primebarbechatbot.dto;

import jakarta.validation.constraints.NotBlank;

public class ClienteRequestDTO {

    @NotBlank(message = "Nome e obrigatorio")
    private String nome;

    @NotBlank(message = "Telefone e obrigatorio")
    private String telefone;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
