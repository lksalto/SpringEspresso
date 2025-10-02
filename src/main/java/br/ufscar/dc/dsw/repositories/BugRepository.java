package br.ufscar.dc.dsw.repositories;

import br.ufscar.dc.dsw.models.BugModel;
import br.ufscar.dc.dsw.models.enums.BugStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BugRepository extends JpaRepository<BugModel, UUID> {

    // Buscar todos os bugs de um projeto específico
    List<BugModel> findByProjetoId(UUID projetoId);

    // Buscar todos os bugs de um projeto com um status específico
    List<BugModel> findByProjetoIdAndStatus(UUID projetoId, BugStatus status);

    // Contar bugs de um projeto com um status específico
    long countByProjetoIdAndStatus(UUID projetoId, BugStatus status);

    // Buscar todos os bugs por status
    List<BugModel> findByStatus(BugStatus status);

    // Outros métodos úteis, caso precise
    List<BugModel> findByTituloContainingIgnoreCase(String titulo);
    List<BugModel> findByDescricaoContainingIgnoreCase(String descricao);
}
