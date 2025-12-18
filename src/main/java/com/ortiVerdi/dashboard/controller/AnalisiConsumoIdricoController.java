package com.ortiVerdi.dashboard.controller;

import com.ortiVerdi.dashboard.model.DatiAmbientali;
import com.ortiVerdi.dashboard.model.DatiProduzione;
import com.ortiVerdi.dashboard.service.DashboardService;
import com.ortiVerdi.dashboard.service.SimulatoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller dedicato all'analisi del consumo idrico.
 * Gestisce l'interazione tra i dati ambientali (piogge/umidità) e il fabbisogno idrico
 * delle colture simulato dal sistema.
 */
@Controller
public class AnalisiConsumoIdricoController {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private SimulatoreService simulatoreService;

    @GetMapping("/analisi-consumo-idrico")
    public String showAnalisiConsumoIdrico(@RequestParam(name = "mese", defaultValue = "1") int mese, Model model) {

        // 1. Nomi dei mesi per il selettore e le etichette del grafico
        String[] nomiMesi = dashboardService.getMesi();
        model.addAttribute("mesi", nomiMesi);
        model.addAttribute("meseSelezionato", mese);

        // 2. Recupero dati ambientali (fondamentali per contestualizzare il consumo idrico)
        DatiAmbientali ambientali = simulatoreService.generaDatiAmbientali(mese);
        model.addAttribute("datiAmbientali", ambientali);

        // 3. Recupero dati produttivi aggregati per il mese (per la tabella di dettaglio)
        DatiProduzione produttivi = simulatoreService.getDatiMensili(mese);
        model.addAttribute("datiProduttivi", produttivi);

        // 4. Dati per il grafico a barre (Consumo mensile totale in litri)
        // Usiamo il metodo del SimulatoreService che recupera la cache o calcola i consumi
        double[] consumoArray = simulatoreService.getConsumoIdricoMensile();
        List<Double> consumoList = Arrays.stream(consumoArray).boxed().collect(Collectors.toList());
        model.addAttribute("consumoMensile", consumoList);

        // 5. Calcolo della soglia media (linea rossa nel grafico verde)
        double sogliaMedia = dashboardService.calcolaMedia(consumoArray);
        model.addAttribute("sogliaMediaConsumo", sogliaMedia);

        return "analisi-consumo-idrico";
    }
}