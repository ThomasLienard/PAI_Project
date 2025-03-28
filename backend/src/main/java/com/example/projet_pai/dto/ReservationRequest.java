package com.example.projet_pai.dto;

public class ReservationRequest {
    private String date_reservation;
    private String creneau_horaire;
    private int nbPersonne;

    public ReservationRequest() {
    }

    public ReservationRequest(String date_reservation, String creneau_horaire, int nbPersonne) {
        this.date_reservation = date_reservation;
        this.creneau_horaire = creneau_horaire;
        this.nbPersonne = nbPersonne;
    }

    public String getDate_reservation() {
        return date_reservation;
    }

    public void setDate_reservation(String date_reservation) {
        this.date_reservation = date_reservation;
    }

    public String getCreneau_horaire() {
        return creneau_horaire;
    }

    public void setCreneau_horaire(String creneau_horaire) {
        this.creneau_horaire = creneau_horaire;
    }

    public int getNbPersonne() {
        return nbPersonne;
    }

    public void setNbPersonne(int nbPersonne) {
        this.nbPersonne = nbPersonne;
    }

    
}
