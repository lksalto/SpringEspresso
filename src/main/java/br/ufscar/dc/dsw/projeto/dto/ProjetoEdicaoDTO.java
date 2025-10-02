package br.ufscar.dc.dsw.projeto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public class ProjetoEdicaoDTO {

    @NotNull
    private UUID id;

    @NotBlank(message = "{NotBlank.projeto.nome}")
    private String nome;

    private String descricao;

    private List<Long> membrosIds;

    private List<Long> estrategiasIds;

    // Getters e Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public List<Long> getMembrosIds() { return membrosIds; }
    public void setMembrosIds(List<Long> membrosIds) { this.membrosIds = membrosIds; }
    public List<Long> getEstrategiasIds() { return estrategiasIds; }
    public void setEstrategiasIds(List<Long> estrategiasIds) { this.estrategiasIds = estrategiasIds; }
}
