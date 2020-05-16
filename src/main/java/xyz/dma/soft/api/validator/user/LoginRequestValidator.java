package xyz.dma.soft.api.validator.user;

import org.springframework.stereotype.Component;
import xyz.dma.soft.api.request.user.LoginRequest;
import xyz.dma.soft.api.validator.ARequestValidator;
import xyz.dma.soft.core.constraint.ConstraintContextBuilder;
import xyz.dma.soft.core.constraint.IConstraintContext;
import xyz.dma.soft.entity.ConstraintType;

import static xyz.dma.soft.constants.ICommonConstants.LANGUAGES;

@Component
public class LoginRequestValidator extends ARequestValidator<LoginRequest> {
    @Override
    public IConstraintContext validate(LoginRequest request) {
        ConstraintContextBuilder context = new ConstraintContextBuilder()
            .assertConstraintViolation(isEmpty(request.getLogin()), ConstraintType.EMPTY, "login")
            .assertConstraintViolation(isEmpty(request.getPassword()), ConstraintType.EMPTY, "password")
            .assertConstraintViolation(0, isEmpty(request.getLanguageIso3()), ConstraintType.EMPTY, "languageIso3")
            .assertConstraintViolation(1, () -> !LANGUAGES.contains(request.getLanguageIso3()), ConstraintType.INVALID, "languageIso3");
        return context.build();
    }
}
