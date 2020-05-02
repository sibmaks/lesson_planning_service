package xyz.dma.soft.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.dma.soft.api.entity.ApiResultCode;
import xyz.dma.soft.api.entity.CourseInfo;
import xyz.dma.soft.api.entity.UserInfo;
import xyz.dma.soft.domain.Course;
import xyz.dma.soft.domain.user.User;
import xyz.dma.soft.exception.ServiceException;
import xyz.dma.soft.repository.CourseRepository;
import xyz.dma.soft.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CourseService {
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public List<CourseInfo> getAll() {
        List<CourseInfo> courseInfos = new ArrayList<>();

        for(Course course : courseRepository.findAll()) {
            courseInfos.add(buildCourseInfo(course));
        }

        return courseInfos;
    }

    @Transactional
    public CourseInfo add(Long userId, String name) {
        User user = userRepository.findFirstById(userId);
        if(courseRepository.findFirstByName(name) != null) {
            throw ServiceException.builder().code(ApiResultCode.ALREADY_EXISTS).build();
        }
        Course course = new Course();
        course.setName(name);
        course.setAuthor(user);
        course = courseRepository.save(course);
        return buildCourseInfo(course);
    }

    public static CourseInfo buildCourseInfo(Course course) {
        return CourseInfo.builder()
                .id(course.getId())
                .name(course.getName())
                .author(new UserInfo(course.getAuthor()))
                .build();
    }

    @Transactional
    public void remove(Long id) {
        if(!courseRepository.findById(id).isPresent()) {
            throw ServiceException.builder().code(ApiResultCode.NOT_EXISTS).build();
        }
        courseRepository.deleteById(id);
    }

    @Transactional
    public CourseInfo update(Long id, String name) {
        Course checkCourse = courseRepository.findFirstByName(name);
        if(checkCourse != null && checkCourse.getId() != id) {
            throw ServiceException.builder().code(ApiResultCode.ALREADY_EXISTS).build();
        }
        Course course = courseRepository
                .findById(id)
                .orElseThrow(() -> ServiceException.builder().code(ApiResultCode.NOT_EXISTS).build());
        course.setName(name);
        course = courseRepository.save(course);
        return buildCourseInfo(course);
    }
}
