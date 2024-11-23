package com.read.read_sphere.controller;

import com.read.read_sphere.config.BearerTokenAuthFilter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;

@RestController
public class LoginController {
    private final BearerTokenAuthFilter bearerTokenAuthFilter;

    @Value("${frontend.redirect-url}")
    private String redirectUrl;

    public LoginController(BearerTokenAuthFilter bearerTokenAuthFilter) {
        this.bearerTokenAuthFilter = bearerTokenAuthFilter;
    }

    @GetMapping("/account")
    public void googleAuth(@RequestParam("code") String code, HttpServletResponse response, HttpSession session) throws IOException {
        if(code == null || code.isEmpty()) {
            throw new BadRequestException("Invalid code");
        }
        bearerTokenAuthFilter.handleGoogleLoginAndCreateUser(code, session);
        response.sendRedirect(redirectUrl);
    }

    @GetMapping("/token")
    public String getToken(HttpSession session){
        return (String) session.getAttribute("token");
    }
}
