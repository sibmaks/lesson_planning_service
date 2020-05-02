package xyz.dma.soft.conf.handler;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;
import xyz.dma.soft.api.entity.ApiResultCode;
import xyz.dma.soft.api.request.StandardRequest;
import xyz.dma.soft.core.IConstraintContext;
import xyz.dma.soft.core.IRequestValidator;
import xyz.dma.soft.core.RequestValidateRequired;
import xyz.dma.soft.exception.ConstraintException;
import xyz.dma.soft.exception.ServiceException;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class RequestValidationAdviceAdapter extends RequestBodyAdviceAdapter {
    private final Map<Class<?>, IRequestValidator> validatorMap;

    public RequestValidationAdviceAdapter(List<IRequestValidator<? extends StandardRequest>> requestValidators) {
        this.validatorMap = new HashMap<>();
        requestValidators.forEach(it -> validatorMap.put(it.getClass(), it));
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        return methodParameter.getMethod() != null &&
                methodParameter.getMethod().isAnnotationPresent(RequestValidateRequired.class) &&
                StandardRequest.class.isAssignableFrom(toClass(targetType));
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
        RequestValidateRequired sessionRequired = parameter.getMethod().getAnnotation(RequestValidateRequired.class);
        IRequestValidator<StandardRequest> requestValidator = validatorMap.get(sessionRequired.beanValidator());
        if (requestValidator == null) {
            throw ServiceException.builder().code(ApiResultCode.UNEXPECTED_ERROR).systemMessage("Request validator not found").build();
        }
        StandardRequest standardRequest = (StandardRequest) body;
        IConstraintContext constraintContext = requestValidator.validate(standardRequest);
        if (!constraintContext.isValid()) {
            throw new ConstraintException(constraintContext);
        }
        return body;
    }
}