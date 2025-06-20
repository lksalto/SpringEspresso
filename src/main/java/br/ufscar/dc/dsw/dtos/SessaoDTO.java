package br.ufscar.dc.dsw.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Duration;
import java.util.UUID;

public record SessaoDTO(
        @NotNull
        UUID projetoId,

        @NotNull
        UUID estrategiaId,

        @NotNull
        Duration duracao,

        @NotBlank
        String descricao
) {
}