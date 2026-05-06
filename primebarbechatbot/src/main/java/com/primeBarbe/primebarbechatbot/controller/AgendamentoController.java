package com.primeBarbe.primebarbechatbot.controller;

import com.primeBarbe.primebarbechatbot.dto.AgendamentoDTO;
import com.primeBarbe.primebarbechatbot.dto.AgendamentoRequestDTO;
import com.primeBarbe.primebarbechatbot.model.Agendamento;
import com.primeBarbe.primebarbechatbot.service.AgendamentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private final AgendamentoService service;

    public AgendamentoController(AgendamentoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AgendamentoDTO> salvar(@RequestBody @Valid AgendamentoRequestDTO request) {
        Agendamento salvo = service.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.toDTO(salvo));
    }

    @GetMapping("/disponiveis")
    public List<LocalDateTime> horariosDisponiveis(@RequestParam String data) {
        LocalDate dataConvertida = LocalDate.parse(data);
        return service.horariosDisponiveis(dataConvertida);
    }

    @GetMapping
    public List<AgendamentoDTO> listar() {
        return service.listarTodos().stream()
                .map(service::toDTO)
                .toList();
    }
}
