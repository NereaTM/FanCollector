package com.svalero.fancollector.dto.patches;

import com.svalero.fancollector.domain.enums.RarezaItem;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ItemRarezaDTO {

    @NotNull(message = "La rareza tiene que ser : COMUN, RARO, EPICO, LEGENDARIO")
    private RarezaItem rareza;
}