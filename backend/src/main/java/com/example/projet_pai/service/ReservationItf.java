package com.example.projet_pai.service;

import java.util.List;

import com.example.projet_pai.dto.ReservationRequest;
import com.example.projet_pai.entite.Reservation;

public interface ReservationItf {
    public void saveReservation(ReservationRequest reservation);
    public List<Reservation> getReservationsByClient(String client);
}
