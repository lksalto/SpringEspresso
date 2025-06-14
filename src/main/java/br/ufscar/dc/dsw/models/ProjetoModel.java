package br.ufscar.dc.dsw.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "projeto")
public class ProjetoModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_projeto", columnDefinition = "binary(16)")
    private UUID id;

    @Column(nullable = false, unique = true, length = 255)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false, updatable = false)
    private LocalDate dataCriacao;

    @JsonManagedReference("projeto-membros")
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "membro_projeto",
            joinColumns = @JoinColumn(name = "id_projeto"),
            inverseJoinColumns = @JoinColumn(name = "id_usuario")
    )
    private Set<UsuarioModel> membros = new HashSet<>();

    @JsonManagedReference("projeto-sessoes")
    @OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SessaoModel> sessoes = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        this.dataCriacao = LocalDate.now();
    }

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

    public Set<SessaoModel> getSessoes() {
        return sessoes;
    }

    public void setSessoes(Set<SessaoModel> sessoes) {
        this.sessoes = sessoes;
    }
}