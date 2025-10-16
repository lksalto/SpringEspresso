package br.ufscar.dc.dsw.projeto.repository;

import br.ufscar.dc.dsw.projeto.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {
    // GARANTA QUE ESTE MÉTODO EXISTA
    UsuarioModel findByEmail(String email);
}