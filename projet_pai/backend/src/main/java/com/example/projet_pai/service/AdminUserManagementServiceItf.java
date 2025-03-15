package com.example.projet_pai.service;

import com.example.projet_pai.dto.AdminRegisterRequest;


public interface AdminUserManagementServiceItf {
    void createUserWithRole(AdminRegisterRequest registerRequest);
}