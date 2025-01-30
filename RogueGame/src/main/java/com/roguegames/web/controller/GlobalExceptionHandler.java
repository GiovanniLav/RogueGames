package com.roguegames.web.controller;

import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpSession;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ModelAndView handleMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, HttpSession session) {
        session.invalidate();
        ModelAndView modelAndView = new ModelAndView("error/methodNotSupported"); // Sostituisci con il nome della tua view
        modelAndView.addObject("error", "Si sono verificati alcuni errori. Ci scusiamo per il disagio");
        return modelAndView;
    }

    public ModelAndView handleGeneralException(Exception ex, HttpSession session) {
        session.invalidate();
        ModelAndView modelAndView = new ModelAndView("error/methodNotSupported"); // Nome della tua view personalizzata
        modelAndView.addObject("error", "Si è verificato un errore interno. Ci scusiamo per il disagio.");
        // Log dell'errore per debugging (opzionale)
        ex.printStackTrace();

        return modelAndView;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView handleNotFoundException(NoHandlerFoundException ex, HttpSession session) {
        session.invalidate();
        ModelAndView modelAndView = new ModelAndView("error/methodNotSupported"); // View personalizzata
        modelAndView.addObject("error", "La pagina richiesta non esiste.");
        return modelAndView;
    }
}