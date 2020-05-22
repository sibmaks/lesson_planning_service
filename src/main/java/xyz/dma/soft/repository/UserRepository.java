package xyz.dma.soft.repository;

import org.springframework.data.repository.CrudRepository;
import xyz.dma.soft.domain.user.User;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    User findFirstByLogin(String login);

    User findFirstById(Long id);

    List<User> findAllByOrderById();

    boolean existsByLogin(String login);
}
