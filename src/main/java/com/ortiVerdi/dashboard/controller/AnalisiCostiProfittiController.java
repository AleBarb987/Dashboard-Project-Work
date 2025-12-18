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
 * Controller dedicato all'analisi economica dell'azienda agricola.
 * Gestisce la visualizzazione dei costi di produzione e dei margini di profitto,
 * permettendo un confronto mensile e annuale.
 */
@Controller
public class AnalisiCostiProfittiController {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private SimulatoreService simulatoreService;

    @GetMapping("/analisi-costi-profitti")
    public String showAnalisiCostiProfitti(@RequestParam(name = "mese", defaultValue = "1") int mese, Model model) {

        // 1. Nomi dei mesi per il selettore e i tag JS
        String[] nomiMesi = dashboardService.getMesi();
        model.addAttribute("mesi", nomiMesi);
        model.addAttribute("meseSelezionato", mese);

        // 2. Dati ambientali (per coerenza con la tabella HTML)
        DatiAmbientali ambientali = simulatoreService.generaDatiAmbientali(mese);
        model.addAttribute("datiAmbientali", ambientali);

        // 3. Dati produttivi e finanziari del mese (per la tabella HTML)
        DatiProduzione produttivi = simulatoreService.getDatiMensili(mese);
        model.addAttribute("datiProduttivi", produttivi);

        // 4. Dati per il grafico annuale (Costi e Profitti)
        // Recuperiamo gli array double[] dai service e li convertiamo in List per Thymeleaf
        double[] costiArray = dashboardService.getCostiAnnuali();
        double[] profittiArray = dashboardService.getProfittiAnnuali();

        List<Double> costiList = Arrays.stream(costiArray).boxed().collect(Collectors.toList());
        List<Double> profittiList = Arrays.stream(profittiArray).boxed().collect(Collectors.toList());

        model.addAttribute("costiMensili", costiList);
        model.addAttribute("profittiMensili", profittiList);

        return "analisi-costi-profitti";
    }
}