package com.roguegames.domain.service;

import com.roguegames.domain.entity.Utente;
import com.roguegames.domain.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Service
@Validated
public class UtenteService {

    @Autowired
    private UtenteRepository utenteRepository;

    // Metodo per registrare un nuovo utente
    public Utente registrati(@Valid Utente utente) {
        // Aggiungere logica, ad esempio controllare se l'email esiste già
        Optional<Utente> existingUser = Optional.ofNullable(utenteRepository.findByEmail(utente.getEmail()));
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("L'email è già registrata");
        }
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

    public boolean isDatabaseConnected() {
        try {
            utenteRepository.testConnection();
            return true;
        } catch (Exception e) {
            return false;
        }

        // Altri metodi di business possono essere aggiunti qui
    }
}