package com.ortiVerdi.dashboard.controller;

import com.ortiVerdi.dashboard.service.DashboardService;
import com.ortiVerdi.dashboard.service.SimulatoreService;
import com.ortiVerdi.dashboard.model.DatiAmbientali;
import com.ortiVerdi.dashboard.model.DatiProduzione;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AnalisiRaccoltoController {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private SimulatoreService simulatoreService;

    @GetMapping("/analisi-raccolto")
    public String showAnalisiRaccolto(@RequestParam(name = "mese", defaultValue = "1") int mese, Model model) {

        // 1. Nomi dei mesi per il selettore (usiamo il metodo del DashboardService)
        String[] nomiMesi = dashboardService.getMesi();
        model.addAttribute("mesi", nomiMesi);
        model.addAttribute("meseSelezionato", mese);

        // 2. Recupero dati ambientali dal SimulatoreService
        DatiAmbientali ambientali = simulatoreService.generaDatiAmbientali(mese);
        model.addAttribute("datiAmbientali", ambientali);

        // 3. Recupero dati produttivi aggregati per il mese (tabella)
        DatiProduzione produttivi = simulatoreService.getDatiMensili(mese);
        model.addAttribute("datiProduttivi", produttivi);

        // 4. Dati per il grafico annuale (usiamo DashboardService)
        double[] raccoltoArray = dashboardService.getRaccoltoAnnuale();
        // Convertiamo l'array in una lista per Thymeleaf/JS
        List<Double> raccoltoList = Arrays.stream(raccoltoArray).boxed().collect(Collectors.toList());
        model.addAttribute("raccoltoAnnuale", raccoltoList);

        // 5. Calcolo della soglia media per la linea del grafico
        double sogliaMedia = dashboardService.calcolaMedia(raccoltoArray);
        model.addAttribute("sogliaMediaRaccolto", sogliaMedia);

        return "analisi-raccolto";
    }
}