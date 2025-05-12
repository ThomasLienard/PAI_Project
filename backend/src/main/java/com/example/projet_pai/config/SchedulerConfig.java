package com.example.projet_pai.config;

import com.example.projet_pai.service.StockAlertServiceItf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    @Autowired
    private StockAlertServiceItf stockAlertService;

    @Scheduled(fixedRate = 3600000) // Exécuter toutes les heures (3600000 ms)
    public void scheduleStockCheck() {
        stockAlertService.checkStockLevels();
    }
}