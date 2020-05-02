package xyz.dma.soft.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import xyz.dma.soft.api.request.role.GrantRoleRequest;
import xyz.dma.soft.api.request.role.ModifyRoleRequest;
import xyz.dma.soft.api.request.role.RetractRoleRequest;
import xyz.dma.soft.api.response.role.GetRolesResponse;
import xyz.dma.soft.api.response.StandardResponse;
import xyz.dma.soft.api.validator.role.GrantRoleRequestValidator;
import xyz.dma.soft.api.validator.role.ModifyRoleRequestValidator;
import xyz.dma.soft.api.validator.role.RetractRoleRequestValidator;
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
    @RequestValidateRequired(beanValidator = ModifyRoleRequestValidator.class)
    @RequestMapping(path = "modify", method = RequestMethod.POST)
    public StandardResponse modify(@RequestBody ModifyRoleRequest request) {
        roleService.modify(request.getRole(), request.getAllowedActions());
        return new StandardResponse();
    }

    @SessionRequired(requiredAction = "MODIFY_USERS")
    @RequestValidateRequired(beanValidator = GrantRoleRequestValidator.class)
    @RequestMapping(path = "grant", method = RequestMethod.POST)
    public StandardResponse grant(@RequestBody GrantRoleRequest request) {
        roleService.grant(request.getRole(), request.getUserId());
        return new StandardResponse();
    }

    @SessionRequired(requiredAction = "MODIFY_USERS")
    @RequestValidateRequired(beanValidator = RetractRoleRequestValidator.class)
    @RequestMapping(path = "retract", method = RequestMethod.POST)
    public StandardResponse retract(@RequestBody RetractRoleRequest request) {
        roleService.retract(request.getRole(), request.getUserId());
        return new StandardResponse();
    }

    @RequestMapping(path = "getAll", method = RequestMethod.POST)
    public StandardResponse getAll() {
        return new GetRolesResponse(roleService.getAll());
    }
}
