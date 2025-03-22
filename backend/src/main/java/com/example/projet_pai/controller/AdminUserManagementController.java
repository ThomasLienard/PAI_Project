package com.example.projet_pai.controller;

import com.example.projet_pai.dto.AdminRegisterRequest;
import com.example.projet_pai.service.AdminUserManagementServiceItf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminUserManagementController {

    @Autowired
    private AdminUserManagementServiceItf adminUserManagementService;

    @PostMapping("/create-user")
    public ResponseEntity<String> createUser(@RequestBody AdminRegisterRequest adminRegisterRequest) {
        try {
            adminUserManagementService.createUserWithRole(adminRegisterRequest);
            return ResponseEntity.ok("Utilisateur créé avec succès !");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}