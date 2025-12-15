package com.ortiVerdi.dashboard.model;

/**
 * Rappresenta i dati produttivi di una coltura per un determinato mese.
 * Contiene le principali metriche agricole ed economiche necessarie
 * per l'analisi della produttività e dell'efficienza dell'azienda.
 *
 * Variabili principali:
 * - mese: numero del mese (1-12)
 * - quantita: quantità di raccolto in kg
 * - consumo: consumo idrico in litri
 * - costi: costi di produzione in €
 * - profitto: profitto mensile in €
 */
public class DatiProduzione {
    private int mese;               // 1-12
    private double quantita;        // kg
    private double consumo;         // litri
    private double costi;           // €
    private double profitto;        // €

    // Costruttore principale per inizializzare tutti i dati produttivi
    public DatiProduzione(int mese, double quantita, double consumo, double costi, double profitto){
        this.mese = mese;
        this.quantita = quantita;
        this.consumo = consumo;
        this.costi = costi;
        this.profitto = profitto;
    }

    // ==============================
    // Getter e Setter
    // ==============================
    public int getMese() { return mese; }
    public void setMese(int mese) { this.mese = mese; }

    public double getQuantitaRaccolto() { return quantita; }
    public void setQuantitaRaccolto(double quantita) { this.quantita = quantita; }

    public double getConsumoIdrico() { return consumo; }
    public void setConsumoIdrico(double consumo) { this.consumo = consumo; }

    public double getCostiProduzione() { return costi; }
    public void setCostiProduzione(double costi) { this.costi = costi; }

    public double getProfitto() { return profitto; }
    public void setProfitto(double profitto) { this.profitto = profitto; }

    /**
     * Ritorna una rappresentazione testuale dell'oggetto,
     * utile per debug e stampa dei dati mensili.
     */
    @Override
    public String toString() {
        return "Mese=" + mese +
                ", Quantita=" + quantita +
                ", Consumo=" + consumo +
                ", Costi=" + costi +
                ", Profitto=" + profitto;
    }
}
