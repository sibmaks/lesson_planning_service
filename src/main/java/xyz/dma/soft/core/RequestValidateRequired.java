package xyz.dma.soft.core;


import xyz.dma.soft.api.request.StandardRequest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestValidateRequired {
    Class<? extends IRequestValidator<? extends StandardRequest>> beanValidator();
}
