package com.grupoginga.service;

import com.grupoginga.model.Aluno;
import com.grupoginga.repository.AlunoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class AlunoService {

    private final AlunoRepository repository;

    public AlunoService(AlunoRepository repository) {
        this.repository = repository;
    }

    public Aluno salvar(Aluno aluno) {
        Optional<Aluno> alunoExistente = repository.findByCpf(aluno.getCpf());

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

    /**
     * Verifica o status financeiro real do aluno para o mês vigente
     */
    public String verificarStatusPagamentoMensal(Aluno aluno) {
        LocalDate hoje = LocalDate.now();
        String mesReferencia = hoje.format(DateTimeFormatter.ofPattern("MM/yyyy"));

        // 1. Verifica se existe alguma mensalidade PAGA para o mês/ano atual
        if (aluno.getMensalidades() != null) {
            boolean jaPago = aluno.getMensalidades().stream()
                    .anyMatch(m -> mesReferencia.equals(m.getReferenciaMesAno()) && "PAGO".equals(m.getStatus()));

            if (jaPago) return "PAGO";
        }

        // 2. Se não houver pagamento, verifica se o dia de hoje ultrapassou o vencimento
        if (hoje.getDayOfMonth() > aluno.getDiaVencimento()) {
            return "ATRASADO";
        }

        return "AGUARDANDO PAGAMENTO";
    }
}