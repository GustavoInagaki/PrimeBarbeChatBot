package com.primeBarbe.primebarbechatbot.service;

import com.primeBarbe.primebarbechatbot.dto.AgendamentoDTO;
import com.primeBarbe.primebarbechatbot.dto.AgendamentoRequestDTO;
import com.primeBarbe.primebarbechatbot.exception.BusinessException;
import com.primeBarbe.primebarbechatbot.model.Agendamento;
import com.primeBarbe.primebarbechatbot.model.Cliente;
import com.primeBarbe.primebarbechatbot.repository.AgendamentoRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AgendamentoService {

    public static final int HORA_ABERTURA = 9;
    public static final int HORA_ALMOCO = 12;
    public static final int HORA_FECHAMENTO = 18;

    private final AgendamentoRepository repository;
    private final ClienteService clienteService;

    public AgendamentoService(AgendamentoRepository repository, ClienteService clienteService) {
        this.repository = repository;
        this.clienteService = clienteService;
    }

    public List<Agendamento> listarTodos() {
        return repository.findAll();
    }

    public List<LocalDateTime> horariosDisponiveis(LocalDate data) {
        List<LocalDateTime> disponiveis = new ArrayList<>();

        for (int hora = HORA_ABERTURA; hora <= HORA_FECHAMENTO; hora++) {
            if (hora == HORA_ALMOCO) {
                continue;
            }

            LocalDateTime dataHora = data.atTime(hora, 0);
            if (!repository.existsByDataHora(dataHora)) {
                disponiveis.add(dataHora);
            }
        }

        return disponiveis;
    }

    public Agendamento salvar(Agendamento agendamento) {
        validarAgendamento(agendamento);

        try {
            return repository.save(agendamento);
        } catch (DataIntegrityViolationException ex) {
            throw new BusinessException("Horario ja esta ocupado");
        }
    }

    public Agendamento salvar(AgendamentoRequestDTO dto) {
        Cliente cliente = clienteService.buscarOuCriarPorTelefone(dto.getTelefoneCliente(), dto.getNomeCliente());

        Agendamento agendamento = new Agendamento();
        agendamento.setDataHora(dto.getDataHora());
        agendamento.setServico(dto.getServico().trim());
        agendamento.setCliente(cliente);

        return salvar(agendamento);
    }

    private void validarAgendamento(Agendamento agendamento) {
        if (agendamento.getCliente() == null) {
            throw new BusinessException("Cliente e obrigatorio");
        }

        if (agendamento.getServico() == null || agendamento.getServico().isBlank()) {
            throw new BusinessException("Servico e obrigatorio");
        }

        if (agendamento.getDataHora() == null) {
            throw new BusinessException("Data e hora sao obrigatorias");
        }

        if (agendamento.getDataHora().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Nao e possivel agendar no passado");
        }

        if (agendamento.getDataHora().getMinute() != 0 || agendamento.getDataHora().getSecond() != 0) {
            throw new BusinessException("Agendamentos devem ser feitos em horarios cheios");
        }

        int hora = agendamento.getDataHora().getHour();

        if (hora < HORA_ABERTURA || hora > HORA_FECHAMENTO) {
            throw new BusinessException("Horario fora do funcionamento (09h as 18h)");
        }

        if (hora == HORA_ALMOCO) {
            throw new BusinessException("Horario indisponivel para almoco");
        }

        if (repository.existsByDataHora(agendamento.getDataHora())) {
            throw new BusinessException("Horario ja esta ocupado");
        }
    }

    public AgendamentoDTO toDTO(Agendamento agendamento) {
        AgendamentoDTO dto = new AgendamentoDTO();
        dto.setId(agendamento.getId());
        dto.setDataHora(agendamento.getDataHora());
        dto.setServico(agendamento.getServico());
        dto.setNomeCliente(agendamento.getCliente().getNome());
        dto.setTelefoneCliente(agendamento.getCliente().getTelefone());
        return dto;
    }

    public List<LocalDateTime> sugerirHorarios(LocalDateTime dataHoraDesejada) {
        List<LocalDateTime> disponiveis = horariosDisponiveis(dataHoraDesejada.toLocalDate());

        List<LocalDateTime> antes = disponiveis.stream()
                .filter(h -> h.isBefore(dataHoraDesejada))
                .sorted((a, b) -> b.compareTo(a))
                .limit(2)
                .toList();

        List<LocalDateTime> depois = disponiveis.stream()
                .filter(h -> h.isAfter(dataHoraDesejada))
                .limit(2)
                .toList();

        List<LocalDateTime> sugestoes = new ArrayList<>();
        sugestoes.addAll(antes);
        sugestoes.addAll(depois);
        sugestoes.sort(LocalDateTime::compareTo);

        return sugestoes;
    }
}
