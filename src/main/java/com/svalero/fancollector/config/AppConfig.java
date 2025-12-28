package com.svalero.fancollector.config;

import com.svalero.fancollector.domain.Coleccion;
import com.svalero.fancollector.domain.Item;
import com.svalero.fancollector.domain.UsuarioColeccion;
import com.svalero.fancollector.domain.UsuarioItem;
import com.svalero.fancollector.dto.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mm = new ModelMapper();

        mm.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // Mapeo los datos del creador (coleccion a ColeccionOutDTO)
        mm.createTypeMap(Coleccion.class, ColeccionOutDTO.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getCreador().getId(),
                            ColeccionOutDTO::setIdCreador);
                    mapper.map(src -> src.getCreador().getNombre(),
                            ColeccionOutDTO::setNombreCreador);
                });

        // item a itemOutDTO (salida)
        mm.createTypeMap(Item.class, ItemOutDTO.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getColeccion().getId(),
                            ItemOutDTO::setIdColeccion);
                    mapper.map(src -> src.getColeccion().getNombre(),
                            ItemOutDTO::setNombreColeccion);
                });

        // itemInDTO a iItem (entrada)
        mm.createTypeMap(ItemInDTO.class, Item.class)
                .addMappings(mapper -> {
                    mapper.skip(Item::setId);
                    mapper.skip(Item::setColeccion);
                });

        // UsuarioColeccion a UsuarioColeccionOutDTO (salida)
        mm.createTypeMap(UsuarioColeccion.class, UsuarioColeccionOutDTO.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getUsuario().getId(),
                            UsuarioColeccionOutDTO::setIdUsuario);
                    mapper.map(src -> src.getColeccion().getId(),
                            UsuarioColeccionOutDTO::setIdColeccion);
                });

        // UsuarioItem a UsuarioItemOutDTO (salida)
        mm.createTypeMap(UsuarioItem.class, UsuarioItemOutDTO.class)
                .addMappings(mapper -> {

                    // parte de usuario
                    mapper.map(src -> src.getUsuario().getId(),
                            UsuarioItemOutDTO::setIdUsuario);
                    mapper.map(src -> src.getUsuario().getNombre(),
                            UsuarioItemOutDTO::setNombreUsuario);

                    // parte de coleccion
                    mapper.map(src -> src.getColeccion().getId(),
                            UsuarioItemOutDTO::setIdColeccion);
                    mapper.map(src -> src.getColeccion().getNombre(),
                            UsuarioItemOutDTO::setNombreColeccion);

                    // parte de item
                    mapper.map(src -> src.getItem().getId(),
                            UsuarioItemOutDTO::setIdItem);
                    mapper.map(src -> src.getItem().getNombre(),
                            UsuarioItemOutDTO::setNombreItem);
                });

        return mm;
    }
}


