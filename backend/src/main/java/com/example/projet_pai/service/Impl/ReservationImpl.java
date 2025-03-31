package com.example.projet_pai.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.projet_pai.dto.ReservationRequest;
import com.example.projet_pai.entite.Reservation;
import com.example.projet_pai.entite.Table;
import com.example.projet_pai.entite.Utilisateur;
import com.example.projet_pai.repository.ReservationRepository;
import com.example.projet_pai.repository.TableRepository;
import com.example.projet_pai.repository.UserRepository;
import com.example.projet_pai.service.ReservationItf;

@Service
public class ReservationImpl implements ReservationItf{

    @Autowired
    private ReservationRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TableRepository tableRepository;

    @Override
    public void saveReservation(ReservationRequest reservation) {
        if(reservation.getcreneauHoraire() == null || reservation.getdateReservation() == null 
           || reservation.getNbPersonne() <= 0 || reservation.getClient() == null 
           || reservation.getTableId() == null) {
            throw new RuntimeException("Données manquantes");
        }
        
        // Récupérer l'utilisateur
        Utilisateur utilisateur = userRepository.findById(Long.parseLong(reservation.getClient()))
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        // Récupérer la table
        Table table = tableRepository.findById(reservation.getTableId())
                .orElseThrow(() -> new RuntimeException("Table non trouvée"));
        
        // Vérifier que la table est disponible
        List<Table> tablesDisponibles = tableRepository.findAvailableTables(
                reservation.getdateReservation(),
                reservation.getcreneauHoraire(),
                reservation.getNbPersonne());
        
        if (tablesDisponibles.stream().noneMatch(t -> t.getId().equals(table.getId()))) {
            throw new RuntimeException("Cette table n'est plus disponible");
        }
        
        // Créer la réservation
        Reservation reservationEntity = new Reservation(
            reservation.getdateReservation(),
            reservation.getcreneauHoraire(),
            reservation.getNbPersonne(),
            utilisateur
        );
        reservationEntity.setTable(table);
        
        repository.save(reservationEntity);
    }

    @Override
    public List<Reservation> getReservationsByClient(String client) {
        return repository.findReservationForUserId(Long.parseLong(client));
    }
    
    @Override
    public void deleteReservation(Long id) {
        Reservation reservation = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée avec l'ID: " + id));
        
        // Option: vérifier que l'utilisateur qui fait la demande est bien propriétaire de la réservation
        // Cette vérification pourrait être ajoutée si nécessaire pour la sécurité
        
        repository.delete(reservation);
    }
}
