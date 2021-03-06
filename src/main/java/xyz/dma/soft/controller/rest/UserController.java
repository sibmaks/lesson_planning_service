package xyz.dma.soft.controller.rest;

import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import xyz.dma.soft.api.entity.ApiResultCode;
import xyz.dma.soft.api.request.StandardRequest;
import xyz.dma.soft.api.request.user.LoginRequest;
import xyz.dma.soft.api.request.user.RegisterRequest;
import xyz.dma.soft.api.request.user.SetBlockRequest;
import xyz.dma.soft.api.request.user.SetPasswordRequest;
import xyz.dma.soft.api.response.GetUsersResponse;
import xyz.dma.soft.api.response.StandardResponse;
import xyz.dma.soft.api.response.user.LoginResponse;
import xyz.dma.soft.api.response.user.SetBlockResponse;
import xyz.dma.soft.api.validator.user.LoginRequestValidator;
import xyz.dma.soft.api.validator.user.RegisterRequestValidator;
import xyz.dma.soft.api.validator.user.SetBlockRequestValidator;
import xyz.dma.soft.api.validator.user.SetPasswordRequestValidator;
import xyz.dma.soft.controller.BaseController;
import xyz.dma.soft.core.RequestValidateRequired;
import xyz.dma.soft.core.SessionRequired;
import xyz.dma.soft.entity.SessionInfo;
import xyz.dma.soft.exception.ServiceException;
import xyz.dma.soft.service.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static xyz.dma.soft.constants.ICommonConstants.X_USER_SESSION_ID_HEADER;

@RestController
@RequestMapping(path = "/v3/user/", consumes = {MediaType.APPLICATION_JSON_VALUE})
public class UserController extends BaseController {
    private final UserService userService;
    private final RoleService roleService;
    private final LocalizationService localizationService;
    private final ProfileService profileService;

    public UserController(SessionService sessionService, UserService userService, RoleService roleService,
                          LocalizationService localizationService, ProfileService profileService) {
        super(sessionService);
        this.userService = userService;
        this.roleService = roleService;
        this.localizationService = localizationService;
        this.profileService = profileService;
    }

    @RequestValidateRequired(beanValidator = LoginRequestValidator.class)
    @RequestMapping(path = "login", method = RequestMethod.POST)
    public StandardResponse login(@RequestBody LoginRequest request, HttpServletRequest httpRequest,
                                  HttpServletResponse response) {
        SessionInfo sessionInfo = getCurrentSession();
        if(sessionInfo != null) {
            if(sessionService.isAuthorized(sessionInfo)) {
                if(userService.isBlocked(sessionInfo.getUserId())) {
                    throw ServiceException.builder()
                            .code(ApiResultCode.USER_BLOCKED)
                            .message(localizationService.getTranslated(sessionInfo, "ui.text.error.user_blocked"))
                            .systemMessage(localizationService.getTranslated("eng", "ui.text.error.user_blocked"))
                            .build();
                }
                response.setHeader(X_USER_SESSION_ID_HEADER, sessionInfo.getId());
                LoginResponse loginResponse = new LoginResponse();
                loginResponse.setUserInfo(profileService.getUserInfo(sessionInfo.getUserId()));
                loginResponse.setRoleInfos(roleService.getRoles(sessionInfo.getUserId()));
                loginResponse.setStartPageUrl("/home/");
                return loginResponse;
            } else {
                sessionService.deleteSession(sessionInfo.getId());
            }
        }
        Pair<String, LoginResponse> loginResult = userService.login(request.getLogin(), request.getPassword(),
                httpRequest.getRemoteAddr(), null, request.getLanguageIso3());
        response.setHeader(X_USER_SESSION_ID_HEADER, loginResult.getFirst());
        return loginResult.getSecond();
    }

    @SessionRequired
    @RequestMapping(path = "logout", method = RequestMethod.POST)
    public StandardResponse logout() {
        SessionInfo sessionInfo = getCurrentSession();
        sessionService.deleteSession(sessionInfo.getId());
        return new StandardResponse();
    }

    @SessionRequired
    @RequestValidateRequired(beanValidator = SetPasswordRequestValidator.class)
    @RequestMapping(path = "setPassword", method = RequestMethod.POST)
    public StandardResponse setPassword(@RequestBody SetPasswordRequest request) {
        SessionInfo sessionInfo = getCurrentSession();
        userService.setPassword(sessionInfo, request.getOldPassword(), request.getNewPassword());
        sessionService.deleteAllExpect(sessionInfo);
        StandardResponse response = new StandardResponse();
        response.getResponseInfo().setMessage(localizationService.getTranslated(sessionInfo,
                "ui.text.password_changed"));
        response.getResponseInfo().setSystemMessage(localizationService.getTranslated("eng",
                "ui.text.password_changed"));
        return response;
    }

    @SessionRequired(requiredAction = "MODIFY_USERS")
    @RequestValidateRequired(beanValidator = RegisterRequestValidator.class)
    @RequestMapping(path = "register", method = RequestMethod.POST)
    public StandardResponse register(@RequestBody RegisterRequest request) {
        SessionInfo sessionInfo = getCurrentSession();
        return userService.register(sessionInfo, request.getLogin(), request.getPassword(), request.getUserInfo(),
                request.getRoles());
    }

    @SessionRequired(requiredAction = "MODIFY_USERS")
    @RequestValidateRequired(beanValidator = RegisterRequestValidator.class)
    @RequestMapping(path = "getAll", method = RequestMethod.POST)
    public StandardResponse getAll(@RequestBody StandardRequest request) {
        SessionInfo sessionInfo = getCurrentSession();
        return new GetUsersResponse(userService.getAll(sessionInfo));
    }

    @SessionRequired
    @RequestValidateRequired(beanValidator = SetBlockRequestValidator.class)
    @RequestMapping(path = "setBlock", method = RequestMethod.POST)
    public StandardResponse setBlock(@RequestBody SetBlockRequest request) {
        SessionInfo sessionInfo = getCurrentSession();
        boolean blocked = userService.setBlock(sessionInfo, request.getUserId(), request.getBlocked());
        if(blocked) {
            sessionService.deleteAllFor(request.getUserId());
        }
        return new SetBlockResponse(request.getUserId(), blocked);
    }
}
