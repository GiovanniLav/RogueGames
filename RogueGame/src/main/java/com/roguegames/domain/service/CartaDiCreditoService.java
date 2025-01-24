package com.roguegames.domain.service;

import com.roguegames.domain.entity.CartaDiCredito;
import com.roguegames.domain.entity.Utente;
import com.roguegames.domain.repository.CartaDiCreditoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartaDiCreditoService {

    @Autowired
    private CartaDiCreditoRepository cartaDiCreditoRepository;

    // Salva una carta di credito
    public void saveCarta(CartaDiCredito carta) {
        cartaDiCreditoRepository.save(carta);
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
}
