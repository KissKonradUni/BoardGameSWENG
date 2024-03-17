package app.kosoft.boardgame;

import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);
    User findByEmail(String email);
    boolean existsByUsername(String username);
}
