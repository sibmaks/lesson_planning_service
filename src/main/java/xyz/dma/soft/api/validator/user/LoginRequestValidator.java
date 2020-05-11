package xyz.dma.soft.api.validator.user;

import org.springframework.stereotype.Component;
import xyz.dma.soft.api.request.user.LoginRequest;
import xyz.dma.soft.api.validator.ARequestValidator;
import xyz.dma.soft.core.IConstraintContext;
import xyz.dma.soft.core.impl.ConstraintContextImpl;
import xyz.dma.soft.entity.ConstraintType;

import static xyz.dma.soft.constants.ICommonConstants.LANGUAGES;

@Component
public class LoginRequestValidator extends ARequestValidator<LoginRequest> {
    @Override
    public IConstraintContext validate(LoginRequest request) {
        ConstraintContextImpl context = new ConstraintContextImpl();
        addConstraint(context, isEmpty(request.getLogin()), ConstraintType.EMPTY, "login");
        addConstraint(context, isEmpty(request.getPassword()), ConstraintType.EMPTY, "password");
        chainConstraint(context)
        .addConstraint(0, () -> isEmpty(request.getLanguageIso3()), ConstraintType.EMPTY, "languageIso3")
        .addConstraint(1, () -> !LANGUAGES.contains(request.getLanguageIso3()), ConstraintType.INVALID, "languageIso3");
        return context;
    }
}
