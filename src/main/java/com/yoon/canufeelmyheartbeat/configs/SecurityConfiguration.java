package com.yoon.canufeelmyheartbeat.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Order(2)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(request -> request.anyRequest().authenticated())
                .oauth2Login(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())
                .sessionManagement(it -> it.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }

    @Order(1)
    @Bean
    public SecurityFilterChain consultantSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .securityMatcher("/consultants/**")
                .authorizeHttpRequests(request -> request
                        .requestMatchers(HttpMethod.POST, "/consultants").permitAll() // test 중 일단 전부 허용
                        .anyRequest().hasRole("CONSULTANT")) // 일반 유저는 접근 할 수 없게. 앞에 prefix ROLE_ 으로 변경됨
                .formLogin(Customizer.withDefaults())
                .sessionManagement(it -> it.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }

    @Order(0)
    @Bean
    public SecurityFilterChain sessionSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .securityMatcher("/session/**")
                .authorizeHttpRequests(request -> request
                        .requestMatchers(HttpMethod.POST, "/session/test").permitAll()
                        .requestMatchers(HttpMethod.GET, "/session/whoami").permitAll()
                        .anyRequest().hasRole("CONSULTANT"))
                .sessionManagement(it -> it.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

