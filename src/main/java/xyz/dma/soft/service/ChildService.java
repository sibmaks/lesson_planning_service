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
import xyz.dma.soft.entity.SessionInfo;
import xyz.dma.soft.exception.ServiceException;
import xyz.dma.soft.repository.ChildInfoRepository;
import xyz.dma.soft.repository.ChildSchedulingCourseInfoRepository;
import xyz.dma.soft.repository.CourseRepository;
import xyz.dma.soft.repository.SchedulingCourseInfoRepository;
import xyz.dma.soft.utils.ConvertUtils;

import javax.transaction.Transactional;
import java.time.LocalTime;
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
    private final LocalizationService localizationService;

    public List<ChildCourseSchedulingInfo> getAll() {
        List<ChildCourseSchedulingInfo> childCourseSchedulingInfos = new ArrayList<>();

        for(ChildInfo childInfo : childInfoRepository.getAllByOrderById()) {
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
    public StandardResponse add(SessionInfo sessionInfo, ChildInfoEntity childInfoEntity, List<CourseSchedulingInfo> courseSchedulingInfos) {
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
                SchedulingCourseInfo finalSchedulingCourseInfo = schedulingCourseInfo;
                if(childSchedulingCourseInfo.getSchedulingCourseInfoList().stream()
                        .filter(it -> it.getDayOfWeek() == finalSchedulingCourseInfo.getDayOfWeek())
                        .filter(it -> it.getTimeStart().equals(finalSchedulingCourseInfo.getTimeStart()))
                        .anyMatch(it -> it.getTimeEnd().equals(finalSchedulingCourseInfo.getTimeEnd()))) {
                    throw ServiceException.builder()
                            .code(ApiResultCode.DUPLICATES)
                            .message(localizationService.getTranslated(sessionInfo, "ui.error.scheduling_duplicates"))
                            .systemMessage(localizationService.getTranslated("eng", "ui.error.scheduling_duplicates"))
                            .build();
                }
                childSchedulingCourseInfo.getSchedulingCourseInfoList().add(schedulingCourseInfo);
            }
            childSchedulingCourseInfoRepository.saveAll(courseIdSchedulingMap.values());
        }

        List<ChildSchedulingCourseInfo> schedulingInfos = childSchedulingCourseInfoRepository.findAllByChildInfo(childInfo);
        response.setCourseSchedulingInfos(buildChildCourseSchedulingInfo(schedulingInfos));
        return response;
    }

    @Transactional
    public StandardResponse update(SessionInfo sessionInfo, ChildInfoEntity childInfoEntity,
                                   List<CourseSchedulingInfo> courseSchedulingInfos) {
        ChildUpdateResponse response = new ChildUpdateResponse();

        ChildInfo childInfo = childInfoRepository.findById(childInfoEntity.getId())
                .orElseThrow(() -> ServiceException.builder().code(ApiResultCode.CHILD_NOT_EXISTS).build());

        childInfo.setName(childInfoEntity.getName());
        childInfo.setParentName(childInfoEntity.getParentName());
        childInfo.setPhone(childInfoEntity.getPhone());

        childInfo = childInfoRepository.save(childInfo);
        response.setChildInfo(new ChildInfoEntity(childInfo));


        if(courseSchedulingInfos != null && !courseSchedulingInfos.isEmpty()) {
            List<CourseSchedulingInfo> schedulingCourseInfosSource =
                    buildChildCourseSchedulingInfo(childSchedulingCourseInfoRepository.findAllByChildInfo(childInfo));

            Map<Number, ChildSchedulingCourseInfo> childSchedulingCourseInfos = new HashMap<>();

            for(CourseSchedulingInfo courseSchedulingInfo : courseSchedulingInfos) {
                CourseSchedulingInfo courseSchedulingInfoSource = schedulingCourseInfosSource.stream()
                        .filter(it -> it.getCourseInfo().getId().equals(courseSchedulingInfo.getCourseInfo().getId()))
                        .filter(it -> it.getDayOfWeek() == courseSchedulingInfo.getDayOfWeek())
                        .filter(it -> it.getTimeStart().equals(courseSchedulingInfo.getTimeStart()))
                        .filter(it -> it.getTimeEnd().equals(courseSchedulingInfo.getTimeEnd()))
                        .findAny()
                        .orElse(null);

                if (courseSchedulingInfoSource != null) {
                    schedulingCourseInfosSource.remove(courseSchedulingInfoSource);
                    courseSchedulingInfo.setId(courseSchedulingInfoSource.getId());
                }

                Course course = courseRepository.findById(courseSchedulingInfo.getCourseInfo().getId())
                        .orElseThrow(() -> ServiceException.builder().code(ApiResultCode.COURSE_NOT_FOUND).build());

                if(!childSchedulingCourseInfos.containsKey(courseSchedulingInfo.getCourseInfo().getId())) {
                    ChildSchedulingCourseInfo childSchedulingCourseInfo = ChildSchedulingCourseInfo.builder()
                            .id(courseSchedulingInfo.getId())
                            .childInfo(childInfo)
                            .course(course)
                            .schedulingCourseInfoList(new ArrayList<>())
                            .build();
                    childSchedulingCourseInfos.put(childSchedulingCourseInfo.getCourse().getId(), childSchedulingCourseInfo);
                }

                LocalTime startTime = ConvertUtils.parseTime(courseSchedulingInfo.getTimeStart());
                LocalTime endTime = ConvertUtils.parseTime(courseSchedulingInfo.getTimeEnd());

                SchedulingCourseInfo schedulingCourseInfo = schedulingCourseInfoRepository.findFirstByDayOfWeekAndTimeStartAndTimeEnd(
                        courseSchedulingInfo.getDayOfWeek(), startTime, endTime);
                if(schedulingCourseInfo == null) {
                    schedulingCourseInfo = SchedulingCourseInfo.builder()
                            .timeStart(startTime)
                            .timeEnd(endTime)
                            .dayOfWeek(courseSchedulingInfo.getDayOfWeek())
                            .build();
                    schedulingCourseInfo = schedulingCourseInfoRepository.save(schedulingCourseInfo);
                }

                ChildSchedulingCourseInfo childSchedulingCourseInfo = childSchedulingCourseInfos.get(courseSchedulingInfo.getCourseInfo().getId());

                SchedulingCourseInfo finalSchedulingCourseInfo = schedulingCourseInfo;
                if(childSchedulingCourseInfo.getSchedulingCourseInfoList().stream()
                        .filter(it -> it.getDayOfWeek() == finalSchedulingCourseInfo.getDayOfWeek())
                        .filter(it -> it.getTimeStart().equals(finalSchedulingCourseInfo.getTimeStart()))
                        .anyMatch(it -> it.getTimeEnd().equals(finalSchedulingCourseInfo.getTimeEnd()))) {
                    throw ServiceException.builder()
                            .code(ApiResultCode.DUPLICATES)
                            .message(localizationService.getTranslated(sessionInfo, "ui.error.scheduling_duplicates"))
                            .systemMessage(localizationService.getTranslated("eng", "ui.error.scheduling_duplicates"))
                            .build();
                }
                childSchedulingCourseInfo.getSchedulingCourseInfoList().add(schedulingCourseInfo);
            }

            schedulingCourseInfosSource.forEach(it -> childSchedulingCourseInfoRepository.deleteById(it.getId()));
            childSchedulingCourseInfoRepository.saveAll(childSchedulingCourseInfos.values());
        } else {
            childSchedulingCourseInfoRepository.deleteAllByChildInfo(childInfo);
        }


        response.setCourseSchedulingInfos(buildChildCourseSchedulingInfo(childSchedulingCourseInfoRepository.findAllByChildInfo(childInfo)));

        return response;
    }
}
