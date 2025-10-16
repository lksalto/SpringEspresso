package br.ufscar.dc.dsw.projeto.repository;
import br.ufscar.dc.dsw.projeto.model.ProjetoModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjetoRepository extends JpaRepository<ProjetoModel, Long> {}