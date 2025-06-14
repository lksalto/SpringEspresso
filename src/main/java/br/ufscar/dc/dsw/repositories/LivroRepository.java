package br.ufscar.dc.dsw.repositories;

import br.ufscar.dc.dsw.models.LivroModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface LivroRepository extends JpaRepository<LivroModel, UUID> {
    LivroModel findLivroModelByTitulo(String titulo);

    @Query(value = "SELECT * FROM tb_livro WHERE publisher_id=  :id", nativeQuery = true)
    List<LivroModel> findLivroModelByPublisherId(@Param("id") UUID id);
}
