package xyz.dma.soft.api.validator.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import xyz.dma.soft.api.request.user.RegisterRequest;
import xyz.dma.soft.api.validator.ARequestValidator;
import xyz.dma.soft.core.IConstraintContext;
import xyz.dma.soft.core.impl.ConstraintContextImpl;
import xyz.dma.soft.repository.UserRoleRepository;

import static java.util.Objects.isNull;

@Component
@AllArgsConstructor
public class RegisterRequestValidator extends ARequestValidator<RegisterRequest> {
    private final UserRoleRepository userRoleRepository;

    @Override
    public IConstraintContext validate(RegisterRequest request) {
        ConstraintContextImpl context = new ConstraintContextImpl();
        addConstraint(context, isEmpty(request.getLogin()), "empty", "login");
        addConstraint(context, isEmpty(request.getPassword()), "empty", "password");

        chainConstraint(context)
                .addConstraint(0, () -> isNull(request.getUserInfo()), "empty", "userInfo")
                .addConstraint(1, () -> isEmpty(request.getUserInfo().getFirstName()), "empty", "userInfo", "firstName")
                .addConstraint(1, () -> isEmpty(request.getUserInfo().getLastName()), "empty", "userInfo", "lastName");

        if (!addConstraint(context, isEmpty(request.getRoles()), "empty", "roles")) {
            for (String role : request.getRoles()) {
                addConstraint(context, !userRoleRepository.existsByName(role), "invalid", "roles", role);
            }
        }
        return context;
    }
}
