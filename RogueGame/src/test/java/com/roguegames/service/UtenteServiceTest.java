package com.roguegames.service;

import com.roguegames.domain.entity.IndirizzoSpedizione;
import com.roguegames.domain.entity.IndirizzoSpedizioneId;
import com.roguegames.domain.entity.Utente;
import com.roguegames.domain.repository.IndirizzoSpedizioneRepository;
import com.roguegames.domain.repository.UtenteRepository;
import com.roguegames.domain.service.UtenteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UtenteServiceTest {

    @Mock
    private UtenteRepository utenteRepository;

    @Mock
    private IndirizzoSpedizioneRepository indirizzoSpedizioneRepository;

    @InjectMocks
    private UtenteService utenteService;

    private Utente utente;
    private IndirizzoSpedizione indirizzoSpedizione;

    @BeforeEach
    void setUp() {
        // Crea un esempio di utente
        utente = new Utente();
        utente.setEmail("mariamarzano@gmail.com");
        utente.setNome("Mario");
        utente.setCognome("Rossi");
        utente.setPassword("Password123!");
        utente.setTel("1234567890");
        utente.setEta(10);

        // Crea un esempio di indirizzo di spedizione
        indirizzoSpedizione = new IndirizzoSpedizione();
        indirizzoSpedizione.setId(new IndirizzoSpedizioneId("MI", 20100, "Via Roma", "10", "Milano", utente.getEmail()));
        indirizzoSpedizione.setUtente(utente);
    }

    @Test
    void testRegistrati_UtenteNuovo() {
        // Mock l'assenza dell'utente nel repository
        when(utenteRepository.findByEmail(utente.getEmail())).thenReturn(null);
        when(utenteRepository.save(utente)).thenReturn(utente);

        // Esegui la registrazione
        Utente result = utenteService.registrati(utente);

        // Verifica che il risultato non sia null
        assertNotNull(result);

        // Verifica che l'utente restituito abbia gli stessi dati
        assertEquals(utente.getEmail(), result.getEmail());
        assertEquals(utente.getNome(), result.getNome());
        assertEquals(utente.getCognome(), result.getCognome());

        // Verifica che il metodo save sia stato chiamato
        verify(utenteRepository, times(1)).save(utente);
    }

    @Test
    void testRegistrati_EmailGiaRegistrata() {
        // Mock l'utente già presente nel repository
        when(utenteRepository.findByEmail(utente.getEmail())).thenReturn(utente);

        // Verifica che venga lanciata l'eccezione
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            utenteService.registrati(utente);
        });

        // Verifica che il messaggio dell'eccezione sia corretto
        assertEquals("L'email è già registrata", thrown.getMessage());
    }

    @Test
    void testAggiungiIndirizzoSpd() {
        // Mock che l'indirizzo non sia già presente nel repository
        IndirizzoSpedizioneId id = indirizzoSpedizione.getId();
        when(indirizzoSpedizioneRepository.findById(id)).thenReturn(Optional.empty());
        when(indirizzoSpedizioneRepository.save(indirizzoSpedizione)).thenReturn(indirizzoSpedizione);

        // Aggiungi l'indirizzo
        IndirizzoSpedizione result = utenteService.aggiungiIndirizzoSpd("MI", 20100, "Via Roma", "10", "Milano", utente);

        // Verifica che l'indirizzo sia stato aggiunto correttamente
        assertNotNull(result);
        assertEquals(indirizzoSpedizione, result);

        // Verifica che il metodo save sia stato chiamato
        verify(indirizzoSpedizioneRepository, times(1)).save(indirizzoSpedizione);
    }

    @Test
    void testAggiungiIndirizzoSpd_IndirizzoEsistente() {
        // Mock che l'indirizzo sia già presente nel repository
        IndirizzoSpedizioneId id = indirizzoSpedizione.getId();
        when(indirizzoSpedizioneRepository.findById(id)).thenReturn(Optional.of(indirizzoSpedizione));

        // Verifica che venga lanciata l'eccezione
        UtenteService.IndirizzoNonDisponibile thrown = assertThrows(UtenteService.IndirizzoNonDisponibile.class, () -> {
            utenteService.aggiungiIndirizzoSpd("MI", 20100, "Via Roma", "10", "Milano", utente);
        });

        // Verifica che il messaggio dell'eccezione sia corretto
        assertEquals("L'indirizzo inserito è già stato salvato", thrown.getMessage());
    }


    @Test
    void testModificaIndirizzoSpd() {
        // Mock che l'indirizzo precedente esista e che l'indirizzo modificato non esista
        IndirizzoSpedizioneId id = indirizzoSpedizione.getId();
        when(indirizzoSpedizioneRepository.findById(id)).thenReturn(Optional.empty());
        when(indirizzoSpedizioneRepository.save(indirizzoSpedizione)).thenReturn(indirizzoSpedizione);

        // Modifica l'indirizzo
        IndirizzoSpedizione result = utenteService.modificaIndirizzoSpd("MI", 20100, "Via Roma", "10", "Milano", utente, indirizzoSpedizione);

        // Verifica che l'indirizzo sia stato modificato correttamente
        assertNotNull(result);
        assertEquals(indirizzoSpedizione, result);

        // Verifica che il metodo delete e save siano stati chiamati
        verify(indirizzoSpedizioneRepository, times(1)).delete(indirizzoSpedizione);
        verify(indirizzoSpedizioneRepository, times(1)).save(indirizzoSpedizione);
    }

    @Test
    void testCancellaIndirizzoSpedizione() {
        // Cancella l'indirizzo senza mockare findById, se non è necessario
        utenteService.cancellaIndirizzoSpedizione(utente, indirizzoSpedizione);

        // Verifica che il metodo delete sia stato chiamato
        verify(indirizzoSpedizioneRepository, times(1)).delete(indirizzoSpedizione);
    }


    @Test
    void testGetIndirizziSpedizione() {
        // Mock il ritorno di una lista di indirizzi
        when(indirizzoSpedizioneRepository.findByUtenteEmail(utente.getEmail()))
                .thenReturn(List.of(indirizzoSpedizione));

        // Verifica che la lista di indirizzi venga restituita correttamente
        List<IndirizzoSpedizione> result = utenteService.getIndirizzoSpedizioni(utente);

        // Verifica che la lista non sia vuota e contenga l'indirizzo
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(indirizzoSpedizione, result.get(0));
    }

    @Test
    void testLoginVerificaCredenziali_Valide() {
        // Mock l'utente esistente nel repository
        when(utenteRepository.findByEmail(utente.getEmail())).thenReturn(utente);

        // Verifica che la funzione restituisca l'utente quando le credenziali sono corrette
        Utente result = utenteService.verificaCredenziali(utente.getEmail(), utente.getPassword());
        assertNotNull(result);
        assertEquals(utente, result);
    }

    @Test
    void testVerificaCredenziali_InvalidePass() {
        // Mock l'utente esistente nel repository
        when(utenteRepository.findByEmail(utente.getEmail())).thenReturn(utente);

        // Verifica che la funzione restituisca null quando le credenziali sono errate
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            utenteService.verificaCredenziali(utente.getEmail(), "WrongPassword");
        });

        // Verifica che il messaggio dell'eccezione sia corretto
        assertEquals("Password errata", exception.getMessage());
    }

    @Test
    void testVerificaCredenziali_EmailNonEsiste() {
        // Mock che l'email non esiste nel repository

        when(utenteRepository.findByEmail(utente.getEmail())).thenReturn(null);

        // Verifica che venga lanciata un'eccezione quando l'email non esiste
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            utenteService.verificaCredenziali(utente.getEmail(), "anyPassword");
        });

        // Verifica che il messaggio dell'eccezione sia corretto
        assertEquals("Email non esiste", exception.getMessage());
    }
}
