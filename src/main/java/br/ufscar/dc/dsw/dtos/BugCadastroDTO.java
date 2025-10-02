package br.ufscar.dc.dsw.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record BugCadastroDTO(
        @NotNull
        UUID projetoId,

        @NotBlank(message = "{bug.titulo.notBlank}")
        @Size(min = 5, max = 100, message = "{bug.titulo.size}")
        String titulo,

        @NotBlank(message = "{bug.descricao.notBlank}")
        @Size(min = 10, max = 1000, message = "{bug.descricao.size}")
        String descricao
) {}