package com.example.projet_pai.repository;

import com.example.projet_pai.entite.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TableRepository extends JpaRepository<Table, Long> {
    
    @Query("SELECT t FROM Table t WHERE t.capacite >= :nbPersonnes AND t.id NOT IN " +
           "(SELECT r.table.id FROM Reservation r WHERE r.dateReservation = :date AND r.creneauHoraire = :creneau)")
    List<Table> findAvailableTables(@Param("date") String date, 
                                    @Param("creneau") String creneau, 
                                    @Param("nbPersonnes") int nbPersonnes);
}