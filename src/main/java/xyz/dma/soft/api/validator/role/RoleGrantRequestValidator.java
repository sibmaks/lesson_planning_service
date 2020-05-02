package xyz.dma.soft.api.validator.role;

import org.springframework.stereotype.Component;
import xyz.dma.soft.api.request.role.GrantRoleRequest;
import xyz.dma.soft.api.validator.ARequestValidator;
import xyz.dma.soft.core.IConstraintContext;
import xyz.dma.soft.core.impl.ConstraintContextImpl;

import static java.util.Objects.isNull;

@Component
public class RoleGrantRequestValidator extends ARequestValidator<GrantRoleRequest> {

    @Override
    public IConstraintContext validate(GrantRoleRequest request) {
        ConstraintContextImpl context = new ConstraintContextImpl();
        addConstraint(context, isNull(request.getRole()), "empty", "role");
        addConstraint(context, isNull(request.getUserId()), "empty", "userId");
        return context;
    }
}
