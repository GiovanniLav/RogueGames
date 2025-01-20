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
                alert("Prodotto eliminato dal carrello!");
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
                alert("Prodotto aggiunto al carrello!");
            },
            error: function(xhr, status, error) {

                if (xhr.status === 401) {
                    alert("Errore: Devi loggarti per poter aggiungere prodotti al carrello" );
                } else if (xhr.responseJSON && xhr.responseJSON.message) {
                    alert("Errore: " + xhr.responseJSON.message); // Gestisce errori con messaggi JSON
                } else if (xhr.responseText) {
                    alert("Errore: " + xhr.responseText); // Gestisce errori con testo semplice
                } else {
                    alert("Errore durante la richiesta.");
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
                location.reload();
                alert("Quantità aggiornata con successo!");
            },
            error: function(xhr, status, error) {
                location.reload();
                if (xhr.status === 401) {
                    window.location.href = "/utenti/login";
                } else if (xhr.responseJSON && xhr.responseJSON.message) {
                    alert("Errore: " + xhr.responseJSON.message); // Gestisce errori con messaggi JSON
                } else if (xhr.responseText) {
                    alert("Errore: " + xhr.responseText); // Gestisce errori con testo semplice
                } else {
                    alert("Errore durante la richiesta.");
                }
            }
        });
    });
})