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
