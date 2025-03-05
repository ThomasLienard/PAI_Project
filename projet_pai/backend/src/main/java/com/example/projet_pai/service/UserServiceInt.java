package com.example.projet_pai.service;

import com.example.projet_pai.dto.LoginRequest;
import com.example.projet_pai.dto.RegisterRequest;
import com.example.projet_pai.entite.Utilisateur;

import java.util.List;


public interface UserServiceInt {
    void registerUser(RegisterRequest registerRequest);

    Boolean loginUser(LoginRequest loginrequest);   
}
