package com.example.projet_pai.dto;

public class LoginResponse {
    private String username;
    private String role;
    private String token;
    private Long UserId;


    public LoginResponse(String username, String role, String token, Long UserId) {
        this.username = username;
        this.role = role;
        this.token = token;
        this.UserId = UserId;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return UserId;
    }

    public void setUserId(Long userId) {
        UserId = userId;
    }
}