package com.roguegames;

import com.roguegames.domain.entity.CartaDiCredito;
import com.roguegames.domain.entity.IndirizzoSpedizione;
import com.roguegames.domain.entity.IndirizzoSpedizioneId;
import com.roguegames.domain.entity.Utente;
import com.roguegames.domain.repository.CartaDiCreditoRepository;
import com.roguegames.domain.repository.IndirizzoSpedizioneRepository;
import com.roguegames.domain.repository.UtenteRepository;
import com.roguegames.domain.service.UtenteService;
import com.roguegames.domain.service.UtenteServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UtenteServiceTest {

    @Mock
    private UtenteRepository utenteRepository;

    @Mock
    private IndirizzoSpedizioneRepository indirizzoSpedizioneRepository;

    @InjectMocks
    private UtenteService utenteService = new UtenteServiceImp();

    private Utente utente;
    private IndirizzoSpedizione indirizzoSpedizione;

    @Mock
    private CartaDiCreditoRepository cartaDiCreditoRepository;


    @InjectMocks

    private CartaDiCredito cartaValida;
    private CartaDiCredito cartaNonValida;


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

        cartaValida = new CartaDiCredito("1234567812345678", "12/30", "123", utente);
        cartaNonValida = new CartaDiCredito("1234", "01/20", "12", utente);
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
        UtenteServiceImp.IndirizzoNonDisponibile thrown = assertThrows(UtenteServiceImp.IndirizzoNonDisponibile.class, () -> {
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

    @Test
    void saveCarta_ValidData_Success() {
        when(cartaDiCreditoRepository.save(cartaValida)).thenReturn(cartaValida);
        assertDoesNotThrow(() -> utenteService.saveCarta(cartaValida));
        verify(cartaDiCreditoRepository, times(1)).save(cartaValida);
    }

    @Test
    void saveCarta_InvalidCif_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> utenteService.saveCarta(cartaNonValida));
        assertEquals("Dati della carta non validi", exception.getMessage());
    }

    @Test
    void saveCarta_InvalidCVV_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> utenteService.saveCarta(cartaNonValida));
        assertEquals("Dati della carta non validi", exception.getMessage());
    }

    @Test
    void saveCarta_InvalidScadenza_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> utenteService.saveCarta(cartaNonValida));
        assertEquals("Dati della carta non validi", exception.getMessage());
    }

    @Test
    void getCarteByUtente_ReturnsList() {
        Utente utente = new Utente();
        when(cartaDiCreditoRepository.findByUtente(utente)).thenReturn(Collections.singletonList(cartaValida));
        List<CartaDiCredito> carte = utenteService.getCarteByUtente(utente);
        assertFalse(carte.isEmpty());
        assertEquals(1, carte.size());
    }

    @Test
    void getCartaByCif_ExistingCif_ReturnsCarta() {
        when(cartaDiCreditoRepository.findById("1234567812345678")).thenReturn(Optional.of(cartaValida));
        CartaDiCredito carta = utenteService.getCartaByCif("1234567812345678");
        assertNotNull(carta);
    }

    @Test
    void getCartaByCif_NonExistingCif_ReturnsNull() {
        when(cartaDiCreditoRepository.findById("0000000000000000")).thenReturn(Optional.empty());
        CartaDiCredito carta = utenteService.getCartaByCif("0000000000000000");
        assertNull(carta);
    }

    @Test
    void deleteCarta_RemovesCarta() {
        doNothing().when(cartaDiCreditoRepository).delete(cartaValida);
        assertDoesNotThrow(() -> utenteService.deleteCarta(cartaValida));
        verify(cartaDiCreditoRepository, times(1)).delete(cartaValida);
    }
}
