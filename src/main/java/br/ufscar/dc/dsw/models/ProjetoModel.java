package br.ufscar.dc.dsw.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "projeto")
public class ProjetoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_projeto", columnDefinition = "binary(16)")
    private UUID id;

    @Column(nullable = false, unique = true, length = 255)
    private String nome;

    @Column(length = 1000)
    private String descricao;

    private LocalDate dataCriacao = LocalDate.now();

    @ManyToMany
    @JoinTable(
            name = "projeto_membros",
            joinColumns = @JoinColumn(name = "projeto_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private Set<UsuarioModel> membros = new HashSet<>();

    // Getters e Setters

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

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Set<UsuarioModel> getMembros() {
        return membros;
    }

    public void setMembros(Set<UsuarioModel> membros) {
        this.membros = membros;
    }
}
