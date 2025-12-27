package com.svalero.fancollector.repository;

import com.svalero.fancollector.domain.Item;
import com.svalero.fancollector.domain.enums.RarezaItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("""
    SELECT i FROM Item i WHERE
    (:nombre IS NULL OR :nombre = '' OR LOWER(i.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))
    AND (:tipo IS NULL OR :tipo = '' OR LOWER(i.tipo) LIKE LOWER(CONCAT('%', :tipo, '%')))
    AND (:rareza IS NULL OR i.rareza = :rareza)
    AND (:idColeccion IS NULL OR i.coleccion.id = :idColeccion)
""")
    List<Item> buscarPorFiltros(
            @Param("nombre") String nombre,
            @Param("tipo") String tipo,
            @Param("rareza") RarezaItem rareza,
            @Param("idColeccion") Long idColeccion
    );
}