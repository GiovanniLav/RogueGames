package com.roguegames;
import com.roguegames.domain.entity.Prodotto;
import com.roguegames.domain.repository.GestoreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

import java.util.Optional;

import static com.roguegames.domain.entity.Prodotto.Piattaforma.PlayStation;
import static com.roguegames.domain.entity.Prodotto.Tipo.Videogiochi;
import static org.junit.jupiter.api.Assertions.*;

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

    @Autowired
    private GestoreRepository gestoreRepository;
    @Test
    @Commit
    public void aggiungiProdotto_NonEsistente() throws Exception {
        Utente utente = new Utente("lollo@lollo.lol", "Tester12!", "User", "Test", 10, "Test 14b", "1112223330");
        Prodotto prodotto = new Prodotto("God of War Ragnarök", "Gow4.jpg", "Gow.mp4.mp4", "descrizione prodotto test", true, 10.00, "Test Srl", PlayStation, "Fantasy", Videogiochi, dataRilascio, 80);
        utente.setRuolo("gestore");

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
}
