package com.primeBarbe.primebarbechatbot.controller;

import com.primeBarbe.primebarbechatbot.dto.AgendamentoDTO;
import com.primeBarbe.primebarbechatbot.dto.AgendamentoRequestDTO;
import com.primeBarbe.primebarbechatbot.dto.ClienteRequestDTO;
import com.primeBarbe.primebarbechatbot.model.Cliente;
import com.primeBarbe.primebarbechatbot.repository.AgendamentoRepository;
import com.primeBarbe.primebarbechatbot.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ControllerFlowTests {

    @Autowired
    private ClienteController clienteController;

    @Autowired
    private AgendamentoController agendamentoController;

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @BeforeEach
    void setup() {
        agendamentoRepository.deleteAll();
        clienteRepository.deleteAll();
    }

    @Test
    void deveCriarClientePorTelefone() {
        ClienteRequestDTO request = new ClienteRequestDTO();
        request.setNome("Gustavo");
        request.setTelefone("11 99999-9999");

        Cliente cliente = clienteController.criarCliente(request);

        assertThat(cliente.getTelefone()).isEqualTo("11999999999");
        assertThat(clienteController.buscarPorTelefone("11999999999").getNome()).isEqualTo("Gustavo");
    }

    @Test
    void deveCriarAgendamentoComClienteExistente() {
        ClienteRequestDTO clienteRequest = new ClienteRequestDTO();
        clienteRequest.setNome("Gustavo");
        clienteRequest.setTelefone("11999999999");
        clienteController.criarCliente(clienteRequest);

        ResponseEntity<AgendamentoDTO> response = agendamentoController.salvar(agendamento("11999999999"));

        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getNomeCliente()).isEqualTo("Gustavo");
    }

    @Test
    void deveCriarAgendamentoComClienteNovo() {
        ResponseEntity<AgendamentoDTO> response = agendamentoController.salvar(agendamento("11888888888"));

        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(clienteRepository.findByTelefone("11888888888")).isPresent();
    }

    private AgendamentoRequestDTO agendamento(String telefone) {
        AgendamentoRequestDTO request = new AgendamentoRequestDTO();
        request.setDataHora(LocalDate.now().plusDays(1).atTime(10, 0));
        request.setServico("Barba");
        request.setNomeCliente("Gustavo");
        request.setTelefoneCliente(telefone);
        return request;
    }
}
