/**
 * SCRIPT: graficoRaccolto.js
 * ----------------------------------
 * Visualizza la quantità di raccolto mensile (kg)
 * con una linea di riferimento per la soglia media.
 *
 * ⚙️ Dipendenze:
 *  - Chart.js (v3+)
 *  - Variabili globali dal backend:
 *      -> labelsData: array dei mesi
 *      -> raccoltoData: valori mensili del raccolto
 *      -> sogliaData: valore medio del raccolto
 */

document.addEventListener("DOMContentLoaded", function() {
    const ctx = document.getElementById('graficoRaccolto').getContext('2d');

    // 🎨 Gradiente verticale per le barre del grafico
    const gradient = ctx.createLinearGradient(0, 0, 0, 400);
    gradient.addColorStop(0, 'rgba(54, 162, 235, 0.8)');  // parte alta blu chiaro
    gradient.addColorStop(1, 'rgba(33, 150, 243, 0.9)');  // parte bassa blu intenso

    // 📊 Creazione del grafico Chart.js
    new Chart(ctx, {
        type: 'bar',   // Grafico a barre
        data: {
            labels: labelsData,   // Etichette asse X (mesi)
            datasets: [
                {
                    label: 'Quantità raccolto (kg)',
                    data: raccoltoData,           // Valori raccolto mensile
                    backgroundColor: gradient,
                    borderColor: 'rgba(33, 150, 243, 1)',
                    borderWidth: 1,
                    borderRadius: 6,
                    borderSkipped: false
                },
                {
                    label: 'Soglia media',
                    data: Array(labelsData.length).fill(sogliaData), // Linea costante media
                    type: 'line',                  // Linea sopra le barre
                    borderColor: 'rgba(255, 99, 132, 0.9)',
                    borderWidth: 2,
                    pointRadius: 4,
                    pointBackgroundColor: '#fff',
                    tension: 0.3                   // Curvatura della linea
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'bottom',
                    labels: { color: '#333', font: { size: 13 } }
                },
                title: {
                    display: true,
                    text: 'Quantità raccolto mensile',
                    color: '#1a237e',
                    font: { size: 18, weight: 'bold' }
                },
                tooltip: {
                    backgroundColor: 'rgba(255,255,255,0.9)',
                    titleColor: '#1a237e',
                    bodyColor: '#333',
                    borderColor: '#2196F3',
                    borderWidth: 1,
                    callbacks: {
                        // Mostra unità kg nel tooltip
                        label: function(context) {
                            return context.dataset.label + ': ' + context.formattedValue + ' kg';
                        }
                    }
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    title: { display: true, text: 'Quantità (kg)', color: '#333' },
                    ticks: { color: '#333' },
                    grid: { color: 'rgba(0,0,0,0.05)' }
                },
                x: {
                    title: { display: true, text: 'Mese', color: '#333' },
                    ticks: { color: '#333' },
                    grid: { display: false }
                }
            },
            animation: { duration: 1000, easing: 'easeOutQuart' }
        }
    });
});

// ===================== NAVBAR DINAMICA =====================
document.addEventListener("DOMContentLoaded", function() {
    const selector = document.getElementById('pageSelector');
    const currentPath = window.location.pathname;

    // ✅ Evidenzia la pagina corrente nel menu
    for (let option of selector.options) {
        if (option.value === currentPath) {
            option.selected = true;
            break;
        }
    }

    // 🚀 Navigazione dinamica verso la pagina selezionata
    selector.addEventListener('change', function() {
        if (this.value) window.location.href = this.value;
    });
});
