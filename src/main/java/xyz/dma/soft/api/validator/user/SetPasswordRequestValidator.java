package xyz.dma.soft.api.validator.user;

import org.springframework.stereotype.Component;
import xyz.dma.soft.api.request.user.SetPasswordRequest;
import xyz.dma.soft.api.validator.ARequestValidator;
import xyz.dma.soft.core.constraint.ConstraintContextBuilder;
import xyz.dma.soft.core.constraint.IConstraintContext;
import xyz.dma.soft.entity.ConstraintType;

@Component
public class SetPasswordRequestValidator extends ARequestValidator<SetPasswordRequest> {
    @Override
    public IConstraintContext validate(SetPasswordRequest request) {
        return new ConstraintContextBuilder()
                .assertConstraintViolation(isEmpty(request.getOldPassword()), ConstraintType.EMPTY, "oldPassword")
                .assertConstraintViolation(0, isEmpty(request.getNewPassword()), ConstraintType.EMPTY, "newPassword")
                .assertConstraintViolation(1, isLess(request.getNewPassword(), 6), ConstraintType.SHORT, "newPassword").build();
    }
}
