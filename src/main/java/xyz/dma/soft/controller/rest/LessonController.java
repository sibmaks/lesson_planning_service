package xyz.dma.soft.controller.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import xyz.dma.soft.api.request.lesson.LessonAddRequest;
import xyz.dma.soft.api.request.lesson.LessonStopRequest;
import xyz.dma.soft.api.request.lesson.LessonUpdateRequest;
import xyz.dma.soft.api.request.lesson.LessonsGetRequest;
import xyz.dma.soft.api.response.StandardResponse;
import xyz.dma.soft.api.response.lesson.LessonAddResponse;
import xyz.dma.soft.api.response.lesson.LessonStopResponse;
import xyz.dma.soft.api.response.lesson.LessonUpdateResponse;
import xyz.dma.soft.api.response.lesson.LessonsGetResponse;
import xyz.dma.soft.api.validator.lesson.LessonAddRequestValidator;
import xyz.dma.soft.api.validator.lesson.LessonStopRequestValidator;
import xyz.dma.soft.api.validator.lesson.LessonUpdateRequestValidator;
import xyz.dma.soft.api.validator.lesson.LessonsGetRequestValidator;
import xyz.dma.soft.controller.BaseController;
import xyz.dma.soft.core.RequestValidateRequired;
import xyz.dma.soft.core.SessionRequired;
import xyz.dma.soft.entity.SessionInfo;
import xyz.dma.soft.service.LessonService;
import xyz.dma.soft.service.SessionService;

@RestController
@RequestMapping(path = "/v3/lesson/", consumes = {MediaType.APPLICATION_JSON_VALUE})
public class LessonController extends BaseController {
    private final LessonService lessonService;

    public LessonController(SessionService sessionService, LessonService lessonService) {
        super(sessionService);
        this.lessonService = lessonService;
    }

    @SessionRequired(requiredAction = "CRUD_LESSONS")
    @RequestValidateRequired(beanValidator = LessonStopRequestValidator.class)
    @RequestMapping(path = "stop", method = RequestMethod.POST)
    public StandardResponse stop(@RequestBody LessonStopRequest request) {
        SessionInfo sessionInfo = getCurrentSession();
        return new LessonStopResponse(lessonService.stop(sessionInfo, request.getLessonId(), request.getLessonEndDate()));
    }

    @SessionRequired(requiredAction = "CRUD_LESSONS")
    @RequestValidateRequired(beanValidator = LessonAddRequestValidator.class)
    @RequestMapping(path = "add", method = RequestMethod.POST)
    public StandardResponse add(@RequestBody LessonAddRequest request) {
        SessionInfo sessionInfo = getCurrentSession();
        return new LessonAddResponse(lessonService.add(sessionInfo, request.getCourseId(),
                request.getDayOfWeek(), request.getTimeStart(), request.getTimeEnd(), request.getLessonStartDate(),
                request.getChildren()));
    }

    @SessionRequired(requiredAction = "CRUD_LESSONS")
    @RequestValidateRequired(beanValidator = LessonUpdateRequestValidator.class)
    @RequestMapping(path = "update", method = RequestMethod.POST)
    public StandardResponse update(@RequestBody LessonUpdateRequest request) {
        SessionInfo sessionInfo = getCurrentSession();
        return new LessonUpdateResponse(lessonService.update(sessionInfo, request.getId(), request.getCourseId(),
                request.getDayOfWeek(), request.getTimeStart(), request.getTimeEnd(), request.getLessonStartDate(),
                request.getChildren()));
    }

    @SessionRequired(requiredAction = "CRUD_LESSONS")
    @RequestValidateRequired(beanValidator = LessonsGetRequestValidator.class)
    @RequestMapping(path = "get", method = RequestMethod.POST)
    public StandardResponse get(@RequestBody LessonsGetRequest request) {
        SessionInfo sessionInfo = getCurrentSession();
        return new LessonsGetResponse(lessonService.get(sessionInfo, request.getCourseId(),
                request.getFromDate(), request.getToDate()));
    }
}
