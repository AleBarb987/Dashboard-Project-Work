package com.ortiVerdi.dashboard.model;

/**
 * Rappresenta i dati ambientali per una coltura in un determinato mese.
 * Tutti i valori sono immutabili, in quanto generati e gestiti dal servizio di simulazione.
 * Include le variabili principali che influenzano la crescita delle colture:
 * - temperatura (°C)
 * - umidità relativa (%)
 * - precipitazioni (mm)
 * - velocità del vento (km/h)
 * - luminosità (lux)
 */
public class DatiAmbientali {
    private final double temperatura;
    private final double umiditaRelativa;
    private final double precipitazioni;
    private final double velocitaVento;
    private final double luminosita;

    // Costruttore per inizializzare tutti i valori ambientali
    public DatiAmbientali(double temperatura, double umidita,
                          double precipitazioni, double vento, double luminosita){
        this.temperatura = temperatura;
        this.umiditaRelativa = umidita;
        this.precipitazioni = precipitazioni;
        this.velocitaVento = vento;
        this.luminosita = luminosita;
    }

    // ==============================
    // Getter per accedere ai dati ambientali
    // ==============================
    public double getTemperatura() { return temperatura; }
    public double getUmiditaRelativa() { return umiditaRelativa; }
    public double getPrecipitazioni() { return precipitazioni; }
    public double getVelocitaVento() { return velocitaVento; }
    public double getLuminosita() { return luminosita; }

    // Non sono presenti setter: i valori sono immutabili e gestiti solo dal servizio di simulazione
}
