package com.svalero.fancollector.repository;

import com.svalero.fancollector.domain.UsuarioColeccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UsuarioColeccionRepository extends JpaRepository<UsuarioColeccion, Long> {

    @Query("""
            SELECT uc FROM UsuarioColeccion uc
            WHERE (:idUsuario IS NULL OR uc.usuario.id = :idUsuario)
              AND (:idColeccion IS NULL OR uc.coleccion.id = :idColeccion)
              AND (:soloFavoritas IS NULL OR uc.esFavorita = :soloFavoritas)
              AND (:esVisible IS NULL OR uc.esVisible = :esVisible)
            """)
    List<UsuarioColeccion> buscarPorFiltros(Long idUsuario,
                                            Long idColeccion,
                                            Boolean soloFavoritas,
                                            Boolean esVisible);

    boolean existsByUsuario_IdAndColeccion_Id(Long usuarioId, Long coleccionId);
}
