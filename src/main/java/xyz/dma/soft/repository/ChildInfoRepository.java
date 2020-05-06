package xyz.dma.soft.repository;

import org.springframework.data.repository.CrudRepository;
import xyz.dma.soft.domain.ChildInfo;

import java.util.List;

public interface ChildInfoRepository extends CrudRepository<ChildInfo, Long> {
    boolean existsById(Long id);

    List<ChildInfo> getAllByIdIn(List<Long> ids);
}
