package com.tads.blockchainweb.controller;

import com.tads.blockchainweb.model.UserDetail;
import com.tads.blockchainweb.service.UserService;
import org.apache.catalina.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {
    private final UserService userService;
    // Inyección de dependencias a través del constructor
    public AuthController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @GetMapping("/")
    public String home(@AuthenticationPrincipal org.springframework.security.core.userdetails.User authUser, Model model) {// Parámetro corregido
        // Busca el usuario por su nombre de usuario
        UserDetail u = userService.findByUsername(authUser.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Añade atributos al modelo
        model.addAttribute("username", u.getUsername());
        model.addAttribute("saldo", u.getSaldo());

        return "home";
    }
}
