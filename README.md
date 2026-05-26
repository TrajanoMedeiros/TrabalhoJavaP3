# Aluno Online API 🎓

Este é o projeto **Aluno Online**, uma API REST desenvolvida em **Java 21** com o framework **Spring Boot** e persistência de dados em **PostgreSQL**. O projeto foi estruturado seguindo as melhores práticas de desenvolvimento backend, adotando a arquitetura em camadas e utilizando anotações modernas (como Spring Data JPA e Lombok) para manter o código limpo, legível e de fácil manutenção.

---

## 🚀 1. Explicação do Projeto

O **Aluno Online** é um sistema de controle acadêmico simplificado. Ele permite que instituições de ensino gerenciem seus principais pilares operacionais:
*   **Alunos:** Cadastro e manutenção dos dados básicos de estudantes.
*   **Professores:** Cadastro de docentes responsáveis pelas disciplinas.
*   **Disciplinas:** Cadastro de matérias escolares, associando-as ao seu respectivo professor e definindo a carga horária.
*   **Matrículas (`MatriculaAluno`):** O coração do sistema, responsável por gerenciar a vinculação entre alunos e disciplinas, registrar as notas de avaliações (`nota1` e `nota2`), calcular médias e definir automaticamente a situação acadêmica do estudante (Aprovado ou Reprovado), além de permitir o trancamento de curso.

---

## 🏛️ 2. Arquitetura Utilizada

A aplicação foi desenvolvida seguindo o padrão de **Arquitetura em Camadas (Tiered Architecture)**, o qual garante uma excelente separação de responsabilidades:

```mermaid
graph TD
    Client[Cliente / Insomnia] <--> |JSON / HTTP| Controller[Camada Controller]
    Controller <--> |Chamadas de Métodos| Service[Camada Service / Regras de Negócio]
    Service <--> |Operações JPA| Repository[Camada Repository / Banco de Dados]
    Repository <--> |SQL / JDBC| PostgreSQL[(Banco de Dados PostgreSQL)]
    
    subgraph DTOs [Objetos de Transferência de Dados]
        dt1[AtualizarNotasRequestDTO]
        dt2[HistoricoAlunoResponseDTO]
        dt3[DisciplinasAlunoResponseDTO]
    end
    
    Service -.-> |Utiliza| DTOs
```

1.  **Camada Model (Entidades):** Representa as tabelas do banco de dados mapeadas através do JPA (Java Persistence API). Localizadas em `br.com.alunoonline.api.model`.
2.  **Camada Repository:** Interfaces que estendem `JpaRepository`, fornecendo todos os métodos de persistência (CRUD) de forma transparente. Localizadas em `br.com.alunoonline.api.repository`.
3.  **Camada Service:** Onde reside toda a lógica de negócio do sistema (por exemplo: cálculo de média para aprovação, validação de status de matrícula para trancamento). Localizadas em `br.com.alunoonline.api.service`.
4.  **Camada Controller:** Porta de entrada da API. Recebe as requisições HTTP, valida os corpos (JSON) e direciona para as regras de negócio adequadas na camada Service. Localizadas in `br.com.alunoonline.api.controller`.
5.  **Camada DTO (Data Transfer Objects):** Classes auxiliares para transferir dados customizados e formatar respostas seguras para a API (ex: o Histórico Escolar). Localizadas em `br.com.alunoonline.api.dtos`.

---

## 💻 3. Detalhamento do Código e Entidades

### A. Aluno (`Aluno.java`)
Mapeia a tabela `aluno` no banco de dados.
*   `id` (Long, Chave Primária, Autoincremento)
*   `nome` (String): Nome completo do estudante.
*   `cpf` (String): CPF para fins cadastrais.
*   `email` (String): Endereço eletrônico de contato.

### B. Professor (`Professor.java`)
Mapeia a tabela `professor` no banco de dados.
*   `id` (Long, Chave Primária, Autoincremento)
*   `nome` (String): Nome completo do docente.
*   `cpf` (String): CPF do docente.
*   `email` (String): E-mail corporativo.

### C. Disciplina (`Disciplina.java`)
Mapeia a tabela `disciplina`. Possui um relacionamento **Muitos-para-Um (`@ManyToOne`)** com a entidade Professor.
*   `id` (Long, Chave Primária, Autoincremento)
*   `nome` (String): Nome da matéria.
*   `cargaHoraria` (Integer): Quantidade de horas da matéria.
*   `professor` (Professor): Instância do docente responsável. Mapeado no banco como a chave estrangeira `professor_id`.

