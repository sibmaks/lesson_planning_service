package xyz.dma.soft.api.validator.role;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import xyz.dma.soft.api.request.role.ModifyRoleRequest;
import xyz.dma.soft.api.validator.ARequestValidator;
import xyz.dma.soft.core.IConstraintContext;
import xyz.dma.soft.core.impl.ConstraintContextImpl;
import xyz.dma.soft.entity.ConstraintType;
import xyz.dma.soft.repository.UserActionRepository;

import static java.util.Objects.isNull;

@Component
@AllArgsConstructor
public class RoleUpdateRequestValidator extends ARequestValidator<ModifyRoleRequest> {
    private final UserActionRepository userActionRepository;

    @Override
    public IConstraintContext validate(ModifyRoleRequest request) {
        ConstraintContextImpl context = new ConstraintContextImpl();
        addConstraint(context, isNull(request.getRole()), ConstraintType.EMPTY, "role");
        if(request.getAllowedActions() != null) {
            for(String action : request.getAllowedActions()) {
                addConstraint(context, isNull(userActionRepository.findFirstByName(action)), ConstraintType.INVALID, "allowedActions", action);
            }
        }
        return context;
    }
}