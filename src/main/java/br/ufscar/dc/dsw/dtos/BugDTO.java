package br.ufscar.dc.dsw.dtos;

import java.time.LocalDateTime;
import java.util.UUID;
import br.ufscar.dc.dsw.models.enums.BugStatus;
public record BugDTO(
    UUID id,
    String titulo,
    String descricao,
    String reporterNome,
    LocalDateTime dataReporte,
    UUID projetoId,
    BugStatus status
) {}


