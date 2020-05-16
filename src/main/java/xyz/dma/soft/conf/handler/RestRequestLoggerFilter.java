package xyz.dma.soft.conf.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;
import xyz.dma.soft.entity.BufferedRequestWrapper;
import xyz.dma.soft.entity.RestLogRecord;
import xyz.dma.soft.utils.RequestLogUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class RestRequestLoggerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String id = UUID.randomUUID().toString();
        RestLogRecord requestLog;
        long time = System.currentTimeMillis();
        if (!isAsyncDispatch(request)) {
            if (!(request instanceof BufferedRequestWrapper)) {
                request = new BufferedRequestWrapper(request);
            }
            if (!(response instanceof ContentCachingResponseWrapper)) {
                response = new ContentCachingResponseWrapper(response);
            }
            requestLog = logRequest(id, request);
        } else {
            requestLog = new RestLogRecord();
            requestLog.setId(id);
            requestLog.setAddress(request.getRequestURI());
            requestLog.setHeaders(getHeaders(request));
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            if (!isAsyncStarted(request)) {
                logResponse(time, response, requestLog);
            }
        }
    }

    private RestLogRecord logRequest(String id, HttpServletRequest request) {
        BufferedRequestWrapper requestToCache = WebUtils.getNativeRequest(request, BufferedRequestWrapper.class);
        String requestPayload = null;
        if(requestToCache != null) {
            requestPayload = requestToCache.extractPayload();
        }

        RestLogRecord restLogRecord = new RestLogRecord();
        restLogRecord.setId(id);
        restLogRecord.setAddress(request.getRequestURI());
        restLogRecord.setHeaders(getHeaders(request));
        restLogRecord.setRequest(requestPayload);
        restLogRecord.setIncomeDirection(true);
        log.info(restLogRecord.toSimpleFormat());
        return restLogRecord;
    }

    private void logResponse(long timeStart, HttpServletResponse response, RestLogRecord restLogRecord) {
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        String responsePayload = null;
        if (wrapper != null) {
            responsePayload = RequestLogUtils.extractPayload(wrapper.getContentAsByteArray(),wrapper.getCharacterEncoding());
            try {
                wrapper.copyBodyToResponse();
            } catch (IOException ex) {
                logger.warn("Can't copy body to response");
            }
        }
        restLogRecord.setIncomeDirection(false);
        restLogRecord.setTimer(System.currentTimeMillis() - timeStart);
        restLogRecord.setStatus(response.getStatus());
        restLogRecord.setPayload(responsePayload);
        log.info(restLogRecord.toSimpleFormat());
    }


    private static Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headersEnums = request.getHeaderNames();
        while (headersEnums.hasMoreElements()) {
            String header = headersEnums.nextElement();
            headers.put(header, request.getHeader(header));
        }
        return headers;
    }
}
