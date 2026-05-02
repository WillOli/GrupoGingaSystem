package com.grupoginga.service;

import com.grupoginga.model.Aluno;
import com.grupoginga.repository.AlunoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public Aluno salvar(Aluno aluno) {
        Optional<Aluno> alunoExistente = repository.findByCpf(aluno.getCpf());

        // Permite salvar se o CPF não existir, ou se existir mas for do próprio aluno (cenário de atualização)
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

    @Transactional
    public Aluno atualizar(Long id, Aluno dadosAtualizados) {
        // Busca o aluno no banco (com todos os dados originais intactos)
        Aluno alunoExistente = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Erro: Aluno não encontrado."));

        // Se o CPF for alterado, verifica se o novo já não pertence a outro aluno
        if (!alunoExistente.getCpf().equals(dadosAtualizados.getCpf())) {
            Optional<Aluno> cpfExistente = repository.findByCpf(dadosAtualizados.getCpf());
            if (cpfExistente.isPresent()) {
                throw new IllegalArgumentException("Erro: O novo CPF já está cadastrado no sistema!");
            }
            alunoExistente.setCpf(dadosAtualizados.getCpf());
        }

        // Atualiza apenas os campos editáveis pelo usuário
        alunoExistente.setNome(dadosAtualizados.getNome());
        alunoExistente.setTelefone(dadosAtualizados.getTelefone());
        alunoExistente.setDataNascimento(dadosAtualizados.getDataNascimento());
        alunoExistente.setGraduacaoAtual(dadosAtualizados.getGraduacaoAtual());
        alunoExistente.setDiaVencimento(dadosAtualizados.getDiaVencimento());
        // alunoExistente.setPlano(dadosAtualizados.getPlano()); // Descomente se o plano for editável aqui

        // O save agora salva a entidade original, mas com os valores novos mesclados
        return repository.save(alunoExistente);
    }

    @Transactional
    public Aluno atualizarFoto(Long id, String nomeArquivoFoto) {
        Aluno aluno = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Erro: Aluno não encontrado."));

        // Atualiza o caminho da foto
        aluno.setCaminhoFoto(nomeArquivoFoto);

        return repository.save(aluno);
    }

    @Transactional
    public void deletar(Long id) {
        // Busca antes para evitar erros 500 no servidor caso o ID não exista ou já esteja inativo
        Aluno aluno = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Erro: Aluno não encontrado ou já excluído."));

        repository.delete(aluno);
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