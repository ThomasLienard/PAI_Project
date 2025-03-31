package com.example.projet_pai.dto;

public class ReservationRequest {
    private String dateReservation;
    private String creneauHoraire;
    private int nbPersonne;
    private String client;
    private Long tableId;

    public ReservationRequest() {
    }

    public ReservationRequest(String dateReservation, String creneauHoraire, int nbPersonne, String client) {
        this.dateReservation = dateReservation;
        this.creneauHoraire = creneauHoraire;
        this.nbPersonne = nbPersonne;
        this.client = client;
    }

    public String getdateReservation() {
        return dateReservation;
    }

    public void setdateReservation(String dateReservation) {
        this.dateReservation = dateReservation;
    }

    public String getcreneauHoraire() {
        return creneauHoraire;
    }

    public void setcreneauHoraire(String creneauHoraire) {
        this.creneauHoraire = creneauHoraire;
    }

    public int getNbPersonne() {
        return nbPersonne;
    }

    public void setNbPersonne(int nbPersonne) {
        this.nbPersonne = nbPersonne;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }
}
