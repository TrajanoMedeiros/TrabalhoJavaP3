package br.com.alunoonline.api.service;

import br.com.alunoonline.api.dtos.AtualizarNotasRequestDTO;
import br.com.alunoonline.api.dtos.DisciplinasAlunoResponseDTO;
import br.com.alunoonline.api.dtos.HistoricoAlunoResponseDTO;
import br.com.alunoonline.api.model.Aluno;
import br.com.alunoonline.api.model.Disciplina;
import br.com.alunoonline.api.model.MatriculaAluno;
import br.com.alunoonline.api.model.MatriculaAlunoStatusEnum;
import br.com.alunoonline.api.repository.MatriculaAlunoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MatriculaAlunoService {

    @Autowired
    private MatriculaAlunoRepository repository;

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private DisciplinaService disciplinaService;

    public void criarMatricula(MatriculaAluno matriculaAluno) {
        if (matriculaAluno.getAluno() == null || matriculaAluno.getAluno().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID do aluno é obrigatório");
        }
        if (matriculaAluno.getDisciplina() == null || matriculaAluno.getDisciplina().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID da disciplina é obrigatório");
        }

        Aluno aluno = alunoService.buscarPorId(matriculaAluno.getAluno().getId());
        Disciplina disciplina = disciplinaService.buscarPorId(matriculaAluno.getDisciplina().getId());

        matriculaAluno.setAluno(aluno);
        matriculaAluno.setDisciplina(disciplina);
        matriculaAluno.setStatus(MatriculaAlunoStatusEnum.MATRICULADO);
        repository.save(matriculaAluno);
    }

    public void trancarMatricula(Long id) {
        MatriculaAluno matricula = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Matrícula não encontrada"));

        if (!MatriculaAlunoStatusEnum.MATRICULADO.equals(matricula.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Só é possível trancar matrículas com status MATRICULADO");
        }

        matricula.setStatus(MatriculaAlunoStatusEnum.TRANCADO);
        repository.save(matricula);
    }

    public void atualizarNotas(Long id, AtualizarNotasRequestDTO request) {
        MatriculaAluno matricula = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Matrícula não encontrada"));

        if (request.getNota1() != null) {
            matricula.setNota1(request.getNota1());
        }
        if (request.getNota2() != null) {
            matricula.setNota2(request.getNota2());
        }

        // Calcula a média e atualiza o status se ambas as notas estiverem presentes
        if (matricula.getNota1() != null && matricula.getNota2() != null) {
            double media = (matricula.getNota1() + matricula.getNota2()) / 2.0;
            if (media >= 7.0) {
                matricula.setStatus(MatriculaAlunoStatusEnum.APROVADO);
            } else {
                matricula.setStatus(MatriculaAlunoStatusEnum.REPROVADO);
            }
        }

        repository.save(matricula);
    }

    public HistoricoAlunoResponseDTO emitirHistorico(Long alunoId) {
        Aluno aluno = alunoService.buscarPorId(alunoId);
        List<MatriculaAluno> matriculas = repository.findByAlunoId(alunoId);

        HistoricoAlunoResponseDTO historico = new HistoricoAlunoResponseDTO();
        historico.setNomeAluno(aluno.getNome());
        historico.setEmailAluno(aluno.getEmail());
        historico.setCpfAluno(aluno.getCpf());

        List<DisciplinasAlunoResponseDTO> disciplinasList = matriculas.stream().map(m -> {
            DisciplinasAlunoResponseDTO dto = new DisciplinasAlunoResponseDTO();
            dto.setNomeDisciplina(m.getDisciplina().getNome());
            
            if (m.getDisciplina().getProfessor() != null) {
                dto.setNomeProfessor(m.getDisciplina().getProfessor().getNome());
            } else {
                dto.setNomeProfessor("Sem professor atribuído");
            }
            
            dto.setNota1(m.getNota1());
            dto.setNota2(m.getNota2());

            if (m.getNota1() != null && m.getNota2() != null) {
                dto.setMedia((m.getNota1() + m.getNota2()) / 2.0);
            } else {
                dto.setMedia(null);
            }

            dto.setStatus(m.getStatus());
            return dto;
        }).toList();

        historico.setDisciplinas(disciplinasList);
        return historico;
    }
}
