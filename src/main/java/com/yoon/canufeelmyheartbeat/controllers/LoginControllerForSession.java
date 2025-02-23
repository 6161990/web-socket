package com.yoon.canufeelmyheartbeat.controllers;

import com.yoon.canufeelmyheartbeat.dtos.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class LoginControllerForSession {

    @PostMapping("/session/test")
    public HttpSession session(HttpServletRequest request, @RequestBody LoginRequest loginRequest) {
        HttpSession session = request.getSession();
        session.setAttribute("username", loginRequest.username());
        return session;
    }

    @GetMapping("/session/whoami")
    public String whoAmI(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null ? "Logged in as: " + session.getAttribute("username") : "No session found";
    }

}
