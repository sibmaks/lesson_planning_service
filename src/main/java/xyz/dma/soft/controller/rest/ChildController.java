package xyz.dma.soft.controller.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import xyz.dma.soft.api.request.child.ChildAddRequest;
import xyz.dma.soft.api.request.child.ChildUpdateRequest;
import xyz.dma.soft.api.response.StandardResponse;
import xyz.dma.soft.api.response.child.ChildrenGetResponse;
import xyz.dma.soft.api.validator.child.ChildAddRequestValidator;
import xyz.dma.soft.api.validator.child.ChildUpdateRequestValidator;
import xyz.dma.soft.controller.BaseController;
import xyz.dma.soft.core.RequestValidateRequired;
import xyz.dma.soft.core.SessionRequired;
import xyz.dma.soft.service.ChildService;
import xyz.dma.soft.service.SessionService;

@RestController
@RequestMapping(path = "/v3/child/", consumes = {MediaType.APPLICATION_JSON_VALUE})
public class ChildController extends BaseController {
    private final ChildService childService;

    public ChildController(SessionService sessionService, ChildService childService) {
        super(sessionService);
        this.childService = childService;
    }

    @SessionRequired(requiredAction = "MODIFY_CHILDREN")
    @RequestValidateRequired(beanValidator = ChildAddRequestValidator.class)
    @RequestMapping(path = "add", method = RequestMethod.POST)
    public StandardResponse add(@RequestBody ChildAddRequest request) {
        return childService.add(request.getChildInfo(), request.getCourseSchedulingInfos());
    }

    @SessionRequired(requiredAction = "MODIFY_CHILDREN")
    @RequestValidateRequired(beanValidator = ChildUpdateRequestValidator.class)
    @RequestMapping(path = "update", method = RequestMethod.POST)
    public StandardResponse update(@RequestBody ChildUpdateRequest request) {
        return childService.update(request.getChildInfo(), request.getCourseSchedulingInfos());
    }

    @SessionRequired
    @RequestMapping(path = "getAll", method = RequestMethod.POST)
    public StandardResponse getAll() {
        return new ChildrenGetResponse(childService.getAll());
    }
}
