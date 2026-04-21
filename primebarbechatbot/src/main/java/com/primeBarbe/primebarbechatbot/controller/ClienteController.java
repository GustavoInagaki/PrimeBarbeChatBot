package com.primeBarbe.primebarbechatbot.controller;


import com.primeBarbe.primebarbechatbot.model.Cliente;
import com.primeBarbe.primebarbechatbot.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService service;

    @PostMapping
    public Cliente criarCliente(@RequestBody Cliente cliente){

        return service.salvar(cliente);

    }

    @GetMapping
    public List<Cliente> listarClientes(){
        return service.listarTodos();
    }
}
