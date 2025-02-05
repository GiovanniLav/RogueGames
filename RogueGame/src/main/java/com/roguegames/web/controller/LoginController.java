package com.roguegames.web.controller;

import com.roguegames.domain.entity.Prodotto;
import com.roguegames.domain.entity.Utente;
import com.roguegames.domain.service.ProdottoService;
import com.roguegames.domain.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
@RequestMapping("/utenti")
public class LoginController {

    @Autowired
    private UtenteService utenteService;

    @Autowired
    private ProdottoService prodottoService;

    // Mostra la pagina di login
    @GetMapping("/login")
    public String showLoginForm(Model model, HttpSession session) {
        Utente utente = (Utente) session.getAttribute("utente");
        if (utente != null) {
            return "redirect:/utenti/home";
        }
        model.addAttribute("error", false);
        return "Login";  // Restituisce la pagina login.html
    }

    // Gestisce il login
    @PostMapping("/login")
    public String loginUtente(@RequestParam("email") String email,
                              @RequestParam("password") String password,
                              HttpSession session,
                              Model model) {
        try {
            // Hash della password prima di verificarla
            String hashPass = UtenteService.hashPassword(password);

            // Verifica credenziali
            Utente utente = utenteService.verificaCredenziali(email, hashPass);

            // Gestione ruolo utente nella sessione
            String ruolo = utente.getRuolo();
            session.setAttribute("isGestore", "gestore".equals(ruolo));

            session.setAttribute("utente", utente);
            return "redirect:/utenti/home";  // Reindirizza alla home dopo il login
        } catch (IllegalArgumentException e) {
            // Se l'eccezione è stata sollevata, mostra un messaggio di errore nella pagina di login
            model.addAttribute("error", e.getMessage());
            return "Login"; // Mostra di nuovo la pagina di login con il messaggio di errore
        }
    }



    // Mostra la dashboard (protetta)
    @GetMapping("/home")
    public String dashboard(HttpSession session, Model model) {
        Utente utente = (Utente) session.getAttribute("utente");
        if (utente == null) {
            return "redirect:/utenti/login";  // Reindirizza al login se l'utente non è loggato
        }
        List<Prodotto> prodotti = prodottoService.get6RandomProdotto();
        List<Prodotto> fantasyProducts= prodottoService.getFantasy();
        List<Prodotto> consoleProducts= prodottoService.getConsole();
        model.addAttribute("fantasyProducts", fantasyProducts);
        model.addAttribute("consoleProducts", consoleProducts);
        model.addAttribute("prodotti", prodotti);
        model.addAttribute("utente", utente);
        return "Home";  // Mostra la pagina di dashboard
    }
}
