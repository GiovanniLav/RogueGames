package com.roguegames.domain.service;

import com.roguegames.domain.entity.CartaDiCredito;
import com.roguegames.domain.entity.IndirizzoSpedizione;
import com.roguegames.domain.entity.IndirizzoSpedizioneId;
import com.roguegames.domain.entity.Utente;
import com.roguegames.domain.repository.CartaDiCreditoRepository;
import com.roguegames.domain.repository.IndirizzoSpedizioneRepository;
import com.roguegames.domain.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.security.MessageDigest;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.security.NoSuchAlgorithmException;

@Service
@Validated
public class UtenteServiceImp implements UtenteService {

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private IndirizzoSpedizioneRepository indirizzoSpedizioneRepository;

    // Metodo per registrare un nuovo utente
    @Override
    public Utente registrati(@Valid Utente utente) {
        // Esegui tutti i controlli necessari usando metodi privati
        validateEmail(utente.getEmail());
        validatePassword(utente.getPassword());
        validateTelefono(utente.getTel());
        validateEta(utente.getEta());
        validateNomeCognome(utente.getNome(), utente.getCognome());

        // Verifica che l'email non sia già registrata
        Optional<Utente> existingUser = Optional.ofNullable(utenteRepository.findByEmail(utente.getEmail()));
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("L'email è già registrata");
        }

