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
        ConstraintContextBuilder contextBuilder = new ConstraintContextBuilder();
        contextBuilder
                .line(request)
                    .validate(it -> notEmpty(it.getLogin()), "login")
                    .validate(it -> notEmpty(it.getPassword()), "password")
                    .chain()
                        .map(LoginRequest::getLanguageIso3, "languageIso3")
                        .validate(this::notEmpty)
                        .validate(it -> LANGUAGES.contains(it) ? null : ConstraintType.INVALID);
        return contextBuilder.build();
    }
}
