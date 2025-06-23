package br.ufscar.dc.dsw.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.Objects;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "estrategia")
public class EstrategiaModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_estrategia", columnDefinition = "binary(16)")
    private UUID id;

    @Column(nullable = false, unique = true, length = 255)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @JsonManagedReference("estrategia-dicas")
    @OneToMany(mappedBy = "estrategia", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<DicaModel> dicas = new HashSet<>();

    @JsonManagedReference("estrategia-exemplos")
    @OneToMany(mappedBy = "estrategia", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<ExemploModel> exemplos = new HashSet<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EstrategiaModel that = (EstrategiaModel) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}