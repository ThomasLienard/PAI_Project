package com.example.projet_pai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.projet_pai.dto.ReservationRequest;
import com.example.projet_pai.service.ReservationItf;

@RestController
@RequestMapping("/api/user/reservation")
public class ReservationController {

    @Autowired
    private ReservationItf service;

    @PostMapping("/create")
    public ResponseEntity<String> createReservation(@RequestBody ReservationRequest reservation) {
        try {
            System.out.println("Réservation reçue : {}"+ reservation);
            service.saveReservation(reservation);
            return ResponseEntity.status(HttpStatus.CREATED).body("Réservation créée avec succès !");
        } 
        catch (Exception e) {
            System.out.println("Erreur lors de la création de la réservation : {}"+ e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur lors de la création de la réservation : " + e.getMessage());
        }
    }

    
}
