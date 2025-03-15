package com.example.projet_pai.controller;

import com.example.projet_pai.dto.LoginRequest;
import com.example.projet_pai.dto.LoginResponse;
import com.example.projet_pai.dto.RegisterRequest;
import com.example.projet_pai.entite.Utilisateur;
import com.example.projet_pai.service.UserServiceItf;
import com.example.projet_pai.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@PreAuthorize("hasRole('ADMIN')")
public class AutentificationController {

    @Autowired
    private UserServiceItf userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            userService.registerUser(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("Utilisateur enregistré avec succès !");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Utilisateur user = userService.loginUser(loginRequest);
            if (user != null) {
                String token = jwtUtil.generateToken(user.getEmail(), user.getRole().getName());
                String role = user.getRole().getName();
                return ResponseEntity.ok(new LoginResponse(user.getUsername(), role, token));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Échec de la connexion !");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
