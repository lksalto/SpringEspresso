package br.ufscar.dc.dsw.repositories;

import br.ufscar.dc.dsw.models.AutorModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AutorRepository extends JpaRepository<AutorModel, UUID> {
}
