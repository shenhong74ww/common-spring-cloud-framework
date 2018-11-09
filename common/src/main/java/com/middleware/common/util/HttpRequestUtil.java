package com.middleware.common.util;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class HttpRequestUtil {

    public static HttpServletRequest getCurrentHttpRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null && requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            return request;
        }
        return null;
    }

    public static String getRequestId() {
        return getRequestId(getCurrentHttpRequest());
    }

    public static String getRequestId(HttpServletRequest request) {
        String requestId = null;
        if (request != null) {
            requestId = request.getHeader("request-id");
            if (StringUtil.isEmpty(requestId)) {
                requestId = String.valueOf(request.getAttribute("request-id"));
            }
        }
        return requestId;
    }

}
