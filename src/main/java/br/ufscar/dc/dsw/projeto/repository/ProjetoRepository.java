package br.ufscar.dc.dsw.projeto.repository;
import br.ufscar.dc.dsw.projeto.model.ProjetoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ProjetoRepository extends JpaRepository<ProjetoModel, UUID> {}