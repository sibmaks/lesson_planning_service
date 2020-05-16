package xyz.dma.soft.api.validator.role;

import org.springframework.stereotype.Component;
import xyz.dma.soft.api.request.role.RetractRoleRequest;
import xyz.dma.soft.api.validator.ARequestValidator;
import xyz.dma.soft.core.constraint.ConstraintContextBuilder;
import xyz.dma.soft.core.constraint.IConstraintContext;
import xyz.dma.soft.entity.ConstraintType;

@Component
public class RoleRetractRequestValidator extends ARequestValidator<RetractRoleRequest> {

    @Override
    public IConstraintContext validate(RetractRoleRequest request) {
        return new ConstraintContextBuilder()
                .assertConstraintViolation(isNull(request.getRole()), ConstraintType.EMPTY, "role")
                .assertConstraintViolation(isNull(request.getUserId()), ConstraintType.EMPTY, "userId")
                .build();
    }
}
