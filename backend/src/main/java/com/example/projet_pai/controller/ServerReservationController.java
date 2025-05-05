package com.example.projet_pai.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.projet_pai.dto.ServerReservationDTO;
import com.example.projet_pai.service.ServerReservationServiceItf;

@RestController
@RequestMapping("/api/reservations")
public class ServerReservationController {

    @Autowired
    private ServerReservationServiceItf reservationService;
    
    // Endpoint accessible uniquement aux serveurs
    @GetMapping("/today")
    @PreAuthorize("hasRole('SERVEUR')")
    public ResponseEntity<List<ServerReservationDTO>> getTodayReservations() {
        try {
            List<ServerReservationDTO> reservations = reservationService.getTodayReservations();
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des réservations du jour: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // D'autres endpoints de réservation ici...
    // Comme créer, modifier ou annuler des réservations
}