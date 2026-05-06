package com.primeBarbe.primebarbechatbot.controller;

import com.primeBarbe.primebarbechatbot.dto.ClienteRequestDTO;
import com.primeBarbe.primebarbechatbot.model.Cliente;
import com.primeBarbe.primebarbechatbot.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @PostMapping
    public Cliente criarCliente(@RequestBody @Valid ClienteRequestDTO request) {
        return service.salvar(request);
    }

    @GetMapping(params = "telefone")
    public Cliente buscarPorTelefone(@RequestParam String telefone) {
        return service.buscarPorTelefone(telefone);
    }

    @GetMapping
    public List<Cliente> listarClientes() {
        return service.listarTodos();
    }
}
