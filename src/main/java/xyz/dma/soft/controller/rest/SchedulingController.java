package xyz.dma.soft.controller.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import xyz.dma.soft.api.request.scheduling.SchedulingGetRequest;
import xyz.dma.soft.api.response.StandardResponse;
import xyz.dma.soft.api.response.scheduling.SchedulingGetResponse;
import xyz.dma.soft.api.validator.scheduling.SchedulingGetRequestValidator;
import xyz.dma.soft.controller.BaseController;
import xyz.dma.soft.core.RequestValidateRequired;
import xyz.dma.soft.core.SessionRequired;
import xyz.dma.soft.service.SchedulingService;
import xyz.dma.soft.service.SessionService;

@RestController
@RequestMapping(path = "/v3/scheduling/", consumes = {MediaType.APPLICATION_JSON_VALUE})
public class SchedulingController extends BaseController {
    private final SchedulingService schedulingService;

    public SchedulingController(SessionService sessionService, SchedulingService schedulingService) {
        super(sessionService);
        this.schedulingService = schedulingService;
    }

    @SessionRequired(requiredAction = "CRUD_LESSONS")
    @RequestValidateRequired(beanValidator = SchedulingGetRequestValidator.class)
    @RequestMapping(path = "get", method = RequestMethod.POST)
    public StandardResponse get(@RequestBody SchedulingGetRequest request) {
        return new SchedulingGetResponse(schedulingService.get(request.getCourseId(),
                request.getFromDate(), request.getToDate()));
    }

}
