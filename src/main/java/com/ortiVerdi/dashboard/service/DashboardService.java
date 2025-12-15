package com.ortiVerdi.dashboard.service;

import com.ortiVerdi.dashboard.model.Coltura;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servizio per aggregare i dati delle colture e fornire statistiche
 * annuali o mensili all'interfaccia della dashboard.
 */
@Service
public class DashboardService {

    private final SimulatoreService simulatoreService;

    // Iniezione del servizio che genera i dati simulati
    public DashboardService(SimulatoreService simulatoreService) {
        this.simulatoreService = simulatoreService;
    }

    // ==============================
    // Restituisce i nomi dei mesi in forma abbreviata
    // ==============================
    public String[] getMesi() {
        return new String[]{"Gen", "Feb", "Mar", "Apr", "Mag", "Giu",
                "Lug", "Ago", "Set", "Ott", "Nov", "Dic"};
    }

    // ==============================
    // Calcola il raccolto totale annuale sommando tutte le colture
    // ==============================
    public double[] getRaccoltoAnnuale() {
        List<Coltura> colture = simulatoreService.getColture();  // Otteniamo tutte le colture
        double[] totale = new double[12];  // Array con 12 mesi

        for (Coltura c : colture) {
            for (int mese = 0; mese < 12; mese++) {
                totale[mese] += c.getRaccoltoMensile()[mese];  // Somma dei raccolti mensili
            }
        }

        return totale;
    }

    // ==============================
    // Restituisce il consumo idrico totale annuale
    // ==============================
    public double[] getConsumoAnnuale() {
        // Il consumo idrico mensile è già calcolato e memorizzato nel simulatore
        return simulatoreService.getConsumoIdricoMensile();
    }

    // ==============================
    // Calcola i costi annuali totali sommando i costi di tutte le colture
    // ==============================
    public double[] getCostiAnnuali() {
        List<Coltura> colture = simulatoreService.getColture();
        double[] totale = new double[12];

        for (Coltura c : colture) {
            for (int mese = 0; mese < 12; mese++) {
                totale[mese] += c.getCostiMensili()[mese];  // Somma dei costi mensili
            }
        }

        return totale;
    }

    // ==============================
    // Calcola i profitti annuali totali per tutte le colture
    // Profitto = ricavo - costi
    // ==============================
    public double[] getProfittiAnnuali() {
        List<Coltura> colture = simulatoreService.getColture();
        double[] totale = new double[12];

        for (Coltura c : colture) {
            for (int mese = 0; mese < 12; mese++) {
                double ricavo = c.getRaccoltoMensile()[mese] * c.getPrezzoVendita(); // Ricavo mensile
                totale[mese] += ricavo - c.getCostiMensili()[mese]; // Profitto mensile sommato
            }
        }

        return totale;
    }

    // ==============================
    // Calcola la media dei valori in un array
    // Utile per determinare soglie o valori medi annuali
    // ==============================
    public double calcolaMedia(double[] valori) {
        if (valori == null || valori.length == 0) return 0; // Evita divisione per zero
        double somma = 0;
        for (double v : valori) somma += v; // Somma dei valori
        return somma / valori.length;       // Media
    }

}
