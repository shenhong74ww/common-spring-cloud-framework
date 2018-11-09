package com.middleware.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RequestIdPreFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        String requestId = UUID.randomUUID().toString();
        RequestContext requestContext = RequestContext.getCurrentContext();
        requestContext.addZuulRequestHeader("request-id", requestId);
        requestContext.getRequest().setAttribute("request-id", requestId);
        return null;
    }
}
