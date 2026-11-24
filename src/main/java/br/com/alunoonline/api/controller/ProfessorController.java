package br.com.alunoonline.api.controller;

import br.com.alunoonline.api.model.Professor;
import br.com.alunoonline.api.service.ProfessorService;
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
@RequestMapping("/professores")
@Tag(name = "3. Professores", description = "Endpoints protegidos para gerenciamento de professores")
@SecurityRequirement(name = "bearer-key")
public class ProfessorController {

    @Autowired
    private ProfessorService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar novo professor")
    public Professor criar(@RequestBody Professor professor) {
        return service.criar(professor);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Listar todos os professores")
    public List<Professor> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Buscar professor por ID")
    public Professor buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Atualizar professor por ID")
    public Professor atualizar(@PathVariable Long id, @RequestBody Professor professor) {
        return service.atualizar(id, professor);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletar professor por ID")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}
