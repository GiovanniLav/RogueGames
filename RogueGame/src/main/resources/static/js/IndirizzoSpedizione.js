$(document).ready(function() {

    function validateForm(provincia, cap, via, civico, citta) {
        // Validazione della provincia (2 lettere maiuscole)
        var provinciaPattern = /^[A-Z]{2}$/;
        if (!provinciaPattern.test(provincia)) {
            provaAlert("Provincia deve contenere solo 2 lettere maiuscole.");
            return false;
        }

        // Validazione del CAP (5 cifre)
        var capPattern = /^\d{5}$/;
        if (!capPattern.test(cap)) {
            provaAlert("Il CAP deve essere composto da 5 cifre.");
            return false;
        }

        // Validazione della via (solo lettere e spazi, min 7, max 45)
        var viaPattern = /^[A-Za-z\s]{7,45}$/;
        if (!viaPattern.test(via)) {
            provaAlert("La via deve essere composta da lettere e spazi, con lunghezza tra 7 e 45 caratteri.");
            return false;
        }

        // Validazione del civico (numeri, lettere e spazi, min 1, max 7)
        var civicoPattern = /^[A-Za-z0-9\s]{1,7}$/;
        if (!civicoPattern.test(civico)) {
            provaAlert("Il civico deve essere una combinazione di numeri, lettere e spazi, con lunghezza tra 1 e 7.");
            return false;
        }

        // Validazione della città (solo lettere e spazi, min 2, max 45)
        var cittaPattern = /^[A-Za-z\s]{2,45}$/;
        if (!cittaPattern.test(citta)) {
            provaAlert("La città deve essere composta solo da lettere e spazi, con lunghezza tra 2 e 45 caratteri.");
            return false;
        }

        return true; // Tutte le validazioni sono passate
    }

    $('.aggiungi-indirizzo').off('click').on('click', function (e) {
        e.preventDefault();

        var provincia = $("input[name='provincia']").val();
        var cap = $("input[name='cap']").val();
        var via = $("input[name='via']").val();
        var civico = $("input[name='civico']").val();
        var citta = $("input[name='citta']").val();

        if (validateForm(provincia, cap, via, civico, citta)) {
            var url = "/aggiungiIndirizzoSpedizione";

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
                    provaAlert("L'indirizzo è stato aggiunto correttamente");
                    setTimeout(() => {
                        window.location.href = "/indirizzo"; // Ricarica la pagina dopo 3 secondi
                    }, 1000);
                },
                error: function (xhr, status, error) {
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
        }
    });



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
            var civicomod = $("input[name='civico-mod']").val();
            var cittamod = $("input[name='citta-mod']").val();

            if (validateForm(provincia, cap, via, civico, citta)) {
                var url = "/modificaIndirizzoSpedizione";

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
                    success: function (response) {
                        provaAlert("L'indirizzo è stato modificato correttamente");

                        setTimeout(() => {
                            window.location.href = "/indirizzo"; // Ricarica la pagina dopo 3 secondi
                        }, 1000);
                    },
                    error: function (xhr, status, error) {
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
            }
        });
});
    $(document).ready(function () {

        $('.rimuovi-indirizzo').off('click').on('click', function (e) {
            e.preventDefault();


            var button = $(this);
            var form = button.closest('form');
            var provincia = form.find('input[name="provincia"]').val();
            var cap = $(this).data('cap');
            var via = form.find('input[name="via"]').val();
            var civico = form.find('input[name="civico"]').val();
            var citta = form.find('input[name="citta"]').val();
            var url = "/rimuoviIndirizzo";

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
    });
    $(document).ready(function () {
        $('.btn-modifica-in').on('click', function (e) {
            e.preventDefault();

            const form = $(this).closest('form');
            const url = "/modificaIndirizzo"


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


