package com.grupoginga.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "alunos")
@Data
// 1. Intercepta o DELETE e faz um UPDATE mudando o status para INATIVO
@SQLDelete(sql = "UPDATE alunos SET status = 'INATIVO' WHERE id = ?")
// 2. Filtra globalmente para que o Hibernate só retorne alunos ATIVOS nas buscas normais
@SQLRestriction("status <> 'INATIVO'")
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(unique = true, nullable = false, length = 14)
    private String cpf;

    private String telefone;

    private LocalDate dataNascimento;

    private String caminhoFoto;

    @Enumerated(EnumType.STRING)
    private Graduacao graduacaoAtual;

    @Column(nullable = false)
    private Integer diaVencimento;

    @ManyToOne
    @JoinColumn(name = "plano_id")
    private Plano plano;

    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL)
    private List<Mensalidade> mensalidades;

    private LocalDateTime dataCadastro = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private StatusAluno status = StatusAluno.ATIVO;

    // Lógica de verificação de atraso para o JSON
    @Transient
    public String getStatusFinanceiroSimplificado() {
        LocalDate hoje = LocalDate.now();

        // TODO (Sprint 3): Melhorar essa regra. Atualmente ela marca como atrasado
        // mesmo se a pessoa já tiver pago a mensalidade do mês atual.
        // O ideal será verificar na lista de "mensalidades" se existe um pagamento no mês/ano vigente.
        if (hoje.getDayOfMonth() > this.diaVencimento) {
            return "ATRASADO";
        }

        return "EM DIA";
    }
}