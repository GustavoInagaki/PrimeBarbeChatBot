package com.primeBarbe.primebarbechatbot.service;

import com.primeBarbe.primebarbechatbot.dto.AgendamentoRequestDTO;
import com.primeBarbe.primebarbechatbot.exception.BusinessException;
import com.primeBarbe.primebarbechatbot.model.Agendamento;
import com.primeBarbe.primebarbechatbot.repository.AgendamentoRepository;
import com.primeBarbe.primebarbechatbot.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class AgendamentoServiceTests {

    @Autowired
    private AgendamentoService agendamentoService;

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
    void deveBloquearAgendamentoNoPassado() {
        AgendamentoRequestDTO request = request(LocalDateTime.now().minusDays(1).withHour(10).withMinute(0));

        assertThatThrownBy(() -> agendamentoService.salvar(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("passado");
    }

    @Test
    void deveBloquearHorarioDeAlmoco() {
        AgendamentoRequestDTO request = request(LocalDate.now().plusDays(1).atTime(12, 0));

        assertThatThrownBy(() -> agendamentoService.salvar(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("almoco");
    }

    @Test
    void deveBloquearHorarioForaDoExpediente() {
        AgendamentoRequestDTO request = request(LocalDate.now().plusDays(1).atTime(8, 0));

        assertThatThrownBy(() -> agendamentoService.salvar(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("funcionamento");
    }

    @Test
    void deveBloquearHorarioRepetido() {
        LocalDateTime horario = LocalDate.now().plusDays(1).atTime(10, 0);
        agendamentoService.salvar(request(horario));

        assertThatThrownBy(() -> agendamentoService.salvar(request(horario)))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("ocupado");
    }

    @Test
    void naoDeveListarAlmocoComoDisponivel() {
        List<LocalDateTime> horarios = agendamentoService.horariosDisponiveis(LocalDate.now().plusDays(1));

        assertThat(horarios)
                .extracting(LocalDateTime::getHour)
                .doesNotContain(12);
    }

    @Test
    void deveSugerirHorariosProximos() {
        LocalDateTime desejado = LocalDate.now().plusDays(1).atTime(15, 0);
        agendamentoService.salvar(request(desejado));

        List<LocalDateTime> sugestoes = agendamentoService.sugerirHorarios(desejado);

        assertThat(sugestoes)
                .extracting(LocalDateTime::getHour)
                .contains(13, 14, 16, 17);
    }

    @Test
    void deveCriarAgendamentoComClienteNovoPorTelefone() {
        Agendamento salvo = agendamentoService.salvar(request(LocalDate.now().plusDays(1).atTime(11, 0)));

        assertThat(salvo.getCliente().getTelefone()).isEqualTo("11999999999");
        assertThat(clienteRepository.findByTelefone("11999999999")).isPresent();
    }

    private AgendamentoRequestDTO request(LocalDateTime dataHora) {
        AgendamentoRequestDTO request = new AgendamentoRequestDTO();
        request.setDataHora(dataHora);
        request.setServico("Corte de cabelo");
        request.setNomeCliente("Gustavo");
        request.setTelefoneCliente("11 99999-9999");
        return request;
    }
}
