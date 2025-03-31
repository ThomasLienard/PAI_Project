package com.example.projet_pai.dto;

import com.example.projet_pai.entite.Reservation;

public class ReservationResponseDTO {
    private Long id;
    private String dateReservation;
    private String creneauHoraire;
    private int nbPersonne;
    private Long clientId;
    private TableDisponibiliteDTO table;
    
    // Constructeur vide
    public ReservationResponseDTO() {}
    
    // Méthode de conversion d'une entité en DTO
    public static ReservationResponseDTO fromEntity(Reservation reservation) {
        ReservationResponseDTO dto = new ReservationResponseDTO();
        dto.setId(reservation.getId());
        dto.setDateReservation(reservation.getDateReservation());
        dto.setCreneauHoraire(reservation.getCreneauHoraire());
        dto.setNbPersonne(reservation.getNbPersonne());
        
        if (reservation.getClient() != null) {
            dto.setClientId(reservation.getClient().getId());
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
    
    public Long getClientId() {
        return clientId;
    }
    
    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
    
    public TableDisponibiliteDTO getTable() {
        return table;
    }
    
    public void setTable(TableDisponibiliteDTO table) {
        this.table = table;
    }
}