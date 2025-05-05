package com.example.projet_pai.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import com.example.projet_pai.entite.Reservation;

@DataJpaTest
public class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;
    
    @Test
    @Sql("/static/sql/test-reservation-data.sql") // Vous devrez créer ce fichier SQL avec des données de test
    public void testFindByDateReservation() {
        // Arrange
        String testDate = "2025-05-05"; // Assurez-vous que cette date correspond à celle dans le fichier SQL
        
        // Act
        List<Reservation> reservations = reservationRepository.findByDateReservation(testDate);
        
        // Assert
        assertNotNull(reservations);
        assertEquals(2, reservations.size()); // Supposant que votre fichier SQL contient 2 réservations pour cette date
        
        // Vérifier les détails d'une réservation
        Reservation reservation = reservations.get(0);
        assertEquals("midi", reservation.getCreneauHoraire());
        assertEquals(testDate, reservation.getDateReservation());
        assertNotNull(reservation.getTable());
        assertNotNull(reservation.getClient());
    }
    
    @Test
    @Sql("/static/sql/test-reservation-data.sql")
    public void testFindByDateReservation_NoResults() {
        // Arrange - une date sans réservations
        String testDate = "2030-01-01";
        
        // Act
        List<Reservation> reservations = reservationRepository.findByDateReservation(testDate);
        
        // Assert
        assertNotNull(reservations);
        assertEquals(0, reservations.size());
    }
}