package com.yoon.canufeelmyheartbeat.services;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.yoon.canufeelmyheartbeat.entities.Member;
import com.yoon.canufeelmyheartbeat.vos.CustomOAuth2User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails extends CustomOAuth2User implements UserDetails {

    public CustomUserDetails(Member member, Map<String, Object> attributeMap) {
        super(member, attributeMap);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(getMember().getRole()));
    }

    @Override
    public String getPassword() {
        return getMember().getPassword();
    }

    @Override
    public String getUsername() {
        return getMember().getName();
    }
}
