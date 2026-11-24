package br.com.alunoonline.api.controller;

import br.com.alunoonline.api.dtos.AtualizarNotasRequestDTO;
import br.com.alunoonline.api.dtos.HistoricoAlunoResponseDTO;
import br.com.alunoonline.api.model.MatriculaAluno;
import br.com.alunoonline.api.service.MatriculaAlunoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/matriculas")
@Tag(name = "5. Matrículas", description = "Endpoints protegidos para gestão de matrículas, notas e histórico")
@SecurityRequirement(name = "bearer-key")
public class MatriculaAlunoController {

    @Autowired
    private MatriculaAlunoService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar nova matrícula para aluno")
    public void criarMatricula(@RequestBody MatriculaAluno matriculaAluno) {
        service.criarMatricula(matriculaAluno);
    }

    @PatchMapping("/trancar/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Trancar matrícula existente")
    public void trancarMatricula(@PathVariable Long id) {
        service.trancarMatricula(id);
    }

    @PatchMapping("/atualizar-notas/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Atualizar notas da matrícula e recalcular aprovação")
    public void atualizarNotas(@PathVariable Long id, @RequestBody AtualizarNotasRequestDTO request) {
        service.atualizarNotas(id, request);
    }

    @GetMapping("/emitir-historico/{alunoId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Emitir histórico escolar completo do aluno por ID")
    public HistoricoAlunoResponseDTO emitirHistorico(@PathVariable Long alunoId) {
        return service.emitirHistorico(alunoId);
    }
}
