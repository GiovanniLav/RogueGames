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
                provaAlert("L'indirizzo è stato aggiunto correttamente");
                window.location.href = "/indirizzo";
            },
            error: function(xhr, status, error) {
                if (xhr.status === 401) {
                    provaAlert("Errore: Devi loggarti per poter aggiungere prodotti al carrello");
                } else if (xhr.responseJSON && xhr.responseJSON.message) {
                    provaAlert("Errore: " + xhr.responseJSON.message); // Gestisce errori con messaggi JSON
                } else if (xhr.responseText) {
                    provaAlert("Errore: " + xhr.responseText); // Gestisce errori con testo semplice
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
                provaAlert("L'indirizzo è stato modifcato correttamente");
                window.location.href = "/indirizzo";
            },
            error: function(xhr, status, error) {
                if (xhr.status === 401) {
                    provaAlert("Errore: Devi loggarti per poter aggiungere prodotti al carrello");
                } else if (xhr.responseJSON && xhr.responseJSON.message) {
                    provaAlert("Errore: " + xhr.responseJSON.message); // Gestisce errori con messaggi JSON
                } else if (xhr.responseText) {
                    provaAlert("Errore: " + xhr.responseText); // Gestisce errori con testo semplice
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


        var button = $(this);
        var form = button.closest('form');
        var provincia = form.find('input[name="provincia"]').val();
        var cap = $(this).data('cap');
        var via = form.find('input[name="via"]').val();
        var civico = form.find('input[name="civico"]').val();
        var citta = form.find('input[name="citta"]').val();
        var url = "/rimuoviIndirizzo" // Sostituisce {nome} con il valore reale

        console.log(provincia)
        console.log(via)
        console.log(cap)
        console.log(civico)
        console.log(citta)

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
                form.closest('.indirizzi-item').remove();
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
                        provaAlert(response.message || 'Errore sconosciuto');
                    }
                }
            },
            error: function (xhr, status, error) {
                console.error('Errore durante la richiesta AJAX:', error);
            }
        });
    });
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