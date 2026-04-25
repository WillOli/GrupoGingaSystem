package model;

public enum Graduacao {
    VERDE_1_VOLTA("Verde 1 Volta"),
    VERDE_2_VOLTAS("Verde 2 Voltas"),
    AMARELO_1_VOLTA("Amarelo 1 Volta"),
    AMARELO_2_VOLTA("Amarelo 2 Volta"),
    // Adicionar as demais conforme a ordem oficial
    MONITOR("Monitor"),
    INSTRUTOR("Instrutor"),
    PROFESSOR("Professor"),
    CONTRA_MESTRE("Contra-Mestre"),
    MESTRE("Mestre");

    private String descricao;

    Graduacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
