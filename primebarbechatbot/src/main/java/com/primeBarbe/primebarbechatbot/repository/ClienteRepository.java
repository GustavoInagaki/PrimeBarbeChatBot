package com.primeBarbe.primebarbechatbot.repository;

import com.primeBarbe.primebarbechatbot.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByTelefone(String telefone);

}
