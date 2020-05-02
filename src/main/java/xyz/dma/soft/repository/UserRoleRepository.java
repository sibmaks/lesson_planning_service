package xyz.dma.soft.repository;

import org.springframework.data.repository.CrudRepository;
import xyz.dma.soft.domain.user.UserRole;

public interface UserRoleRepository extends CrudRepository<UserRole, Long> {
    boolean existsByName(String name);

    UserRole findFirstByName(String name);

    UserRole findFirstById(Long id);
}
