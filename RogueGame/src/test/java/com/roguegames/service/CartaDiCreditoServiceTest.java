package com.roguegames.service;

import com.roguegames.domain.entity.CartaDiCredito;
import com.roguegames.domain.entity.Utente;
import com.roguegames.domain.repository.CartaDiCreditoRepository;
import com.roguegames.domain.service.CartaDiCreditoService;
import com.roguegames.domain.service.UtenteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartaDiCreditoServiceTest {

    @Mock
    private CartaDiCreditoRepository cartaDiCreditoRepository;

    private UtenteService utenteService;

    @InjectMocks
    private CartaDiCreditoService cartaDiCreditoService;

    private Utente utente;
    private CartaDiCredito cartaValida;
    private CartaDiCredito cartaNonValida;

    @BeforeEach
    void setUp() {
        cartaValida = new CartaDiCredito("1234567812345678", "12/30", "123", utente);
        cartaNonValida = new CartaDiCredito("1234", "01/20", "12", utente);
    }

    @Test
    void saveCarta_ValidData_Success() {
        when(cartaDiCreditoRepository.save(cartaValida)).thenReturn(cartaValida);
        assertDoesNotThrow(() -> cartaDiCreditoService.saveCarta(cartaValida));
        verify(cartaDiCreditoRepository, times(1)).save(cartaValida);
    }

    @Test
    void saveCarta_InvalidCif_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> cartaDiCreditoService.saveCarta(cartaNonValida));
        assertEquals("Dati della carta non validi", exception.getMessage());
    }

    @Test
    void saveCarta_InvalidCVV_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> cartaDiCreditoService.saveCarta(cartaNonValida));
        assertEquals("Dati della carta non validi", exception.getMessage());
    }

    @Test
    void saveCarta_InvalidScadenza_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> cartaDiCreditoService.saveCarta(cartaNonValida));
        assertEquals("Dati della carta non validi", exception.getMessage());
    }

    @Test
    void getCarteByUtente_ReturnsList() {
        Utente utente = new Utente();
        when(cartaDiCreditoRepository.findByUtente(utente)).thenReturn(Collections.singletonList(cartaValida));
        List<CartaDiCredito> carte = cartaDiCreditoService.getCarteByUtente(utente);
        assertFalse(carte.isEmpty());
        assertEquals(1, carte.size());
    }

    @Test
    void getCartaByCif_ExistingCif_ReturnsCarta() {
        when(cartaDiCreditoRepository.findById("1234567812345678")).thenReturn(Optional.of(cartaValida));
        CartaDiCredito carta = cartaDiCreditoService.getCartaByCif("1234567812345678");
        assertNotNull(carta);
    }

    @Test
    void getCartaByCif_NonExistingCif_ReturnsNull() {
        when(cartaDiCreditoRepository.findById("0000000000000000")).thenReturn(Optional.empty());
        CartaDiCredito carta = cartaDiCreditoService.getCartaByCif("0000000000000000");
        assertNull(carta);
    }

    @Test
    void deleteCarta_RemovesCarta() {
        doNothing().when(cartaDiCreditoRepository).delete(cartaValida);
        assertDoesNotThrow(() -> cartaDiCreditoService.deleteCarta(cartaValida));
        verify(cartaDiCreditoRepository, times(1)).delete(cartaValida);
    }
}
