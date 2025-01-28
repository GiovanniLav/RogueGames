$(document).ready(function() {
    // ... (altro codice JavaScript)

    $('.rimuovi-carrello').off('click').on('click', function (e) {
        e.preventDefault();

        var form = $(this).closest('form');
        const $button = $(this);
        var nomeProdotto = $(this).data('nome-prodotto'); // Assumiamo che il nome del prodotto sia memorizzato in un attributo data
        var url = "/rimuovi/" + encodeURIComponent(nomeProdotto); // Sostituisce {nome} con il valore reale


        $.ajax({
            url: url,
            type: "POST",
            data: { nome: nomeProdotto }, // Invia il nome del prodotto nel corpo della richiesta
            success: function(response) {
                <!-- $button.closest('div').remove();-->
                $button.closest('.contenitore-grid').remove();
                provaAlert("Prodotto eliminato dal carrello!");
            },
            error: function(xhr, status, error) {
                // Mostra un messaggio di errore personalizzato
                $('#error-message').text('Si è verificato un errore durante l\'aggiunta al carrello: ' + error);
                $('#error-message').show();
                // Registra l'errore in console per debugging
            }
        });
    });
});

$(document).ready(function() {

    $('.aggiungi-carrello').off('click').on('click', function (e) {
        e.preventDefault();

        var nomeProdotto = $(this).data('nome-prodotto'); // Assumiamo che il nome del prodotto sia memorizzato in un attributo data
        var url = "/aggiungi/" + encodeURIComponent(nomeProdotto); // Sostituisce {nome} con il valore reale

        $.ajax({
            url: url,
            type: "POST",
            data: { nome: nomeProdotto }, // Invia il nome del prodotto nel corpo della richiesta
            success: function(response) {
                provaAlert(nomeProdotto + " è aggiunto al carrello!");
            },
            error: function(xhr, status, error) {
                if (xhr.status === 401) {
                    provaAlert("Errore: Devi loggarti per poter aggiungere prodotti al carrello");
                } else if (xhr.responseJSON && xhr.responseJSON.message) {
                    provaAlert("Errore: " + xhr.responseJSON.message); // Gestisce errori con messaggi JSON
                } else if (xhr.responseText) {
                    provaAlert("Errore: " + xhr.responseText); // Gestisce errori con testo semplice
                } else {
                    provaAlert("Errore durante la richiesta.");
                }
            }
        });
    });
});

$(document).ready(function() {
    $('.aumentaQuantita').off('click').on('click', function(e) {
        e.preventDefault();

        const quantita = $(this).closest('.item-grid').find('#quantita').val(); // Trova l'input quantita relativo al bottone
        const nomeProdotto = $(this).closest('.item-grid').find('#nome').val(); // Trova l'input nome relativo al bottone

        $.ajax({
            type: "POST",
            url: "/aumentaQnt",
            data: { nomeProdotto: nomeProdotto, quantita: quantita }, // Invia i dati come parametri del form

            success: function(response) {
                provaAlert("Quantità aggiornata con successo!");
                setTimeout(() => {
                    location.reload(); // Ricarica la pagina dopo 3 secondi
                }, 3000);
            },
            error: function(xhr, status, error) {
                location.reload();
                if (xhr.status === 401) {
                    window.location.href = "/utenti/login";
                } else if (xhr.responseJSON && xhr.responseJSON.message) {
                    provaAlert("Errore: " + xhr.responseJSON.message); // Gestisce errori con messaggi JSON
                } else if (xhr.responseText) {
                    provaAlert("Errore: " + xhr.responseText); // Gestisce errori con testo semplice
                } else {
                    provaAlert("Errore durante la richiesta.");
                }
            }
        });
    });
})

document.getElementById('btnAcquista').addEventListener('click', function(event) {
    event.preventDefault(); // Impedisci il submit del form

    if (carrelloVuoto()) {
        document.getElementById('popup').classList.remove('hidden');
    }
});

document.getElementById('btnClose').addEventListener('click', function() {
    document.getElementById('popup').classList.add('hidden');
});

function provaAlert(message) {
    const alertDiv = document.createElement("messageDiv");
    alertDiv.textContent = message;
    alertDiv.setAttribute("style", `
                background-color: #222;
                color: #ff9900;
                padding: 10px;
                border-radius: 5px;
                position: fixed;
                bottom: 20px;
                right: 20px;
                z-index: 1000;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            `);

    document.body.appendChild(alertDiv);

    // Rimuove l'alert dopo 3 secondi
    setTimeout(() => {
        alertDiv.remove();
    }, 3000);
}