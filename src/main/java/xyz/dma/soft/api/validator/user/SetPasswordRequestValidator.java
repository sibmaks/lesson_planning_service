package xyz.dma.soft.api.validator.user;

import org.springframework.stereotype.Component;
import xyz.dma.soft.api.request.user.SetPasswordRequest;
import xyz.dma.soft.api.validator.ARequestValidator;
import xyz.dma.soft.core.IConstraintContext;
import xyz.dma.soft.core.impl.ConstraintContextImpl;
import xyz.dma.soft.entity.ConstraintType;

@Component
public class SetPasswordRequestValidator extends ARequestValidator<SetPasswordRequest> {
    @Override
    public IConstraintContext validate(SetPasswordRequest request) {
        ConstraintContextImpl context = new ConstraintContextImpl();
        addConstraint(context, isEmpty(request.getOldPassword()), ConstraintType.EMPTY, "oldPassword");

        chainConstraint(context)
                .addConstraint(() -> isEmpty(request.getNewPassword()), ConstraintType.EMPTY, "newPassword")
                .addConstraint(() -> isLess(request.getNewPassword(), 6), ConstraintType.SHORT, "newPassword");

        return context;
    }
}
