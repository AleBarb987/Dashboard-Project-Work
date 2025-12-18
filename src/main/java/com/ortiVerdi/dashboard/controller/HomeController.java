package com.ortiVerdi.dashboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller dedicato esclusivamente alla gestione della Home Page.
 * Gestisce la rotta principale fornendo la vista di navigazione dell'applicazione.
 */
@Controller
public class HomeController {

    /**
     * Mappa la richiesta sulla root ("/") e restituisce il template home.html.
     * Poiché la home attuale contiene solo link di navigazione, non sono necessari
     * oggetti nel Model.
     *
     * @return il nome del file template (home.html)
     */
    @GetMapping("/")
    public String showHome() {
        // Ritorna semplicemente la vista home.html senza logica aggiuntiva
        return "home";
    }
}