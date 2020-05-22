package xyz.dma.soft.controller.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import xyz.dma.soft.api.entity.LessonEntity;
import xyz.dma.soft.api.request.lesson.LessonAddRequest;
import xyz.dma.soft.api.request.lesson.LessonUpdateRequest;
import xyz.dma.soft.api.request.lesson.LessonsGetRequest;
import xyz.dma.soft.api.response.StandardResponse;
import xyz.dma.soft.api.response.lesson.LessonAddResponse;
import xyz.dma.soft.api.response.lesson.LessonUpdateResponse;
import xyz.dma.soft.api.response.lesson.LessonsGetResponse;
import xyz.dma.soft.api.validator.lesson.LessonAddRequestValidator;
import xyz.dma.soft.api.validator.lesson.LessonUpdateRequestValidator;
import xyz.dma.soft.api.validator.lesson.LessonsGetRequestValidator;
import xyz.dma.soft.controller.BaseController;
import xyz.dma.soft.core.RequestValidateRequired;
import xyz.dma.soft.core.SessionRequired;
import xyz.dma.soft.entity.SessionInfo;
import xyz.dma.soft.service.LessonService;
import xyz.dma.soft.service.LocalizationService;
import xyz.dma.soft.service.SessionService;

@RestController
@RequestMapping(path = "/v3/lesson/", consumes = {MediaType.APPLICATION_JSON_VALUE})
public class LessonController extends BaseController {
    private final LessonService lessonService;
    private final LocalizationService localizationService;

    public LessonController(SessionService sessionService, LessonService lessonService,
                            LocalizationService localizationService) {
        super(sessionService);
        this.lessonService = lessonService;
        this.localizationService = localizationService;
    }

    @SessionRequired(requiredAction = "CRUD_LESSONS")
    @RequestValidateRequired(beanValidator = LessonAddRequestValidator.class)
    @RequestMapping(path = "add", method = RequestMethod.POST)
    public StandardResponse add(@RequestBody LessonAddRequest request) {
        SessionInfo sessionInfo = getCurrentSession();
        LessonEntity lessonEntity = request.getLessonEntity();
        lessonEntity = lessonService.add(sessionInfo, lessonEntity.getCourseInfo().getId(),
                lessonEntity.getDayOfWeek(), lessonEntity.getTimeStart(), lessonEntity.getTimeEnd(), lessonEntity.getLessonStartDate(),
                lessonEntity.getLessonEndDate(), lessonEntity.getChildren());
        StandardResponse response = new LessonAddResponse(lessonEntity,
                String.format("/lesson/?mode=update&lesson=%d&lessonDate=%s", lessonEntity.getId(), lessonEntity.getLessonStartDate()));
        response.getResponseInfo().setMessage(localizationService.getTranslated(sessionInfo,
                "ui.text.successfully_saved"));
        response.getResponseInfo().setSystemMessage(localizationService.getTranslated("eng",
                "ui.text.successfully_saved"));
        return response;
    }

    @SessionRequired(requiredAction = "CRUD_LESSONS")
    @RequestValidateRequired(beanValidator = LessonUpdateRequestValidator.class)
    @RequestMapping(path = "update", method = RequestMethod.POST)
    public StandardResponse update(@RequestBody LessonUpdateRequest request) {
        SessionInfo sessionInfo = getCurrentSession();
        LessonEntity lessonEntity = request.getLessonEntity();
        StandardResponse response = new LessonUpdateResponse(lessonService.update(sessionInfo, lessonEntity.getId(),
                lessonEntity.getDayOfWeek(), lessonEntity.getTimeStart(), lessonEntity.getTimeEnd(), lessonEntity.getLessonStartDate(),
                lessonEntity.getLessonEndDate(), lessonEntity.getChildren()));
        response.getResponseInfo().setMessage(localizationService.getTranslated(sessionInfo,
                "ui.text.successfully_saved"));
        response.getResponseInfo().setSystemMessage(localizationService.getTranslated("eng",
                "ui.text.successfully_saved"));
        return response;
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
