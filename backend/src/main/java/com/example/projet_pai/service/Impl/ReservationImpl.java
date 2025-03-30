package com.example.projet_pai.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.projet_pai.dto.ReservationRequest;
import com.example.projet_pai.entite.Reservation;
import com.example.projet_pai.repository.ReservationRepository;
import com.example.projet_pai.repository.UserRepository;
import com.example.projet_pai.service.ReservationItf;

@Service
public class ReservationImpl implements ReservationItf{

    @Autowired
    private ReservationRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void saveReservation(ReservationRequest reservation) {
        if(reservation.getcreneauHoraire() == null || reservation.getdateReservation() == null || reservation.getNbPersonne() <=0 || reservation.getClient() == null) {
            throw new RuntimeException("Données manquantes");
        }
        Reservation reservationEntity = new Reservation(
            reservation.getdateReservation(),
            reservation.getcreneauHoraire(),
            reservation.getNbPersonne(),
            userRepository.findById(Long.parseLong(reservation.getClient()))
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"))
        );

        repository.save(reservationEntity);
    }

    @Override
    public List<Reservation> getReservationsByClient(String client) {
        return repository.findReservationForUserId(Long.parseLong(client));
    }
    
}
