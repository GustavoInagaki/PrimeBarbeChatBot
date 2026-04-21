package com.primeBarbe.primebarbechatbot.service;


import com.primeBarbe.primebarbechatbot.model.Agendamento;
import com.primeBarbe.primebarbechatbot.model.Cliente;
import com.primeBarbe.primebarbechatbot.repository.AgendamentoRepository;
import com.primeBarbe.primebarbechatbot.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository repository;

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Agendamento> listarTodos(){
        return repository.findAll();
    }

    public List<LocalDateTime> horariosDisponiveis(LocalDate data){
        List<LocalDateTime> disponiveis = new ArrayList<>();

        //horário de funcionamento
        for(int hora = 9; hora <= 18; hora++){
           LocalDateTime dataHora = data.atTime(hora, 0);

           boolean ocupado = repository.existsByDataHora(dataHora);

           if(!ocupado){
               disponiveis.add(dataHora);
           }

        }
        return disponiveis;
    }

    public Agendamento salvar(Agendamento agendamento){

        //Buscar cliente no banco
        Cliente cliente = clienteRepository.findById(
                agendamento.getCliente().getId()
        ).orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        agendamento.setCliente(cliente);


        //Regra de horário
        if (repository.existsByDataHora(agendamento.getDataHora())) {
            throw new RuntimeException("Horário já está ocupado!");
        }

        return repository.save(agendamento);
    }

}
