package com.roguegames;
import com.roguegames.domain.entity.Prodotto;
import com.roguegames.domain.repository.GestoreRepository;
import com.roguegames.domain.service.GestoreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.Commit;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roguegames.domain.entity.Utente;
import com.roguegames.domain.entity.PCarrello;
import com.roguegames.domain.entity.PCarrelloId;
import com.roguegames.domain.repository.PCarrelloRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static com.roguegames.domain.entity.Prodotto.Piattaforma.PlayStation;
import static com.roguegames.domain.entity.Prodotto.Tipo.Videogiochi;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class GestoreServiceIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GestoreService gestoreService;

    @Test
    @Commit
    public void aggiungiProdotto_NonEsistente() throws Exception {
        Utente utente = new Utente("lollo@lollo.lol", "Tester12!", "User", "Test", 10, "Test 14b", "1112223330");

        String dataRilascioStringa = "2002-09-22";
        SimpleDateFormat formatoData = new SimpleDateFormat("yyyy-MM-dd");
        Date dataRilascio = null;

        try {
            dataRilascio = formatoData.parse(dataRilascioStringa);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Prodotto prodotto = new Prodotto("test1", "Gow4.jpg", "Gow.mp4.mp4", "descrizione prodotto test", true, 10.00, "Test Srl", PlayStation, "Fantasy", Videogiochi, dataRilascio, 80);
        utente.setRuolo("gestore");

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("utente", utente);


        mockMvc.perform(post("/utenti/prodotti/aggiungi")
                        .contentType(MediaType.APPLICATION_JSON)
                        .flashAttr("prodotto", prodotto)
                        .session(session))
            .andExpect(status().isOk());
    }

    @Test
    @Commit
    public void aggiungiProdotto_Esistente() throws Exception {
        Utente utente = new Utente("lollo@lollo.lol", "Tester12!", "User", "Test", 10, "Test 14b", "1112223330");

        String dataRilascioStringa = "2002-09-22";
        SimpleDateFormat formatoData = new SimpleDateFormat("yyyy-MM-dd");
        Date dataRilascio = null;

        try {
            dataRilascio = formatoData.parse(dataRilascioStringa);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Prodotto prodotto = new Prodotto("test1", "Gow4.jpg", "Gow.mp4.mp4", "descrizione prodotto test", true, 10.00, "Test Srl", PlayStation, "Fantasy", Videogiochi, dataRilascio, 80);
        utente.setRuolo("gestore");

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("utente", utente);

        when(gestoreService.getProductByName("test1")).thenReturn(prodotto);

        mockMvc.perform(post("/utenti/prodotti/aggiungi")
                        .contentType(MediaType.APPLICATION_JSON)
                        .flashAttr("prodotto", prodotto)
                        .session(session))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Commit
    public void aggiungiProdotto_UtenteNonAutenticato() throws Exception {

        String dataRilascioStringa = "2002-09-22";
        SimpleDateFormat formatoData = new SimpleDateFormat("yyyy-MM-dd");
        Date dataRilascio = null;

        try {
            dataRilascio = formatoData.parse(dataRilascioStringa);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Prodotto prodotto = new Prodotto("test1", "Gow4.jpg", "Gow.mp4.mp4", "descrizione prodotto test", true, 10.00, "Test Srl", PlayStation, "Fantasy", Videogiochi, dataRilascio, 80);

        mockMvc.perform(multipart("/utenti/prodotti/aggiungi")
                        .flashAttr("prodotto", prodotto))
                .andExpect(status().isNetworkAuthenticationRequired());
    }

    @Test
    @Commit
    public void aggiungiProdotto_UtenteNonAutorizzato() throws Exception {
        Utente utente = new Utente("lollo@lollo.lol", "Tester12!", "User", "Test", 10, "Test 14b", "1112223330");

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("utente", utente);

        String dataRilascioStringa = "2002-09-22";
        SimpleDateFormat formatoData = new SimpleDateFormat("yyyy-MM-dd");
        Date dataRilascio = null;

        try {
            dataRilascio = formatoData.parse(dataRilascioStringa);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Prodotto prodotto = new Prodotto("test1", "Gow4.jpg", "Gow.mp4.mp4", "descrizione prodotto test", true, 10.00, "Test Srl", PlayStation, "Fantasy", Videogiochi, dataRilascio, 80);

        mockMvc.perform(multipart("/utenti/prodotti/aggiungi")
                        .session(session)
                        .flashAttr("prodotto", prodotto))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Commit
    public void modificaProdotto_Esistente() throws Exception {
        Utente utente = new Utente("lollo@lollo.lol", "Tester12!", "User", "Test", 10, "Test 14b", "1112223330");

        String dataRilascioStringa = "2002-09-22";
        SimpleDateFormat formatoData = new SimpleDateFormat("yyyy-MM-dd");
        Date dataRilascio = null;

        try {
            dataRilascio = formatoData.parse(dataRilascioStringa);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Prodotto prodotto = new Prodotto("test1", "Gow4.jpg", "Gow.mp4.mp4", "descrizione prodotto test aggiornato", true, 10.00, "Test Srl", PlayStation, "Fantasy", Videogiochi, dataRilascio, 80);
        utente.setRuolo("gestore");

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("utente", utente);


        mockMvc.perform(post("/utenti/prodotti/modifica/test1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .flashAttr("prodotto", prodotto)
                        .content(prodotto.getNome())
                        .session(session))
                .andExpect(redirectedUrl("/utenti/prodotti"));
    }

    @Test
    @Commit
    public void modificaProdotto_NonEsistente() throws Exception {
        Utente utente = new Utente("lollo@lollo.lol", "Tester12!", "User", "Test", 10, "Test 14b", "1112223330");

        String dataRilascioStringa = "2002-09-22";
        SimpleDateFormat formatoData = new SimpleDateFormat("yyyy-MM-dd");
        Date dataRilascio = null;

        try {
            dataRilascio = formatoData.parse(dataRilascioStringa);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Prodotto prodotto = new Prodotto("test", "Gow4.jpg", "Gow.mp4.mp4", "descrizione prodotto test aggiornato", true, 10.00, "Test Srl", PlayStation, "Fantasy", Videogiochi, dataRilascio, 80);
        utente.setRuolo("gestore");

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("utente", utente);


        mockMvc.perform(post("/utenti/prodotti/modifica/test1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .flashAttr("prodotto", prodotto)
                        .content(prodotto.getNome())
                        .session(session))
                .andExpect(redirectedUrl("/utenti/prodotti"));
    }


    @Test
    @Commit
    public void modificaProdotto_UtenteNonAutenticato() throws Exception {

        String dataRilascioStringa = "2002-09-22";
        SimpleDateFormat formatoData = new SimpleDateFormat("yyyy-MM-dd");
        Date dataRilascio = null;

        try {
            dataRilascio = formatoData.parse(dataRilascioStringa);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Prodotto prodotto = new Prodotto("test1", "Gow4.jpg", "Gow.mp4.mp4", "descrizione prodotto test", true, 10.00, "Test Srl", PlayStation, "Fantasy", Videogiochi, dataRilascio, 80);

        mockMvc.perform(multipart("/utenti/prodotti/modifica/test1")
                        .flashAttr("prodotto", prodotto)
                        .content(prodotto.getNome()))
                .andExpect(redirectedUrl("/utenti/login"));
    }

    @Test
    @Commit
    public void modificaProdotto_UtenteNonAutorizzato() throws Exception {
        Utente utente = new Utente("lollo@lollo.lol", "Tester12!", "User", "Test", 10, "Test 14b", "1112223330");

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("utente", utente);

        String dataRilascioStringa = "2002-09-22";
        SimpleDateFormat formatoData = new SimpleDateFormat("yyyy-MM-dd");
        Date dataRilascio = null;

        try {
            dataRilascio = formatoData.parse(dataRilascioStringa);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Prodotto prodotto = new Prodotto("test1", "Gow4.jpg", "Gow.mp4.mp4", "descrizione prodotto test", true, 10.00, "Test Srl", PlayStation, "Fantasy", Videogiochi, dataRilascio, 80);

        mockMvc.perform(multipart("/utenti/prodotti/modifica/test1")
                        .flashAttr("prodotto", prodotto)
                        .content(prodotto.getNome()))
                .andExpect(redirectedUrl("/utenti/login"));
    }

    @Test
    @Commit
    public void eliminaProdotto_Esistente() throws Exception {
        Utente utente = new Utente("lollo@lollo.lol", "Tester12!", "User", "Test", 10, "Test 14b", "1112223330");

        String dataRilascioStringa = "2002-09-22";
        SimpleDateFormat formatoData = new SimpleDateFormat("yyyy-MM-dd");
        Date dataRilascio = null;

        try {
            dataRilascio = formatoData.parse(dataRilascioStringa);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Prodotto prodotto = new Prodotto("test1", "Gow4.jpg", "Gow.mp4.mp4", "descrizione prodotto test aggiornato", true, 10.00, "Test Srl", PlayStation, "Fantasy", Videogiochi, dataRilascio, 80);
        utente.setRuolo("gestore");

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("utente", utente);


        mockMvc.perform(post("/utenti/prodotti/elimina/test1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(prodotto.getNome())
                        .session(session))
                .andExpect(redirectedUrl("/utenti/prodotti"));
    }

    @Test
    @Commit
    public void eliminaProdotto_NonEsistente() throws Exception {
        Utente utente = new Utente("lollo@lollo.lol", "Tester12!", "User", "Test", 10, "Test 14b", "1112223330");

        String dataRilascioStringa = "2002-09-22";
        SimpleDateFormat formatoData = new SimpleDateFormat("yyyy-MM-dd");
        Date dataRilascio = null;

        try {
            dataRilascio = formatoData.parse(dataRilascioStringa);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Prodotto prodotto = new Prodotto("test1", "Gow4.jpg", "Gow.mp4.mp4", "descrizione prodotto test aggiornato", true, 10.00, "Test Srl", PlayStation, "Fantasy", Videogiochi, dataRilascio, 80);
        utente.setRuolo("gestore");

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("utente", utente);

        doThrow(new IllegalArgumentException("Non esiste un prodotto con questo nome."))
                .when(gestoreService).deleteProduct("test1");

        mockMvc.perform(post("/utenti/prodotti/elimina/test1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(prodotto.getNome())
                        .session(session))
                .andExpect(redirectedUrl("/utenti/prodotti"));
    }

    @Test
    @Commit
    public void eliminaProdotto_UtenteNonAutenticato() throws Exception {

        String dataRilascioStringa = "2002-09-22";
        SimpleDateFormat formatoData = new SimpleDateFormat("yyyy-MM-dd");
        Date dataRilascio = null;

        try {
            dataRilascio = formatoData.parse(dataRilascioStringa);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Prodotto prodotto = new Prodotto("EldenRing", "Gow4.jpg", "Gow.mp4.mp4", "descrizione prodotto test", true, 10.00, "Test Srl", PlayStation, "Fantasy", Videogiochi, dataRilascio, 80);

        mockMvc.perform(post("/utenti/prodotti/elimina/EldenRing")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(prodotto.getNome()))
                .andExpect(redirectedUrl("/utenti/login"));;
    }

    @Test
    @Commit
    public void eliminaProdotto_UtenteNonAutorizzato() throws Exception {
        Utente utente = new Utente("lollo@lollo.lol", "Tester12!", "User", "Test", 10, "Test 14b", "1112223330");

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("utente", utente);

        String dataRilascioStringa = "2002-09-22";
        SimpleDateFormat formatoData = new SimpleDateFormat("yyyy-MM-dd");
        Date dataRilascio = null;

        try {
            dataRilascio = formatoData.parse(dataRilascioStringa);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Prodotto prodotto = new Prodotto("EldenRing", "Gow4.jpg", "Gow.mp4.mp4", "descrizione prodotto test", true, 10.00, "Test Srl", PlayStation, "Fantasy", Videogiochi, dataRilascio, 80);

        mockMvc.perform(post("/utenti/prodotti/elimina/test1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(prodotto.getNome())
                        .session(session))
                .andExpect(redirectedUrl("/utenti/login"));
    }
}
