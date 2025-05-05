package com.example.projet_pai.service.Impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.projet_pai.dto.ServerReservationDTO;
import com.example.projet_pai.entite.Reservation;
import com.example.projet_pai.repository.ReservationRepository;
import com.example.projet_pai.service.ServerReservationServiceItf;

@Service
public class ServerReservationServiceImpl implements ServerReservationServiceItf {

    @Autowired
    private ReservationRepository reservationRepository;
    
    @Override
    public List<ServerReservationDTO> getTodayReservations() {
        // Obtenir la date du jour au format utilisé dans l'application
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        
        // Récupérer les réservations du jour
        List<Reservation> reservations = reservationRepository.findByDateReservation(today);
        
        // Convertir les entités en DTOs
        return reservations.stream()
                .map(ServerReservationDTO::fromEntity)
                .collect(Collectors.toList());
    }
}