package com.svalero.fancollector.config;

import com.svalero.fancollector.domain.Coleccion;
import com.svalero.fancollector.domain.Item;
import com.svalero.fancollector.dto.ColeccionOutDTO;
import com.svalero.fancollector.dto.ItemInDTO;
import com.svalero.fancollector.dto.ItemOutDTO;
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

        return mm;
    }
}


