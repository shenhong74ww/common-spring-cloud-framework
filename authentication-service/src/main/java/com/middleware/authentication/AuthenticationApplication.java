package com.middleware.authentication;

import com.middleware.common.repository.CustomizedJpaRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import static com.middleware.common.constant.Constants.BASE_PACKAGE;

@SpringBootApplication
@ComponentScan(BASE_PACKAGE)
@EntityScan(BASE_PACKAGE)
@EnableFeignClients
@EnableResourceServer
@EnableJpaRepositories(repositoryBaseClass = CustomizedJpaRepositoryImpl.class)
public class AuthenticationApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthenticationApplication.class, args);
    }
}
