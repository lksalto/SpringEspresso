package br.ufscar.dc.dsw.projeto.repository;

import br.ufscar.dc.dsw.projeto.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {
    UsuarioModel findByEmail(String email);
}