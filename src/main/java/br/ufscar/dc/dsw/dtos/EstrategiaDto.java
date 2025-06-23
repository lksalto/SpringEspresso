package br.ufscar.dc.dsw.dtos;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class EstrategiaDto {
    private UUID id;
    private String nome;
    private String descricao;
    private Set<DicaDto> dicas = new HashSet<>();
    private Set<ExemploDto> exemplos = new HashSet<>();

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public Set<DicaDto> getDicas() { return dicas; }
    public void setDicas(Set<DicaDto> dicas) { this.dicas = dicas; }
    public Set<ExemploDto> getExemplos() { return exemplos; }
    public void setExemplos(Set<ExemploDto> exemplos) { this.exemplos = exemplos; }
}