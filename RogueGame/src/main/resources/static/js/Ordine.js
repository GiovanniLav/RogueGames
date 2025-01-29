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
            error: function (jqXHR, textStatus, errorThrown) {
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: 'Si è verificato un errore durante l\'elaborazione dell\'ordine.'
                });
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
                alert("La carta è stata aggiunta correttamente");

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
                    alert("Errore: Devi loggarti per poter inserire la carta");
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
                alert("L'indirizzo è stato aggiunto correttamente");
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

