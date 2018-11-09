package com.middleware.authentication.feign;

import com.middleware.authentication.config.FeignConfig;
import com.middleware.authentication.feign.response.Member;
import com.middleware.common.model.ResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "member", configuration = FeignConfig.class)
public interface MemberClient {
    @RequestMapping("/")
    ResponseEntity<Member> getUser(@RequestParam(value = "phone") String phone, @RequestParam(value = "regionCode") Integer regionCode);
}
