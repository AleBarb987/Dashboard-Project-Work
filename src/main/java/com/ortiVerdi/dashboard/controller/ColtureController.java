package com.ortiVerdi.dashboard.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ortiVerdi.dashboard.model.Coltura;
import com.ortiVerdi.dashboard.service.DashboardService;
import com.ortiVerdi.dashboard.service.SimulatoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Controller dedicato alla gestione del dettaglio per singola coltura.
 * Permette di visualizzare andamenti comparativi e bilanci economici
 * specifici per ogni tipologia di prodotto agricolo.
 */
@Controller
public class ColtureController {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private SimulatoreService simulatoreService;

    // Mapper per convertire gli oggetti Java in stringhe JSON leggibili dal JS
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/colture")
    public String showColture(Model model) {

        // 1. Recupero della lista completa delle colture dal SimulatoreService
        List<Coltura> listaColture = simulatoreService.getColture();
        model.addAttribute("colture", listaColture);

        // 2. Nomi dei mesi per le etichette dell'asse X
        String[] nomiMesi = dashboardService.getMesi();
        model.addAttribute("mesi", nomiMesi);

        // 3. Conversione della lista colture in JSON per il blocco <script> nel template
        // Questo permette al file colture.js di avere i dati pronti per Chart.js
        try {
            String coltureJson = objectMapper.writeValueAsString(listaColture);
            model.addAttribute("coltureJson", coltureJson);
        } catch (JsonProcessingException e) {
            model.addAttribute("coltureJson", "[]");
            // In una fase successiva documenteremo la gestione delle eccezioni qui
        }

        return "colture";
    }
}