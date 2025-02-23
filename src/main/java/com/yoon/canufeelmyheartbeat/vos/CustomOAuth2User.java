package com.yoon.canufeelmyheartbeat.vos;

import java.io.Serializable;
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
public class CustomOAuth2User implements OAuth2User , Serializable {
    /*Redis를 세션 저장소로 사용할 때, **세션 속성(Session Attributes)**을 Redis에 저장하기 위해 **객체 직렬화(Serialization)**가 필요함*/

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
