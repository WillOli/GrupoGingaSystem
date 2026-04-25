package com.grupoginga.service;

import com.grupoginga.model.Aluno;
import com.grupoginga.repository.AlunoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlunoService {

    private final AlunoRepository repository;

    public AlunoService(AlunoRepository repository) {
        this.repository = repository;
    }

    public Aluno salvar(Aluno aluno) {
        // Regra de Negócio: Bloquear cadastro de CPF duplicado
        Optional<Aluno> alunoExistente = repository.findByCpf(aluno.getCpf());

        // Se já existe no banco e não é o mesmo aluno sendo atualizado
        if (alunoExistente.isPresent() && !alunoExistente.get().getId().equals(aluno.getId())) {
            throw new IllegalArgumentException("Erro: Este CPF já está cadastrado no sistema!");
        }

        return repository.save(aluno);
    }

    public List<Aluno> listarTodos() {
        return repository.findAll();
    }

    public Optional<Aluno> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Optional<Aluno> buscarPorCpf(String cpf) {
        return repository.findByCpf(cpf);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}