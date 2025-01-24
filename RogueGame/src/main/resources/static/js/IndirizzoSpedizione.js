$(document).ready(function() {

    $('.aggiungi-indirizzo').off('click').on('click', function (e) {
        e.preventDefault();

        var provincia = $("input[name='provincia']").val();
        var cap = $("input[name='cap']").val();
        var via = $("input[name='via']").val();
        var civico = $("input[name='civico']").val();
        var citta = $("input[name='citta']").val(); // Assumiamo che il nome del prodotto sia memorizzato in un attributo data
        var url = "/aggiungiIndirizzoSpedizione" // Sostituisce {nome} con il valore reale


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
                // Gestisci la risposta del server (ad esempio, visualizza un messaggio di successo)
                console.log(response);
            },
            error: function (error) {
                // Gestisci gli errori (ad esempio, visualizza un messaggio di errore)
                console.error(error);
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

        console.log(provincia);
        console.log(via);
        console.log(cap);
        console.log(civico);
        console.log(citta);


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
                // Gestisci la risposta del server (ad esempio, visualizza un messaggio di successo)
                console.log(response);
            },
            error: function (error) {
                // Gestisci gli errori (ad esempio, visualizza un messaggio di errore)
                console.error(error);
            }
        });
    });
})