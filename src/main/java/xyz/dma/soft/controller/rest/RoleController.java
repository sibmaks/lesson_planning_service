package xyz.dma.soft.controller.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import xyz.dma.soft.api.request.role.GrantRoleRequest;
import xyz.dma.soft.api.request.role.ModifyRoleRequest;
import xyz.dma.soft.api.request.role.RetractRoleRequest;
import xyz.dma.soft.api.response.role.RolesGetResponse;
import xyz.dma.soft.api.response.StandardResponse;
import xyz.dma.soft.api.validator.role.RoleGrantRequestValidator;
import xyz.dma.soft.api.validator.role.RoleUpdateRequestValidator;
import xyz.dma.soft.api.validator.role.RoleRetractRequestValidator;
import xyz.dma.soft.controller.BaseController;
import xyz.dma.soft.core.RequestValidateRequired;
import xyz.dma.soft.core.SessionRequired;
import xyz.dma.soft.service.RoleService;
import xyz.dma.soft.service.SessionService;

@RestController
@RequestMapping(path = "/v3/role/", consumes = {MediaType.APPLICATION_JSON_VALUE})
public class RoleController extends BaseController {
    private final RoleService roleService;

    public RoleController(SessionService sessionService, RoleService roleService) {
        super(sessionService);
        this.roleService = roleService;
    }

    @SessionRequired(requiredAction = "MODIFY_ROLES")
    @RequestValidateRequired(beanValidator = RoleUpdateRequestValidator.class)
    @RequestMapping(path = "update", method = RequestMethod.POST)
    public StandardResponse update(@RequestBody ModifyRoleRequest request) {
        roleService.update(request.getRole(), request.getAllowedActions());
        return new StandardResponse();
    }

    @SessionRequired(requiredAction = "MODIFY_USERS")
    @RequestValidateRequired(beanValidator = RoleGrantRequestValidator.class)
    @RequestMapping(path = "grant", method = RequestMethod.POST)
    public StandardResponse grant(@RequestBody GrantRoleRequest request) {
        roleService.grant(request.getRole(), request.getUserId());
        return new StandardResponse();
    }

    @SessionRequired(requiredAction = "MODIFY_USERS")
    @RequestValidateRequired(beanValidator = RoleRetractRequestValidator.class)
    @RequestMapping(path = "retract", method = RequestMethod.POST)
    public StandardResponse retract(@RequestBody RetractRoleRequest request) {
        roleService.retract(request.getRole(), request.getUserId());
        return new StandardResponse();
    }

    @SessionRequired
    @RequestMapping(path = "getAll", method = RequestMethod.POST)
    public StandardResponse getAll() {
        return new RolesGetResponse(roleService.getAll());
    }
}
