package com.roguegames.web.controller;

import com.roguegames.domain.entity.Utente;
import com.roguegames.domain.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/utenti")
public class ProfiloController {

    @Autowired
    private UtenteService utenteService;

    // Metodo per visualizzare il profilo dell'utente
    @GetMapping("/profilo")
    public String visualizzaProfilo(HttpSession session, Model model) {
        Utente utente = (Utente) session.getAttribute("utente");
        if (utente == null) {
            return "redirect:/utenti/login";  // Se l'utente non è loggato, lo reindirizza al login
        }

        model.addAttribute("utente", utente); // Passa l'utente al modello per visualizzarlo nella pagina
        return "profilo"; // Ritorna il nome della pagina HTML del profilo
    }

    // Metodo per modificare un campo del profilo
    @PostMapping("/modifica")
    @ResponseBody
    public ResponseEntity<?> modificaCampo(HttpSession session, @RequestBody Map<String, String> request) {
        Utente utente = (Utente) session.getAttribute("utente");
        if (utente == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utente non loggato");
        }

        String campo = request.get("campo");
        String valore = request.get("valore");

        try {
            switch (campo) {
                case "email":
                    utente.setEmail(valore);
                    break;
                case "nome":
                    utente.setNome(valore);
                    break;
                case "cognome":
                    utente.setCognome(valore);
                    break;
                case "eta":
                    utente.setEta(Integer.parseInt(valore));
                    break;
                case "password":
                    utente.setPassword(valore); // Aggiungi hashing della password in produzione
                    break;
                default:
                    return ResponseEntity.badRequest().body("Campo non valido");
            }

            utenteService.aggiornaUtente(utente); // Salva le modifiche
            return ResponseEntity.ok("Campo aggiornato");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante l'aggiornamento");
        }
    }
}
