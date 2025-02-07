package com.roguegames;

import com.roguegames.domain.entity.CartaDiCredito;
import com.roguegames.domain.entity.IndirizzoSpedizione;
import com.roguegames.domain.entity.Utente;
import com.roguegames.domain.repository.CartaDiCreditoRepository;
import com.roguegames.domain.repository.UtenteRepository;
import com.roguegames.domain.service.UtenteService;
import com.roguegames.domain.service.UtenteServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest  // Usa la configurazione di Spring Boot e connetti al database di test
class UtenteServiceIntegrationTest {

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private CartaDiCreditoRepository cartaDiCreditoRepository;

    @Autowired
    private UtenteService utenteService;

    private Utente utente;
    private CartaDiCredito cartaValida;
    private CartaDiCredito cartaNonValidaCif;
    private CartaDiCredito cartaNonValidaCVV;
    private CartaDiCredito cartaNonValidaScadenza;

    @BeforeEach
    void setUp() {
        // Creazione di un utente per i test
        utente = new Utente();
        utente.setEmail("lollo@lollo.lol");
        utente.setNome("Mario");
        utente.setCognome("Rossi");
        utente.setResidenza("Via mario 123");
        utente.setPassword("MARIA1234@");
        utente.setTel("1234567890");
        utente.setEta(25);

        cartaValida = new CartaDiCredito("1234567812345678", "12/30", "123", utente);
        cartaNonValidaCif = new CartaDiCredito("123456781234567", "12/30", "123", utente);
        cartaNonValidaCVV = new CartaDiCredito("1234567812345678", "12/30", "12", utente);
        cartaNonValidaScadenza = new CartaDiCredito("1234567812345678", "12/20", "123", utente);
    }

    @Test
    void registrati_Success() {
        // Crea un nuovo utente
        Utente nuovoUtente = new Utente();
        nuovoUtente.setEmail("newuser@example.com");
        nuovoUtente.setNome("Luigi");
        nuovoUtente.setCognome("Verdi");
        nuovoUtente.setPassword("Password123!");
        nuovoUtente.setResidenza("Via mario 123");
        nuovoUtente.setTel("0987654321");
        nuovoUtente.setEta(30);

        // Registra il nuovo utente
        Utente registrato = utenteService.registrati(nuovoUtente);

        // Verifica che l'utente sia stato correttamente registrato
        assertNotNull(registrato);
        assertEquals(nuovoUtente.getEmail(), registrato.getEmail());
    }

