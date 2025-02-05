package com.roguegames;

import com.roguegames.domain.entity.Preferiti;
import com.roguegames.domain.entity.PreferitiId;
import com.roguegames.domain.entity.Prodotto;
import com.roguegames.domain.entity.Utente;
import com.roguegames.domain.repository.PreferitiRepository;
import com.roguegames.domain.repository.ProdottoRepository;
import com.roguegames.domain.service.PreferitiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.roguegames.domain.entity.Prodotto.Piattaforma.PlayStation;
import static com.roguegames.domain.entity.Prodotto.Tipo.Videogiochi;
import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SpringBootTest  // Usa la configurazione di Spring Boot e connetti al database di test
class PreferitiServiceIntegrationTest {

    @Autowired
    private PreferitiRepository preferitiRepository;

    @Autowired
    private ProdottoRepository prodottoRepository;

    @Autowired
    private PreferitiService preferitiService;

    private Utente utente;
    private Prodotto prodotto;
    private String nomeProdotto;

    @BeforeEach
    void setUp() throws ParseException {
        // Creazione dell'utente
        utente = new Utente();
        utente.setEmail("lollo@lollo.lol");
        utente.setNome("Mario");
        utente.setCognome("Rossi");
        utente.setPassword("Password123!");
        utente.setTel("1234567890");
        utente.setEta(10);

        // Creazione della data di rilascio
        String dataRilascioStringa = "2002-09-22";
        SimpleDateFormat formatoData = new SimpleDateFormat("yyyy-MM-dd");
        Date dataRilascio = formatoData.parse(dataRilascioStringa);

        // Creazione del prodotto
        prodotto = new Prodotto("EldenRing", "test.png", "test.mp4", "descrizione prodotto test", true, 10.00, "Test Srl", PlayStation, "Fantasy", Videogiochi, dataRilascio, 80);
        prodottoRepository.save(prodotto);

        // Nome del prodotto da utilizzare nei test
        nomeProdotto = prodotto.getNome();
    }

    @Test
    void aggiungiPreferito_Success() {
        // Aggiungi il prodotto ai preferiti
        preferitiService.aggiungiPreferito(nomeProdotto, utente);

        // Verifica che il preferito sia stato aggiunto
        Preferiti preferito = preferitiRepository.findById(new PreferitiId(nomeProdotto, utente.getEmail())).orElse(null);
        assertNotNull(preferito);
        assertEquals(nomeProdotto, preferito.getId().getNome());
        assertEquals(utente.getEmail(), preferito.getUtente().getEmail());
    }

    @Test
    void rimuoviPreferito_Success() {
        // Aggiungi il prodotto ai preferiti
        preferitiService.aggiungiPreferito(nomeProdotto, utente);

        // Rimuovi il prodotto dai preferiti
        preferitiService.rimuoviPreferito(nomeProdotto, utente);

        // Verifica che il preferito sia stato rimosso
        Preferiti preferito = preferitiRepository.findById(new PreferitiId(nomeProdotto, utente.getEmail())).orElse(null);
        assertNull(preferito);
    }

    @Test
    void ePreferito_ReturnsTrueIfPresent() {
        // Aggiungi il prodotto ai preferiti
        preferitiService.aggiungiPreferito(nomeProdotto, utente);

        // Verifica che il prodotto sia tra i preferiti
        boolean isPreferito = preferitiService.ePreferito(nomeProdotto, utente);
        assertTrue(isPreferito);
    }


    @Test
    void trovaPerUtente_ReturnsPreferiti() {
        // Aggiungi il prodotto ai preferiti
        preferitiService.aggiungiPreferito(nomeProdotto, utente);

        // Verifica che i preferiti siano correttamente recuperati per l'utente
        List<Preferiti> preferiti = preferitiService.trovaPerUtente(utente);
        assertFalse(preferiti.isEmpty());
        assertTrue(preferiti.stream().anyMatch(p -> p.getId().getNome().equals(nomeProdotto)));
    }
}

