package com.magnxpyr.prometheus.config;

import io.prometheus.client.Summary;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * Created by Stefan Chiriac on 10.05.2017.
 */
public class RequestTimingInterceptor extends HandlerInterceptorAdapter {

    private static final String REQ_PARAM_TIMING = "timing";

    private static final Summary responseTimeInMs = Summary
            .build()
            .name("http_response_time_milliseconds")
            .labelNames("method", "handler", "status")
            .help("Request completed time in milliseconds")
            .register();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // The response time is measured here as time lapsed between the calls to preHandle and afterCompletion methods
        request.setAttribute(REQ_PARAM_TIMING, System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        Long timingAttr = (Long) request.getAttribute(REQ_PARAM_TIMING);
        long completedTime = System.currentTimeMillis() - timingAttr;
        String handlerLabel = handler.toString();
        // get short form of handler method name
        if (handler instanceof HandlerMethod) {
            Method method = ((HandlerMethod) handler).getMethod();
            handlerLabel = method.getDeclaringClass().getSimpleName() + "." + method.getName();
        }
        // The summary metric is updated by calling the observe() method with the response time value
        responseTimeInMs.labels(request.getMethod(), handlerLabel, Integer.toString(response.getStatus())).observe(
                completedTime);
    }
}
