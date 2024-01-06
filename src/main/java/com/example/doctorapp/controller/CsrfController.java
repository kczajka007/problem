package com.example.doctorapp.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CsrfController {

    @GetMapping("/getCsrfToken")
    public String getCsrfToken(CsrfToken csrfToken) {
        return csrfToken.getToken();
    }
}