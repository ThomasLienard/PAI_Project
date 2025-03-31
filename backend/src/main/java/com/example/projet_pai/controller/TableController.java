package com.example.projet_pai.controller;

import com.example.projet_pai.dto.TableDisponibiliteDTO;
import com.example.projet_pai.service.TableServiceItf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tables")
public class TableController {

    @Autowired
    private TableServiceItf tableService;
    
    @GetMapping("/disponibles")
    public ResponseEntity<List<TableDisponibiliteDTO>> getTablesDisponibles(
            @RequestParam String date,
            @RequestParam String creneau,
            @RequestParam int nbPersonnes) {
        
        List<TableDisponibiliteDTO> tables = tableService.getTablesDisponibles(date, creneau, nbPersonnes);
        return ResponseEntity.ok(tables);
    }
}