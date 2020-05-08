package xyz.dma.soft.controller.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import xyz.dma.soft.api.request.profile.ChangeProfileRequest;
import xyz.dma.soft.api.response.profile.GetProfileResponse;
import xyz.dma.soft.api.response.StandardResponse;
import xyz.dma.soft.controller.BaseController;
import xyz.dma.soft.core.SessionRequired;
import xyz.dma.soft.entity.SessionInfo;
import xyz.dma.soft.service.ProfileService;
import xyz.dma.soft.service.SessionService;

@RestController
@RequestMapping(path = "/v3/profile/", consumes = {MediaType.APPLICATION_JSON_VALUE})
public class ProfileController extends BaseController {
    private final ProfileService profileService;

    public ProfileController(SessionService sessionService, ProfileService profileService) {
        super(sessionService);
        this.profileService = profileService;
    }

    @SessionRequired
    @RequestMapping(path = "getProfile", method = RequestMethod.POST)
    public StandardResponse getProfile() {
        SessionInfo sessionInfo = getCurrentSession();
        return new GetProfileResponse(profileService.getUserInfo(sessionInfo.getUserId()));
    }

    @SessionRequired
    @RequestMapping(path = "changeProfile", method = RequestMethod.POST)
    public StandardResponse changeProfile(@RequestBody ChangeProfileRequest request) {
        SessionInfo sessionInfo = getCurrentSession();
        profileService.changeProfile(sessionInfo.getUserId(), request.getUserInfo());
        return new StandardResponse();
    }
}
