package com.roguegames.domain.service;

import com.roguegames.domain.entity.IndirizzoSpedizione;
import com.roguegames.domain.entity.IndirizzoSpedizioneId;
import com.roguegames.domain.entity.Utente;
import com.roguegames.domain.repository.IndirizzoSpedizioneRepository;
import com.roguegames.domain.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.security.MessageDigest;
import java.util.List;
import java.util.Optional;
import java.security.NoSuchAlgorithmException;

@Service
@Validated
public class UtenteService {

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private IndirizzoSpedizioneRepository indirizzoSpedizioneRepository;

    // Metodo per registrare un nuovo utente
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

    // Metodo per criptare la password (se non hai già un'implementazione)



    // Metodo per ottenere un utente tramite email
    public Utente getUtenteByEmail(@NotNull String email) {
        return utenteRepository.findByEmail(email);
    }

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

    public void aggiornaUtente(Utente utente) {
        utenteRepository.save(utente);
    }

    public boolean isDatabaseConnected() {
        try {
            utenteRepository.testConnection();
            return true;
        } catch (Exception e) {
            return false;
        }

        // Altri metodi di business possono essere aggiunti qui
    }

    public static String hashPassword(String password) {
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

    public IndirizzoSpedizione aggiungiIndirizzoSpd(String provincia, Integer cap, String via, String civico, String citta, Utente utente){
        IndirizzoSpedizioneId id = new IndirizzoSpedizioneId(provincia, cap, via, civico, citta, utente.getEmail());
        Optional<IndirizzoSpedizione> i = getIndirizzoSpedizioneById(id);
        if(i.isPresent()){
            throw new IndirizzoNonDisponibile("L'indirizzo inserito è già stato salvato");
        }
        IndirizzoSpedizione is = new IndirizzoSpedizione(id ,utente);
        return indirizzoSpedizioneRepository.save(is);
    }

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

    public List<IndirizzoSpedizione> getIndirizzoSpedizioni(Utente utente) {
        return indirizzoSpedizioneRepository.findByUtenteEmail(utente.getEmail());
    }

    public Optional<IndirizzoSpedizione> getIndirizzoSpedizioneById(IndirizzoSpedizioneId id) {
        return indirizzoSpedizioneRepository.findById(id);
    }

    public void cancellaIndirizzoSpedizione(Utente utente, IndirizzoSpedizione indirizzoSpedizione) {
        indirizzoSpedizioneRepository.delete(indirizzoSpedizione);
    }

}

