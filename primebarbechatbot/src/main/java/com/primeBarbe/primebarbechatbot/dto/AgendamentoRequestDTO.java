package com.primeBarbe.primebarbechatbot.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public class AgendamentoRequestDTO {

    @NotNull(message = "Data e hora são obrigatórias")
    @Future(message = "A data deve ser no futuro")
    private LocalDateTime dataHora;

    @NotBlank(message = "Serviço é obrigatório")
    private String servico;

    @NotNull(message = "Cliente é obrigatório")
    private Long clienteId;

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

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }
}