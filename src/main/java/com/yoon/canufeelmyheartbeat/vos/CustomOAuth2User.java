package com.yoon.canufeelmyheartbeat.vos;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.yoon.canufeelmyheartbeat.entities.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
@AllArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    Member member;
    Map<String, Object> attributeMap;

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributeMap;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> this.member.getRole());
    }

    @Override
    public String getName() {
        return this.member.getNickName();
    }
}
