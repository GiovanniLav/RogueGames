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
        // Aggiungere logica, ad esempio controllare se l'email esiste già
        Optional<Utente> existingUser = Optional.ofNullable(utenteRepository.findByEmail(utente.getEmail()));
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("L'email è già registrata");
        }
        utente.setPassword(hashPassword(utente.getPassword()));
        return utenteRepository.save(utente);
    }

    // Metodo per ottenere un utente tramite email
    public Utente getUtenteByEmail(@NotNull String email) {
        return utenteRepository.findByEmail(email);
    }

    public Utente verificaCredenziali(String email, String password) {
        Utente utente = utenteRepository.findByEmail(email);
        if (utente != null && utente.getPassword().equals(password)) { // Assicurati di aggiungere hashing in futuro
            return utente;
        }
        return null;
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

