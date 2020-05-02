package xyz.dma.soft.repository;

import org.springframework.data.repository.CrudRepository;
import xyz.dma.soft.domain.ChildInfo;

public interface ChildInfoRepository extends CrudRepository<ChildInfo, Long> {
    boolean existsById(Long id);
}
