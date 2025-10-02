package br.ufscar.dc.dsw.repositories;

import br.ufscar.dc.dsw.models.EstrategiaModel;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstrategiaRepository extends JpaRepository<EstrategiaModel, UUID> {
    List<EstrategiaModel> findByProjetoIsNull();
}