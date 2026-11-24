package br.com.alunoonline.api.controller;

import br.com.alunoonline.api.model.Aluno;
import br.com.alunoonline.api.service.AlunoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/alunos")
@Tag(name = "2. Alunos", description = "Endpoints protegidos para gerenciamento de alunos")
@SecurityRequirement(name = "bearer-key")
public class AlunoController {

    @Autowired
    private AlunoService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar novo aluno")
    public Aluno criar(@RequestBody Aluno aluno) {
        return service.criar(aluno);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Listar todos os alunos")
    public List<Aluno> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Buscar aluno por ID")
    public Aluno buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Atualizar dados do aluno por ID")
    public Aluno atualizar(@PathVariable Long id, @RequestBody Aluno aluno) {
        return service.atualizar(id, aluno);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletar aluno por ID")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}
