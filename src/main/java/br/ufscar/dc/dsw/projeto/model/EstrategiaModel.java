package br.ufscar.dc.dsw.projeto.model;
import jakarta.persistence.*;
@Entity
@Table(name = "estrategias")
public class EstrategiaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    
    @Column(columnDefinition="TEXT")
    private String descricao;
    
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
}

