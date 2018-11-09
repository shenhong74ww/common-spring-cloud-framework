package com.middleware.demo.feign;

import com.middleware.common.model.ResponseEntity;
import com.middleware.common.model.TokenResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "authentication")
public interface AuthenticationTokenClient {

    @RequestMapping("/token")
    ResponseEntity<TokenResult> getToken(@RequestParam("auth_type") String authType,
                                         @RequestParam("client_id") String clientId,
                                         @RequestParam("client_secret") String clientSecret);

    @RequestMapping("/token")
    ResponseEntity<TokenResult> getToken(@RequestParam("auth_type") String authType,
                                         @RequestParam("client_id") String clientId,
                                         @RequestParam("region_code") Integer regionCode,
                                         @RequestParam("phone") String phone,
                                         @RequestParam("password") String password);
}
