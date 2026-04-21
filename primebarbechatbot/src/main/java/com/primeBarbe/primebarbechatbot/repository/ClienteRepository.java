package com.primeBarbe.primebarbechatbot.repository;

import com.primeBarbe.primebarbechatbot.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}
