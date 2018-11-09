package com.middleware.gateway.filter;

import com.google.common.io.CharStreams;
import com.middleware.common.util.HttpRequestUtil;
import com.middleware.common.util.JsonUtil;
import com.middleware.common.util.LogUtil;
import com.middleware.gateway.model.AccessLog;
import com.middleware.gateway.model.RequestInfo;
import com.middleware.gateway.model.ResponseInfo;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;

@Component
public class AccessLogFilter extends ZuulFilter {

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        try {
            RequestContext context = RequestContext.getCurrentContext();

            HttpServletRequest request = context.getRequest();
            RequestInfo requestInfo = convertRequestToRequestInfo(request);

            String responseBody = context.getResponseBody();
            if (responseBody == null) {
                InputStream inputStream = context.getResponseDataStream();
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream);
                    try {
                        responseBody = CharStreams.toString(reader);
                        context.setResponseBody(responseBody);
                    } catch (IOException e) {
                        LogUtil.error(this.getClass(), "Read response body failed", e);
                    }
                }
            }
            int responseStatus = context.getResponseStatusCode();

            ResponseInfo responseInfo = new ResponseInfo();
            responseInfo.setStatus(responseStatus);
            responseInfo.setResponseStr(responseBody);

            AccessLog accessLog = new AccessLog();
            accessLog.setRequestInfo(requestInfo);
            accessLog.setResponseInfo(responseInfo);

            LogUtil.debug(this.getClass(), JsonUtil.toJson(accessLog));
        } catch (Exception e) {
            LogUtil.error(this.getClass(), "Write access log failed", e);
        }

        return null;
    }

    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    private RequestInfo convertRequestToRequestInfo(HttpServletRequest request) {
        RequestInfo requestInfo = new RequestInfo();
        String requestId = HttpRequestUtil.getRequestId(request);
        Long requestTime = (Long) request.getAttribute("request-time");
        requestInfo.setRequestTime(new Date(requestTime));
        requestInfo.setRequestDuration(Math.toIntExact(System.currentTimeMillis() - requestTime));
        requestInfo.setRequestId(requestId);
        requestInfo.setMethodName(request.getMethod());
        requestInfo.setPathInfo(request.getPathInfo());
        requestInfo.setQueryString(request.getQueryString());
        requestInfo.setRemoteAddr(request.getRemoteAddr());
        requestInfo.setRemoteHost(request.getRemoteHost());
        requestInfo.setRemoteUser(request.getRemoteUser());
        requestInfo.setRemotePort(request.getRemotePort());
        requestInfo.setRequestUrl(request.getRequestURL().toString());
        Enumeration<String> requestHeaderNames = request.getHeaderNames();
        HashMap<String, String> headers = new HashMap<>();
        while (requestHeaderNames.hasMoreElements()) {
            String headerName = requestHeaderNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }
        requestInfo.setRequestHeaders(headers);

        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream);
                requestInfo.setRequestBody(CharStreams.toString(reader));
            }
        } catch (Exception e) {
            LogUtil.error(this.getClass(), "Read request body failed", e);
        }

        return requestInfo;
    }

}
