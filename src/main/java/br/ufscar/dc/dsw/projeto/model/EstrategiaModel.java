package br.ufscar.dc.dsw.projeto.model;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
@Entity
@Table(name = "estrategias")
public class EstrategiaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String descricao;

    @ManyToMany
    @JoinTable(
        name = "estrategia_projeto",
        joinColumns = @JoinColumn(name = "estrategia_id"),
        inverseJoinColumns = @JoinColumn(name = "projeto_id")
    )
    private List<ProjetoModel> projetos = new ArrayList<>();

    public EstrategiaModel() {} // <--- precisa do construtor padrão



    public Long getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public List<ProjetoModel> getProjetos() { return projetos; }
    public void setProjetos(List<ProjetoModel> projetos) { this.projetos = projetos; }
}
