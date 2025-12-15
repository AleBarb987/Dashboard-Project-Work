package com.ortiVerdi.dashboard.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ortiVerdi.dashboard.model.Coltura;
import com.ortiVerdi.dashboard.model.DatiAmbientali;
import com.ortiVerdi.dashboard.model.DatiProduzione;
import com.ortiVerdi.dashboard.service.DashboardService;
import com.ortiVerdi.dashboard.service.SimulatoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private SimulatoreService simulatoreService;

    // ==============================
    // Pagina Home
    // ==============================
    @GetMapping("/")
    public String home() {
        return "home";  // Restituisce la pagina home.html
    }

    // ==============================
    // Visualizzazione delle colture
    // ==============================
    @GetMapping("/colture")
    public String visualizzaColture(Model model) throws JsonProcessingException {

        // Recupera la lista delle colture dal servizio di simulazione
        List<Coltura> colture = simulatoreService.getColture();

        // Converte la lista di oggetti Coltura in JSON per l'uso lato frontend (grafici, script JS)
        ObjectMapper mapper = new ObjectMapper();
        String coltureJson = mapper.writeValueAsString(colture);

        // Passaggio dei dati al modello Thymeleaf
        model.addAttribute("colture", colture);           // lista oggetti
        model.addAttribute("coltureJson", coltureJson);   // versione JSON per grafici
        model.addAttribute("mesi", dashboardService.getMesi()); // nomi mesi

        return "colture";
    }

    // ==============================
    // Analisi raccolto mensile
    // ==============================
    @GetMapping("/analisi-raccolto")
    public String analisiRaccolto(@RequestParam(name="mese", required=false, defaultValue="1") int mese,
                                  Model model)  {

        // Ottiene i dati produttivi e ambientali per il mese selezionato
        DatiProduzione datiMensili = simulatoreService.getDatiMensili(mese);
        DatiAmbientali datiAmbientali = simulatoreService.generaDatiAmbientali(mese);

        // Calcola il raccolto totale annuale sommando tutte le colture
        double[] raccoltoAnnuale = dashboardService.getRaccoltoAnnuale();

        // Aggiunge al modello i dati per la visualizzazione nella view
        model.addAttribute("raccoltoAnnuale", raccoltoAnnuale);
        model.addAttribute("datiAmbientali", datiAmbientali);
        model.addAttribute("datiProduttivi", datiMensili);
        model.addAttribute("mesi", dashboardService.getMesi());
        model.addAttribute("meseSelezionato", mese);
        model.addAttribute("sogliaMediaRaccolto", dashboardService.calcolaMedia(raccoltoAnnuale));

        return "analisi-raccolto"; // restituisce la pagina analisi-raccolto.html
    }

    // ==============================
    // Analisi costi e profitti
    // ==============================
    @GetMapping("/analisi-costi-profitti")
    public String analisiCostiProfitti(
            @RequestParam(name="mese", required=false, defaultValue="1") int mese,
            Model model) {

        // Recupera dati mensili e dati ambientali per il mese selezionato
        DatiProduzione datiMensili = simulatoreService.getDatiMensili(mese);
        DatiAmbientali datiAmbientali = simulatoreService.generaDatiAmbientali(mese);

        // Array annuali da utilizzare nei grafici per confronto tra mesi
        double[] costiMensili = dashboardService.getCostiAnnuali();
        double[] profittiMensili = dashboardService.getProfittiAnnuali();

        model.addAttribute("datiAmbientali", datiAmbientali);
        model.addAttribute("datiProduttivi", datiMensili);
        model.addAttribute("costiMensili", costiMensili);
        model.addAttribute("profittiMensili", profittiMensili);
        model.addAttribute("mesi", dashboardService.getMesi());
        model.addAttribute("meseSelezionato", mese);

        return "analisi-costi-profitti"; // restituisce la pagina analisi-costi-profitti.html
    }

    // ==============================
    // Analisi consumo idrico
    // ==============================
    @GetMapping("/analisi-consumo-idrico")
    public String analisiConsumoIdrico(@RequestParam(name="mese", required=false, defaultValue="1") int mese,
                                       Model model)  {

        // Recupera i dati mensili e ambientali per il mese selezionato
        DatiProduzione datiMensili = simulatoreService.getDatiMensili(mese);
        DatiAmbientali datiAmbientali = simulatoreService.generaDatiAmbientali(mese);

        // Ottiene il consumo idrico totale annuale delle colture
        double[] consumoData = dashboardService.getConsumoAnnuale();

        // Aggiunge al modello i dati per visualizzazione nella view
        model.addAttribute("consumoMensile", consumoData);
        model.addAttribute("datiAmbientali", datiAmbientali);
        model.addAttribute("datiProduttivi", datiMensili);
        model.addAttribute("mesi", dashboardService.getMesi());
        model.addAttribute("meseSelezionato", mese);
        model.addAttribute("sogliaMediaConsumo", dashboardService.calcolaMedia(consumoData));

        return "analisi-consumo-idrico"; // restituisce la pagina analisi-consumo-idrico.html
    }
}
