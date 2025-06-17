package br.ufscar.dc.dsw.repositories;

import br.ufscar.dc.dsw.models.EstrategiaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EstrategiaRepository extends JpaRepository<EstrategiaModel, UUID> {

    
    List<EstrategiaModel> findAllByOrderByNomeAsc();

    Optional<EstrategiaModel> findByNome(String nome);

    boolean existsByNome(String nome);

}