package br.ufscar.dc.dsw.dtos;

import java.util.Set;
import java.util.UUID;

public record LivroRecordDto(
        String titulo,
        UUID publisherId,
        Set<UUID> autorIds,
        String reviewComentario
) {
}
