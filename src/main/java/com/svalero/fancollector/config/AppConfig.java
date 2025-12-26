package com.svalero.fancollector.config;

import com.svalero.fancollector.domain.Coleccion;
import com.svalero.fancollector.dto.ColeccionOutDTO;
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

        return mm;
    }
}


