package com.example.projet_pai.service;

import java.util.List;
import com.example.projet_pai.dto.ServerReservationDTO;

public interface ServerReservationServiceItf {
    
    /**
     * Récupère les réservations pour la date du jour
     * @return La liste des réservations du jour
     */
    List<ServerReservationDTO> getTodayReservations();
    /**
    * Récupère le nombre total de tables dans le restaurant
     * @return Le nombre de tables
     */
    int getTotalTables();
}