package com.ortiVerdi.dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principale dell'applicazione OrtiVerdi Dashboard.
 *
 * È il punto di ingresso dell'applicazione Spring Boot:
 * - Avvia il contesto Spring (dependency injection, configurazioni e component scanning)
 * - Rileva automaticamente Controller, Service e Repository all'interno del package base
 * - Esegue il server web embedded (Tomcat di default)
 *
 * L'annotazione @SpringBootApplication racchiude:
 *  - @Configuration → consente di definire bean di configurazione
 *  - @EnableAutoConfiguration → attiva le configurazioni automatiche di Spring Boot
 *  - @ComponentScan → abilita la scansione dei componenti nel package corrente e sottopacchetti
 */
@SpringBootApplication
public class DashboardApplication {

    /**
     * Metodo main dell’applicazione.
     * Utilizza SpringApplication.run() per avviare l'intero contesto applicativo.
     * Da qui parte l'esecuzione del progetto: vengono caricati i servizi, i controller
     * e tutte le dipendenze definite nel contesto Spring.
     */
    public static void main(String[] args) {
        SpringApplication.run(DashboardApplication.class, args);
    }
}