        // Se tutti i controlli sono passati, cripta la password e salva l'utente
        utente.setPassword(hashPassword(utente.getPassword()));
        return utenteRepository.save(utente);
    }

    // Metodo privato per validare l'email
    private void validateEmail(String email) {
        if (email == null || !email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("L'email non è nel formato corretto");
        }
    }

    // Metodo privato per validare la password
    private void validatePassword(String password) {
        if (password == null || password.length() < 8 || !password.matches(".*[A-Z].*")
                || !password.matches(".*[0-9].*") || !password.matches(".*[!@#$%^&*()].*")) {
            throw new IllegalArgumentException("La password deve essere lunga almeno 8 caratteri, contenere almeno una maiuscola, un numero e un carattere speciale");
        }
    }

    // Metodo privato per validare il telefono
    private void validateTelefono(String telefono) {
        if (telefono == null || telefono.length() != 10 || !telefono.matches("[0-9]+")) {
            throw new IllegalArgumentException("Il numero di telefono deve contenere 10 cifre numeriche");
        }
    }

    // Metodo privato per validare l'età
    private void validateEta(int eta) {
        if (eta < 10) {
            throw new IllegalArgumentException("L'età deve essere maggiore di 10");
        }
    }

    // Metodo privato per validare nome e cognome
    private void validateNomeCognome(String nome, String cognome) {
        if (nome == null || nome.length() < 2 || nome.length() > 45 || cognome == null || cognome.length() < 2 || cognome.length() > 45) {
            throw new IllegalArgumentException("Il nome e il cognome devono essere compresi tra 2 e 45 caratteri");
        }
    }

    @Override
    public Utente getUtenteByEmail(@NotNull String email) {
        return utenteRepository.findByEmail(email);
    }

    @Override
    public Utente verificaCredenziali(String email, String password) {
        Utente utente = utenteRepository.findByEmail(email);
        if (utente != null ) { // Assicurati di aggiungere hashing in futuro
            if(utente.getPassword().equals(password)) {
                return utente;
            }else{
                throw new IllegalArgumentException("Password errata");
            }
        }
        else{
            throw new IllegalArgumentException("Email non esiste");
        }
    }

    @Override
    public void aggiornaUtente(Utente utente, String campo, String nuovoValore) {
        switch (campo.toLowerCase()) {
            case "email":
                validateEmail(nuovoValore);
                Optional<Utente> existingUser = Optional.ofNullable(utenteRepository.findByEmail(nuovoValore));
                if (existingUser.isPresent() && !existingUser.get().getEmail().equals(utente.getEmail())) {
                    throw new IllegalArgumentException("L'email è già registrata");
                }
                utente.setEmail(nuovoValore);
                break;
            case "nome":
                validateNomeCognome(nuovoValore, utente.getCognome());
                utente.setNome(nuovoValore);
                break;
            case "cognome":
                validateNomeCognome(utente.getNome(), nuovoValore);
                utente.setCognome(nuovoValore);
                break;
            case "eta":
                int eta = Integer.parseInt(nuovoValore);
                validateEta(eta);
                utente.setEta(eta);
                break;
            case "telefono":
                validateTelefono(nuovoValore);
                utente.setTel(nuovoValore);
                break;
            case "residenza":
                if (nuovoValore == null || nuovoValore.length() < 5) {
                    throw new IllegalArgumentException("La residenza deve contenere almeno 5 caratteri");
                }
                utente.setResidenza(nuovoValore);
                break;
            case "password":
                validatePassword(nuovoValore);
                utente.setPassword(hashPassword(nuovoValore));
                break;
            default:
                throw new IllegalArgumentException("Campo non valido");
        }
        utenteRepository.save(utente);
    }


    @Override
    public boolean isDatabaseConnected() {
        try {
            utenteRepository.testConnection();
            return true;
        } catch (Exception e) {
            return false;
        }

        // Altri metodi di business possono essere aggiunti qui
    }

    public String hashPassword(String password) {
        try {
            // Crea un'istanza di MessageDigest con l'algoritmo SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Applica l'algoritmo alla password convertita in bytes
            byte[] encodedHash = digest.digest(password.getBytes());

            // Converte il risultato in una stringa esadecimale
            return bytesToHex(encodedHash);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Errore durante l'hashing della password", e);
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0'); // Aggiunge uno zero per formattare correttamente i valori
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
    public static class IndirizzoNonDisponibile extends RuntimeException {
        public IndirizzoNonDisponibile(String message) {
            super(message);
        }
    }

    @Override
    public IndirizzoSpedizione aggiungiIndirizzoSpd(String provincia, Integer cap, String via, String civico, String citta, Utente utente){
        IndirizzoSpedizioneId id = new IndirizzoSpedizioneId(provincia, cap, via, civico, citta, utente.getEmail());
        Optional<IndirizzoSpedizione> i = getIndirizzoSpedizioneById(id);
        if(i.isPresent()){
            throw new IndirizzoNonDisponibile("L'indirizzo inserito è già stato salvato");
        }
        IndirizzoSpedizione is = new IndirizzoSpedizione(id ,utente);
        return indirizzoSpedizioneRepository.save(is);
    }

    @Override
    public IndirizzoSpedizione modificaIndirizzoSpd(String provincia, Integer cap, String via, String civico, String citta, Utente utente, IndirizzoSpedizione is){
        IndirizzoSpedizioneId id = new IndirizzoSpedizioneId(provincia, cap, via, civico, citta, utente.getEmail());
        Optional<IndirizzoSpedizione> i = getIndirizzoSpedizioneById(id);
        if(i.isPresent()){
            throw new IndirizzoNonDisponibile("L'indirizzo inserito è già stato salvato");
        }
        IndirizzoSpedizione is1 = new IndirizzoSpedizione(id ,utente);
        indirizzoSpedizioneRepository.delete(is);
        return indirizzoSpedizioneRepository.save(is1);
    }

    @Override
    public List<IndirizzoSpedizione> getIndirizzoSpedizioni(Utente utente) {
        return indirizzoSpedizioneRepository.findByUtenteEmail(utente.getEmail());
    }

    @Override
    public Optional<IndirizzoSpedizione> getIndirizzoSpedizioneById(IndirizzoSpedizioneId id) {
        return indirizzoSpedizioneRepository.findById(id);
    }

    @Override
    public void cancellaIndirizzoSpedizione(Utente utente, IndirizzoSpedizione indirizzoSpedizione) {
        indirizzoSpedizioneRepository.delete(indirizzoSpedizione);
    }

    @Autowired
    private CartaDiCreditoRepository cartaDiCreditoRepository;

    // Salva una carta di credito con validazioni
    @Override
    public void saveCarta(CartaDiCredito carta) {
        if (isValidCif(carta.getCif()) && isValidScadenza(carta.getScadenza()) && isValidCvv(carta.getCvv())) {
            cartaDiCreditoRepository.save(carta);
        } else {
            throw new IllegalArgumentException("Dati della carta non validi");
        }
    }

    // Ottieni le carte di credito di un utente
    @Override
    public List<CartaDiCredito> getCarteByUtente(Utente utente) {
        return cartaDiCreditoRepository.findByUtente(utente);
    }

    // Ottieni una carta di credito tramite il CIF
    @Override
    public CartaDiCredito getCartaByCif(String cif) {
        return cartaDiCreditoRepository.findById(cif).orElse(null);
    }

    // Elimina una carta di credito
    @Override
    public void deleteCarta(CartaDiCredito carta) {
        cartaDiCreditoRepository.delete(carta);
    }

    // Controlla se il CIF è valido (16 cifre numeriche)
    private boolean isValidCif(String cif) {
        return cif != null && cif.matches("\\d{16}");
    }

    // Controlla se la scadenza è valida (MM/YY e non passata)
    private boolean isValidScadenza(String scadenza) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
            YearMonth expiryDate = YearMonth.parse(scadenza, formatter);
            return !expiryDate.isBefore(YearMonth.now());
        } catch (Exception e) {
            return false;
        }
    }

    // Controlla se il CVV è valido (3 cifre numeriche)
    private boolean isValidCvv(String cvv) {
        return cvv != null && cvv.matches("\\d{3}");
    }
}

