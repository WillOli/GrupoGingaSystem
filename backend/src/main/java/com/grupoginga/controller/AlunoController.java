package com.grupoginga.controller;

import com.grupoginga.model.Aluno;
import com.grupoginga.service.AlunoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final AlunoService service;

    public AlunoController(AlunoService service) {
        this.service = service;
    }

    // CREATE: Salvar novo aluno
    @PostMapping
    public ResponseEntity<?> criarAluno(@RequestBody Aluno aluno) {
        try {
            Aluno salvo = service.salvar(aluno);
            return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
        } catch (IllegalArgumentException e) {
            // Se cair na regra do CPF, devolve um erro 400 Bad Request
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // READ: Listar todos
    @GetMapping
    public ResponseEntity<List<Aluno>> listarAlunos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    // READ: Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Aluno> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // READ: Buscar por CPF
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Aluno> buscarPorCpf(@PathVariable String cpf) {
        return service.buscarPorCpf(cpf)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE: Atualizar aluno existente
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarAluno(@PathVariable Long id, @RequestBody Aluno aluno) {
        return service.buscarPorId(id).map(existente -> {
            aluno.setId(existente.getId()); // Garante que vai sobrescrever o aluno certo
            try {
                Aluno atualizado = service.salvar(aluno);
                return ResponseEntity.ok(atualizado);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE: Excluir aluno
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAluno(@PathVariable Long id) {
        if (service.buscarPorId(id).isPresent()) {
            service.deletar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}