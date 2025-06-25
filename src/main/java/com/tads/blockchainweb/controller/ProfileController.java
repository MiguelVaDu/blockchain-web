package com.tads.blockchainweb.controller;

import com.tads.blockchainweb.model.UserDetail;
import com.tads.blockchainweb.service.UserService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProfileController {
    @Autowired private UserService userService;
    @Autowired private PasswordEncoder passwordEncoder;

    @GetMapping("/profile")
    public String perfilForm(Model model, @AuthenticationPrincipal User authUser) {
        UserDetail u = userService.findByUsername(authUser.getUsername()).orElseThrow();
        model.addAttribute("user", u);
        return "profile";
    }

    @PostMapping("/profile")
    public String updatePerfil(@ModelAttribute UserDetail dto,
                               @AuthenticationPrincipal User authUser,
                               Model model) {
        UserDetail u = userService.findByUsername(authUser.getUsername()).orElseThrow();
        u.setNomcompleto(dto.getNomcompleto());
        u.setEmail(dto.getEmail());
        u.setDni(dto.getDni());
        userService.save(u);
        model.addAttribute("user", u);
        model.addAttribute("msg", "Perfil actualizado");
        return "profile";
    }

    @PostMapping("/profile/password")
    public String changePassword(@RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 @AuthenticationPrincipal User authUser,
                                 Model model) {
        UserDetail u = userService.findByUsername(authUser.getUsername()).orElseThrow();
        if (!passwordEncoder.matches(oldPassword, u.getPassword())) {
            model.addAttribute("msg", "Contraseña actual incorrecta");
        } else {
            userService.changePassword(u.getId(), newPassword);
            model.addAttribute("msg", "Contraseña cambiada correctamente");
        }
        model.addAttribute("user", u);
        return "profile";
    }
}
