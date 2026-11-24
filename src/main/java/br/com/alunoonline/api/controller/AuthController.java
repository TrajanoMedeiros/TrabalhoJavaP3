package br.com.alunoonline.api.controller;

import br.com.alunoonline.api.dtos.LoginRequestDTO;
import br.com.alunoonline.api.dtos.RegisterRequestDTO;
import br.com.alunoonline.api.dtos.TokenResponseDTO;
import br.com.alunoonline.api.model.User;
import br.com.alunoonline.api.model.UserRole;
import br.com.alunoonline.api.repository.UserRepository;
import br.com.alunoonline.api.security.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
@Tag(name = "1. Autenticação", description = "Endpoints públicos para cadastro de usuários e login com JWT")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "1. Cadastrar Novo Usuário", description = "Cadastra um usuário com senha criptografada via BCrypt")
    public ResponseEntity<Void> register(@RequestBody RegisterRequestDTO data) {
        if (this.userRepository.findByLogin(data.getLogin()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já cadastrado com esse login");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.getPassword());
        UserRole role = data.getRole() != null ? data.getRole() : UserRole.USER;
        User newUser = new User(data.getLogin(), encryptedPassword, role);

        this.userRepository.save(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    @Operation(summary = "2. Realizar Login e Obter Token JWT", description = "Valida as credenciais e retorna o Bearer Token JWT para usar no Authorize")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody LoginRequestDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.getLogin(), data.getPassword());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(new TokenResponseDTO(token));
    }
}
