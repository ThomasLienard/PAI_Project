package com.example.projet_pai.service.Impl;

import com.example.projet_pai.dto.AdminRegisterRequest;
import com.example.projet_pai.entite.Role;
import com.example.projet_pai.entite.Utilisateur;
import com.example.projet_pai.repository.RoleRepository;
import com.example.projet_pai.repository.UserRepository;
import com.example.projet_pai.service.AdminUserManagementServiceItf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminUserManagementServiceImpl implements AdminUserManagementServiceItf {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void createUserWithRole(AdminRegisterRequest adminRegisterRequest) {
        if (adminRegisterRequest.getUsername() == null || adminRegisterRequest.getEmail() == null || adminRegisterRequest.getPassword() == null || adminRegisterRequest.getRole() == null) {
            throw new RuntimeException("Données manquantes");
        }
        if (userRepository.findByEmail(adminRegisterRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email déjà utilisé !");
        }

        // Récupérer le rôle depuis la base de données
        Role role = roleRepository.findByName(adminRegisterRequest.getRole())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        Utilisateur user = new Utilisateur(
            adminRegisterRequest.getUsername(),
            adminRegisterRequest.getEmail(),
            passwordEncoder.encode(adminRegisterRequest.getPassword()),
            role // Attribuer le rôle spécifié
        );
        userRepository.save(user);
    }
}