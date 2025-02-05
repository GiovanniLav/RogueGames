package com.roguegames;

import com.roguegames.domain.entity.PCarrello;
import com.roguegames.domain.entity.PCarrelloId;
import com.roguegames.domain.entity.Prodotto;
import com.roguegames.domain.entity.Utente;
import com.roguegames.domain.repository.CarrelloRepository;
import com.roguegames.domain.repository.PCarrelloRepository;
import com.roguegames.domain.service.CarrelloService;

import org.junit.jupiter.api.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import org.junit.jupiter.api.Test;

import static com.roguegames.domain.entity.Prodotto.Piattaforma.PlayStation;
import static com.roguegames.domain.entity.Prodotto.Tipo.Videogiochi;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CarrelloServiceTest {

    @InjectMocks
    private CarrelloService carrelloService;

    @Mock
    private CarrelloRepository carrelloRepository;
    @Mock
    private PCarrelloRepository pCarrelloRepository;
    private Prodotto prodotto;
    private Utente utente;
    private PCarrelloId carrelloId;

    @BeforeEach // Annota il metodo setUp con BeforeEach
    void setUp() {
        utente = new Utente("test@example.com", "Tester12!", "User", "Test", 10,"Test 14b", "1112223330");

        String dataRilascioStringa = "2002-09-22";
        SimpleDateFormat formatoData = new SimpleDateFormat("yyyy-MM-dd");
        Date dataRilascio = null;

        try {
            dataRilascio = formatoData.parse(dataRilascioStringa);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        prodotto = new Prodotto("Test1", "test.png", "test.mp4", "descrizione prodotto test", true, 10.00, "Test Srl", PlayStation, "Fantasy", Videogiochi, dataRilascio, 80);
        carrelloId = new PCarrelloId(prodotto.getNome(), utente.getEmail());
    }

    @Test
    void aggiungiProdottoValido_nuovoProdotto() {
        when(pCarrelloRepository.findById(carrelloId)).thenReturn(Optional.empty());

        carrelloService.aggiungiProdotto(prodotto, utente);

        ArgumentCaptor<PCarrello> carrelloCaptor = ArgumentCaptor.forClass(PCarrello.class);
        verify(carrelloRepository).save(carrelloCaptor.capture());

        PCarrello carrelloSalvato = carrelloCaptor.getValue();


        when(carrelloRepository.findByNomeAndEmail(anyString(), anyString()))
                .thenReturn(Optional.of(carrelloSalvato));

        Optional<PCarrello> carrelloVerificato = carrelloRepository.findByNomeAndEmail(carrelloSalvato.getId().getNome(), carrelloSalvato.getId().getEmail());

        assertTrue(carrelloVerificato.isPresent());
        assertEquals(1, carrelloVerificato.get().getQuantita());
        assertEquals(prodotto.getNome(), carrelloVerificato.get().getProdotto().getNome());
        assertEquals(utente.getEmail(), carrelloVerificato.get().getUtente().getEmail());
    }

    @Test
    void aggiungiProdottoValido_prodottoEsistente() {
        when(pCarrelloRepository.findById(carrelloId)).thenReturn(Optional.empty());
        carrelloService.aggiungiProdotto(prodotto, utente);

        when(pCarrelloRepository.findById(carrelloId)).thenReturn(Optional.of(new PCarrello(carrelloId, 1, prodotto, utente)));
        carrelloService.aggiungiProdotto(prodotto, utente);

        ArgumentCaptor<PCarrello> carrelloCaptor = ArgumentCaptor.forClass(PCarrello.class);

        verify(carrelloRepository, times(2)).save(carrelloCaptor.capture());

        PCarrello carrelloSalvato = carrelloCaptor.getValue();

        when(carrelloRepository.findByNomeAndEmail(anyString(), anyString()))
                .thenReturn(Optional.of(carrelloSalvato));

        Optional<PCarrello> carrelloVerificato = carrelloRepository.findByNomeAndEmail(carrelloSalvato.getId().getNome(), carrelloSalvato.getId().getEmail());

        assertTrue(carrelloVerificato.isPresent());
        assertEquals(2, carrelloVerificato.get().getQuantita());
        assertEquals(prodotto.getNome(), carrelloVerificato.get().getProdotto().getNome()); // Confronta per nome (più robusto)
        assertEquals(utente.getEmail(), carrelloVerificato.get().getUtente().getEmail()); // Confronta per email (più robusto)
    }

    @Test
    void aggiungiProdotto_quantitaNonDisponibile() {

        when(pCarrelloRepository.findById(carrelloId)).thenReturn(Optional.of(new PCarrello(carrelloId, 80, prodotto, utente)));

        CarrelloService.QuantitaNonDisponibileException exception = assertThrows(CarrelloService.QuantitaNonDisponibileException.class, () -> {
            when(pCarrelloRepository.findById(carrelloId)).thenReturn(Optional.of(new PCarrello(carrelloId, 80, prodotto, utente)));
            carrelloService.aggiungiProdotto(prodotto, utente); // Tenta di aggiungere un'unità extra
        });


        String expectedMessage = "La quantità non è disponibile. La quantità massima di " + prodotto.getNome() + " è di " + prodotto.getQuantita();
        assertEquals(expectedMessage, exception.getMessage(), "Messaggio eccezione errato");

        verify(carrelloRepository, times(0)).save(any(PCarrello.class));
    }

    @Test
    void rimuoviProdotto_prodottoEsistente(){
        when(pCarrelloRepository.findById(carrelloId)).thenReturn(Optional.of(new PCarrello(carrelloId, 1, prodotto, utente)));

        Optional<PCarrello> carrelloVerificato = pCarrelloRepository.findById(carrelloId);
        assertTrue(carrelloVerificato.isPresent());

        PCarrello carrelloSalvato = carrelloVerificato.get();
        carrelloService.rimuoviProdotto(carrelloSalvato.getProdotto(), carrelloSalvato.getUtente());

        verify(carrelloRepository, times(1)).delete(any(PCarrello.class));
    }

    @Test
    void rimuoviProdotto_prodottoNonEsistente(){
        when(pCarrelloRepository.findById(carrelloId)).thenReturn(Optional.empty());

        CarrelloService.QuantitaNonDisponibileException exception = assertThrows(CarrelloService.QuantitaNonDisponibileException.class, () -> {
            when(pCarrelloRepository.findById(carrelloId)).thenReturn(Optional.empty());
            carrelloService.rimuoviProdotto(prodotto, utente); // Tenta di aggiungere un'unità extra
        });

        String expectedMessage = "Prodotto non presente";
        assertEquals(expectedMessage, exception.getMessage(), "Messaggio eccezione errato");

        verify(carrelloRepository, times(0)).delete(any(PCarrello.class));
    }

    @Test
    void modificaQuantita_ProdottoEsistente() {
        when(pCarrelloRepository.findById(carrelloId)).thenReturn(Optional.of(new PCarrello(carrelloId, 1, prodotto, utente)));

        carrelloService.modificaQnt(prodotto, utente, "7");

        ArgumentCaptor<PCarrello> carrelloCaptor = ArgumentCaptor.forClass(PCarrello.class);
        verify(carrelloRepository).save(carrelloCaptor.capture());

        PCarrello carrelloSalvato = carrelloCaptor.getValue();


        when(carrelloRepository.findByNomeAndEmail(anyString(), anyString()))
                .thenReturn(Optional.of(carrelloSalvato));

        Optional<PCarrello> carrelloVerificato = carrelloRepository.findByNomeAndEmail(carrelloSalvato.getId().getNome(), carrelloSalvato.getId().getEmail());

        assertTrue(carrelloVerificato.isPresent());
        assertEquals(7, carrelloVerificato.get().getQuantita());
        assertEquals(prodotto.getNome(), carrelloVerificato.get().getProdotto().getNome()); // Confronta per nome (più robusto)
        assertEquals(utente.getEmail(), carrelloVerificato.get().getUtente().getEmail()); // Confronta per email (più robusto)

    }

    @Test
    void modificaQuantita_ProdottoNonEsistente(){
        when(pCarrelloRepository.findById(carrelloId)).thenReturn(Optional.empty());

        CarrelloService.QuantitaNonDisponibileException exception = assertThrows(CarrelloService.QuantitaNonDisponibileException.class, () -> {
            when(pCarrelloRepository.findById(carrelloId)).thenReturn(Optional.empty());
            carrelloService.modificaQnt(prodotto, utente, "7"); // Tenta di aggiungere un'unità extra
        });

        String expectedMessage = "Prodotto non presente";
        assertEquals(expectedMessage, exception.getMessage(), "Messaggio eccezione errato");

        verify(carrelloRepository, times(0)).save(any(PCarrello.class));
    }

    @Test
    void modificaQuantita_ValoreNonIntero(){
        CarrelloService.QuantitaNonDisponibileException exception = assertThrows(CarrelloService.QuantitaNonDisponibileException.class, () -> {
            when(pCarrelloRepository.findById(carrelloId)).thenReturn(Optional.of(new PCarrello(carrelloId, 1, prodotto, utente)));
            carrelloService.modificaQnt(prodotto, utente, ","); // Tenta di aggiungere un'unità extra
        });

        String expectedMessage = "La quantità deve essere un numero intero valido.";
        assertEquals(expectedMessage, exception.getMessage(), "Messaggio eccezione errato");

        verify(carrelloRepository, times(0)).save(any(PCarrello.class));
    }

    @Test
    void modificaQuantita_QuantitaNonDisponibile() {
        CarrelloService.QuantitaNonDisponibileException exception = assertThrows(CarrelloService.QuantitaNonDisponibileException.class, () -> {
            when(pCarrelloRepository.findById(carrelloId)).thenReturn(Optional.of(new PCarrello(carrelloId, 80, prodotto, utente)));
            carrelloService.modificaQnt(prodotto, utente, "81"); // Tenta di aggiungere un'unità extra
        });

        String expectedMessage = "La quantità non è disponibile. La quantità massima di " + prodotto.getNome() + " è di " + prodotto.getQuantita();
        assertEquals(expectedMessage, exception.getMessage(), "Messaggio eccezione errato");

        verify(carrelloRepository, times(0)).save(any(PCarrello.class));
    }
}
