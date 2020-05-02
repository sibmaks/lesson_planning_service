package xyz.dma.soft.repository;

import org.springframework.data.repository.CrudRepository;
import xyz.dma.soft.domain.ChildInfo;
import xyz.dma.soft.domain.ChildSchedulingCourseInfo;

import java.util.List;

public interface ChildSchedulingCourseInfoRepository extends CrudRepository<ChildSchedulingCourseInfo, Long> {
    List<ChildSchedulingCourseInfo> findAllByChildInfo(ChildInfo childInfo);

    void deleteAllByChildInfo(ChildInfo childInfo);
}
