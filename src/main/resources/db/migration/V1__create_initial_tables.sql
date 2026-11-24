CREATE TABLE aluno (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(20) NOT NULL,
    email VARCHAR(255) NOT NULL
);

CREATE TABLE professor (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(20) NOT NULL,
    email VARCHAR(255) NOT NULL
);

CREATE TABLE disciplina (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    carga_horaria INT,
    professor_id BIGINT,
    CONSTRAINT fk_disciplina_professor FOREIGN KEY (professor_id) REFERENCES professor(id) ON DELETE SET NULL
);

CREATE TABLE matricula_aluno (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    aluno_id BIGINT NOT NULL,
    disciplina_id BIGINT NOT NULL,
    nota1 DOUBLE,
    nota2 DOUBLE,
    status VARCHAR(50) NOT NULL,
    CONSTRAINT fk_matricula_aluno FOREIGN KEY (aluno_id) REFERENCES aluno(id) ON DELETE CASCADE,
    CONSTRAINT fk_matricula_disciplina FOREIGN KEY (disciplina_id) REFERENCES disciplina(id) ON DELETE CASCADE
);
