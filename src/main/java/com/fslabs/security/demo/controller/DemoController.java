package com.fslabs.security.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
public class DemoController
{
    private static final Map<String,int[]> accessCounter = new HashMap<>(2);

    static
    {
        accessCounter.put("USER",new int[] {0});
        accessCounter.put("ADMIN",new int[] {0});
    }

    @GetMapping("/demo")
    @PreAuthorize("hasRole('SALES') and hasAuthority('CREATE')")
    public ResponseEntity<String> userOnly()
    {
        int[] counterArr = accessCounter.get("USER");
        counterArr[0]++;
        return ResponseEntity.ok("Hello from user only url. Access count#:" + counterArr[0]);
    }

    @GetMapping("/admin_only")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> adminOnly()
    {
        int[] counterArr = accessCounter.get("ADMIN");
        counterArr[0]++;
        return ResponseEntity.ok("Hello from admin only url. Access count#:" + counterArr[0]);
    }

    @GetMapping("/seeker")
    @PreAuthorize("hasRole('PROJECT_SEEKER') and hasAuthority('CREATE')")
    public ResponseEntity<String> seekerOnly()
    {
        return ResponseEntity.ok("Your Resume is Uploaded successfully. Current Time:" + new Date());
    }

    @GetMapping("/advertiser")
    @PreAuthorize("hasRole('PROJECT_ADVERTISER') and hasAuthority('CREATE')")
    public ResponseEntity<String> advertiserOnly()
    {
        return ResponseEntity.ok("Your Job Description is Uploaded successfully. Current Time:" + new Date());
    }

}