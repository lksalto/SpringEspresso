package br.ufscar.dc.dsw.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "dica")
public class DicaModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_dica")
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String dica;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estrategia", nullable = false)
    private EstrategiaModel estrategia;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDica() {
        return dica;
    }

    public void setDica(String dica) {
        this.dica = dica;
    }

    public EstrategiaModel getEstrategia() {
        return estrategia;
    }

    public void setEstrategia(EstrategiaModel estrategia) {
        this.estrategia = estrategia;
    }
}