### D. Matrícula (`MatriculaAluno.java` & `MatriculaAlunoStatusEnum.java`)
Intermedia a relação entre Aluno e Disciplina. Possui relacionamentos **Muitos-para-Um (`@ManyToOne`)** com ambas as entidades.
*   `id` (Long, Chave Primária, Autoincremento)
*   `aluno` (Aluno): Aluno matriculado (`aluno_id`).
*   `disciplina` (Disciplina): Disciplina vinculada (`disciplina_id`).
*   `nota1` (Double): Primeira nota avaliativa.
*   `nota2` (Double): Segunda nota avaliativa.
*   `status` (MatriculaAlunoStatusEnum): Pode assumir quatro estados:
    *   `MATRICULADO`: Status inicial automático ao efetuar a matrícula.
    *   `TRANCADO`: Definido ao trancar a matrícula (não permite notas).
    *   `APROVADO`: Definido automaticamente se a média `(nota1 + nota2) / 2` for $\ge 7.0$.
    *   `REPROVADO`: Definido automaticamente se a média `(nota1 + nota2) / 2` for $< 7.0$.

---

## 📈 4. Regras de Negócio e Métodos de Matrícula

A classe `MatriculaAlunoService` implementa as principais regras acadêmicas da instituição:

*   **Matricular Aluno (`criarMatricula`)**: Cria o vínculo entre um aluno e uma disciplina. Define por padrão o status inicial como `MATRICULADO`. Valida a existência prévia de ambos no banco de dados.
*   **Trancar Matrícula (`trancarMatricula`)**: Altera o status de uma matrícula específica para `TRANCADO`. Apenas matrículas que estão com o status ativo (`MATRICULADO`) podem ser trancadas. Caso contrário, gera um erro HTTP 400 (Bad Request).
*   **Atualizar Notas (`atualizarNotas`)**: Permite o lançamento parcial de notas (`nota1` ou `nota2`). Se **ambas** as notas forem preenchidas, o sistema calcula a média aritmética e altera de forma automática o status para `APROVADO` (média $\ge 7.0$) ou `REPROVADO` (média $< 7.0$).
*   **Emitir Histórico Escolar (`emitirHistorico`)**: Gera uma estrutura detalhada contendo informações básicas do aluno e um histórico completo das disciplinas que ele cursa ou cursou, com notas, média final calculada em tempo de execução e a situação da disciplina.

---

## 📡 5. Requisições (Insomnia / Postman)
POST PROFESSOR :

<img width="1908" height="1080" alt="image" src="https://github.com/user-attachments/assets/368ec0ac-f12a-44e0-8257-4236bf6c8f3a" />


POST ALUNO :

<img width="1912" height="1080" alt="image" src="https://github.com/user-attachments/assets/1c4ccb94-a4d2-458d-82f7-8f7301fb3a1f" />

POST DISCIPLINA :

<img width="1921" height="1037" alt="image" src="https://github.com/user-attachments/assets/cd80ea89-b235-4677-810b-a6a319d4fd39" />


POST MATRICULA : 

<img width="1914" height="1040" alt="image" src="https://github.com/user-attachments/assets/7c58a11f-b8ed-4467-a0ad-910be8ecc6b6" />

PATCH NOTA :

<img width="1902" height="978" alt="image" src="https://github.com/user-attachments/assets/08005d1d-46bf-4dc3-939d-ab111127b0b1" />

GET HISTORICO:

<img width="1917" height="1048" alt="image" src="https://github.com/user-attachments/assets/313dc833-ed4e-45bf-a164-a97d34e37eae" />



---

## 🗄️ 6. Tabelas do Banco de Dados no DBeaver (Visualização Conceitual)

Graças ao Hibernate configurado com `ddl-auto=update`, as tabelas são criadas automaticamente no PostgreSQL. Abaixo estão representados os dados utilizados para testes conforme visualizados nas tabelas através do **DBeaver**:

### Tabela: `aluno`
| id (PK) | nome                    | cpf            | email                 |
|:--------|:------------------------|:---------------|:----------------------|
| 1       | Artur Medeiros          | 123.456.789-00 | artur@email.com       |
| 2       | Maria Silva Oliveira    | 111.222.333-44 | maria.silva@email.com |

### Tabela: `professor`
| id (PK) | nome           | cpf            | email                        |
|:--------|:---------------|:---------------|:-----------------------------|
| 1       | Kelson Almeida | 987.654.321-11 | kelson.almeida@uniesp.edu.br |

### Tabela: `disciplina`
| id (PK) | nome                                 | carga_horaria | professor_id (FK) |
|:--------|:-------------------------------------|:--------------|:------------------|
| 1       | Desenvolvimento Web com Spring Boot  | 80            | 1                 |
| 2       | Estrutura de Dados em Java           | 60            | 1                 |

### Tabela: `matricula_aluno`
| id (PK) | aluno_id (FK) | disciplina_id (FK) | nota1 | nota2 | status       |
|:--------|:--------------|:-------------------|:------|:------|:-------------|
| 1       | 1             | 1                  | 8.5   | 7.5   | APROVADO     |
| 2       | 1             | 2                  | NULL  | NULL  | MATRICULADO  |
| 3       | 2             | 1                  | 4.0   | 5.0   | REPROVADO    |
