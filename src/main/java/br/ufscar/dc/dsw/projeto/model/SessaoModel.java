package br.ufscar.dc.dsw.projeto.model;

import jakarta.persistence.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "sessoes")
public class SessaoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    private Duration duracao; // Mude de String para Duration

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projeto_id", nullable = false)
    private ProjetoModel projeto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estrategia_id", nullable = false)
    private EstrategiaModel estrategia;

    @OneToMany(mappedBy = "sessao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BugModel> bugs = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tester_id") // Adicione esta anotação
    private UsuarioModel tester; // Adicione este campo

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusSessao status = StatusSessao.CRIADO;


    public SessaoModel() {
    }

    public SessaoModel(String descricao, Duration duracao, ProjetoModel projeto, EstrategiaModel estrategia) {
        this.descricao = descricao;
        this.duracao = duracao;
        this.projeto = projeto;
        this.estrategia = estrategia;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Duration getDuracao() { // Mude o tipo de retorno
        return duracao;
    }

    public void setDuracao(Duration duracao) { // Mude o tipo do parâmetro
        this.duracao = duracao;
    }

    public ProjetoModel getProjeto() {
        return projeto;
    }

    public void setProjeto(ProjetoModel projeto) {
        this.projeto = projeto;
    }

    public EstrategiaModel getEstrategia() {
        return estrategia;
    }

    public void setEstrategia(EstrategiaModel estrategia) {
        this.estrategia = estrategia;
    }

    // Adicione os getters e setters para o novo campo 'tester'
    public UsuarioModel getTester() {
        return tester;
    }

    public void setTester(UsuarioModel tester) {
        this.tester = tester;
    }

    public StatusSessao getStatus() {
        return status;
    }

    public void setStatus(StatusSessao status) {
        this.status = status;
    }

    public List<BugModel> getBugs() {
        return bugs;
    }

    public void setBugs(List<BugModel> bugs) {
        this.bugs = bugs;
    }
}