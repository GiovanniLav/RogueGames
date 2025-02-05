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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PreferitiServiceTest {

    @InjectMocks
    private PreferitiService preferitiService;

    @Mock
    private PreferitiRepository preferitiRepository;

    @Mock
    private ProdottoRepository prodottoRepository;

    private Utente utente;
    private Prodotto prodotto;
    private Preferiti preferiti;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup di esempio
        utente = new Utente();
        utente.setEmail("test@utente.com");

        prodotto = new Prodotto();
        prodotto.setNome("Prodotto1");
        prodotto.setImmagine("url_immagine");

        PreferitiId id = new PreferitiId("Prodotto1", utente.getEmail());
        preferiti = new Preferiti(id, prodotto.getImmagine());
    }

    @Test
    void trovaPerUtenteTest() {
        when(preferitiRepository.findByUtente(utente)).thenReturn(List.of(preferiti));

        List<Preferiti> preferitiList = preferitiService.trovaPerUtente(utente);

        assertNotNull(preferitiList);
        assertFalse(preferitiList.isEmpty());
        assertEquals(1, preferitiList.size());
        assertEquals("Prodotto1", preferitiList.get(0).getId().getNome());
    }

    @Test
    void aggiungiPreferitoTest() {
        when(prodottoRepository.findByNome("Prodotto1")).thenReturn(prodotto);

        preferitiService.aggiungiPreferito("Prodotto1", utente);

        verify(preferitiRepository, times(1)).save(any(Preferiti.class));
    }

    @Test
    void rimuoviPreferitoTest() {
        PreferitiId preferitiId = new PreferitiId("Prodotto1", utente.getEmail());
        doNothing().when(preferitiRepository).deleteById(preferitiId);

        preferitiService.rimuoviPreferito("Prodotto1", utente);

        verify(preferitiRepository, times(1)).deleteById(preferitiId);
    }

    @Test
    void ePreferitoTest() {
        when(preferitiRepository.existsById(new PreferitiId("Prodotto1", utente.getEmail()))).thenReturn(true);

        boolean result = preferitiService.ePreferito("Prodotto1", utente);

        assertTrue(result);
    }
}

