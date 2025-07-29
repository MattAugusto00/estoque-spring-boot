package com.estoque.estoque_api.repository;

import com.estoque.estoque_api.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    Optional<Produto> findByNomeAndDescricao(String nome, String descricao);

    @Query("SELECT p FROM Produto p " +
            "WHERE (:nome IS NULL OR LOWER(COALESCE(p.nome, '')) LIKE LOWER(CONCAT('%', :nome, '%'))) " +
            "AND (:descricao IS NULL OR LOWER(COALESCE(p.descricao, '')) LIKE LOWER(CONCAT('%', :descricao, '%')))")
    List<Produto> buscarPorFiltros(@Param("nome") String nome, @Param("descricao") String descricao);
}
