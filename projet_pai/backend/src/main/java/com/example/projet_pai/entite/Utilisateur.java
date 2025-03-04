package com.example.projet_pai.entite;

import jakarta.persistence.*;
import java.util.Set;

@Entity
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Utilisation d'un UUID pour l'ID
    private String id;

    private String username;
    private String email;
    private String password;

    @ElementCollection(fetch = FetchType.EAGER) // Pour stocker les rôles sous forme de Set<String>
    private Set<String> roles;

    public Utilisateur() {}

    public Utilisateur(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