    @Test
    void registrati_EmailGiàEsistente_LancioEccezione() {
        // Tentiamo di registrare un utente con la stessa email già presente nel database
        Utente nuovoUtente = new Utente();
        nuovoUtente.setEmail("lollo@lollo.lol");
        nuovoUtente.setNome("Luigi");
        nuovoUtente.setCognome("Verdi");
        nuovoUtente.setPassword("Password123!");
        nuovoUtente.setResidenza("Via mario 123");
        nuovoUtente.setTel("0987654321");
        nuovoUtente.setEta(30);

        // Verifica che venga lanciata un'eccezione quando si cerca di registrare con una email duplicata
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            utenteService.registrati(nuovoUtente);
        });

        // Verifica che l'eccezione contenga il messaggio corretto
        assertEquals("L'email è già registrata", exception.getMessage());
    }

    @Test
    void verificaCredenziali_Success() {
        // Verifica le credenziali dell'utente creato nel setUp
        Utente verificato = utenteService.verificaCredenziali("lollo@lollo.lol", utenteService.hashPassword("lollolol"));
        assertNotNull(verificato);
        assertEquals(utente.getEmail(), verificato.getEmail());
    }

    @Test
    void aggiungiIndirizzoSpd_Success() {
        // Aggiungi un indirizzo di spedizione
        IndirizzoSpedizione indirizzo = utenteService.aggiungiIndirizzoSpd("RO", 10100, "Via Test", "10", "Roma", utente);

        // Verifica che l'indirizzo sia stato aggiunto correttamente
        assertNotNull(indirizzo);
        assertEquals("RO", indirizzo.getId().getProvincia());
        assertEquals(10100, indirizzo.getId().getCap());
    }

    @Test
    void aggiungiIndirizzoSpd_LancioEccezioneSeIndirizzoEsistente() {
        // Aggiungi un indirizzo di spedizione
        utenteService.aggiungiIndirizzoSpd("RI", 10101, "Via Test", "10", "Rimini", utente);

        // Verifica che venga lanciata un'eccezione se cerchi di aggiungere lo stesso indirizzo
        assertThrows(UtenteServiceImp.IndirizzoNonDisponibile.class, () -> {
            utenteService.aggiungiIndirizzoSpd("RI", 10101, "Via Test", "10", "Rimini", utente);
        });
    }

    @Test
    void modificaIndirizzoSpd_Success() {
        // Aggiungi un indirizzo di spedizione
        IndirizzoSpedizione indirizzo = utenteService.aggiungiIndirizzoSpd("MI", 10100, "Via Test", "10", "Milano", utente);

        // Modifica l'indirizzo
        IndirizzoSpedizione indirizzoModificato = utenteService.modificaIndirizzoSpd("MI", 10100, "Via Test", "11", "Milano", utente, indirizzo);

        // Verifica che l'indirizzo sia stato modificato correttamente
        assertNotNull(indirizzoModificato);
        assertEquals("11", indirizzoModificato.getId().getCivico());
    }

    @Test
    void getIndirizziSpedizione_Success() {
        // Aggiungi un indirizzo di spedizione
        utenteService.aggiungiIndirizzoSpd("EI", 10100, "Via Test", "10", "Roma", utente);

        // Ottieni gli indirizzi di spedizione
        List<IndirizzoSpedizione> indirizzi = utenteService.getIndirizzoSpedizioni(utente);

        // Verifica che gli indirizzi siano stati recuperati correttamente
        assertNotNull(indirizzi);
        assertFalse(indirizzi.isEmpty());
    }

    @Test
    void cancellaIndirizzoSpedizione_Success() {
        // Aggiungi un indirizzo di spedizione
        IndirizzoSpedizione indirizzo = utenteService.aggiungiIndirizzoSpd("EU", 10100, "Via Test", "10", "Roma", utente);

        // Cancella l'indirizzo di spedizione
        utenteService.cancellaIndirizzoSpedizione(utente, indirizzo);

        // Verifica che l'indirizzo sia stato cancellato correttamente
        Optional<IndirizzoSpedizione> indirizzoCancellato = utenteService.getIndirizzoSpedizioneById(indirizzo.getId());
        assertFalse(indirizzoCancellato.isPresent());
    }

    @Test
    void saveCarta_ValidData_Success() {
        utenteService.saveCarta(cartaValida);

        CartaDiCredito savedCarta = cartaDiCreditoRepository.findById(cartaValida.getCif()).orElse(null);
        assertNotNull(savedCarta);
        assertEquals(cartaValida.getCif(), savedCarta.getCif());
    }

    @Test
    void saveCarta_NonValidCif_ThrowsException() {
        // Verifica che venga lanciata IllegalArgumentException per CIF non valido
        assertThrows(IllegalArgumentException.class, () -> utenteService.saveCarta(cartaNonValidaCif));
    }

    @Test
    void saveCarta_NonValidCVV_ThrowsException() {
        // Verifica che venga lanciata IllegalArgumentException per CVV non valido
        assertThrows(IllegalArgumentException.class, () -> utenteService.saveCarta(cartaNonValidaCVV));
    }

    @Test
    void saveCarta_NonValidScadenza_ThrowsException() {
        // Verifica che venga lanciata IllegalArgumentException per scadenza non valida
        assertThrows(IllegalArgumentException.class, () -> utenteService.saveCarta(cartaNonValidaScadenza));
    }


    @Test
    void getCarteByUtente_ReturnsCarte() {
        utenteService.saveCarta(cartaValida);

        List<CartaDiCredito> carte = utenteService.getCarteByUtente(utente);
        assertFalse(carte.isEmpty());
        assertTrue(carte.contains(cartaValida));
    }

    @Test
    void getCartaByCif_ExistingCif_ReturnsCarta() {
        utenteService.saveCarta(cartaValida);

        CartaDiCredito carta = utenteService.getCartaByCif(cartaValida.getCif());
        assertNotNull(carta);
        assertEquals(cartaValida.getCif(), carta.getCif());
    }

    @Test
    void deleteCarta_RemovesCarta() {
        utenteService.saveCarta(cartaValida);

        utenteService.deleteCarta(cartaValida);

        CartaDiCredito cartaEliminata = cartaDiCreditoRepository.findById(cartaValida.getCif()).orElse(null);
        assertNull(cartaEliminata);
    }

}
