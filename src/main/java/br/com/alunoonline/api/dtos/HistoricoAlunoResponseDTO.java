package br.com.alunoonline.api.dtos;

import java.util.List;
import lombok.Data;

@Data
public class HistoricoAlunoResponseDTO {
    private String nomeAluno;
    private String emailAluno;
    private String cpfAluno;
    private List<DisciplinasAlunoResponseDTO> disciplinas;
}
