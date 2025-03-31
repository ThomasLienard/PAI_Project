package com.example.projet_pai.service.Impl;

import com.example.projet_pai.dto.TableDisponibiliteDTO;
import com.example.projet_pai.entite.Table;
import com.example.projet_pai.repository.TableRepository;
import com.example.projet_pai.service.TableServiceItf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableServiceImpl implements TableServiceItf {

    @Autowired
    private TableRepository tableRepository;
    
    @Override
    public List<TableDisponibiliteDTO> getTablesDisponibles(String date, String creneau, int nbPersonnes) {
        List<Table> tables = tableRepository.findAvailableTables(date, creneau, nbPersonnes);
        
        return tables.stream()
                .map(table -> new TableDisponibiliteDTO(
                        table.getId(),
                        table.getNumero(),
                        table.getCapacite()))
                .collect(Collectors.toList());
    }
}