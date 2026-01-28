package org.example.cinemamanagement.infrastructure.security;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    //Só username de propósito para simular um login
    @PostMapping("/login")
    public String login(@RequestParam String username) {
        return jwtService.generateToken(username);
    }
}
