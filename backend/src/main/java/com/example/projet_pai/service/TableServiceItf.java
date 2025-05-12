package com.example.projet_pai.service;

import com.example.projet_pai.dto.TableDisponibiliteDTO;
import com.example.projet_pai.dto.TableDTO;
import java.util.List;

public interface TableServiceItf {
    List<TableDisponibiliteDTO> getTablesDisponibles(String date, String creneau, int nbPersonnes);
    List<TableDTO> getAllTables();
}