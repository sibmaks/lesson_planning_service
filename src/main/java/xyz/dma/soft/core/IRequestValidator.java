package xyz.dma.soft.core;

import xyz.dma.soft.api.request.StandardRequest;


public interface IRequestValidator<T extends StandardRequest> {
    IConstraintContext validate(T request);
}
