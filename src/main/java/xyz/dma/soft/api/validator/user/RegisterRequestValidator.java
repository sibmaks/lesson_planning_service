package xyz.dma.soft.api.validator.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import xyz.dma.soft.api.request.user.RegisterRequest;
import xyz.dma.soft.api.validator.ARequestValidator;
import xyz.dma.soft.core.constraint.ConstraintContextBuilder;
import xyz.dma.soft.core.constraint.IConstraintContext;
import xyz.dma.soft.entity.ConstraintType;
import xyz.dma.soft.repository.UserRoleRepository;

@Component
@AllArgsConstructor
public class RegisterRequestValidator extends ARequestValidator<RegisterRequest> {
    private final UserRoleRepository userRoleRepository;

    @Override
    public IConstraintContext validate(RegisterRequest request) {
        ConstraintContextBuilder context = new ConstraintContextBuilder()
                .assertConstraintViolation(isEmpty(request.getLogin()), ConstraintType.EMPTY, "login")
                .assertConstraintViolation(isEmpty(request.getPassword()), ConstraintType.EMPTY, "password")
                .assertConstraintViolation(0, isNull(request.getUserInfo()), ConstraintType.EMPTY, "userInfo")
                .assertConstraintViolation(1, isEmpty(request.getUserInfo().getFirstName()), ConstraintType.EMPTY, "userInfo", "firstName")
                .assertConstraintViolation(1, isEmpty(request.getUserInfo().getLastName()), ConstraintType.EMPTY, "userInfo", "lastName")
                .assertConstraintViolation(0, isEmpty(request.getRoles()), ConstraintType.EMPTY, "roles")
                .assertConstraintViolation(1, () -> {
                    for (String role : request.getRoles()) {
                        if(!userRoleRepository.existsByName(role)) {
                            return true;
                        }
                    }
                    return false;
                }, ConstraintType.INVALID, "roles");

        return context.build();
    }
}
