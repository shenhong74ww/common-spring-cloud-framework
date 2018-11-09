package com.middleware.common.util.extractor;

import com.middleware.common.constant.enums.RevokeReason;
import com.middleware.common.exception.RemoteLoginException;
import com.middleware.common.exception.ResetPasswordReloginException;
import com.middleware.common.model.ResponseEntity;
import com.middleware.common.model.TokenInfo;
import com.middleware.common.util.JsonUtil;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserPrincipalExtractor implements PrincipalExtractor {

    @Override
    public Object extractPrincipal(Map<String, Object> map) {
        String mapJson = JsonUtil.toJson(map);
        ResponseEntity<TokenInfo> tokenResult = JsonUtil.fromJson(mapJson, ResponseEntity.class, TokenInfo.class);
        RevokeReason revokeReason = null;

        if (tokenResult.getResult() != null) {
            revokeReason = tokenResult.getResult().getRevokeReason();
        }

        if (tokenResult.getErrorCode() != 0) {
            throw OAuth2Exception.create("invalid_token", tokenResult.getErrorMsg());
        } else if (revokeReason != null) {

            if (revokeReason == RevokeReason.REMOTE_LOGIN) {
                throw new RemoteLoginException("Your account have been logged elsewhere, please login again");
            } else if (revokeReason == RevokeReason.RESET_PASSWORD) {
                throw new ResetPasswordReloginException("You have reset your password, please login again");
            }
        }

        return tokenResult.getResult();
    }
}
