package br.ufscar.dc.dsw.models;

import br.ufscar.dc.dsw.models.enums.Papel;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuario")
public class UsuarioModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @Column(nullable = false, length = 256)
    private String nome;

    @Column(nullable = false, unique = true, length = 255, name = "email")
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false, length = 60)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Papel papel;

    @JsonBackReference("projeto-membros")
    @ManyToMany(mappedBy = "membros", fetch = FetchType.LAZY)
    private Set<ProjetoModel> projetos = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Papel getPapel() {
        return papel;
    }

    public void setPapel(Papel papel) {
        this.papel = papel;
    }

    public Set<ProjetoModel> getProjetos() {
        return projetos;
    }

    public void setProjetos(Set<ProjetoModel> projetos) {
        this.projetos = projetos;
    }
}
