package com.example.projet_pai.controller;

import com.example.projet_pai.entite.Utilisateur;
import com.example.projet_pai.service.UserServiceItf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserServiceItf userService;

    @GetMapping
    public List<Utilisateur> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public Utilisateur createUser(@RequestBody Utilisateur user) {
        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    public Utilisateur updateUser(@PathVariable String id, @RequestBody com.example.projet_pai.entite.Utilisateur user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
    }
}