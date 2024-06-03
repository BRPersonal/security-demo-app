package com.fslabs.security.demo.filter;

import com.fslabs.security.demo.model.AccessPermissions;
import com.fslabs.security.demo.util.SecurityUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class AccessPermissionFilter  extends OncePerRequestFilter
{
    @Value("${application.security.get-permission-url}")
    private String permissionUrl;

    private final RestTemplate restTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        log.debug("doFilterInternal called");

        String token = SecurityUtil.extractRawTokenFromHeader(request, false);

        if ((token != null) && (SecurityContextHolder.getContext().getAuthentication() == null))
        {
            token = "Bearer " + token;
            HttpHeaders headers = SecurityUtil.getHeaders();
            headers.set("Authorization", token);

            log.debug("Invoking security server permission url...");
            HttpEntity<String> jwtEntity = new HttpEntity<String>(headers);
            ResponseEntity<AccessPermissions> respEntity = restTemplate.exchange(permissionUrl,HttpMethod.GET, jwtEntity, AccessPermissions.class);

            if (respEntity.getStatusCode().equals(HttpStatus.OK))
            {
                AccessPermissions permissions = respEntity.getBody();
                log.debug("permissions={}", permissions);

                SecurityUtil.setSecurityContext(request,
                        permissions.getEmailId(),
                        permissions.getRoles(),
                        permissions.getPermissions());
            }

        }

        filterChain.doFilter(request,response);
    }


}
