package com.fslabs.security.demo.model;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails
{
    private final String userName;
    private final List<String> roles;
    private final List<String> permissions;

    public CustomUserDetails(String userName, List<String> roles, List<String> permissions)
    {
        this.userName = userName;
        this.roles = roles;
        this.permissions = permissions;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        List<SimpleGrantedAuthority> roles = this.roles.stream()
                .map(s -> "ROLE_" + s) //prefix "ROLE_" for roles
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        List<SimpleGrantedAuthority> permissions = this.permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        List<SimpleGrantedAuthority> result = new ArrayList<>();
        result.addAll(roles);
        result.addAll(permissions);
        return result;

    }

    @Override
    public String getPassword()
    {
        throw new RuntimeException("This method is not supposed to be called");
    }

    @Override
    public String getUsername()
    {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        return true;
    }
}
