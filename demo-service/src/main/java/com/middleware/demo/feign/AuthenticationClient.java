package com.middleware.demo.feign;

import com.middleware.common.constant.enums.RevokeReason;
import com.middleware.common.model.ResponseEntity;
import com.middleware.common.model.TokenInfo;
import com.middleware.demo.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "authentication", configuration = FeignConfig.class)
public interface AuthenticationClient {

    @GetMapping("/token/current")
    ResponseEntity<TokenInfo> getTokenInfo();

    @DeleteMapping("/token/revoke")
    ResponseEntity revokeToken(
            @RequestBody TokenInfo tokenInfo,
            @RequestParam(value = "reason", required = false) RevokeReason revokeReason);
}
