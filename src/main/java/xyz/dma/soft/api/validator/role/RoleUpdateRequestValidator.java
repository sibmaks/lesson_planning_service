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
        ConstraintContextBuilder context = new ConstraintContextBuilder()
                .assertConstraintViolation(isNull(request.getRole()), ConstraintType.EMPTY, "role");
        if(request.getAllowedActions() != null) {
            for(String action : request.getAllowedActions()) {
                context.assertConstraintViolation(isNull(userActionRepository.findFirstByName(action)), ConstraintType.INVALID, "allowedActions", action);
            }
        }
        return context.build();
    }
}
