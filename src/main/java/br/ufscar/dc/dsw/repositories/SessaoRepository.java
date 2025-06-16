package br.ufscar.dc.dsw.repositories;

import br.ufscar.dc.dsw.models.SessaoModel;
import br.ufscar.dc.dsw.models.enums.StatusSessao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SessaoRepository extends JpaRepository<SessaoModel, UUID> {

    List<SessaoModel> findByProjeto_Id(UUID projetoId);
    List<SessaoModel> findByTester_Id(UUID testerId);
    List<SessaoModel> findByStatus(StatusSessao status);
    List<SessaoModel> findByProjeto_IdAndStatus(UUID projetoId, StatusSessao status);
}