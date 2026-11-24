package br.com.alunoonline.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Aluno Online API",
                version = "v1.0",
                description = "API RESTful para gestão acadêmica com autenticação stateless via Spring Security e JWT.",
                contact = @Contact(name = "Equipe Aluno Online")
        ),
        tags = {
                @Tag(name = "1. Autenticação", description = "Endpoints públicos para cadastro de usuários e login com JWT"),
                @Tag(name = "2. Alunos", description = "Endpoints protegidos para gerenciamento de alunos"),
                @Tag(name = "3. Professores", description = "Endpoints protegidos para gerenciamento de professores"),
                @Tag(name = "4. Disciplinas", description = "Endpoints protegidos para gerenciamento de disciplinas"),
                @Tag(name = "5. Matrículas", description = "Endpoints protegidos para gestão de matrículas, notas e histórico")
        }
)
@SecurityScheme(
        name = "bearer-key",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Insira o token JWT gerado após o login para autorizar as requisições protegidas."
)
public class OpenApiConfig {
}
