package xyz.dma.soft.api.validator.user;

import org.springframework.stereotype.Component;
import xyz.dma.soft.api.request.user.SetPasswordRequest;
import xyz.dma.soft.api.validator.ARequestValidator;
import xyz.dma.soft.core.IConstraintContext;
import xyz.dma.soft.core.impl.ConstraintContextImpl;

@Component
public class SetPasswordRequestValidator extends ARequestValidator<SetPasswordRequest> {
    @Override
    public IConstraintContext validate(SetPasswordRequest request) {
        ConstraintContextImpl context = new ConstraintContextImpl();
        addConstraint(context, isEmpty(request.getOldPassword()), "empty", "oldPassword");

        chainConstraint(context)
                .addConstraint(() -> isEmpty(request.getNewPassword()), "empty", "newPassword")
                .addConstraint(() -> isLess(request.getNewPassword(), 6), "short", "newPassword");

        return context;
    }
}
