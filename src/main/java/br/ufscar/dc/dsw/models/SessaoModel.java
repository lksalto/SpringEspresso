package br.ufscar.dc.dsw.models;

import br.ufscar.dc.dsw.models.enums.StatusSessao;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sessao")
public class SessaoModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_sessao")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_projeto", nullable = false)
    private ProjetoModel projeto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tester", nullable = false)
    private UsuarioModel tester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estrategia", nullable = false)
    private EstrategiaModel estrategia;

    private LocalTime duracao;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusSessao status;

    @OneToMany(mappedBy = "sessao", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<HistoricoStatusModel> historico = new HashSet<>();

    @OneToMany(mappedBy = "sessao", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<BugModel> bugs = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProjetoModel getProjeto() {
        return projeto;
    }

    public void setProjeto(ProjetoModel projeto) {
        this.projeto = projeto;
    }

    public UsuarioModel getTester() {
        return tester;
    }

    public void setTester(UsuarioModel tester) {
        this.tester = tester;
    }

    public EstrategiaModel getEstrategia() {
        return estrategia;
    }

    public void setEstrategia(EstrategiaModel estrategia) {
        this.estrategia = estrategia;
    }

    public LocalTime getDuracao() {
        return duracao;
    }

    public void setDuracao(LocalTime duracao) {
        this.duracao = duracao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public StatusSessao getStatus() {
        return status;
    }

    public void setStatus(StatusSessao status) {
        this.status = status;
    }

    public Set<HistoricoStatusModel> getHistorico() {
        return historico;
    }

    public void setHistorico(Set<HistoricoStatusModel> historico) {
        this.historico = historico;
    }

    public Set<BugModel> getBugs() {
        return bugs;
    }

    public void setBugs(Set<BugModel> bugs) {
        this.bugs = bugs;
    }
}
