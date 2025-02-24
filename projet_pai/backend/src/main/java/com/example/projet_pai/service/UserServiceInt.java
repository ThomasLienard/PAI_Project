package com.example.projet_pai.service;

import com.example.projet_pai.models.Utilisateur;
import com.example.projet_pai.dto.RegisterRequest;
import java.util.List;


public interface UserServiceInt {
    void registerUser(RegisterRequest registerRequest);
   
}
