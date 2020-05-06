package xyz.dma.soft.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.dma.soft.api.entity.*;
import xyz.dma.soft.api.response.StandardResponse;
import xyz.dma.soft.api.response.child.ChildAddResponse;
import xyz.dma.soft.api.response.child.ChildUpdateResponse;
import xyz.dma.soft.constants.ICommonConstants;
import xyz.dma.soft.domain.ChildInfo;
import xyz.dma.soft.domain.ChildSchedulingCourseInfo;
import xyz.dma.soft.domain.Course;
import xyz.dma.soft.domain.SchedulingCourseInfo;
import xyz.dma.soft.exception.ServiceException;
import xyz.dma.soft.repository.ChildInfoRepository;
import xyz.dma.soft.repository.ChildSchedulingCourseInfoRepository;
import xyz.dma.soft.repository.CourseRepository;
import xyz.dma.soft.repository.SchedulingCourseInfoRepository;
import xyz.dma.soft.utils.ConvertUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ChildService {
    private final ChildInfoRepository childInfoRepository;
    private final ChildSchedulingCourseInfoRepository childSchedulingCourseInfoRepository;
    private final CourseRepository courseRepository;
    private final SchedulingCourseInfoRepository schedulingCourseInfoRepository;

    public List<ChildCourseSchedulingInfo> getAll() {
        List<ChildCourseSchedulingInfo> childCourseSchedulingInfos = new ArrayList<>();

        for(ChildInfo childInfo : childInfoRepository.findAll()) {
            List<ChildSchedulingCourseInfo> kidSchedulingInfo = childSchedulingCourseInfoRepository.findAllByChildInfo(childInfo);

            ChildCourseSchedulingInfo childCourseSchedulingInfo = ChildCourseSchedulingInfo.builder()
                    .childInfo(new ChildInfoEntity(childInfo))
                    .courseSchedulingInfos(buildChildCourseSchedulingInfo(kidSchedulingInfo))
                    .build();
            childCourseSchedulingInfos.add(childCourseSchedulingInfo);
        }

        return childCourseSchedulingInfos;
    }

    private List<CourseSchedulingInfo> buildChildCourseSchedulingInfo(List<ChildSchedulingCourseInfo> kidSchedulingInfos) {
        List<CourseSchedulingInfo> courseSchedulingInfos = new ArrayList<>();
        for(ChildSchedulingCourseInfo kidSchedulingInfo : kidSchedulingInfos) {
            for(SchedulingCourseInfo schedulingCourseInfo : kidSchedulingInfo.getSchedulingCourseInfoList()) {
                CourseSchedulingInfo courseSchedulingInfo = CourseSchedulingInfo.builder()
                        .id(kidSchedulingInfo.getId())
                        .dayOfWeek(schedulingCourseInfo.getDayOfWeek())
                        .timeStart(schedulingCourseInfo.getTimeStart().format(ICommonConstants.TIME_FORMATTER))
                        .timeEnd(schedulingCourseInfo.getTimeEnd().format(ICommonConstants.TIME_FORMATTER))
                        .courseInfo(new CourseInfo(kidSchedulingInfo.getCourse()))
                        .build();
                courseSchedulingInfos.add(courseSchedulingInfo);
            }
        }
        return courseSchedulingInfos;
    }

    public SchedulingCourseInfo buildSchedulingCourseInfo(CourseSchedulingInfo courseSchedulingInfo) {
        return SchedulingCourseInfo.builder()
                .dayOfWeek(courseSchedulingInfo.getDayOfWeek())
                .timeStart(ConvertUtils.parseTime(courseSchedulingInfo.getTimeStart()))
                .timeEnd(ConvertUtils.parseTime(courseSchedulingInfo.getTimeEnd()))
                .build();
    }

    @Transactional
    public StandardResponse add(ChildInfoEntity childInfoEntity, List<CourseSchedulingInfo> courseSchedulingInfos) {
        ChildAddResponse response = new ChildAddResponse();

        ChildInfo childInfo = ChildInfo.builder()
                .name(childInfoEntity.getName())
                .parentName(childInfoEntity.getParentName())
                .phone(childInfoEntity.getPhone())
                .build();
        childInfo = childInfoRepository.save(childInfo);
        response.setChildInfo(new ChildInfoEntity(childInfo));

        if(courseSchedulingInfos != null) {
            Map<Long, ChildSchedulingCourseInfo> courseIdSchedulingMap = new HashMap<>();

            for(CourseSchedulingInfo courseSchedulingInfo : courseSchedulingInfos) {
                Long courseId = courseSchedulingInfo.getCourseInfo().getId();
                if(!courseIdSchedulingMap.containsKey(courseId)) {
                    Course course = courseRepository.findById(courseId)
                            .orElseThrow(() -> ServiceException.builder().code(ApiResultCode.COURSE_NOT_FOUND).build());

                    ChildSchedulingCourseInfo childSchedulingCourseInfo = ChildSchedulingCourseInfo
                            .builder()
                            .childInfo(childInfo)
                            .course(course)
                            .schedulingCourseInfoList(new ArrayList<>())
                            .build();

                    courseIdSchedulingMap.put(courseId, childSchedulingCourseInfo);
                }
                ChildSchedulingCourseInfo childSchedulingCourseInfo = courseIdSchedulingMap.get(courseId);
                SchedulingCourseInfo schedulingCourseInfo =
                        schedulingCourseInfoRepository.findFirstByDayOfWeekAndTimeStartAndTimeEnd(
                                courseSchedulingInfo.getDayOfWeek(),
                                ConvertUtils.parseTime(courseSchedulingInfo.getTimeStart()),
                                ConvertUtils.parseTime(courseSchedulingInfo.getTimeEnd()));
                if(schedulingCourseInfo == null) {
                    schedulingCourseInfo = buildSchedulingCourseInfo(courseSchedulingInfo);
                    schedulingCourseInfo = schedulingCourseInfoRepository.save(schedulingCourseInfo);
                }
                childSchedulingCourseInfo.getSchedulingCourseInfoList().add(schedulingCourseInfo);
            }
            childSchedulingCourseInfoRepository.saveAll(courseIdSchedulingMap.values());
        }

        response.setCourseSchedulingInfos(buildChildCourseSchedulingInfo(childSchedulingCourseInfoRepository.findAllByChildInfo(childInfo)));
        return response;
    }

    @Transactional
    public StandardResponse update(ChildInfoEntity childInfoEntity, List<CourseSchedulingInfo> courseSchedulingInfos) {
        ChildUpdateResponse response = new ChildUpdateResponse();

        ChildInfo childInfo = childInfoRepository.findById(childInfoEntity.getId())
                .orElseThrow(() -> ServiceException.builder().code(ApiResultCode.CHILD_NOT_EXISTS).build());

        childInfo.setName(childInfoEntity.getName());
        childInfo.setParentName(childInfoEntity.getParentName());
        childInfo.setPhone(childInfoEntity.getPhone());

        childInfo = childInfoRepository.save(childInfo);
        response.setChildInfo(new ChildInfoEntity(childInfo));

        List<ChildSchedulingCourseInfo> schedulingCourseInfosForRemove = childSchedulingCourseInfoRepository.findAllByChildInfo(childInfo);
        if(courseSchedulingInfos != null && !courseSchedulingInfos.isEmpty()) {
            Map<Long, ChildSchedulingCourseInfo> courseIdSchedulingMap = new HashMap<>();

            for(CourseSchedulingInfo courseSchedulingInfo : courseSchedulingInfos) {
                Long courseId = courseSchedulingInfo.getCourseInfo().getId();
                if(!courseIdSchedulingMap.containsKey(courseId)) {
                    ChildSchedulingCourseInfo childSchedulingCourseInfo = schedulingCourseInfosForRemove.stream()
                            .filter(it -> it.getId() == courseId).findFirst().orElse(null);
                    if(childSchedulingCourseInfo == null) {
                        Course course = courseRepository.findById(courseId)
                                .orElseThrow(() -> ServiceException.builder().code(ApiResultCode.COURSE_NOT_FOUND).build());

                        childSchedulingCourseInfo = ChildSchedulingCourseInfo
                                .builder()
                                .childInfo(childInfo)
                                .course(course)
                                .schedulingCourseInfoList(new ArrayList<>())
                                .build();
                    } else {
                        childSchedulingCourseInfo.setSchedulingCourseInfoList(new ArrayList<>());
                        schedulingCourseInfosForRemove.remove(childSchedulingCourseInfo);
                    }
                    courseIdSchedulingMap.put(courseId, childSchedulingCourseInfo);
                }
                ChildSchedulingCourseInfo childSchedulingCourseInfo = courseIdSchedulingMap.get(courseId);
                SchedulingCourseInfo schedulingCourseInfo =
                        schedulingCourseInfoRepository.findFirstByDayOfWeekAndTimeStartAndTimeEnd(
                                courseSchedulingInfo.getDayOfWeek(),
                                ConvertUtils.parseTime(courseSchedulingInfo.getTimeStart()),
                                ConvertUtils.parseTime(courseSchedulingInfo.getTimeEnd()));
                if(schedulingCourseInfo == null) {
                    schedulingCourseInfo = buildSchedulingCourseInfo(courseSchedulingInfo);
                    schedulingCourseInfo = schedulingCourseInfoRepository.save(schedulingCourseInfo);
                }
                childSchedulingCourseInfo.getSchedulingCourseInfoList().add(schedulingCourseInfo);
            }

            courseIdSchedulingMap.values().forEach(childSchedulingCourseInfoRepository::save);
        }
        childSchedulingCourseInfoRepository.deleteAll(schedulingCourseInfosForRemove);

        response.setCourseSchedulingInfos(buildChildCourseSchedulingInfo(childSchedulingCourseInfoRepository.findAllByChildInfo(childInfo)));

        return response;
    }
}
