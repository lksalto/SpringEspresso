package br.ufscar.dc.dsw.repositories;

import br.ufscar.dc.dsw.models.ReviewModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewRepository extends JpaRepository<ReviewModel, UUID> {
}
