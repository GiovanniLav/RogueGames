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
        String hashPass= utenteService.hashPassword(password);
        try {
            Utente utente = utenteService.verificaCredenziali(email, hashPass);

            String ruolo = utente.getRuolo();
            if ("gestore".equals(ruolo)) {
                session.setAttribute("isGestore", true);
            } else {
                session.setAttribute("isGestore", false);
            }
            session.setAttribute("utente", utente);
            return "redirect:/utenti/home";
        }catch(IllegalArgumentException e){
            model.addAttribute("error", true);
            return "redirect:/utenti/login";
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

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "Logout";  // Reindirizza alla home
    }
}
