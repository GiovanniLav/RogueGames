package com.roguegames.web.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;



@ControllerAdvice

public class CustomErrorController implements ErrorController {

    private static final String PATH = "/error";



    @RequestMapping(value = PATH)
    public String error() {
        return "redirect:/";
    }


    public String getErrorPath() {
        return PATH;
    }
}

