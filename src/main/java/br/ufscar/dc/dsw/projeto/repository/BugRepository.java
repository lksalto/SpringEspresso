package br.ufscar.dc.dsw.projeto.repository;

import br.ufscar.dc.dsw.projeto.model.BugModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BugRepository extends JpaRepository<BugModel, Long> {
    
    List<BugModel> findBySessaoId(Long sessaoId);
}
