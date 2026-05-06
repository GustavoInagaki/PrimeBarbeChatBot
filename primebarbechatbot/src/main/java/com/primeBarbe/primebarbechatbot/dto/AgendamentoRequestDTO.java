package com.primeBarbe.primebarbechatbot.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class AgendamentoRequestDTO {

    @NotNull(message = "Data e hora sao obrigatorias")
    @Future(message = "A data deve ser no futuro")
    private LocalDateTime dataHora;

    @NotBlank(message = "Servico e obrigatorio")
    private String servico;

    @NotBlank(message = "Telefone do cliente e obrigatorio")
    private String telefoneCliente;

    private String nomeCliente;

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getServico() {
        return servico;
    }

    public void setServico(String servico) {
        this.servico = servico;
    }

    public String getTelefoneCliente() {
        return telefoneCliente;
    }

    public void setTelefoneCliente(String telefoneCliente) {
        this.telefoneCliente = telefoneCliente;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }
}
