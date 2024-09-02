package com.gethealthy.eventservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("AUTHENTICATION-SERVICE")
@Component
public interface AuthenticationInterface {
    @GetMapping("api/v1/auth/get-logged-in-userid")
    ResponseEntity<Long> getLoggedInUserId(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader);
}