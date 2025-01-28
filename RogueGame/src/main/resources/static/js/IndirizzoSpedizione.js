$(document).ready(function() {

    $('.aggiungi-indirizzo').off('click').on('click', function (e) {
        e.preventDefault();

        var provincia = $("input[name='provincia']").val();
        var cap = $("input[name='cap']").val();
        var via = $("input[name='via']").val();
        var civico = $("input[name='civico']").val();
        var citta = $("input[name='citta']").val();
        var url = "/aggiungiIndirizzoSpedizione"

        $.ajax({
            url: url,
            type: "POST",
            data: {
                provincia: provincia,
                cap: cap,
                via: via,
                civico: civico,
                citta: citta
            },
            success: function(response) {
                alert("L'indirizzo è stato aggiunto correttamente");
            },
            error: function(xhr, status, error) {
                if (xhr.status === 401) {
                    alert("Errore: Devi loggarti per poter aggiungere prodotti al carrello");
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

$(document).ready(function() {

    $('.modifica-indirizzo').off('click').on('click', function (e) {
        e.preventDefault();

        var provincia = $("input[name='provincia']").val();
        var cap = $("input[name='cap']").val();
        var via = $("input[name='via']").val();
        var civico = $("input[name='civico']").val();
        var citta = $("input[name='citta']").val();
        var provinciamod = $("input[name='provincia-mod']").val();
        var capmod = $("input[name='cap-mod']").val();
        var viamod = $("input[name='via-mod']").val();
        var civicomod = $("input[name='civico']").val();
        var cittamod = $("input[name='citta']").val();
        var url = "/modificaIndirizzoSpedizione"

        $.ajax({
            url: url,
            type: "POST",
            data: {
                provincia: provincia,
                cap: cap,
                via: via,
                civico: civico,
                citta: citta,
                provinciamod: provinciamod,
                capmod: capmod,
                viamod: viamod,
                civicomod: civicomod,
                cittamod: cittamod
            },
            success: function(response) {
                alert("L'indirizzo è stato modifcato correttamente");
                window.location.href = "/indirizzo";
            },
            error: function(xhr, status, error) {
                if (xhr.status === 401) {
                    alert("Errore: Devi loggarti per poter aggiungere prodotti al carrello");
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



$(document).ready(function() {

    $('.rimuovi-indirizzo').off('click').on('click', function (e) {
        e.preventDefault();

        var form = $(this).closest('form');

        var provincia = document.getElementById('provincia').value
        var via = document.getElementById('via').value
        var cap = $(this).data('cap');
        var civico = document.getElementById('civico').value
        var citta = document.getElementById('citta').value // Assumiamo che il nome del prodotto sia memorizzato in un attributo data
        var url = "/rimuoviIndirizzo" // Sostituisce {nome} con il valore reale

        $.ajax({
            url: url,
            type: "POST",
            data: {
                provincia: provincia,
                cap: cap,
                via: via,
                civico: civico,
                citta: citta
            },
            success: function (response) {
                location.reload()
            },
            error: function (error) {
                console.error(error);
            }
        });
    });
})

$(document).ready(function () {
    $('.btn-modifica-in').on('click', function (e) {
        e.preventDefault();

        const form = $(this).closest('form');
        const url= "/modificaIndirizzo"

        // Crea un oggetto FormData per gestire i dati del form
        const formData = new FormData();
        formData.append('provincia', $('#provincia-m').val());
        formData.append('cap', $('#cap').val());
        formData.append('via', $('#via-m').val());
        formData.append('civico', $('#civico-m').val());
        formData.append('citta', $('#citta-m').val());

        // Aggiungi altri campi al FormData se necessario
        // formData.append('altroCampo', $('#altroCampo').val());

        // Invia la richiesta AJAX
        $.ajax({
            url: url,
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function (response) {
                if (response.success) {
                    // Reindirizza al URL fornito
                    window.location.href = response.redirectUrl;
                } else {
                    if (response.redirectUrl) {
                        // Reindirizza per login o altri scenari
                        window.location.href = response.redirectUrl;
                    } else {
                        console.error('Errore:', response.message);
                        alert(response.message || 'Errore sconosciuto');
                    }
                }
            },
            error: function (xhr, status, error) {
                console.error('Errore durante la richiesta AJAX:', error);
            }
        });
    });
});