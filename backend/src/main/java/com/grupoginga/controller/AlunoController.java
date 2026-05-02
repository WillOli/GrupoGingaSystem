package com.grupoginga.controller;

import com.grupoginga.model.Aluno;
import com.grupoginga.service.AlunoService;
import com.grupoginga.service.StorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final AlunoService service;
    private final StorageService storageService;

    // Construtor agora injeta também o serviço de arquivos
    public AlunoController(AlunoService service, StorageService storageService) {
        this.service = service;
        this.storageService = storageService;
    }

    // CREATE: Salvar novo aluno
    @PostMapping
    public ResponseEntity<?> criarAluno(@RequestBody Aluno aluno) {
        try {
            Aluno salvo = service.salvar(aluno);
            return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
        } catch (IllegalArgumentException e) {
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
        try {
            // Delega a responsabilidade do "merge" seguro para o Service
            Aluno atualizado = service.atualizar(id, aluno);
            return ResponseEntity.ok(atualizado);
        } catch (IllegalArgumentException e) {
            // Retorna 400 se der erro de CPF, ou 404 se não achar o aluno
            if (e.getMessage().contains("não encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE: Excluir aluno (Soft Delete acionado automaticamente)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAluno(@PathVariable Long id) {
        try {
            service.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            // Se o Service lançar erro porque o ID não existe, devolve 404
            return ResponseEntity.notFound().build();
        }
    }

    // UPLOAD: Atualizar a foto do aluno
    @PostMapping("/{id}/foto")
    public ResponseEntity<?> uploadFotoAluno(@PathVariable Long id, @RequestParam("foto") MultipartFile foto) {
        try {
            // 1. Verifica se o aluno existe antes de salvar o arquivo no disco
            if (service.buscarPorId(id).isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            // 2. Salva o arquivo no disco usando o serviço que criamos
            String nomeArquivoGerado = storageService.salvar(foto);

            // 3. Atualiza o banco de dados vinculando o nome da foto ao aluno
            Aluno alunoAtualizado = service.atualizarFoto(id, nomeArquivoGerado);

            return ResponseEntity.ok(alunoAtualizado);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body("Erro interno ao salvar a imagem: " + e.getMessage());
        }
    }
}