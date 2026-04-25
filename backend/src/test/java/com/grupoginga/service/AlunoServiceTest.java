package com.grupoginga.service;

import com.grupoginga.model.Aluno;
import com.grupoginga.repository.AlunoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlunoServiceTest {

    @Mock
    private AlunoRepository repository;

    @InjectMocks
    private AlunoService service;

    @Test
    @DisplayName("Deve salvar aluno com sucesso quando o CPF não existe")
    void deveSalvarAlunoComSucesso() {
        Aluno aluno = new Aluno();
        aluno.setCpf("123.456.789-00");

        when(repository.findByCpf(aluno.getCpf())).thenReturn(Optional.empty());
        when(repository.save(aluno)).thenReturn(aluno);

        Aluno salvo = service.salvar(aluno);

        assertNotNull(salvo);
        verify(repository, times(1)).save(aluno);
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar salvar aluno com CPF de outro aluno")
    void deveLancarErroCpfDuplicado() {
        Aluno alunoNovo = new Aluno();
        alunoNovo.setId(2L);
        alunoNovo.setCpf("123.456.789-00");

        Aluno alunoExistenteNoBanco = new Aluno();
        alunoExistenteNoBanco.setId(1L);
        alunoExistenteNoBanco.setCpf("123.456.789-00");

        when(repository.findByCpf("123.456.789-00")).thenReturn(Optional.of(alunoExistenteNoBanco));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.salvar(alunoNovo);
        });

        assertEquals("Erro: Este CPF já está cadastrado no sistema!", exception.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Deve permitir atualizar aluno quando o CPF já pertence a ele mesmo")
    void devePermitirAtualizarProprioAluno() {
        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setCpf("123.456.789-00");

        when(repository.findByCpf("123.456.789-00")).thenReturn(Optional.of(aluno));
        when(repository.save(aluno)).thenReturn(aluno);

        Aluno salvo = service.salvar(aluno);

        assertNotNull(salvo);
        verify(repository, times(1)).save(aluno);
    }

    @Test
    @DisplayName("Deve retornar vazio ao buscar ID inexistente")
    void deveRetornarVazioAoBuscarIdInexistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        Optional<Aluno> resultado = service.buscarPorId(99L);
        assertTrue(resultado.isEmpty());
    }
}