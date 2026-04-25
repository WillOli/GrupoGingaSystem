package com.grupoginga.repository;

import com.grupoginga.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    // O Spring faz a "mágica" de criar a query SQL por trás dos panos apenas lendo este nome!
    Optional<Aluno> findByCpf(String cpf);
}