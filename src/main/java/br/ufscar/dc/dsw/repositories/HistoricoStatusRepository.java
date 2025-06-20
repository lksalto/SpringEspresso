package br.ufscar.dc.dsw.repositories;

import br.ufscar.dc.dsw.models.HistoricoStatusModel;
import br.ufscar.dc.dsw.models.SessaoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HistoricoStatusRepository extends JpaRepository<HistoricoStatusModel, UUID> {

    /**
     * Busca todo o histórico de uma sessão, ordenado pela data/hora de forma ascendente.
     * O Spring Data JPA gera a query:
     * "SELECT h FROM HistoricoStatusSessao h WHERE h.sessao.id_sessao = :sessaoId ORDER BY h.dataHora ASC"
     *
     * @param sessaoId O ID da sessão para a qual o histórico será buscado.
     * @return Uma lista de objetos HistoricoStatusSessao.
     */
    List<HistoricoStatusModel> findBySessaoIdOrderByDataHoraAsc(UUID sessaoId);

    List<HistoricoStatusModel> findBySessaoOrderByDataHoraAsc(SessaoModel sessao);
}