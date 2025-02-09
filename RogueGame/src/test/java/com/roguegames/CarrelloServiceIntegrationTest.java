package com.roguegames;
import com.roguegames.domain.repository.CarrelloRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roguegames.domain.entity.Utente;
import com.roguegames.domain.entity.PCarrello;
import com.roguegames.domain.entity.PCarrelloId;
import com.roguegames.domain.repository.PCarrelloRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CarrelloServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PCarrelloRepository pCarrelloRepository;
    @Autowired
    private CarrelloRepository carrelloRepository;

    @Test
    @Commit
    void aggiungiProdottoValido_nuovoProdotto() throws Exception {

        Utente utente = new Utente("lollo@lollo.lol", "Tester12!", "User", "Test", 10, "Test 14b", "1112223330");

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("utente", utente);


        String nomeProdotto = "Cassette Beasts";

        mockMvc.perform(post("/aggiungi/Cassette Beasts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nomeProdotto)
                        .session(session))
                .andExpect(status().isOk());

        PCarrelloId carrelloId = new PCarrelloId(nomeProdotto, utente.getEmail());
        Optional<PCarrello> carrello = pCarrelloRepository.findById(carrelloId);
        assertTrue(carrello.isPresent());
        assertEquals(1, carrello.get().getQuantita());
        assertEquals(nomeProdotto, carrello.get().getProdotto().getNome());
        assertEquals(utente.getEmail(), carrello.get().getUtente().getEmail());
    }

    @Test
    @Commit
    void aggiungiProdottoValido_ProdottoEsistente() throws Exception {

        Utente utente = new Utente("lollo@lollo.lol", "Tester12!", "User", "Test", 10, "Test 14b", "1112223330");


        MockHttpSession session = new MockHttpSession();
        session.setAttribute("utente", utente);

        String nomeProdotto = "The legend of Zelda Action figure Link";

        mockMvc.perform(post("/aggiungi/The legend of Zelda Action figure Link")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nomeProdotto)
                        .session(session))
                .andExpect(status().isOk());

        mockMvc.perform(post("/aggiungi/The legend of Zelda Action figure Link")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nomeProdotto)
                        .session(session))
                .andExpect(status().isOk());

        PCarrelloId carrelloId = new PCarrelloId(nomeProdotto, utente.getEmail());
        Optional<PCarrello> carrello = pCarrelloRepository.findById(carrelloId);
        assertTrue(carrello.isPresent());
        assertEquals(2, carrello.get().getQuantita());
        assertEquals(nomeProdotto, carrello.get().getProdotto().getNome());
        assertEquals(utente.getEmail(), carrello.get().getUtente().getEmail());
    }

    @Test
    @Commit
    void aggiungiProdotto_quantitaNonDisponibile() throws Exception {
        Utente utente = new Utente("lollo@lollo.lol", "Tester12!", "User", "Test", 10, "Test 14b", "1112223330");



        MockHttpSession session = new MockHttpSession();
        session.setAttribute("utente", utente);

        String nomeProdotto = "Tazza 3D Pokeball";
        int quantita = 1;
        mockMvc.perform(post("/aggiungi/Tazza 3D Pokeball")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nomeProdotto)
                        .session(session))
                .andExpect(status().isOk());

        mockMvc.perform(post("/aggiungi/Tazza 3D Pokeball")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nomeProdotto)
                        .session(session))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("La quantità non è disponibile. La quantità massima di " + nomeProdotto + " è di " + quantita));
    }


    @Test
    @Commit
    void rimuoviProdotto_prodottoEsistente() throws Exception {

        Utente utente = new Utente("lollo@lollo.lol", "Tester12!", "User", "Test", 10, "Test 14b", "1112223330");

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("utente", utente);


        String nomeProdotto = "Tazza 3D Pokeball";

        mockMvc.perform(post("/rimuovi/Tazza 3D Pokeball")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nomeProdotto)
                        .session(session))
                .andExpect(status().isOk());

        PCarrelloId carrelloId = new PCarrelloId(nomeProdotto, utente.getEmail());
        Optional<PCarrello> carrello = pCarrelloRepository.findById(carrelloId);
        assertFalse(carrello.isPresent());
    }

    @Test
    @Commit
    void rimuoviProdotto_prodottoNonEsistente() throws Exception {

        Utente utente = new Utente("lollo@lollo.lol", "Tester12!", "User", "Test", 10, "Test 14b", "1112223330");

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("utente", utente);


        String nomeProdotto = "lost"; // Il nome del prodotto

        mockMvc.perform(post("/rimuovi/lost") // URL senza {nome}
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nomeProdotto) // Invia solo il nome come stringa
                        .session(session))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Prodotto non presente"));

    }

    @Test
    @Commit
    void modificaQuantita_ProdottoEsistente() throws Exception {
        Utente utente = new Utente("lollo@lollo.lol", "Tester12!", "User", "Test", 10, "Test 14b", "1112223330");

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("utente", utente);

        String nomeProdotto = "The legend of Zelda Action figure Link";
        String qnt = "10";

        mockMvc.perform(post("/aggiungi/The legend of Zelda Action figure Link")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nomeProdotto)
                        .session(session))
                .andExpect(status().isOk());

        mockMvc.perform(post("/aumentaQnt")
                        .param("nomeProdotto", nomeProdotto)
                        .param("quantita", qnt)
                        .session(session))
                .andExpect(status().isOk());

        PCarrelloId carrelloId = new PCarrelloId(nomeProdotto, utente.getEmail());
        Optional<PCarrello> carrello = pCarrelloRepository.findById(carrelloId);
        assertTrue(carrello.isPresent());
        assertEquals(10, carrello.get().getQuantita());
        assertEquals(nomeProdotto, carrello.get().getProdotto().getNome());
        assertEquals(utente.getEmail(), carrello.get().getUtente().getEmail());
    }

    @Test
    @Commit
    void modificaQuantita_ProdottoNonEsistente() throws Exception {
        Utente utente = new Utente("lollo@lollo.lol", "Tester12!", "User", "Test", 10, "Test 14b", "1112223330");

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("utente", utente);

        String nomeProdotto = "lost";
        String qnt = "10";


        mockMvc.perform(post("/aumentaQnt")
                        .param("nomeProdotto", nomeProdotto)
                        .param("quantita", qnt)
                        .session(session))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Prodotto non presente"));
    }

    @Test
    @Commit
    void modificaQuantita_ValoreNonIntero() throws Exception {
        Utente utente = new Utente("lollo@lollo.lol", "Tester12!", "User", "Test", 10, "Test 14b", "1112223330");

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("utente", utente);

        String nomeProdotto = "The legend of Zelda Action figure Link";
        String qnt = "a";


        mockMvc.perform(post("/aumentaQnt")
                        .param("nomeProdotto", nomeProdotto)
                        .param("quantita", qnt)
                        .session(session))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("La quantità deve essere un numero intero valido."));
    }

    @Test
    @Commit
    void modificaQuantita_QuantitaNonDisponibile() throws Exception {
        Utente utente = new Utente("lollo@lollo.lol", "Tester12!", "User", "Test", 10, "Test 14b", "1112223330");

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("utente", utente);

        String nomeProdotto = "The legend of Zelda Action figure Link";
        String qnt = "44";


        mockMvc.perform(post("/aumentaQnt")
                        .param("nomeProdotto", nomeProdotto)
                        .param("quantita", qnt)
                        .session(session))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("La quantità non è disponibile. La quantità massima di " + nomeProdotto + " è di 43"));
    }

    @Test
    void aggiungiCarrello_utenteNonAutenticato() throws Exception {
        String nomeProdotto = "Cassette Beasts";
        mockMvc.perform(post("/aggiungi/Cassette Beasts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nomeProdotto))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void aggiungiCarrello_utenteGestore() throws Exception {
        Utente gestore = new Utente("gestore@test.com", "password", "Gestore", "Test", 10, "Test 14b", "1112223330");
        gestore.setRuolo("gestore");

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("utente", gestore);

        String nomeProdotto = "Cassette Beasts";
        mockMvc.perform(post("/aggiungi/Cassette Beasts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session)
                        .content(nomeProdotto))
                .andExpect(status().isForbidden());
    }

    @Test
    void rimuoviCarrello_utenteNonAutenticato() throws Exception {
        String nomeProdotto = "Cassette Beasts";
        mockMvc.perform(post("/aggiungi/Cassette Beasts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nomeProdotto))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void modificaCarrello_utenteNonAutenticato() throws Exception {
        String nomeProdotto = "Cassette Beasts";
        String qnt = "44";


        mockMvc.perform(post("/aumentaQnt")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("nomeProdotto", nomeProdotto)
                        .param("quantita", qnt))
                .andExpect(status().isUnauthorized())
                .andExpect(header().string("Location", "/utenti/login"));
    }
}