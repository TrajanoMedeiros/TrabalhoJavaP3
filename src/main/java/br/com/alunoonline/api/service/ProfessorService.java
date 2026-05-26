package br.com.alunoonline.api.service;

import br.com.alunoonline.api.model.Professor;
import br.com.alunoonline.api.repository.ProfessorRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProfessorService {

    @Autowired
    private ProfessorRepository repository;

    public Professor criar(Professor professor) {
        return repository.save(professor);
    }

    public List<Professor> listarTodos() {
        return repository.findAll();
    }

    public Professor buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor não encontrado"));
    }

    public void deletar(Long id) {
        Professor professor = buscarPorId(id);
        repository.delete(professor);
    }

    public Professor atualizar(Long id, Professor professorAtualizado) {
        Professor professor = buscarPorId(id);
        professor.setNome(professorAtualizado.getNome());
        professor.setEmail(professorAtualizado.getEmail());
        professor.setCpf(professorAtualizado.getCpf());
        return repository.save(professor);
    }
}
