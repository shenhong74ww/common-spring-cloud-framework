package com.middleware.common.util;

import com.middleware.common.model.TokenInfo;
import org.springframework.security.core.context.SecurityContextHolder;

public final class TokenInfoUtil {

    public static TokenInfo getCurrentTokenInfo() {
        if (SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null
                && SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof TokenInfo) {
            return (TokenInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        return null;
    }
}
