package xyz.dma.soft.controller.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import xyz.dma.soft.api.request.course.CourseAddRequest;
import xyz.dma.soft.api.request.course.CourseUpdateRequest;
import xyz.dma.soft.api.response.StandardResponse;
import xyz.dma.soft.api.response.course.AddCourseResponse;
import xyz.dma.soft.api.response.course.GetCoursesResponse;
import xyz.dma.soft.api.response.course.UpdateCourseResponse;
import xyz.dma.soft.api.validator.course.CourseAddRequestValidator;
import xyz.dma.soft.api.validator.course.CourseUpdateRequestValidator;
import xyz.dma.soft.controller.BaseController;
import xyz.dma.soft.core.RequestValidateRequired;
import xyz.dma.soft.core.SessionRequired;
import xyz.dma.soft.entity.SessionInfo;
import xyz.dma.soft.service.CourseService;
import xyz.dma.soft.service.SessionService;

@RestController
@RequestMapping(path = "/v3/course/", consumes = {MediaType.APPLICATION_JSON_VALUE})
public class CourseController extends BaseController {
    private final CourseService courseService;

    public CourseController(SessionService sessionService, CourseService courseService) {
        super(sessionService);
        this.courseService = courseService;
    }

    @SessionRequired(requiredAction = "MODIFY_COURSES")
    @RequestValidateRequired(beanValidator = CourseAddRequestValidator.class)
    @RequestMapping(path = "add", method = RequestMethod.POST)
    public StandardResponse add(@RequestBody CourseAddRequest request) {
        SessionInfo sessionInfo = getCurrentSession();
        return new AddCourseResponse(courseService.add(sessionInfo, request.getName()));
    }

    @SessionRequired(requiredAction = "MODIFY_COURSES")
    @RequestValidateRequired(beanValidator = CourseUpdateRequestValidator.class)
    @RequestMapping(path = "update", method = RequestMethod.POST)
    public StandardResponse update(@RequestBody CourseUpdateRequest request) {
        SessionInfo sessionInfo = getCurrentSession();
        return new UpdateCourseResponse(courseService.update(sessionInfo, request.getId(), request.getName()));
    }

    @SessionRequired
    @RequestMapping(path = "getAll", method = RequestMethod.POST)
    public StandardResponse getAll() {
        return new GetCoursesResponse(courseService.getAll());
    }
}
