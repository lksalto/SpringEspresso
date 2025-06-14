package br.ufscar.dc.dsw.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "estrategia")
public class EstrategiaModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_estrategia")
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @OneToMany(mappedBy = "estrategia", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<DicaModel> dicas = new HashSet<>();

    @OneToMany(mappedBy = "estrategia", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<ExemploModel> exemplos = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Set<DicaModel> getDicas() {
        return dicas;
    }

    public void setDicas(Set<DicaModel> dicas) {
        this.dicas = dicas;
    }

    public Set<ExemploModel> getExemplos() {
        return exemplos;
    }

    public void setExemplos(Set<ExemploModel> exemplos) {
        this.exemplos = exemplos;
    }
}