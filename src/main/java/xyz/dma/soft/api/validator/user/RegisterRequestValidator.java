package xyz.dma.soft.api.validator.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import xyz.dma.soft.api.request.user.RegisterRequest;
import xyz.dma.soft.api.validator.ARequestValidator;
import xyz.dma.soft.core.constraint.ConstraintContextBuilder;
import xyz.dma.soft.core.constraint.IConstraintContext;
import xyz.dma.soft.core.constraint.ILineConstraintValidator;
import xyz.dma.soft.entity.ConstraintType;
import xyz.dma.soft.repository.UserRoleRepository;

@Component
@AllArgsConstructor
public class RegisterRequestValidator extends ARequestValidator<RegisterRequest> {
    private final UserRoleRepository userRoleRepository;

    @Override
    public IConstraintContext validate(RegisterRequest request) {
        ConstraintContextBuilder contextBuilder = new ConstraintContextBuilder();
        
        ILineConstraintValidator<RegisterRequest> constraintValidator = contextBuilder
                .line(request)
                    .validate(it -> notEmpty(it.getLogin()), "login")
                    .validate(it -> notEmpty(it.getPassword()), "password");

        constraintValidator
                .chain()
                    .map(RegisterRequest::getUserInfo, "userInfo")
                    .validate(this::notNull)
                    .line()
                        .validate(it -> notEmpty(it.getFirstName()), "firstName")
                        .validate(it -> notEmpty(it.getLastName()), "lastName");

        constraintValidator
                .chain()
                    .map(RegisterRequest::getRoles, "roles")
                    .validate(this::notEmpty)
                    .validate((it) -> {
                        for (String role : it) {
                            if(!userRoleRepository.existsByName(role)) {
                                return ConstraintType.INVALID;
                            }
                        }
                        return null;
                    });

        return contextBuilder.build();
    }
}
