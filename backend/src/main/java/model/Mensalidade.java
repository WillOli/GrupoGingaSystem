package model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "mensalidades")
@Data
public class Mensalidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @Column(nullable = false)
    private String referenciaMesAno; // Ex: "05/2026"

    @Column(nullable = false)
    private BigDecimal valorCobrado;
    private BigDecimal valorPago;

    @Column(nullable = false)
    private LocalDate dataVencimento;
    private LocalDate dataPagamento;

    private String status = "PENDENTE";
    private String metodoPagamento;
}
