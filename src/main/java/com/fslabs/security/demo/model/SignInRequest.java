package com.fslabs.security.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SignInRequest
{
    @JsonProperty("email")
    private String emailId;

    private String password;
}
