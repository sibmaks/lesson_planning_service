package xyz.dma.soft.conf.handler;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;
import xyz.dma.soft.api.request.StandardRequest;
import xyz.dma.soft.core.constraint.IConstraintContext;
import xyz.dma.soft.core.constraint.IRequestValidator;
import xyz.dma.soft.core.RequestValidateRequired;
import xyz.dma.soft.exception.ConstraintException;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
@ConditionalOnBean(value = IRequestValidator.class)
public class RequestValidationAdviceAdapter extends RequestBodyAdviceAdapter {
    private final Map<Class<IRequestValidator<? extends StandardRequest>>, IRequestValidator<? extends StandardRequest>> validatorMap;

    public RequestValidationAdviceAdapter(List<IRequestValidator<? extends StandardRequest>> requestValidators) {
        this.validatorMap = new HashMap<>();
        requestValidators.forEach(it -> validatorMap.put(getClass(it), it));
    }

    private Class<IRequestValidator<? extends StandardRequest>> getClass(IRequestValidator<?> requestValidator) {
        return (Class<IRequestValidator<? extends StandardRequest>>) requestValidator.getClass();
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        Method method = methodParameter.getMethod();
        if (method != null && method.isAnnotationPresent(RequestValidateRequired.class) &&
                StandardRequest.class.isAssignableFrom(toClass(targetType))) {
            RequestValidateRequired validateRequired = method.getAnnotation(RequestValidateRequired.class);
            IRequestValidator<? extends StandardRequest> requestValidator = validatorMap.get(validateRequired.beanValidator());
            if(requestValidator == null) {
                throw new RuntimeException("Invalid validator used");
            }
            return true;
        }
        return false;
    }

    private static Class<?> toClass(Type o) {
        if (o instanceof GenericArrayType) {
            return Array.newInstance(toClass(((GenericArrayType)o).getGenericComponentType()), 0).getClass();
        }
        return (Class<?>)o;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
                                Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        RequestValidateRequired validateRequired = parameter.getMethod().getAnnotation(RequestValidateRequired.class);
        IRequestValidator<? extends StandardRequest> requestValidator = validatorMap.get(validateRequired.beanValidator());
        IConstraintContext constraintContext = requestValidator.validate(body);
        if (!constraintContext.isValid()) {
            throw new ConstraintException(constraintContext);
        }
        return body;
    }
}