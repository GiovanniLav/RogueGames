package com.roguegames.web.controller;

import com.roguegames.domain.comandi.AggiungiCommand;
import com.roguegames.domain.comandi.CommandManager;
import com.roguegames.domain.comandi.Command;
import com.roguegames.domain.comandi.RimuoviCommand;
import com.roguegames.domain.entity.Preferiti;
import com.roguegames.domain.entity.Utente;  // Aggiungi l'importazione di Utente
import com.roguegames.domain.service.PreferitiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/utenti")
public class PreferitiController {

    @Autowired
    private PreferitiService preferitiService;

    private final CommandManager commandManager = new CommandManager();

    // Mostra i preferiti dell'utente loggato
    @GetMapping("/preferiti")
    public String mostraPreferiti(Model model, HttpSession session) {
        Utente utente = (Utente) session.getAttribute("utente");  // Recupera l'utente dalla sessione

        // Controlla se l'utente è loggato


        // Recupera la lista dei prodotti preferiti per l'utente
        List<Preferiti> preferiti = preferitiService.trovaPerUtente(utente);

        // Aggiungi la lista dei preferiti al modello
        model.addAttribute("preferiti", preferiti);
        model.addAttribute("utente", utente);
        return "Preferiti";  // Restituisci la vista dei preferiti
    }

    // Aggiungi o rimuovi un prodotto dai preferiti
    @PostMapping("/preferiti/toggle/{nome}")
    public String togglePreferiti(@PathVariable String nome, HttpSession session) {
        Utente utente = (Utente) session.getAttribute("utente");  // Recupera l'utente dalla sessione
        if (utente == null) {
            return "redirect:/utenti/login";  // Se l'utente non è loggato, lo reindirizza al login
        }

        Command comando;
        if (preferitiService.ePreferito(nome, utente)) {  // Verifica se il prodotto è già nei preferiti
            // Se il prodotto è già nei preferiti, rimuovilo
            comando = new RimuoviCommand(preferitiService, nome, utente);
        } else {
            // Altrimenti, aggiungilo ai preferiti
            comando = new AggiungiCommand(preferitiService, nome, utente);
        }

        commandManager.eseguiComando(comando);  // Usa il comando generico
        return "redirect:/catalogo/prodotti";  // Torna alla pagina del catalogo
    }
}



