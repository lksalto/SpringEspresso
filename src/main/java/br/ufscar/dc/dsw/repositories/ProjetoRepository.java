package br.ufscar.dc.dsw.repositories;

import br.ufscar.dc.dsw.models.ProjetoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjetoRepository extends JpaRepository<ProjetoModel, UUID> {

    Optional<ProjetoModel> findByNome(String nome);
    boolean existsByNome(String nome);

}