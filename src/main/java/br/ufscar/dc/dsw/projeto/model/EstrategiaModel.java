package br.ufscar.dc.dsw.projeto.model;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "estrategias")
public class EstrategiaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    
    @Column(columnDefinition="TEXT")
    private String descricao;

    // NOVO: Relacionamento com Dicas
    @OneToMany(mappedBy = "estrategia", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DicaModel> dicas = new ArrayList<>();

    // NOVO: Relacionamento com Exemplos
    @OneToMany(mappedBy = "estrategia", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ExemploModel> exemplos = new ArrayList<>();
    
    public EstrategiaModel() {}
    public EstrategiaModel(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }   



    public Long getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    // NOVO: Getters e Setters para as listas
    public List<DicaModel> getDicas() { return dicas; }
    public void setDicas(List<DicaModel> dicas) { this.dicas = dicas; }
    public List<ExemploModel> getExemplos() { return exemplos; }
    public void setExemplos(List<ExemploModel> exemplos) { this.exemplos = exemplos; }
}

