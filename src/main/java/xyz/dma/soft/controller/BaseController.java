package xyz.dma.soft.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.dma.soft.entity.SessionInfo;
import xyz.dma.soft.service.SessionService;

import static xyz.dma.soft.utils.HttpUtils.getCurrentHttpRequest;

@Slf4j
@AllArgsConstructor
public abstract class BaseController {
    protected final SessionService sessionService;

    public SessionInfo getCurrentSession() {
        return sessionService.getCurrentSession(getCurrentHttpRequest());
    }
}
