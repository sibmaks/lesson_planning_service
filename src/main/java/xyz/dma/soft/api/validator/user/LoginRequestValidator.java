package xyz.dma.soft.api.validator.user;

import org.springframework.stereotype.Component;
import xyz.dma.soft.api.request.user.LoginRequest;
import xyz.dma.soft.api.validator.ARequestValidator;
import xyz.dma.soft.core.IConstraintContext;
import xyz.dma.soft.core.impl.ConstraintContextImpl;

import static xyz.dma.soft.constants.ICommonConstants.LANGUAGES;

@Component
public class LoginRequestValidator extends ARequestValidator<LoginRequest> {
    @Override
    public IConstraintContext validate(LoginRequest request) {
        ConstraintContextImpl context = new ConstraintContextImpl();
        addConstraint(context, isEmpty(request.getLogin()), "empty", "login");
        addConstraint(context, isEmpty(request.getPassword()), "empty", "password");
        addConstraint(context, !LANGUAGES.contains(request.getLanguageIso3()), "invalid", "language");
        return context;
    }
}
