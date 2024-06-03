package com.fslabs.security.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class SignInResponse
{
    private String firstName;
    private String token;
    private String message;
    private List<String> roles;
    private List<String> permissions;

}
