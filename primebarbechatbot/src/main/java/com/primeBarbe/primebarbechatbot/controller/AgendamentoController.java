package com.primeBarbe.primebarbechatbot.controller;


import com.primeBarbe.primebarbechatbot.model.Agendamento;
import com.primeBarbe.primebarbechatbot.service.AgendamentoService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Agendamento salvar(@RequestBody Agendamento agendamento){
        return service.salvar(agendamento);
    }


    @GetMapping
    public List<Agendamento> listar(){
        return service.listarTodos();
    }

    @GetMapping("/disponiveis")
    public List<LocalDateTime> horariosDisponiveis(@RequestParam String data){
        LocalDate dataConvetida = LocalDate.parse(data);

        return service.horariosDisponiveis(dataConvetida);
    }

}
