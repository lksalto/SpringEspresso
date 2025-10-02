package br.ufscar.dc.dsw.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import br.ufscar.dc.dsw.models.enums.BugStatus;

@Entity
@Table(name = "bug")
public class BugModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    private LocalDateTime dataReporte = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projeto_id", nullable = false)
    private ProjetoModel projeto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    private UsuarioModel reporter;

    @Enumerated(EnumType.STRING) // salva como texto no banco
    @Column(nullable = false)
    private BugStatus status = BugStatus.ABERTO; // default

    // getters e setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public LocalDateTime getDataReporte() { return dataReporte; }
    public void setDataReporte(LocalDateTime dataReporte) { this.dataReporte = dataReporte; }

    public ProjetoModel getProjeto() { return projeto; }
    public void setProjeto(ProjetoModel projeto) { this.projeto = projeto; }

    public UsuarioModel getReporter() { return reporter; }
    public void setReporter(UsuarioModel reporter) { this.reporter = reporter; }

    public BugStatus getStatus() { return status; }
    public void setStatus(BugStatus status) { this.status = status; }
}