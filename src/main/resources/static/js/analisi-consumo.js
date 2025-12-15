/**
 * SCRIPT: graficoConsumo.js
 * -------------------------
 * Gestisce la visualizzazione del grafico del consumo idrico mensile
 * e la logica di selezione dinamica della pagina nella navbar.
 *
 * ⚙️ Dipendenze:
 *  - Chart.js (v3 o superiore)
 *  - Variabili globali fornite lato server: labelsData, consumoData, sogliaData
 *
 * 📊 Funzionalità principali:
 *  - Creazione di un grafico a barre + linea della soglia media
 *  - Gradienti dinamici per le barre
 *  - Tooltip e legenda personalizzati
 *  - Animazioni fluide
 *  - Navbar interattiva che cambia pagina senza ricaricare manualmente
 */

// ===================== GRAFICO CONSUMO IDRICO =====================
document.addEventListener("DOMContentLoaded", function() {
    // Recupero del contesto canvas per Chart.js
    const ctx = document.getElementById('graficoConsumo').getContext('2d');

    // 🎨 Creazione di un gradiente verticale per le barre
    const gradient = ctx.createLinearGradient(0, 0, 0, 400);
    gradient.addColorStop(0, 'rgba(76, 175, 80, 0.8)');   // verde chiaro in cima
    gradient.addColorStop(1, 'rgba(56, 142, 60, 0.9)');   // verde scuro in basso

    // 🧭 Inizializzazione del grafico con Chart.js
    new Chart(ctx, {
        type: 'bar', // Grafico a barre principale
        data: {
            labels: labelsData, // Nomi dei mesi da visualizzare sull'asse X
            datasets: [
                {
                    label: 'Consumo Idrico (litri)', // Etichetta per la legenda
                    data: consumoData, // Valori mensili del consumo idrico
                    backgroundColor: gradient, // Colore delle barre
                    borderRadius: 6, // Arrotondamento angoli barre
                    borderSkipped: false // Tutti gli angoli arrotondati
                },
                {
                    label: 'Soglia media', // Linea di riferimento per la media
                    data: Array(labelsData.length).fill(sogliaData), // Linea costante
                    type: 'line', // Linea sovrapposta alle barre
                    borderColor: 'rgba(255, 99, 132, 0.9)', // Colore linea (rosso)
                    borderWidth: 2,
                    pointRadius: 4, // Dimensione dei punti sulla linea
                    pointBackgroundColor: 'white', // Colore dei punti
                    tension: 0.3 // Curvatura della linea
                }
            ]
        },
        options: {
            responsive: true, // Grafico adattabile a diverse dimensioni
            maintainAspectRatio: false, // Mantiene altezza dinamica
            plugins: {
                legend: {
                    position: 'bottom', // Posizione della legenda
                    labels: {
                        color: '#333',
                        font: { size: 13 }
                    }
                },
                title: {
                    display: true,
                    text: 'Andamento mensile del consumo idrico', // Titolo grafico
                    color: '#2e7d32',
                    font: { size: 18, weight: 'bold' }
                },
                tooltip: {
                    backgroundColor: 'rgba(255,255,255,0.9)', // Colore sfondo tooltip
                    titleColor: '#2e7d32', // Colore titolo tooltip
                    bodyColor: '#333', // Colore testo tooltip
                    borderColor: '#4CAF50', // Bordo tooltip
                    borderWidth: 1
                }
            },
            scales: {
                y: {
                    beginAtZero: true, // L'asse Y parte da zero
                    ticks: { color: '#333' },
                    grid: { color: 'rgba(0,0,0,0.05)' } // Colore griglia leggero
                },
                x: {
                    ticks: { color: '#333' },
                    grid: { display: false } // Nessuna linea verticale di griglia
                }
            },
            animation: {
                duration: 1000, // Durata animazione iniziale in ms
                easing: 'easeOutQuart' // Tipo di easing
            }
        }
    });
});

// ===================== GESTIONE NAVBAR =====================
document.addEventListener("DOMContentLoaded", function() {
    const selector = document.getElementById('pageSelector');

    // 🔄 Seleziona automaticamente la voce corrispondente alla pagina corrente
    const currentPath = window.location.pathname;
    for (let option of selector.options) {
        if (option.value === currentPath) {
            option.selected = true; // Evidenzia pagina corrente
            break;
        }
    }

    // 🚀 Navigazione dinamica: cambia pagina quando l'utente seleziona un'opzione
    selector.addEventListener('change', function() {
        if (this.value) window.location.href = this.value;
    });
});
