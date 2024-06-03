package com.fslabs.security.demo.controller;

import com.fslabs.security.demo.model.SignInRequest;
import com.fslabs.security.demo.model.SignInResponse;
import com.fslabs.security.demo.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.ResponseEntity;


@Slf4j
@RequiredArgsConstructor
@RestController
public class SigninController
{
    @Value("${application.security.signin-url}")
    private String signinUrl;

    private final RestTemplate restTemplate;

    @PostMapping("/auth/signin")
    public ResponseEntity<SignInResponse> signin(@RequestBody SignInRequest signInRequest, HttpServletRequest request)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        log.debug("Invoking security server signin url...");

        HttpEntity<SignInRequest> entityRequest = new HttpEntity<SignInRequest>(signInRequest,headers);
        ResponseEntity<SignInResponse> entityResponse = restTemplate.exchange(signinUrl,HttpMethod.POST,entityRequest,SignInResponse.class);

        if (entityResponse.getStatusCode().equals(HttpStatus.OK))
        {
            SignInResponse response = entityResponse.getBody();

            log.debug("response={}", response);

            SecurityUtil.setSecurityContext(request,
                    signInRequest.getEmailId(),
                    response.getRoles(),
                    response.getPermissions());

            return ResponseEntity.ok(response);
        }
        else
        {
            throw new RuntimeException(
                    String.format("Signin Failed with statuscode=%s", entityResponse.getStatusCode()));
        }

    }
}
