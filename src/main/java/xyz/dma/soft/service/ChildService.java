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
            List<ChildSchedulingCourseInfo> kidSchedulingInfo = childSchedulingCourseInfoRepository.findAllByChildInfoOrderById(childInfo);

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
                .timeStart(ConvertUtils.parseTimeWithoutSeconds(courseSchedulingInfo.getTimeStart()))
                .timeEnd(ConvertUtils.parseTimeWithoutSeconds(courseSchedulingInfo.getTimeEnd()))
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
                                ConvertUtils.parseTimeWithoutSeconds(courseSchedulingInfo.getTimeStart()),
                                ConvertUtils.parseTimeWithoutSeconds(courseSchedulingInfo.getTimeEnd()));
                if(schedulingCourseInfo == null) {
                    schedulingCourseInfo = buildSchedulingCourseInfo(courseSchedulingInfo);
                    schedulingCourseInfo = schedulingCourseInfoRepository.save(schedulingCourseInfo);
                }
                checkDuplicates(sessionInfo, childSchedulingCourseInfo, schedulingCourseInfo);
            }
            childSchedulingCourseInfoRepository.saveAll(courseIdSchedulingMap.values());
        }

        List<ChildSchedulingCourseInfo> schedulingInfos = childSchedulingCourseInfoRepository.findAllByChildInfoOrderById(childInfo);
        response.setCourseSchedulingInfos(buildChildCourseSchedulingInfo(schedulingInfos));
        return response;
    }

    private void checkDuplicates(SessionInfo sessionInfo, ChildSchedulingCourseInfo childSchedulingCourseInfo, SchedulingCourseInfo schedulingCourseInfo) {
        if(childSchedulingCourseInfo.getSchedulingCourseInfoList().stream()
                .filter(it -> it.getDayOfWeek() == schedulingCourseInfo.getDayOfWeek())
                .filter(it -> it.getTimeStart().equals(schedulingCourseInfo.getTimeStart()))
                .anyMatch(it -> it.getTimeEnd().equals(schedulingCourseInfo.getTimeEnd()))) {
            throw ServiceException.builder()
                    .code(ApiResultCode.DUPLICATES)
                    .message(localizationService.getTranslated(sessionInfo, "ui.error.scheduling_duplicates"))
                    .systemMessage(localizationService.getTranslated("eng", "ui.error.scheduling_duplicates"))
                    .build();
        }
        childSchedulingCourseInfo.getSchedulingCourseInfoList().add(schedulingCourseInfo);
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
                    buildChildCourseSchedulingInfo(childSchedulingCourseInfoRepository.findAllByChildInfoOrderById(childInfo));

            Map<Number, ChildSchedulingCourseInfo> childSchedulingCourseInfos = new HashMap<>();

            for(CourseSchedulingInfo courseSchedulingInfo : courseSchedulingInfos) {
                long courseSchedulingInfoId = schedulingCourseInfosSource.stream()
                        .filter(it -> it.getCourseInfo().getId().equals(courseSchedulingInfo.getCourseInfo().getId()))
                        .filter(it -> it.getDayOfWeek() == courseSchedulingInfo.getDayOfWeek())
                        .filter(it -> it.getTimeStart().equals(courseSchedulingInfo.getTimeStart()))
                        .filter(it -> it.getTimeEnd().equals(courseSchedulingInfo.getTimeEnd()))
                        .findAny()
                        .map(CourseSchedulingInfo::getId)
                        .orElse(0L);
                courseSchedulingInfo.setId(courseSchedulingInfoId);

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

                LocalTime startTime = ConvertUtils.parseTimeWithoutSeconds(courseSchedulingInfo.getTimeStart());
                LocalTime endTime = ConvertUtils.parseTimeWithoutSeconds(courseSchedulingInfo.getTimeEnd());

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

                checkDuplicates(sessionInfo, childSchedulingCourseInfo, schedulingCourseInfo);
            }
            childSchedulingCourseInfoRepository.saveAll(childSchedulingCourseInfos.values());
        } else {
            childSchedulingCourseInfoRepository.deleteAllByChildInfo(childInfo);
        }


        response.setCourseSchedulingInfos(buildChildCourseSchedulingInfo(childSchedulingCourseInfoRepository.findAllByChildInfoOrderById(childInfo)));

        return response;
    }
}
