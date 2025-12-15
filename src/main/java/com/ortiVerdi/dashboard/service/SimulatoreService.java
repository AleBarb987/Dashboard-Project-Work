package com.ortiVerdi.dashboard.service;

import com.ortiVerdi.dashboard.model.Coltura;
import com.ortiVerdi.dashboard.model.DatiAmbientali;
import com.ortiVerdi.dashboard.model.DatiProduzione;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SimulatoreService {

    private final Random rnd = new Random(); // Generatore di numeri casuali

    // ======================================================
    // CACHE: I DATI SI GENERANO UNA VOLTA SOLA per migliorare le performance
    // ======================================================
    private List<Coltura> coltureCache = null;
    private double[] consumoIdricoCache = null;
    private DatiProduzione datiAnnualiCache = null;
    private Map<Integer, DatiProduzione> datiMensiliCache = new HashMap<>();
    private Map<Integer, DatiAmbientali> datiAmbientaliCache = new HashMap<>(); // Cache dei dati ambientali

    // ======================================================
    // Helper per generare valori con distribuzione gaussiana
    // ======================================================
    private double gauss(double media, double deviazione) {
        return media + rnd.nextGaussian() * deviazione;
    }

    // ======================================================
    // Generazione dati ambientali per un mese (memorizzati nella cache)
    // ======================================================
    public DatiAmbientali generaDatiAmbientali(int mese) {
        if (datiAmbientaliCache.containsKey(mese)) {
            return datiAmbientaliCache.get(mese); // Restituisce dati già generati
        }

        // Generazione randomica dei valori ambientali, con distribuzione realistica
        DatiAmbientali da = new DatiAmbientali(
                gauss(18, 7),       // Temperatura media ± deviazione
                gauss(55, 15),      // Umidità relativa
                Math.abs(gauss(80, 40)), // Precipitazioni (valore positivo)
                Math.abs(gauss(3, 1)),   // Velocità del vento
                Math.abs(gauss(20000, 8000)) // Luminosità
        );

        datiAmbientaliCache.put(mese, da); // Salva nella cache
        return da;
    }

    // ======================================================
    // Profili stagionali delle colture (valori normalizzati 0-1)
    // ======================================================
    private final double[] pomodoroStagionale = {0,0,0.1,0.3,0.6,1,1,0.9,0.6,0.3,0.1,0};
    private final double[] limoneStagionale   = {0.2,0.2,0.3,0.5,0.7,0.9,0.9,0.8,0.7,0.6,0.4,0.3};
    private final double[] uvaStagionale      = {0,0,0.1,0.2,0.4,0.7,1,1,0.8,0.5,0.2,0};
    private final double[] olivoStagionale    = {0.3,0.3,0.4,0.5,0.6,0.8,0.9,0.8,0.6,0.5,0.4,0.3};
    private final double[] granoStagionale    = {0,0.1,0.3,0.6,0.9,1,0.8,0.5,0.2,0.1,0,0};
    private final double[] nocciolaStagionale = {0.1,0.1,0.2,0.4,0.5,0.7,0.8,0.8,0.6,0.4,0.2,0.1};
    private final double[] pescheStagionale   = {0,0,0.2,0.5,0.8,1,1,0.9,0.6,0.3,0.1,0};

    private final String[] nomiColture = {"Pomodoro","Limone","Uva","Olivo","Grano Duro","Nocciola","Pesche"};
    private final double[] prezziUnitari = {2.0,1.5,3.0,5.0,1.2,4.0,2.5};

    // Consumo idrico stimato per kg di prodotto
    private final Map<String, Double> consumoIdricoPerKg = Map.of(
            "Pomodoro", 2.0,
            "Limone", 1.5,
            "Uva", 2.5,
            "Olivo", 3.0,
            "Grano Duro", 1.2,
            "Nocciola", 3.5,
            "Pesche", 2.8
    );

    // ======================================================
    // Restituisce il profilo stagionale di una coltura
    // ======================================================
    private double[] getProfiloStagionale(String nome) {
        return switch (nome) {
            case "Pomodoro" -> pomodoroStagionale;
            case "Limone" -> limoneStagionale;
            case "Uva" -> uvaStagionale;
            case "Olivo" -> olivoStagionale;
            case "Grano Duro" -> granoStagionale;
            case "Nocciola" -> nocciolaStagionale;
            case "Pesche" -> pescheStagionale;
            default -> new double[12]; // Se non esiste, array di zeri
        };
    }

    // ======================================================
    // Genera le colture e le loro statistiche (una sola volta)
    // ======================================================
    public List<Coltura> getColture() {
        if (coltureCache != null) return coltureCache;

        List<Coltura> lista = new ArrayList<>();

        for (int i = 0; i < nomiColture.length; i++) {
            String nome = nomiColture[i];
            double prezzo = prezziUnitari[i];

            double[] raccolto = new double[12];
            double[] costi = new double[12];

            double[] profilo = getProfiloStagionale(nome);

            for (int m = 0; m < 12; m++) {
                // Genera valori casuali di raccolto proporzionati al profilo stagionale
                double base = 20 + rnd.nextDouble() * 80; // produzione base
                raccolto[m] = base * profilo[m];
                // Calcolo dei costi in funzione della produzione
                costi[m] = 5 + raccolto[m] * (0.5 + rnd.nextDouble() * 0.5);
            }

            lista.add(new Coltura(nome, prezzo, costi, raccolto));
        }

        coltureCache = lista; // Salva nella cache
        return lista;
    }

    // ======================================================
    // Calcola il consumo idrico totale mensile di tutte le colture
    // ======================================================
    public double[] getConsumoIdricoMensile() {
        if (consumoIdricoCache != null) return consumoIdricoCache;

        double[] totale = new double[12];

        for (Coltura c : getColture()) {
            double coeff = consumoIdricoPerKg.get(c.getNome());

            for (int m = 0; m < 12; m++) {
                totale[m] += c.getRaccoltoMensile()[m] * coeff; // consumo per mese
            }
        }

        consumoIdricoCache = totale;
        return totale;
    }

    // ======================================================
    // Calcola i dati produttivi mensili aggregati
    // ======================================================
    public DatiProduzione getDatiMensili(int mese) {
        if (datiMensiliCache.containsKey(mese)) return datiMensiliCache.get(mese);

        int idx = mese - 1; // indice array 0-11
        double raccolto = 0, costi = 0, profitto = 0, consumo = 0;

        for (Coltura c : getColture()) {
            double r = c.getRaccoltoMensile()[idx];
            double costo = c.getCostiMensili()[idx];

            raccolto += r;
            costi += costo;
            profitto += r * c.getPrezzoVendita() - costo;
            consumo += r * consumoIdricoPerKg.get(c.getNome());
        }

        DatiProduzione dp = new DatiProduzione(mese, raccolto, consumo, costi, profitto);
        datiMensiliCache.put(mese, dp); // Salva nella cache
        return dp;
    }

    // ======================================================
    // Calcola i dati produttivi annuali aggregati
    // ======================================================
    public DatiProduzione getDatiAnnuali() {
        if (datiAnnualiCache != null) return datiAnnualiCache;

        double raccolto = 0, costi = 0, profitto = 0, consumo = 0;

        // Somma tutti i dati mensili per ottenere i valori annuali
        for (int mese = 1; mese <= 12; mese++) {
            DatiProduzione dp = getDatiMensili(mese);
            raccolto += dp.getQuantitaRaccolto();
            costi += dp.getCostiProduzione();
            profitto += dp.getProfitto();
            consumo += dp.getConsumoIdrico();
        }

        datiAnnualiCache = new DatiProduzione(0, raccolto, consumo, costi, profitto);
        return datiAnnualiCache;
    }
}
