package com.erms.employee_management_system.security;

import com.erms.employee_management_system.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {
    private Long id;
    private String username;
    private String email;
    private String department;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    private UserPrincipal(Builder builder) {
        this.id = builder.id;
        this.username = builder.username;
        this.email = builder.email;
        this.department = builder.department;
        this.password = builder.password;
        this.authorities = builder.authorities;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static UserPrincipal create(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        return UserPrincipal.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .department(user.getDepartment())
                .authorities(authorities)
                .build();
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getDepartment() {
        return department;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static class Builder {
        private Long id;
        private String username;
        private String email;
        private String department;
        private String password;
        private Collection<? extends GrantedAuthority> authorities;

        private Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder department(String department) {
            this.department = department;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder authorities(Collection<? extends GrantedAuthority> authorities) {
            this.authorities = authorities;
            return this;
        }

        public UserPrincipal build() {
            return new UserPrincipal(this);
        }
    }
}