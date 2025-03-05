package com.example.projet_pai.service.Impl;

import com.example.projet_pai.repository.UserRepository;
import com.example.projet_pai.dto.LoginRequest;
import com.example.projet_pai.dto.RegisterRequest;
import com.example.projet_pai.entite.Utilisateur;
import com.example.projet_pai.service.UserServiceInt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserServiceInt {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;;

    @Override
    public void registerUser(RegisterRequest registerRequest) {
        if (registerRequest.getUsername() == null || registerRequest.getEmail() == null || registerRequest.getPassword() == null) {
            throw new RuntimeException("Données manquantes");
        }
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email déjà utilisé !");
        }
        Utilisateur user = new Utilisateur(
            registerRequest.getUsername(),
            registerRequest.getEmail(),
            passwordEncoder.encode(registerRequest.getPassword())
        );
        userRepository.save(user);
    }

    @Override
    public Boolean loginUser(LoginRequest loginRequest) {
        if (loginRequest.getEmail() == null || loginRequest.getPassword() == null) {
            throw new RuntimeException("Données manquantes");
        }

        Optional<Utilisateur> userOptional = userRepository.findByEmail(loginRequest.getEmail());
        if (userOptional.isEmpty()) {
            throw new RuntimeException("Email ou mot de passe incorrect");
        }

        Utilisateur user = userOptional.get();
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Email ou mot de passe incorrect");
        }

        return true;
    }
}

