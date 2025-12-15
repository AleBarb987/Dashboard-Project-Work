package com.ortiVerdi.dashboard.model;

import java.util.Arrays;

/**
 * Modello dati per rappresentare una coltura agricola.
 * Contiene informazioni su:
 * - nome della coltura
 * - prezzo di vendita unitario
 * - costi mensili di produzione
 * - raccolto mensile
 *
 * Include metodi per calcolare quantitativi e valori economici annuali.
 */
public class Coltura {
    private String nome;
    private double prezzoVendita;
    private double[] costiMensili;
    private double[] raccoltoMensile;

    // Costruttore per inizializzare la coltura con dati principali
    public Coltura(String nome, double prezzoVendita, double[] costiMensili, double[] raccoltoMensile){
        this.nome = nome;
        this.prezzoVendita = prezzoVendita;
        this.costiMensili = costiMensili;
        this.raccoltoMensile = raccoltoMensile;
    }

    // ==============================
    // Getter per i campi
    // ==============================
    public String getNome() { return nome; }
    public double getPrezzoVendita() { return prezzoVendita; }
    public double[] getCostiMensili() { return costiMensili; }
    public double[] getRaccoltoMensile() { return raccoltoMensile; }

    // ==============================
    // Metodi di calcolo annuali
    // ==============================

    /** Somma totale del raccolto su 12 mesi */
    public double getQuantitaAnnuale() {
        return Arrays.stream(raccoltoMensile).sum();
    }

    /** Somma totale dei costi su 12 mesi */
    public double getCostoAnnuale() {
        return Arrays.stream(costiMensili).sum();
    }

    /** Ricavo annuale = quantità totale * prezzo unitario */
    public double getRicavoAnnuale() {
        return getQuantitaAnnuale() * prezzoVendita;
    }

    /** Profitto annuale = ricavo - costi */
    public double getProfittoAnnuale() {
        return getRicavoAnnuale() - getCostoAnnuale();
    }

    /** Margine medio per unità prodotta */
    public double getMarginePerUnita() {
        double q = getQuantitaAnnuale();
        return q > 0 ? getProfittoAnnuale() / q : 0; // evita divisione per zero
    }
}
