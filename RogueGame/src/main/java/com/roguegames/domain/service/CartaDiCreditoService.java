package com.roguegames.domain.service;

import com.roguegames.domain.entity.CartaDiCredito;
import com.roguegames.domain.entity.Utente;
import com.roguegames.domain.repository.CartaDiCreditoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CartaDiCreditoService {

    @Autowired
    private CartaDiCreditoRepository cartaDiCreditoRepository;

    // Salva una carta di credito con validazioni
    public void saveCarta(CartaDiCredito carta) {
        if (isValidCif(carta.getCif()) && isValidScadenza(carta.getScadenza()) && isValidCvv(carta.getCvv())) {
            cartaDiCreditoRepository.save(carta);
        } else {
            throw new IllegalArgumentException("Dati della carta non validi");
        }
    }

    // Ottieni le carte di credito di un utente
    public List<CartaDiCredito> getCarteByUtente(Utente utente) {
        return cartaDiCreditoRepository.findByUtente(utente);
    }

    // Ottieni una carta di credito tramite il CIF
    public CartaDiCredito getCartaByCif(String cif) {
        return cartaDiCreditoRepository.findById(cif).orElse(null);
    }

    // Elimina una carta di credito
    public void deleteCarta(CartaDiCredito carta) {
        cartaDiCreditoRepository.delete(carta);
    }

    // Controlla se il CIF è valido (16 cifre numeriche)
    private boolean isValidCif(String cif) {
        return cif != null && cif.matches("\\d{16}");
    }

    // Controlla se la scadenza è valida (MM/YY e non passata)
    private boolean isValidScadenza(String scadenza) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
            YearMonth expiryDate = YearMonth.parse(scadenza, formatter);
            return !expiryDate.isBefore(YearMonth.now());
        } catch (Exception e) {
            return false;
        }
    }

    // Controlla se il CVV è valido (3 cifre numeriche)
    private boolean isValidCvv(String cvv) {
        return cvv != null && cvv.matches("\\d{3}");
    }
}