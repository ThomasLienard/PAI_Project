package com.example.projet_pai.entite;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Reservation {

    @Column(name = "id_reservation")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_reservation;

    @Column
    private String date_reservation;

    @Column
    private String creneau_horaire;

    @Column(name = "nb_personne")
    private int nbPersonne;

    public Reservation() {
    }

    public Reservation(String date_reservation, String creneau_horaire, int nbPersonne) {
        this.date_reservation = date_reservation;
        this.creneau_horaire = creneau_horaire;
        this.nbPersonne = nbPersonne;
    }

    public int getId_reservation() {
        return id_reservation;
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
