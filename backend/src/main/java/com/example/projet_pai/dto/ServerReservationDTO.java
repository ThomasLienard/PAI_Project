package com.example.projet_pai.dto;

import com.example.projet_pai.entite.Reservation;

public class ServerReservationDTO {
    private Long id;
    private String dateReservation;
    private String creneauHoraire;
    private int nbPersonne;
    private String clientName;
    private TableDisponibiliteDTO table;
    private String specialRequests; // Pour des exigences spéciales (à ajouter à l'entité Reservation si nécessaire)
    
    public ServerReservationDTO() {}
    
    // Méthode de conversion d'une entité Reservation en DTO
    public static ServerReservationDTO fromEntity(Reservation reservation) {
        ServerReservationDTO dto = new ServerReservationDTO();
        dto.setId(reservation.getId());
        dto.setDateReservation(reservation.getDateReservation());
        dto.setCreneauHoraire(reservation.getCreneauHoraire());
        dto.setNbPersonne(reservation.getNbPersonne());
        
        if (reservation.getClient() != null) {
            dto.setClientName(reservation.getClient().getUsername());
        }
        
        if (reservation.getTable() != null) {
            TableDisponibiliteDTO tableDTO = new TableDisponibiliteDTO();
            tableDTO.setId(reservation.getTable().getId());
            tableDTO.setNumero(reservation.getTable().getNumero());
            tableDTO.setCapacite(reservation.getTable().getCapacite());
            dto.setTable(tableDTO);
        }
        
        return dto;
    }
    
    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public TableDisponibiliteDTO getTable() {
        return table;
    }

    public void setTable(TableDisponibiliteDTO table) {
        this.table = table;
    }

    public String getSpecialRequests() {
        return specialRequests;
    }

    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }
}