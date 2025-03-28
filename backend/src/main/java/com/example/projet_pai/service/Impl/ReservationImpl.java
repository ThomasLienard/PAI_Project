package com.example.projet_pai.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.projet_pai.dto.ReservationRequest;
import com.example.projet_pai.entite.Reservation;
import com.example.projet_pai.repository.ReservationRepository;
import com.example.projet_pai.service.ReservationItf;

@Service
public class ReservationImpl implements ReservationItf{

    @Autowired
    private ReservationRepository repository;

    @Override
    public void saveReservation(ReservationRequest reservation) {
        if(reservation.getCreneau_horaire() == null || reservation.getDate_reservation() == null || reservation.getNbPersonne() <=0) {
            throw new RuntimeException("Données manquantes");
        }
        Reservation reservationEntity = new Reservation(
            reservation.getDate_reservation(),
            reservation.getCreneau_horaire(),
            reservation.getNbPersonne()
        );

        repository.save(reservationEntity);
    }
    
}
