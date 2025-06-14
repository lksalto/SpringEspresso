package br.ufscar.dc.dsw.repositories;

import br.ufscar.dc.dsw.models.PublisherModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PublisherRepository extends JpaRepository<PublisherModel, UUID> {
}
