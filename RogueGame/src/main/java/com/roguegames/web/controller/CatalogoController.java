package com.roguegames.web.controller;

import com.roguegames.domain.entity.Prodotto;
import com.roguegames.domain.entity.Utente;
import com.roguegames.domain.service.PreferitiService;
import com.roguegames.domain.service.ProdottoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/catalogo") // Base path per tutte le operazioni relative al catalogo
public class CatalogoController {

    @Autowired
    private ProdottoService prodottoService;

    @Autowired
    private PreferitiService preferitiService;

    @GetMapping("/prodotti") // Gestisce sia utenti loggati che non loggati
    public String mostraProdotti(Model model, HttpSession session) {
        // Recupera l'utente dalla sessione
        Object utenteObject = session.getAttribute("utente");
        boolean utenteLoggato = (utenteObject != null);

        // Ottieni tutti i prodotti
        List<Prodotto> prodotti = prodottoService.getAllProdotti();

        if (utenteLoggato) {
            // Se l'utente è loggato, aggiungi i preferiti
            Utente utente = (Utente) utenteObject; // Cast dell'oggetto utente
            model.addAttribute("utente", utente);
            for (Prodotto prodotto : prodotti) {
                boolean isPreferito = preferitiService.ePreferito(prodotto.getNome(), utente);
                prodotto.setPreferito(isPreferito);
            }
        }

        model.addAttribute("products", prodotti);

        return "Catalogo"; // Ritorna alla pagina del catalogo
    }
}
