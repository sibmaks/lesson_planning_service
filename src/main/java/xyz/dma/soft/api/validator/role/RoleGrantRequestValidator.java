package xyz.dma.soft.api.validator.role;

import org.springframework.stereotype.Component;
import xyz.dma.soft.api.request.role.GrantRoleRequest;
import xyz.dma.soft.api.validator.ARequestValidator;
import xyz.dma.soft.core.constraint.ConstraintContextBuilder;
import xyz.dma.soft.core.constraint.IConstraintContext;

@Component
public class RoleGrantRequestValidator extends ARequestValidator<GrantRoleRequest> {

    @Override
    public IConstraintContext validate(GrantRoleRequest request) {
        ConstraintContextBuilder contextBuilder = new ConstraintContextBuilder();
        contextBuilder
                .line(request)
                .validate(it -> notNull(it.getRole()), "role")
                .validate(it -> notNull(it.getUserId()), "userId");
        return contextBuilder.build();
    }
}
