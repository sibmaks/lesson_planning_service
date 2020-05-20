package xyz.dma.soft.api.validator.role;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import xyz.dma.soft.api.request.role.ModifyRoleRequest;
import xyz.dma.soft.api.validator.ARequestValidator;
import xyz.dma.soft.core.constraint.ConstraintContextBuilder;
import xyz.dma.soft.core.constraint.IConstraintContext;
import xyz.dma.soft.entity.ConstraintType;
import xyz.dma.soft.repository.UserActionRepository;

@Component
@AllArgsConstructor
public class RoleUpdateRequestValidator extends ARequestValidator<ModifyRoleRequest> {
    private final UserActionRepository userActionRepository;

    @Override
    public IConstraintContext validate(ModifyRoleRequest request) {
        ConstraintContextBuilder contextBuilder = new ConstraintContextBuilder();
        contextBuilder
                .line(request)
                    .validate(it -> notNull(it.getRole()), "role")
                    .chain()
                        .filter(it -> notEmpty(it.getAllowedActions()))
                        .flatMap(ModifyRoleRequest::getAllowedActions, "allowedActions")
                            .validate(this::notNull)
                            .validate(it -> userActionRepository.findFirstByName(it) == null ? ConstraintType.INVALID : null);
        return contextBuilder.build();
    }
}
