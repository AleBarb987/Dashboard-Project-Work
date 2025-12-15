/**
 * SCRIPT: graficoCostiProfitti.js
 * ----------------------------------
 * Visualizza il confronto mensile tra costi e guadagni.
 * Include anche la gestione dinamica della navbar.
 *
 * ⚙️ Dipendenze:
 *  - Chart.js (v3 o superiore)
 *  - Variabili globali fornite dal backend:
 *      -> mesi: array dei nomi dei mesi
 *      -> costi: array di costi mensili (€)
 *      -> profitti: array di profitti mensili (€)
 */

document.addEventListener("DOMContentLoaded", function () {
    // Ottiene il contesto del canvas dove disegnare il grafico
    const ctx = document.getElementById('graficoCostiProfitti').getContext('2d');

    // 💰 Calcolo dei guadagni totali = profitti + costi
    // Creiamo un array dei guadagni per ogni mese
    const guadagni = profitti.map((p, i) => p + costi[i]);

    // 📊 Creazione del grafico con Chart.js
    new Chart(ctx, {
        type: 'bar', // Tipo di grafico: barre
        data: {
            labels: mesi, // Etichette sull'asse X
            datasets: [
                {
                    label: 'Costi (€)',          // Nome della serie dati
                    data: costi,                 // Dati dei costi mensili
                    backgroundColor: 'rgba(255, 99, 132, 0.7)',   // Colore barre (rosso tenue)
                    borderColor: 'rgba(255, 99, 132, 1)',         // Colore bordo barre
                    borderWidth: 1,
                    borderRadius: 6,             // Angoli arrotondati
                },
                {
                    label: 'Guadagni (€)',
                    data: guadagni,              // Dati dei guadagni mensili
                    backgroundColor: 'rgba(54, 162, 235, 0.7)',   // Colore barre (blu tenue)
                    borderColor: 'rgba(54, 162, 235, 1)',
                    borderWidth: 1,
                    borderRadius: 6,
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false, // Il grafico si adatta al contenitore
            plugins: {
                legend: {
                    position: 'bottom',       // Legenda in basso
                    labels: {
                        color: '#333',        // Colore testo legenda
                        font: { size: 13 }
                    }
                },
                title: {
                    display: true,            // Mostra titolo
                    text: 'Costi e Guadagni Mensili',
                    color: '#1a237e',
                    font: { size: 18, weight: 'bold' }
                },
                tooltip: {
                    backgroundColor: 'rgba(255,255,255,0.9)',
                    titleColor: '#1a237e',
                    bodyColor: '#333',
                    borderColor: '#90caf9',
                    borderWidth: 1,
                    callbacks: {
                        // Formatta il tooltip mostrando € davanti al valore
                        label: function (context) {
                            return context.dataset.label + ': €' + context.formattedValue;
                        }
                    }
                }
            },
            scales: {
                x: {
                    title: { display: true, text: 'Mese', color: '#333' },
                    ticks: { color: '#333' },
                    grid: { display: false },          // Nessuna griglia verticale
                    categoryPercentage: 0.6,           // Spaziatura delle categorie
                    barPercentage: 0.8                 // Larghezza barre
                },
                y: {
                    beginAtZero: true,                 // Asse Y parte da zero
                    title: { display: true, text: 'Euro (€)', color: '#333' },
                    ticks: {
                        color: '#333',
                        // Callback per aggiungere € davanti ai valori
                        callback: function (value) {
                            return '€' + value;
                        }
                    },
                    grid: { color: 'rgba(0,0,0,0.05)' }
                }
            },
            animation: {
                duration: 1000,                        // Durata animazione
                easing: 'easeOutQuart'                 // Tipo di easing
            }
        }
    });
});

// ===================== GESTIONE NAVBAR =====================
document.addEventListener("DOMContentLoaded", function () {
    const selector = document.getElementById('pageSelector');
    const currentPath = window.location.pathname; // Percorso corrente della pagina

    // ✅ Evidenzia la voce della pagina corrente nel menu a tendina
    for (let option of selector.options) {
        if (option.value === currentPath) {
            option.selected = true;
            break;
        }
    }

    // 🚀 Cambio pagina dinamico al cambiare selezione nel menu
    selector.addEventListener('change', function () {
        if (this.value) window.location.href = this.value;
    });
});
