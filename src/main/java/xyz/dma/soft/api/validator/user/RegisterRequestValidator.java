package xyz.dma.soft.api.validator.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import xyz.dma.soft.api.request.user.RegisterRequest;
import xyz.dma.soft.api.validator.ARequestValidator;
import xyz.dma.soft.core.IConstraintContext;
import xyz.dma.soft.core.impl.ConstraintContextImpl;
import xyz.dma.soft.entity.ConstraintType;
import xyz.dma.soft.repository.UserRoleRepository;

import static java.util.Objects.isNull;

@Component
@AllArgsConstructor
public class RegisterRequestValidator extends ARequestValidator<RegisterRequest> {
    private final UserRoleRepository userRoleRepository;

    @Override
    public IConstraintContext validate(RegisterRequest request) {
        ConstraintContextImpl context = new ConstraintContextImpl();
        addConstraint(context, isEmpty(request.getLogin()), ConstraintType.EMPTY, "login");
        addConstraint(context, isEmpty(request.getPassword()), ConstraintType.EMPTY, "password");

        chainConstraint(context)
                .addConstraint(0, () -> isNull(request.getUserInfo()), ConstraintType.EMPTY, "userInfo")
                .addConstraint(1, () -> isEmpty(request.getUserInfo().getFirstName()), ConstraintType.EMPTY, "userInfo", "firstName")
                .addConstraint(1, () -> isEmpty(request.getUserInfo().getLastName()), ConstraintType.EMPTY, "userInfo", "lastName");

        if (!addConstraint(context, isEmpty(request.getRoles()), ConstraintType.EMPTY, "roles")) {
            for (String role : request.getRoles()) {
                addConstraint(context, !userRoleRepository.existsByName(role), ConstraintType.INVALID, "roles", role);
            }
        }
        return context;
    }
}
