package xyz.dma.soft.repository;

import org.springframework.data.repository.CrudRepository;
import xyz.dma.soft.domain.user.User;

public interface UserRepository extends CrudRepository<User, Long> {
    User findFirstByLogin(String login);

    User findFirstById(Long id);

    boolean existsByLogin(String login);
}
