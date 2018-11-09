package com.middleware.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.stereotype.Component;

@Component
public class RequestTimePreFilter extends ZuulFilter {
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
        Long requestTime = System.currentTimeMillis();
        RequestContext requestContext = RequestContext.getCurrentContext();
        requestContext.getRequest().setAttribute("request-time", requestTime);
        return null;
    }
}
