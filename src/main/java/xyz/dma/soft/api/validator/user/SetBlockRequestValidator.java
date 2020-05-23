package xyz.dma.soft.api.validator.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import xyz.dma.soft.api.request.user.SetBlockRequest;
import xyz.dma.soft.api.validator.ARequestValidator;
import xyz.dma.soft.core.constraint.IConstraintContext;
import xyz.dma.soft.core.constraint.impl.ConstraintContextBuilder;
import xyz.dma.soft.entity.ConstraintType;
import xyz.dma.soft.entity.SessionInfo;
import xyz.dma.soft.service.SessionService;

import static xyz.dma.soft.utils.HttpUtils.getCurrentHttpRequest;

@Slf4j
@Component
@AllArgsConstructor
public class SetBlockRequestValidator extends ARequestValidator<SetBlockRequest> {
    protected final SessionService sessionService;

    @Override
    public IConstraintContext validate(SetBlockRequest request) {
        SessionInfo sessionInfo = sessionService.getCurrentSession(getCurrentHttpRequest());

        ConstraintContextBuilder contextBuilder = new ConstraintContextBuilder();
        contextBuilder
                .line(request)
                    .validate(it -> notNull(it.getBlocked()), "blocked")
                    .chain()
                        .validate(it -> notNull(it.getUserId()), "userId")
                        .validate(it -> !it.getUserId().equals(sessionInfo.getUserId()) ? ConstraintType.INVALID : null, "userId");
        return contextBuilder.build();
    }
}
