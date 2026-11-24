package br.com.alunoonline.api.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AtualizarNotasRequestDTO {

    @Schema(description = "Nota do 1º Bimestre", example = "9.5")
    private Double nota1;

    @Schema(description = "Nota do 2º Bimestre", example = "10.0")
    private Double nota2;
}
