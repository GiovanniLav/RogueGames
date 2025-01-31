$(document).ready(function() {
    $('.processa-ordine').off('click').on('click', function (e) {
        e.preventDefault();

        var form = $(this).closest('form');
        const $button = $(this);
        var totale = $(this).data('totale'); // Assumiamo che il nome del prodotto sia memorizzato in un attributo data
        var url = "/aggiungiOrdine"; // Sostituisce {nome} con il valore reale

        $.ajax({
            url: url,
            type: "POST",
            data: { totale: totale },
            // ... altri parametri
            success: function (response) {
                Swal.fire({
                    title: 'Ordine Confermato!',
                    text: response,
                    icon: 'success'
                }).then((result) => {
                    if (result.isConfirmed) {
                            window.location.href = "/catalogo/prodotti";
                    }
                });
            },
            error: function (jqXHR, textStatus, errorThrown, xhr) {
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: 'Si è verificato un errore durante l\'elaborazione dell\'ordine.'
                });
                 if (xhr.status === 400) {
                     provaAlert("Errore: Devi loggarti per poter aggiungere prodotti al carrello");
                     setTimeout(() => {
                         window.location.href = "/utenti/home"; // Ricarica la pagina dopo 3 secondi
                     }, 2000);
                }else if (xhr.status === 401) {
            provaAlert("Errore: Il gestore non può acquistare. Registrati con l'account cliente");
                     provaAlert("Errore: Devi loggarti per poter aggiungere prodotti al carrello");
                     setTimeout(() => {
                         window.location.href = "/utenti/login"; // Ricarica la pagina dopo 3 secondi
                     }, 2000);
        }
            }
        })
    })
})

$(document).ready(function() {

    $('.aggiungi-carta').off('click').on('click', function (e) {
        e.preventDefault();

        var cif = $("input[name='cif']").val();
        var scadenza = $("input[name='scadenza']").val();
        var cvv = $("input[name='cvv']").val();
        var url = "/carte/salvaCartaOrdine"

        console.log(cif);
        console.log(scadenza);
        console.log(cvv);

        $.ajax({
            url: url,
            type: "POST",
            data: {
                cif: cif,
                scadenza: scadenza,
                cvv: cvv,
            },
            success: function(response) {
                provaAlert("La carta è stata aggiunta correttamente");

                $.ajax({
                    url: "/carte/aggiornaCarte",
                    type: "GET",
                    success: function (response) {
                        console.log("Risposta AJAX:", response); // <--- Aggiunto per debug

                        $('#cartacredito').empty();
                        $('#cartacredito').append('<option value="">Seleziona la carta</option>');

                        if (response && response.carte) { // Assicurati che l'oggetto contenga le carte
                            $.each(response.carte, function (index, carta) {
                                console.log("Aggiungo carta:", carta); // <--- Debug per vedere i dati
                                $('#cartacredito').append(`<option value="${carta.cif}">${carta.cif} ${carta.scadenza}</option>`);
                            });
                        } else {
                            console.error("Errore: La risposta non contiene carte valide!");
                        }
                    },
                    error: function (xhr, status, error) {
                        console.error("Errore AJAX:", status, error);
                    }
                });
            },
            error: function(xhr, status, error) {
                if (xhr.status === 401) {
                    provaAlert("Errore: Devi loggarti per poter inserire la carta");
                } else {
                    alert("Errore durante la richiesta.");
                }
            }
        });
    });
})

$(document).ready(function() {
    $('.aggiungi-indirizzo').off('click').on('click', function (e) {
        e.preventDefault();

        var provincia = $("input[name='provincia']").val();
        var cap = $("input[name='cap']").val();
        var via = $("input[name='via']").val();
        var civico = $("input[name='civico']").val();
        var citta = $("input[name='citta']").val();
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
            success: function(response) {
                provaAlert("L'indirizzo è stato aggiunto correttamente");
                closeForm("myFormIndirizzo");
                // Aggiunge il nuovo indirizzo direttamente alla select

                // Aggiorna gli indirizzi con AJAX per sincronizzare con il backend
                aggiornaListaIndirizzi();
            },
            error: function(xhr, status, error) {
                if (xhr.status === 401) {
                    alert("Errore: Devi loggarti per poter aggiungere un indirizzo");
                } else if (xhr.responseJSON && xhr.responseJSON.message) {
                    alert("Errore: " + xhr.responseJSON.message);
                } else if (xhr.responseText) {
                    alert("Errore: " + xhr.responseText);
                } else {
                    alert("Errore durante la richiesta.");
                }
            }
        });
    });

    // Funzione per aggiornare la lista degli indirizzi dal backend
    function aggiornaListaIndirizzi() {
        $.ajax({
            url: "/aggiornaIndirizzi",
            type: "GET",
            success: function(response) {
                $('#indirizzo').empty();
                $('#indirizzo').append('<option value="">Seleziona indirizzo</option>');

                if (response && response.indirizzi) {
                    $.each(response.indirizzi, function(index, is) {
                        let option = `<option value="${is.id.provincia}-${indirizzo.id.cap}-${is.id.via}-${is.id.civico}-${is.id.citta}">
                            ${is.id.provincia} ${is.id.cap} ${is.id.via} ${is.id.civico} ${is.id.citta}
                        </option>`;
                        $('#indirizzo').append(option);
                    });
                }
            },
            error: function(xhr, status, error) {
                console.error("Errore nel recupero degli indirizzi:", error);
            }
        });
    }
});


function openForm(formId) {
    const formElement = document.getElementById(formId);
    if (formElement) {
        formElement.style.display = "block";
    } else {
        console.error("Form element with ID", formId, "not found");
    }
}

function closeForm(formId) {
    const formElement = document.getElementById(formId);
    if (formElement) {
        formElement.style.display = "none";
    } else {
        console.error("Form element with ID", formId, "not found");
    }
}

$(document).ready(function() {
    $('#indirizzo, #cartacredito').change(function() {
        const indirizzoValue = $('#indirizzo').val();
        const cartaCreditoValue = $('#cartacredito').val();
        $('.processa-ordine').prop('disabled', indirizzoValue === '' || cartaCreditoValue === '');
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
                }, 2000);
            },
            error: function(xhr, status, error) {
                location.reload();
                if (xhr.status === 401) {
                    provaAlert("Errore: Devi loggarti per poter aggiungere prodotti al carrello");
                    setTimeout(() => {
                        window.location.href = "/utenti/login"; // Ricarica la pagina dopo 3 secondi
                    }, 2000);
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