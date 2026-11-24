package br.com.alunoonline.api.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {

    @Schema(description = "Login do usuário", example = "admin")
    private String login;

    @Schema(description = "Senha do usuário", example = "123")
    private String password;
}
