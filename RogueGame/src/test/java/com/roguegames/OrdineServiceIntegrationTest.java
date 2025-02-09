package com.roguegames;
import com.roguegames.domain.entity.*;
import com.roguegames.domain.repository.CarrelloRepository;
import com.roguegames.web.controller.Item.CarrelloItem;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.Commit;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roguegames.domain.repository.PCarrelloRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OrdineServiceIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CarrelloRepository carrelloRepository;

    @Test
    void processaOrdine_carrelloVuoto() throws Exception {
        Utente utente = new Utente("lollo@lollo.lol", "Tester12!", "User", "Test", 10, "Test 14b", "1112223330");
        List< CarrelloItem> listaCarrello = new ArrayList< CarrelloItem >();
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("utente", utente);
        session.setAttribute("carrelloItem", listaCarrello);

        mockMvc.perform(post("/aggiungiOrdine")
                        .session(session)
                        .param("totale", "100.0"))
                .andExpect(status().isOk());
    }

    @Test
    @Commit
    void processaOrdine_carrelloSingoloElemento() throws Exception {

        Utente utente = new Utente("lollo@lollo.lol", "Tester12!", "User", "Test", 10, "Test 14b", "1112223330");
        PCarrelloId id = new PCarrelloId("EldenRing", "lollo@lollo.lol");
        Prodotto p= new Prodotto();

        p.setNome("EldenRing");
        p.setImmagine("EldenRing.jpg");
        p.setPrezzo(39.99);

        PCarrello c = new PCarrello(id, 1, p, utente);
        CarrelloItem ci = new CarrelloItem(p, c);
        List< CarrelloItem> listaCarrello = new ArrayList<>();
        listaCarrello.add(ci);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("utente", utente);
        session.setAttribute("carrelloItem", listaCarrello);

        mockMvc.perform(post("/aggiungiOrdine")
                        .session(session)
                        .param("totale", "39.99"))
                .andExpect(status().isOk());
    }

    @Test
    @Commit
    void processaOrdine_carrelloMultipliElementi() throws Exception {
        List< CarrelloItem> listaCarrello = new ArrayList<>();
        Utente utente = new Utente("lollo@lollo.lol", "Tester12!", "User", "Test", 10, "Test 14b", "1112223330");

        Prodotto p;

        p= new Prodotto();
        p.setNome("Cassette Beasts");
        p.setImmagine("Cassette Beasts.jpg");
        p.setPrezzo(20.00);
        PCarrelloId id = new PCarrelloId("Cassette Beasts", "lollo@lollo.lol");
        PCarrello c = new PCarrello(id, 2, p, utente);
        CarrelloItem ci = new CarrelloItem(p, c);
        listaCarrello.add(ci);

        p= new Prodotto();
        p.setNome("Pokemon Perla Splendente");
        p.setImmagine("PokemonPerla.jpg");
        p.setPrezzo(20.00);
        id = new PCarrelloId("Pokemon Perla Splendente", "lollo@lollo.lol");
        c = new PCarrello(id, 1, p, utente);
        ci = new CarrelloItem(p, c);
        listaCarrello.add(ci);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("utente", utente);
        session.setAttribute("carrelloItem", listaCarrello);

        mockMvc.perform(post("/aggiungiOrdine")
                        .session(session)
                        .param("totale", "80"))
                .andExpect(status().isOk());
    }

    @Test
    void aggiungiOrdine_utenteNonAutenticato() throws Exception {
        MockHttpSession session = new MockHttpSession();
        mockMvc.perform(post("/aggiungiOrdine")
                        .param("totale", "80"))
                .andExpect(status().isUnauthorized())
                .andExpect(header().string("Location", "/utenti/login"));
    }

    @Test
    void aggiungiOrdine_utenteGestore() throws Exception {
        Utente gestore = new Utente("gestore@test.com", "password", "Gestore", "Test", 10,"Test 14b", "1112223330");
        gestore.setRuolo("gestore");

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("utente", gestore);

        mockMvc.perform(post("/aggiungiOrdine")
                        .session(session)
                        .param("totale", "100.0"))
                .andExpect(status().isBadRequest());
    }
}
