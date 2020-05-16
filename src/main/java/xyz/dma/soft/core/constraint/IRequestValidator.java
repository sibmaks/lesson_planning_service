package xyz.dma.soft.core.constraint;

import xyz.dma.soft.api.request.StandardRequest;


public interface IRequestValidator<T extends StandardRequest> {
    IConstraintContext validate(T request);

    default IConstraintContext validate(Object request) {
        return validate((T) request);
    }
}
