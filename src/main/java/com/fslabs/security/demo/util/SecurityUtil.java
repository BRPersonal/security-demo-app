package com.fslabs.security.demo.util;

import com.fslabs.security.demo.model.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.util.List;

@Slf4j
public class SecurityUtil
{
    public static void setSecurityContext(HttpServletRequest request,String emailId, List<String> roles, List<String> permissions)
    {
        UserDetails userDetails = new CustomUserDetails(emailId,
                roles,permissions);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null,userDetails.getAuthorities());
        authToken.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        log.debug("Setting Security Context...");
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    public static HttpHeaders getHeaders()
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    public static String extractRawTokenFromHeader(HttpServletRequest request
            , boolean throwExceptionIfNotFound)
    {
        String token = null;
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(authHeader == null || !authHeader.startsWith("Bearer "))
        {
            if (throwExceptionIfNotFound)
            {
                throw new RuntimeException("Bearer token missing");
            }
        }
        else
        {
            token = authHeader.substring(7);
        }

        return token;

    }
}
