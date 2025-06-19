// package br.ufscar.dc.dsw.dtos;

package br.ufscar.dc.dsw.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import br.ufscar.dc.dsw.models.enums.Papel;

public record UsuarioEdicaoDTO(
        UUID id,

        @NotBlank(message = "{usuario.nome.notBlank}")
        @Size(max = 256, message = "{usuario.nome.size}")
        String nome,

        @NotBlank(message = "{usuario.email.notBlank}")
        @Email(message = "{usuario.email.notValid}")
        @Size(max = 255, message = "{usuario.email.size}")
        String email,

        @Size(min = 6, max = 60, message = "{usuario.senha.size}")
        String senha, // Optional password change

        Papel papel
) {}