package com.example.projet_pai.dto;

public class LoginResponse {
    private String username;
    private String role;

    public LoginResponse(String username, String role) {
        this.username = username;
        this.role = role;
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}