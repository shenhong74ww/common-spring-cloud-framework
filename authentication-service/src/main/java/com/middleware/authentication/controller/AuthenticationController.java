package com.middleware.authentication.controller;

import com.middleware.authentication.constant.Constants;
import com.middleware.authentication.exception.authentication.PasswordMissingException;
import com.middleware.authentication.exception.authentication.PhoneInvalidExcaption;
import com.middleware.authentication.service.TokenService;
import com.middleware.common.constant.enums.RevokeReason;
import com.middleware.common.model.ResponseEntity;
import com.middleware.common.model.TokenInfo;
import com.middleware.common.model.TokenResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Pattern;

import static com.middleware.common.constant.Constants.AUTH_TYPE_CLIENT;
import static com.middleware.common.constant.Constants.AUTH_TYPE_MEMBER;


@RestController
public class AuthenticationController {
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/token", method = { RequestMethod.GET, RequestMethod.POST })
    public ResponseEntity<TokenResult> getToken(
            @RequestParam(value = "auth_type", required = false, defaultValue = "") String authType,
            @RequestParam(value = "client_id", required = false, defaultValue = "") String clientId,
            @RequestParam(value = "client_secret", required = false, defaultValue = "") String clientSecret,
            @RequestParam(value = "region_code", required = false, defaultValue = "86") Integer regionCode,
            @RequestParam(value = "phone", required = false, defaultValue = "") String phone,
            @RequestParam(value = "password", required = false, defaultValue = "") String password,
            @RequestParam(value = "refresh_token", required = false, defaultValue = "") String refreshToken) {
        TokenResult result = null;

        if (!StringUtils.isEmpty(refreshToken)) {
            result = tokenService.refreshToken(refreshToken, clientId);
            return new ResponseEntity<>(result);
        }

        if (AUTH_TYPE_CLIENT.equals(authType)) {
            result = tokenService.generateTokenForClient(clientId, clientSecret);
        } else if (AUTH_TYPE_MEMBER.equals(authType)) {
            if (StringUtils.isEmpty(password)) {
                throw new PasswordMissingException("Password can not be null or empty!");
            }
            if ((regionCode == Constants.DOMESTIC_REGION_CODE && !Pattern.matches(Constants.DOMESTIC_PHONE_REGEX, phone)) || !Pattern.matches(Constants.INTERNATIONAL_PHONE_REGEX, phone)) {
                throw new PhoneInvalidExcaption("Incorrect mobile phone number.");
            }
            result = tokenService.generateToken(clientId, regionCode, phone, password);
        }

        return new ResponseEntity<>(result);
    }

    @GetMapping("/token/current")
    public ResponseEntity<TokenInfo> getCurrentUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        TokenInfo currentTokenInfo = tokenService.getCurrentToken(authentication);
        return new ResponseEntity<>(currentTokenInfo);
    }

    @GetMapping("/token/revoke")
    public ResponseEntity revokeToken(@RequestParam(value = "reason", required = false) RevokeReason revokeReason) {
        return revokeToken(null, revokeReason);
    }

    @DeleteMapping("/token/revoke")
    public ResponseEntity revokeToken(
            @RequestBody TokenInfo tokenInfo,
            @RequestParam(value = "reason", required = false) RevokeReason revokeReason) {

        if (tokenInfo == null) {
            OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
            tokenService.revokeToken(oAuth2Authentication, revokeReason);
        } else {
            tokenService.revokeAllTokens(tokenInfo, revokeReason);
        }

        return new ResponseEntity();
    }

}
