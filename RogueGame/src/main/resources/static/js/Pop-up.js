$(document).ready(function() {
    // ... (altro codice JavaScript)

    $('.aggiungi-carrello').click(function(e) {
        e.preventDefault();

        var form = $(this).closest('form');
        var url = form.attr('action');
        var nomeProdotto = $(this).data('nome-prodotto'); // Assumiamo che il nome del prodotto sia memorizzato in un attributo data

        $.ajax({
            url: url,
            type: "POST",
            data: { nome: nomeProdotto }, // Invia il nome del prodotto nel corpo della richiesta
            success: function() {
                $("#popup-message").show();
                setTimeout(function() {
                    $("#popup-message").hide();
                }, 3000);
            },
            error: function(xhr, status, error) {
                // Mostra un messaggio di errore personalizzato
                $('#error-message').text('Si è verificato un errore durante l\'aggiunta al carrello: ' + error);
                $('#error-message').show();
                // Registra l'errore in console per debugging
                console.error('Errore AJAX:', xhr, status, error);
            }
        });
    });
});