package com.primeBarbe.primebarbechatbot.service;

import com.primeBarbe.primebarbechatbot.dto.ClienteRequestDTO;
import com.primeBarbe.primebarbechatbot.exception.BusinessException;
import com.primeBarbe.primebarbechatbot.model.Cliente;
import com.primeBarbe.primebarbechatbot.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente salvar(Cliente cliente) {
        cliente.setTelefone(normalizarTelefone(cliente.getTelefone()));
        validarCliente(cliente);

        Optional<Cliente> existente = clienteRepository.findByTelefone(cliente.getTelefone());
        if (existente.isPresent()) {
            Cliente clienteExistente = existente.get();
            clienteExistente.setNome(cliente.getNome());
            return clienteRepository.save(clienteExistente);
        }

        return clienteRepository.save(cliente);
    }

    public Cliente salvar(ClienteRequestDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome());
        cliente.setTelefone(dto.getTelefone());
        return salvar(cliente);
    }

    public Cliente buscarPorTelefone(String telefone) {
        String telefoneNormalizado = normalizarTelefone(telefone);
        return clienteRepository.findByTelefone(telefoneNormalizado)
                .orElseThrow(() -> new BusinessException("Cliente nao encontrado"));
    }

    public Cliente buscarOuCriarPorTelefone(String telefone, String nome) {
        String telefoneNormalizado = normalizarTelefone(telefone);

        return clienteRepository.findByTelefone(telefoneNormalizado)
                .map(cliente -> atualizarNomeSeInformado(cliente, nome))
                .orElseGet(() -> criarCliente(telefoneNormalizado, nome));
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public String normalizarTelefone(String telefone) {
        if (telefone == null) {
            return "";
        }
        return telefone.replaceAll("\\D", "");
    }

    private Cliente atualizarNomeSeInformado(Cliente cliente, String nome) {
        if (nome != null && !nome.isBlank()) {
            cliente.setNome(nome.trim());
            return clienteRepository.save(cliente);
        }
        return cliente;
    }

    private Cliente criarCliente(String telefone, String nome) {
        Cliente cliente = new Cliente();
        cliente.setTelefone(telefone);
        cliente.setNome(nome == null || nome.isBlank() ? "Cliente WhatsApp" : nome.trim());
        validarCliente(cliente);
        return clienteRepository.save(cliente);
    }

    private void validarCliente(Cliente cliente) {
        if (cliente.getNome() == null || cliente.getNome().isBlank()) {
            throw new BusinessException("Nome e obrigatorio");
        }

        if (cliente.getTelefone() == null || cliente.getTelefone().isBlank()) {
            throw new BusinessException("Telefone e obrigatorio");
        }

        if (cliente.getTelefone().length() < 10 || cliente.getTelefone().length() > 13) {
            throw new BusinessException("Telefone deve ter entre 10 e 13 digitos");
        }
    }
}
