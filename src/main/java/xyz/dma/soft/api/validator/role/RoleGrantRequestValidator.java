package xyz.dma.soft.api.validator.role;

import org.springframework.stereotype.Component;
import xyz.dma.soft.api.request.role.GrantRoleRequest;
import xyz.dma.soft.api.validator.ARequestValidator;
import xyz.dma.soft.core.constraint.ConstraintContextBuilder;
import xyz.dma.soft.core.constraint.IConstraintContext;
import xyz.dma.soft.entity.ConstraintType;

@Component
public class RoleGrantRequestValidator extends ARequestValidator<GrantRoleRequest> {

    @Override
    public IConstraintContext validate(GrantRoleRequest request) {
        return new ConstraintContextBuilder()
                .assertConstraintViolation(isNull(request.getRole()), ConstraintType.EMPTY, "role")
                .assertConstraintViolation(isNull(request.getUserId()), ConstraintType.EMPTY, "userId")
                .build();
    }
}
