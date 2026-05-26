package br.com.alunoonline.api.service;

import br.com.alunoonline.api.model.Disciplina;
import br.com.alunoonline.api.model.Professor;
import br.com.alunoonline.api.repository.DisciplinaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class DisciplinaService {

    @Autowired
    private DisciplinaRepository repository;

    @Autowired
    private ProfessorService professorService;

    public Disciplina criar(Disciplina disciplina) {
        if (disciplina.getProfessor() != null && disciplina.getProfessor().getId() != null) {
            Professor professor = professorService.buscarPorId(disciplina.getProfessor().getId());
            disciplina.setProfessor(professor);
        }
        return repository.save(disciplina);
    }

    public List<Disciplina> listarTodos() {
        return repository.findAll();
    }

    public Disciplina buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Disciplina não encontrada"));
    }

    public void deletar(Long id) {
        Disciplina disciplina = buscarPorId(id);
        repository.delete(disciplina);
    }

    public Disciplina atualizar(Long id, Disciplina disciplinaAtualizada) {
        Disciplina disciplina = buscarPorId(id);
        disciplina.setNome(disciplinaAtualizada.getNome());
        disciplina.setCargaHoraria(disciplinaAtualizada.getCargaHoraria());

        if (disciplinaAtualizada.getProfessor() != null && disciplinaAtualizada.getProfessor().getId() != null) {
            Professor professor = professorService.buscarPorId(disciplinaAtualizada.getProfessor().getId());
            disciplina.setProfessor(professor);
        } else {
            disciplina.setProfessor(null);
        }

        return repository.save(disciplina);
    }
}
