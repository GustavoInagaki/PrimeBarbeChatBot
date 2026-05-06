package com.primeBarbe.primebarbechatbot.chatBot;

import com.primeBarbe.primebarbechatbot.repository.AgendamentoRepository;
import com.primeBarbe.primebarbechatbot.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AtendimentoChatServiceTests {

    @Autowired
    private AtendimentoChatService chatService;

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
    void devePedirTelefoneQuandoClienteQuerAgendar() {
        String resposta = chatService.responder(null, "Quero agendar dia 2026-05-05 as 15:00");

        assertThat(resposta).contains("telefone");
    }

    @Test
    void deveResponderHorariosDisponiveisPorPeriodo() {
        String resposta = chatService.responder(null, "Quais horarios amanha de manha?");

        assertThat(resposta).contains("09:00", "10:00", "11:00");
    }

    @Test
    void deveCriarAgendamentoComDataHoraETelefone() {
        String data = LocalDate.now().plusDays(1).toString();

        String resposta = chatService.responder("11999999999", "Quero agendar dia " + data + " as 15:00");

        assertThat(resposta).contains("confirmado");
        assertThat(agendamentoRepository.existsByDataHora(LocalDate.now().plusDays(1).atTime(15, 0))).isTrue();
    }
}
