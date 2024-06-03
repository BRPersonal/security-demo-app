package com.fslabs.security.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * Added this configuration to resolve cors issue when accessing this spring boot server
 * from React UI
 */
@Slf4j
@Configuration
public class CorsConfig implements WebMvcConfigurer
{

    @Value("${application.cors.allowed-origins}")
    private String allowedOrigins;

    @Value("${application.cors.allowed-methods}")
    private String allowedMethods;

    @Override
    public void addCorsMappings(CorsRegistry registry)
    {
        if ("ALL".equals(allowedOrigins))
        {
            allowedOrigins = "*"; //yaml is not allowing to specify *
        }

        log.debug("allowedOrigins={},allowedMethods={}", allowedOrigins,allowedMethods);

        registry.addMapping("/**")
                .allowedOriginPatterns(allowedOrigins)
                .allowedMethods(allowedMethods.split(","))
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
