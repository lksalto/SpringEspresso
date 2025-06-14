package br.ufscar.dc.dsw.models;

import br.ufscar.dc.dsw.models.enums.StatusSessao;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "historico_status_sessao")
public class HistoricoStatusModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_historico")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_anterior")
    private StatusSessao statusAnterior;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_novo", nullable = false)
    private StatusSessao statusNovo;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sessao", nullable = false)
    private SessaoModel sessao;

    @PrePersist
    protected void onCreate() {
        dataHora = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StatusSessao getStatusAnterior() {
        return statusAnterior;
    }

    public void setStatusAnterior(StatusSessao statusAnterior) {
        this.statusAnterior = statusAnterior;
    }

    public StatusSessao getStatusNovo() {
        return statusNovo;
    }

    public void setStatusNovo(StatusSessao statusNovo) {
        this.statusNovo = statusNovo;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public SessaoModel getSessao() {
        return sessao;
    }

    public void setSessao(SessaoModel sessao) {
        this.sessao = sessao;
    }
}
