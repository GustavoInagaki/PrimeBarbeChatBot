package com.primeBarbe.primebarbechatbot.controller;


import com.primeBarbe.primebarbechatbot.dto.AgendamentoDTO;
import com.primeBarbe.primebarbechatbot.dto.AgendamentoRequestDTO;
import com.primeBarbe.primebarbechatbot.model.Agendamento;
import com.primeBarbe.primebarbechatbot.service.AgendamentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoService service;

    @PostMapping
    public ResponseEntity<AgendamentoDTO> salvar(@RequestBody @Valid AgendamentoRequestDTO request) {

        Agendamento agendamento = service.fromDTO(request);

        Agendamento salvo = service.salvar(agendamento);

        AgendamentoDTO dto = service.toDTO(salvo);

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }


    @GetMapping("/disponiveis")
    public List<LocalDateTime> horariosDisponiveis(@RequestParam String data){
        LocalDate dataConvetida = LocalDate.parse(data);

        return service.horariosDisponiveis(dataConvetida);
    }
    @GetMapping
    public List<AgendamentoDTO> listar() {
        return service.listarTodos().stream()
                .map(service::toDTO)
                .toList();
    }

}
