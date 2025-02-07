package com.roguegames;
import com.roguegames.domain.entity.*;
import com.roguegames.domain.repository.GestoreRepository;
import com.roguegames.domain.repository.OrdineRepository;
import com.roguegames.domain.repository.ProdottoRepository;
import com.roguegames.domain.service.CarrelloService;
import com.roguegames.domain.service.GestoreService;
import com.roguegames.domain.service.GestoreServiceImp;
import com.roguegames.domain.service.OrdineService;
import com.roguegames.web.controller.Item.CarrelloItem;
import org.junit.jupiter.api.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import static com.roguegames.domain.entity.Prodotto.Piattaforma.PlayStation;
import static com.roguegames.domain.entity.Prodotto.Tipo.Videogiochi;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GestoreServiceTest {

    @InjectMocks
    GestoreService gestoreService = new GestoreServiceImp();

    @Mock
    GestoreRepository gestoreRepository;

    private Prodotto prodotto;

    @BeforeEach // Annota il metodo setUp con BeforeEach
    void setUp() {
        String dataRilascioStringa = "2002-09-22";
        SimpleDateFormat formatoData = new SimpleDateFormat("yyyy-MM-dd");
        Date dataRilascio = null;

        try {
            dataRilascio = formatoData.parse(dataRilascioStringa);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        prodotto = new Prodotto("Test1", "test.png", "test.mp4", "descrizione prodotto test", true, 10.00, "Test Srl", PlayStation, "Fantasy", Videogiochi, dataRilascio, 80);
    }

    @Test
    public void aggiungiProdotto_NonEsistente() {
        when(gestoreRepository.findById(prodotto.getNome())).thenReturn(Optional.empty());

        gestoreService.saveProduct(prodotto);

        verify(gestoreRepository, times(1)).save(any(Prodotto.class));

    }

    @Test
    public void aggiungiProdotto_Esistente() {
        when(gestoreRepository.findById(prodotto.getNome())).thenReturn(Optional.of(prodotto));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> gestoreService.saveProduct(prodotto));

        String expectedMessage = "Esiste già un prodotto con questo nome.";
        assertEquals(expectedMessage, exception.getMessage(), "Messaggio eccezione errato");

        verify(gestoreRepository, times(0)).save(any(Prodotto.class));

    }

    @Test
    public void modificaProdotto_Esistente() {
        when(gestoreRepository.findById(prodotto.getNome())).thenReturn(Optional.of(prodotto));

        gestoreService.updateProduct(prodotto);

        verify(gestoreRepository, times(1)).save(any(Prodotto.class));

    }

    @Test
    public void modificaProdotto_NonEsistente() {
        when(gestoreRepository.findById(prodotto.getNome())).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> gestoreService.updateProduct(prodotto));

        String expectedMessage = "Non esiste un prodotto con questo nome.";
        assertEquals(expectedMessage, exception.getMessage(), "Messaggio eccezione errato");

        verify(gestoreRepository, times(0)).save(any(Prodotto.class));

    }
    @Test
    public void eliminaProdotto_NonEsistente() {
        when(gestoreRepository.findById(prodotto.getNome())).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> gestoreService.deleteProduct(prodotto.getNome()));

        String expectedMessage = "Non esiste un prodotto con questo nome.";
        assertEquals(expectedMessage, exception.getMessage(), "Messaggio eccezione errato");

        verify(gestoreRepository, times(0)).deleteById(any(String.class));
    }

    @Test
    public void eliminaProdotto_Esistente() {
        when(gestoreRepository.findById(prodotto.getNome())).thenReturn(Optional.of(prodotto));

        gestoreService.deleteProduct(prodotto.getNome());

        verify(gestoreRepository, times(1)).deleteById(any(String.class));

    }

}
