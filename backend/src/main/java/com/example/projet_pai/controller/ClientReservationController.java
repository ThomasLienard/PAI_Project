package com.example.projet_pai.controller;

import java.util.ArrayList;
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
import com.example.projet_pai.dto.ReservationResponseDTO;
import com.example.projet_pai.entite.Reservation;
import com.example.projet_pai.entite.Utilisateur;
import com.example.projet_pai.service.ReservationItf;

@RestController
@RequestMapping("/api/user/reservation")
public class ClientReservationController {

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
    public ResponseEntity<List<ReservationResponseDTO>> getReservations(@RequestParam String clientId) {
        try {
            System.out.println("Récupération des réservations pour l'utilisateur: " + clientId);
            
            List<Reservation> reservations = service.getReservationsByClient(clientId);
            
            // Convertir les entités en DTOs
            List<ReservationResponseDTO> dtos = new ArrayList<>();
            for (Reservation reservation : reservations) {
                dtos.add(ReservationResponseDTO.fromEntity(reservation));
            }
            
            System.out.println("Nombre de réservations trouvées: " + dtos.size());
            
            // Afficher les détails pour le débogage
            for (ReservationResponseDTO dto : dtos) {
                System.out.println("Réservation ID: " + dto.getId() + 
                                  ", Date: " + dto.getDateReservation() +
                                  ", Table: " + (dto.getTable() != null ? dto.getTable().getNumero() : "N/A"));
            }
            
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des réservations: " + e.getMessage());
            e.printStackTrace();
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
