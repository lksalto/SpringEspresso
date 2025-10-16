package br.ufscar.dc.dsw.projeto.model;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "projeto")
public class ProjetoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String descricao;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<EstrategiaModel> estrategias = new ArrayList<>();

    // getters e setters

    public ProjetoModel() {}
    public ProjetoModel(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
        this.estrategias = new ArrayList<>();
    }
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public List<EstrategiaModel> getEstrategias() { return estrategias; }
    public void setEstrategias(List<EstrategiaModel> estrategias) { this.estrategias = estrategias; }
}


