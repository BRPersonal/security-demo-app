package com.fslabs.security.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AccessPermissions
{
    @JsonProperty("email")
    private String emailId;

    private List<String> roles;
    private List<String> permissions;

    public AccessPermissions()
    {
        this("");
    }

    public AccessPermissions(String emailId)
    {
        this.emailId = emailId;
        roles = new ArrayList<>();
        permissions = new ArrayList<>();
    }
}
