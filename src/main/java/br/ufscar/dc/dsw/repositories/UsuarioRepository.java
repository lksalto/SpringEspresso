package br.ufscar.dc.dsw.repositories;

import java.util.Optional;

import br.ufscar.dc.dsw.models.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {
    // Optional usado para caso retorne null
    Optional<UsuarioModel> findByLogin(String login);

}