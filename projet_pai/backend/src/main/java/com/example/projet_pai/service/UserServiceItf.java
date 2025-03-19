package com.example.projet_pai.service;

import java.util.List;

import com.example.projet_pai.dto.LoginRequest;
import com.example.projet_pai.dto.RegisterRequest;
import com.example.projet_pai.entite.Utilisateur;


public interface UserServiceItf {
    void registerUser(RegisterRequest registerRequest);
    Utilisateur loginUser(LoginRequest loginrequest);
}
