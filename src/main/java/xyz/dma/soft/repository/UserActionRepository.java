package xyz.dma.soft.repository;

import org.springframework.data.repository.CrudRepository;
import xyz.dma.soft.domain.user.UserAction;

public interface UserActionRepository extends CrudRepository<UserAction, Long> {
    UserAction findFirstByName(String name);
}
