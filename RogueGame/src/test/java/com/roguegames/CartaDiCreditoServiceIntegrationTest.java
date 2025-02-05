package com.roguegames;

import com.roguegames.domain.entity.CartaDiCredito;
import com.roguegames.domain.entity.Utente;
import com.roguegames.domain.repository.CartaDiCreditoRepository;
import com.roguegames.domain.service.CartaDiCreditoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@SpringBootTest  // Usa la configurazione di Spring Boot e connetti al database di test
class CartaDiCreditoServiceIntegrationTest {

    @Autowired
    private CartaDiCreditoRepository cartaDiCreditoRepository;

    @Autowired
    private CartaDiCreditoService cartaDiCreditoService;

    private Utente utente;
    private CartaDiCredito cartaValida;
    private CartaDiCredito cartaNonValidaCif;
    private CartaDiCredito cartaNonValidaCVV;
    private CartaDiCredito cartaNonValidaScadenza;

    @BeforeEach
    void setUp() {
        utente = new Utente();
        utente.setEmail("lollo@lollo.lol");
        utente.setNome("Mario");
        utente.setCognome("Rossi");
        utente.setPassword("Password123!");
        utente.setTel("1234567890");
        utente.setEta(10);

        cartaValida = new CartaDiCredito("1234567812345678", "12/30", "123", utente);
        cartaNonValidaCif = new CartaDiCredito("123456781234567", "12/30", "123", utente);
        cartaNonValidaCVV = new CartaDiCredito("1234567812345678", "12/30", "12", utente);
        cartaNonValidaScadenza = new CartaDiCredito("1234567812345678", "12/20", "123", utente);
    }

    @Test
    void saveCarta_ValidData_Success() {
        cartaDiCreditoService.saveCarta(cartaValida);

        CartaDiCredito savedCarta = cartaDiCreditoRepository.findById(cartaValida.getCif()).orElse(null);
        assertNotNull(savedCarta);
        assertEquals(cartaValida.getCif(), savedCarta.getCif());
    }

    @Test
    void saveCarta_NonValidCif_ThrowsException() {
        // Verifica che venga lanciata IllegalArgumentException per CIF non valido
        assertThrows(IllegalArgumentException.class, () -> cartaDiCreditoService.saveCarta(cartaNonValidaCif));
    }

    @Test
    void saveCarta_NonValidCVV_ThrowsException() {
        // Verifica che venga lanciata IllegalArgumentException per CVV non valido
        assertThrows(IllegalArgumentException.class, () -> cartaDiCreditoService.saveCarta(cartaNonValidaCVV));
    }

    @Test
    void saveCarta_NonValidScadenza_ThrowsException() {
        // Verifica che venga lanciata IllegalArgumentException per scadenza non valida
        assertThrows(IllegalArgumentException.class, () -> cartaDiCreditoService.saveCarta(cartaNonValidaScadenza));
    }


    @Test
    void getCarteByUtente_ReturnsCarte() {
        cartaDiCreditoService.saveCarta(cartaValida);

        List<CartaDiCredito> carte = cartaDiCreditoService.getCarteByUtente(utente);
        assertFalse(carte.isEmpty());
        assertTrue(carte.contains(cartaValida));
    }

    @Test
    void getCartaByCif_ExistingCif_ReturnsCarta() {
        cartaDiCreditoService.saveCarta(cartaValida);

        CartaDiCredito carta = cartaDiCreditoService.getCartaByCif(cartaValida.getCif());
        assertNotNull(carta);
        assertEquals(cartaValida.getCif(), carta.getCif());
    }

    @Test
    void deleteCarta_RemovesCarta() {
        cartaDiCreditoService.saveCarta(cartaValida);

        cartaDiCreditoService.deleteCarta(cartaValida);

        CartaDiCredito cartaEliminata = cartaDiCreditoRepository.findById(cartaValida.getCif()).orElse(null);
        assertNull(cartaEliminata);
    }
}
