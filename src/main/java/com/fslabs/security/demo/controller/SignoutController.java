package com.fslabs.security.demo.controller;

import com.fslabs.security.demo.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@RestController
public class SignoutController
{
    @Value("${application.security.signout-url}")
    private String signoutUrl;

    private final RestTemplate restTemplate;

    @GetMapping("/auth/signout")
    public void signout(HttpServletRequest request)
    {
        HttpHeaders headers = SecurityUtil.getHeaders();
        String token = SecurityUtil.extractRawTokenFromHeader(request, true);
        token = "Bearer " + token;
        headers.set("Authorization", token);

        log.debug("Invoking security server signout url...");
        HttpEntity<String> entityRequest = new HttpEntity<String>(headers);

        ResponseEntity<Void> entityResponse = restTemplate.exchange(signoutUrl, HttpMethod.GET,entityRequest,Void.class);

        if (entityResponse.getStatusCode().equals(HttpStatus.OK))
        {
            log.debug("signout successful");
            SecurityContextHolder.clearContext();
        }
        else
        {
            throw new RuntimeException(
                    String.format("Signout Failed with statuscode=%s", entityResponse.getStatusCode()));
        }

    }

}
