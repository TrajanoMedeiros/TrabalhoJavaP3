package br.com.alunoonline.api.repository;

import br.com.alunoonline.api.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    UserDetails findByLogin(String login);
    Optional<User> findUserByLogin(String login);
}
