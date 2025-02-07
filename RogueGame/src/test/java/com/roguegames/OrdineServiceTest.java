package com.roguegames;

import com.roguegames.domain.entity.*;
import com.roguegames.domain.repository.OrdineRepository;
import com.roguegames.domain.service.OrdineService;
import com.roguegames.domain.service.OrdineServiceImp;
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

import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import static com.roguegames.domain.entity.Prodotto.Piattaforma.PlayStation;
import static com.roguegames.domain.entity.Prodotto.Tipo.Videogiochi;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrdineServiceTest {
    @Mock
    private OrdineRepository ordineRepository;

    @InjectMocks
    private OrdineService carrelloService = new OrdineServiceImp();

    private Utente utente;
    private Prodotto prodotto;
    private List<CarrelloItem> cItem;
    private PCarrelloId id;
    private PCarrello carrello;
    @BeforeEach // Annota il metodo setUp con BeforeEach
    void setUp() {
        utente = new Utente("test@example.com", "Tester12!", "User", "Test", 10, "Test 14b", "1112223330");
        cItem = new ArrayList<>();

        String dataRilascioStringa = "2002-09-22";
        SimpleDateFormat formatoData = new SimpleDateFormat("yyyy-MM-dd");
        Date dataRilascio = null;

        try {
            dataRilascio = formatoData.parse(dataRilascioStringa);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        prodotto = new Prodotto("Test1", "test.png", "test.mp4", "descrizione prodotto test", true, 10.00, "Test Srl", PlayStation, "Fantasy", Videogiochi, dataRilascio, 80);
        id = new PCarrelloId(prodotto.getNome(), utente.getEmail());
        carrello = new PCarrello(id, 1, prodotto, utente);
    }

    @Test
    void processaOrdine_carrelloVuoto() {
        assertThrows(IllegalArgumentException.class, () -> {
            carrelloService.processaOrdine(utente, null, 100.0);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            carrelloService.processaOrdine(utente, new ArrayList<>(), 100.0);
        });
    }

    @Test
    void processaOrdine_carrelloSingoloElemento() {

        CarrelloItem cart = new CarrelloItem(prodotto, carrello);
        cItem.add(cart);
        OrdineId ido = new OrdineId(1);
        Ordine ordine = new Ordine();
        ordine.setId(ido);
        when(ordineRepository.findByNomeAndEmailAndData(anyString(), anyString(), anyString())).thenReturn(ordine);

        carrelloService.processaOrdine(utente, cItem, 10.00);

        ArgumentCaptor<Ordine> ordineCaptor = ArgumentCaptor.forClass(Ordine.class);
        verify(ordineRepository, times(1)).save(ordineCaptor.capture());
        Ordine ordineSalvato = ordineCaptor.getValue();

        assertEquals(10.0, ordineSalvato.getPrezzoTot()); // Verifica il totale
        assertEquals(1, ordineSalvato.getQuantita());
        assertEquals("Test1", ordineSalvato.getId().getNome());
        assertEquals(utente.getEmail(), ordineSalvato.getId().getEmail());
    }

    @Test
    @Transactional
    void processaOrdine_carrelloMultipliElementi() {

        String dataRilascioStringa = "2002-09-22";
        SimpleDateFormat formatoData = new SimpleDateFormat("yyyy-MM-dd");
        Date data = null;
        try {
            data = formatoData.parse(dataRilascioStringa);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        CarrelloItem cart = new CarrelloItem(prodotto, carrello);
        cItem.add(cart);


        Prodotto prod1 = new Prodotto("Test2", "test.png", "test.mp4", "descrizione prodotto test", true, 20.00, "Test Srl", PlayStation, "Fantasy", Videogiochi, data, 80);
        id = new PCarrelloId(prod1.getNome(), utente.getEmail());
        carrello = new PCarrello(id, 1, prod1, utente);
        CarrelloItem cart1 = new CarrelloItem(prod1, carrello);
        cItem.add(cart1);

        Prodotto prod2 = new Prodotto("Test3", "test.png", "test.mp4", "descrizione prodotto test", true, 20.00, "Test Srl", PlayStation, "Fantasy", Videogiochi, data, 80);
        id = new PCarrelloId(prod2.getNome(), utente.getEmail());
        carrello = new PCarrello(id, 1, prod2, utente);
        CarrelloItem cart2 = new CarrelloItem(prod2, carrello);
        cItem.add(cart2);

        OrdineId ido = new OrdineId(1);
        Ordine ordine = new Ordine();
        ordine.setId(ido);

        when(ordineRepository.findByNomeAndEmailAndData(anyString(), anyString(), anyString())).thenReturn(ordine);

        carrelloService.processaOrdine(utente, cItem, 60.00);

        verify(ordineRepository, times(1)).save(any(Ordine.class));
        verify(ordineRepository, times(2)).saveAndFlush(any(Ordine.class));

    }
}
