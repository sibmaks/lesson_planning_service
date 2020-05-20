package xyz.dma.soft.api.validator.user;

import org.springframework.stereotype.Component;
import xyz.dma.soft.api.request.user.SetPasswordRequest;
import xyz.dma.soft.api.validator.ARequestValidator;
import xyz.dma.soft.core.constraint.ConstraintContextBuilder;
import xyz.dma.soft.core.constraint.IConstraintContext;

@Component
public class SetPasswordRequestValidator extends ARequestValidator<SetPasswordRequest> {
    @Override
    public IConstraintContext validate(SetPasswordRequest request) {
        ConstraintContextBuilder contextBuilder = new ConstraintContextBuilder();

        contextBuilder
                .line(request)
                    .validate(it -> notEmpty(it.getOldPassword()), "oldPassword")
                    .chain()
                        .map(SetPasswordRequest::getNewPassword, "newPassword")
                        .validate(this::notEmpty)
                        .validate(it -> isMore(it, 6));

        return contextBuilder.build();
    }
}
