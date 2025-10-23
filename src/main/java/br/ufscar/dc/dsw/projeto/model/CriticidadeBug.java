package br.ufscar.dc.dsw.projeto.model;

public enum CriticidadeBug {
    BAIXA("Baixa", "#28a745"),
    MEDIA("Média", "#ffc107"), 
    ALTA("Alta", "#fd7e14"),
    CRITICA("Crítica", "#dc3545");

    private final String descricao;
    private final String cor;

    CriticidadeBug(String descricao, String cor) {
        this.descricao = descricao;
        this.cor = cor;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getCor() {
        return cor;
    }
}