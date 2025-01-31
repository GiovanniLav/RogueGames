document.getElementById("registrationForm").addEventListener("submit", function(event) {
    var password = document.getElementById("password").value;
    var passwordError = document.getElementById("passwordError");

    // Regex per validare la password: almeno 8 caratteri, una maiuscola, un numero e un carattere speciale
    var passwordPattern = /^(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_+=-])[A-Za-z\d!@#$%^&*()_+=-]{8,}$/;

    if (!passwordPattern.test(password)) {
    event.preventDefault();  // Impedisce l'invio del form
    passwordError.textContent = "La password deve contenere almeno 8 caratteri, una lettera maiuscola, un numero e un carattere speciale.";
} else {
    passwordError.textContent = ""; // Rimuove eventuali messaggi di errore precedenti
}
});
