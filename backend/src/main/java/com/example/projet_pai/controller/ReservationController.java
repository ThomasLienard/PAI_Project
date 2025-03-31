package com.example.projet_pai.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.projet_pai.dto.ReservationRequest;
import com.example.projet_pai.entite.Reservation;
import com.example.projet_pai.entite.Utilisateur;
import com.example.projet_pai.service.ReservationItf;

@RestController
@RequestMapping("/api/user/reservation")
public class ReservationController {

    @Autowired
    private ReservationItf service;

    @PostMapping("/create")
    public ResponseEntity<String> createReservation(@RequestBody ReservationRequest reservation) {
        try {
            service.saveReservation(reservation);
            return ResponseEntity.status(HttpStatus.CREATED).body("Réservation créée avec succès !");
        } 
        catch (Exception e) {
            System.out.println("Erreur lors de la création de la réservation : "+ e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur lors de la création de la réservation : " + e.getMessage());
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Reservation>> getReservations(@RequestParam String clientId) {
        try {
            List<Reservation> reservations = service.getReservationsByClient(clientId);
            System.out.println("les réservations: "+reservations);
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteReservation(@PathVariable Long id) {
        try {
            System.out.println("Suppression de la réservation avec l'ID: " + id);
            service.deleteReservation(id);
            return ResponseEntity.ok("Réservation annulée avec succès");
        } catch (Exception e) {
            System.out.println("Erreur lors de l'annulation de la réservation: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur lors de l'annulation de la réservation: " + e.getMessage());
        }
    }
    
}
