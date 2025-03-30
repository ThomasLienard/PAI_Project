package com.example.projet_pai.entite;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Reservation {

    @Column(name = "id_reservation")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idReservation;

    @Column(name = "date_reservation")
    private String dateReservation;

    @Column(name = "creneau_horaire")
    private String creneauHoraire;

    @Column(name = "nb_personne")
    private int nbPersonne;

    @ManyToOne
    private Utilisateur client;

    public Reservation() {
    }

    public Reservation(String dateReservation, String creneauHoraire, int nbPersonne, Utilisateur client) {
        this.dateReservation = dateReservation;
        this.creneauHoraire = creneauHoraire;
        this.nbPersonne = nbPersonne;
        this.client = client;
    }

    public int getIdReservation() {
        return idReservation;
    }

    public String getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(String dateReservation) {
        this.dateReservation = dateReservation;
    }

    public String getCreneauHoraire() {
        return creneauHoraire;
    }

    public void setCreneauHoraire(String creneauHoraire) {
        this.creneauHoraire = creneauHoraire;
    }

    public int getNbPersonne() {
        return nbPersonne;
    }

    public void setNbPersonne(int nbPersonne) {
        this.nbPersonne = nbPersonne;
    }

    public Utilisateur getClient() {
        return client;
    }

    public void setClient(Utilisateur client) {
        this.client = client;
    }
}
