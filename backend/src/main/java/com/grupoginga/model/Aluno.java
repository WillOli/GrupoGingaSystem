package com.grupoginga.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "alunos")
@Data
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

    // Lógica de verificação de atraso para o JSON'
    @Transient
    public String getStatusFinanceiroSimplificado() {
        LocalDate hoje = LocalDate.now();

        // Se o dia de hoje for maior que o dia do vencimento, marca como ATRASADO
        if (hoje.getDayOfMonth() > this.diaVencimento) {
            return "ATRASADO";
        }

        return "EM DIA";
    }
}