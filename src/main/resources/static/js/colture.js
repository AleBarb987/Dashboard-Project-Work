/**
 * SCRIPT: colture.js
 * ------------------------------------------
 * Gestisce la visualizzazione dei dati relativi alle colture:
 *  - Grafico 1: Andamento mensile del raccolto per coltura (line chart)
 *  - Grafico 2: Confronto costi e guadagni annuali per coltura (bar chart)
 *  - Interazione: Attivazione/disattivazione delle colture tramite rettangoli colorati
 *
 * ⚙️ Dipendenze:
 *   - Chart.js
 *   - Variabili globali dal backend:
 *       → coltureArray: array di oggetti "Coltura"
 *       → mesi: nomi brevi dei mesi
 */

 // ==========================================================
 // 🎨 Mappa colori fissi per le colture
 // Serve a garantire coerenza dei colori dei grafici per ogni coltura
 // ==========================================================
const coloriColture = {
    "Pomodoro": "#FF0000",    // Rosso
    "Limone": "#FFFF00",      // Giallo
    "Uva": "#00008B",         // Blu scuro
    "Olivo": "#00BFFF",       // Azzurro
    "Grano Duro": "#008000",  // Verde
    "Nocciola": "#C8A2C8",    // Lilla
    "Pesche": "#000000"       // Nero
};

// ==========================================================
// 📈 Funzione per creare i dataset del grafico lineare
// Ogni coltura selezionata genera una linea distinta
// ==========================================================
function creaDatasets(coltureSelezionate) {
    return coltureSelezionate.map(c => ({
        label: c.nome,                       // Nome della coltura
        data: c.raccoltoMensile,             // Array dei valori mensili raccolto
        borderColor: coloriColture[c.nome] || '#000000',  // Colore della linea
        backgroundColor: 'transparent',      // Nessun riempimento sotto la linea
        tension: 0.3,                        // Curvatura della linea
        fill: false                           // Non riempie l'area sotto la linea
    }));
}

// ==========================================================
// 🌾 GRAFICO 1 — Andamento mensile del raccolto per coltura
// ==========================================================
const ctxLine = document.getElementById('graficoColture').getContext('2d');

// Tutte le colture attive inizialmente
let coltureSelezionate = [...coltureArray];
let datasetsLine = creaDatasets(coltureSelezionate);

// Inizializzazione grafico Chart.js
let graficoColture = new Chart(ctxLine, {
    type: 'line',                  // Grafico a linee
    data: {
        labels: mesi,              // Etichette asse X: mesi
        datasets: datasetsLine     // Dataset generati dalla funzione
    },
    options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            legend: { position: 'bottom' },
            tooltip: {
                mode: 'index',
                intersect: false,
                backgroundColor: 'rgba(255,255,255,0.9)',
                titleColor: '#2e7d32',
                bodyColor: '#333',
                borderColor: '#4CAF50',
                borderWidth: 1
            }
        },
        interaction: { mode: 'nearest', intersect: false },
        scales: {
            y: { title: { display: true, text: 'Quantità raccolto (kg)' }, beginAtZero: true },
            x: { title: { display: true, text: 'Mese' } }
        },
        animation: { duration: 800, easing: 'easeOutQuart' }
    }
});

// ==========================================================
// 🟩 Gestione interattiva dei rettangoli-colture
// Cliccando su un rettangolo si mostra/nasconde la relativa linea nel grafico
// ==========================================================
const rettangoli = document.querySelectorAll('#checkbox-colture li');

rettangoli.forEach(rect => {
    rect.addEventListener('click', () => {
        rect.classList.toggle('active'); // Cambia lo stato visivo (attivo/non attivo)

        // Filtra solo le colture attive (quelle con classe 'active')
        const coltureSelezionate = Array.from(rettangoli)
            .filter(r => r.classList.contains('active'))
            .map(r => coltureArray.find(c => c.nome === r.dataset.nome))
            .filter(c => c !== undefined);

        // Aggiorna dinamicamente il grafico con le colture selezionate
        graficoColture.data.datasets = creaDatasets(coltureSelezionate);
        graficoColture.update();
    });
});

// ==========================================================
// 💰 GRAFICO 2 — Confronto Costi vs Guadagni annuali per coltura
// ==========================================================
const ctxBar = document.getElementById('graficoCostiGuadagni').getContext('2d');

// Etichette e dati per il grafico a barre
const labelsBar = coltureArray.map(c => c.nome);  // Nomi delle colture
const costiAnnuali = coltureArray.map(c => c.costiMensili.reduce((a, b) => a + b, 0)); // Somma costi
const guadagniAnnuali = coltureArray.map(c => c.raccoltoMensile.reduce((a, b) => a + b, 0) * c.prezzoVendita); // Somma guadagni

// Creazione grafico a barre
let graficoCostiGuadagni = new Chart(ctxBar, {
    type: 'bar',
    data: {
        labels: labelsBar,
        datasets: [
            {
                label: 'Costi Annuali (€)',
                data: costiAnnuali,
                backgroundColor: 'rgba(255, 99, 132, 0.8)'
            },
            {
                label: 'Guadagni Annuali (€)',
                data: guadagniAnnuali,
                backgroundColor: 'rgba(76, 175, 80, 0.8)'
            }
        ]
    },
    options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            legend: { position: 'bottom' },
            title: { display: true, text: 'Confronto Costi e Guadagni per Coltura' },
            tooltip: {
                callbacks: {
                    label: function(ctx) {
                        return `${ctx.dataset.label}: € ${ctx.formattedValue}`; // Tooltip personalizzato con simbolo €
                    }
                }
            }
        },
        scales: {
            y: {
                beginAtZero: true,
                title: { display: true, text: 'Euro (€)' },
                grid: { color: 'rgba(0,0,0,0.05)' }
            },
            x: { title: { display: true, text: 'Coltura' } }
        },
        animation: { duration: 800, easing: 'easeOutQuart' }
    }
});

// ==========================================================
// 🧭 GESTIONE NAVBAR — Selezione dinamica pagina
// ==========================================================
document.addEventListener("DOMContentLoaded", function() {
    const selector = document.getElementById('pageSelector');
    const currentPath = window.location.pathname;

    // Seleziona automaticamente la pagina corrente nella navbar
    for (let option of selector.options) {
        if (option.value === currentPath) {
            option.selected = true;
            break;
        }
    }

    // Navigazione dinamica al cambio del select
    selector.addEventListener('change', function() {
        if (this.value) window.location.href = this.value;
    });
});
