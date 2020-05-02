package xyz.dma.soft.repository;

import org.springframework.data.repository.CrudRepository;
import xyz.dma.soft.domain.user.UserInfo;

public interface UserInfoRepository extends CrudRepository<UserInfo, Long> {
    UserInfo findFirstById(Long id);
}
