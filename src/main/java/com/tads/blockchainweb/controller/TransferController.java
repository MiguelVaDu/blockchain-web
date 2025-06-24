package com.tads.blockchainweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.tads.blockchainweb.model.UserDetail;
import com.tads.blockchainweb.service.TransactionService;
import com.tads.blockchainweb.service.UserService;

@Controller
public class TransferController {

    @Autowired
    private UserService userService;
    @Autowired
    private TransactionService txService;

    @GetMapping("/transfer")
    public String mostrarFormulario(Model model, @AuthenticationPrincipal User authUser) {
        String username = authUser.getUsername();
        UserDetail remitente = userService.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));
        model.addAttribute("saldo", remitente.getSaldo());
        model.addAttribute("usuarios", userService.findAllExcept(remitente.getId()));
        model.addAttribute("txForm", new TxForm());
        return "transfer";
    }

    @PostMapping("/transfer")
    public String procesarTransfer(
            @ModelAttribute("txForm") TxForm txForm,
            @AuthenticationPrincipal User authUser,
            Model model
    ) {
        UserDetail remitente = userService.findByUsername(authUser.getUsername())
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));
        // Validar input
        if (txForm.getCantidad() <= 0) {
            model.addAttribute("error", "Cantidad debe ser mayor a cero.");
            return mostrarFormulario(model, authUser);
        }
        if (txForm.getCantidad() > remitente.getSaldo()) {
            model.addAttribute("error", "Saldo insuficiente.");
            return mostrarFormulario(model, authUser);
        }
        try {
            txService.crearYProcesarTransaccion(remitente.getId(), txForm.getReceptorId(), txForm.getCantidad());
            model.addAttribute("mensaje", "Transferencia registrada correctamente. Se procesará en breve.");
        } catch (Exception e) {
            model.addAttribute("error", "Error al procesar transferencia: " + e.getMessage());
            return mostrarFormulario(model, authUser);
        }
        return "transferResult";
    }

    // Clase interna para formulario
    public static class TxForm {
        private Long receptorId;
        private double cantidad;
        public Long getReceptorId() { return receptorId; }
        public void setReceptorId(Long receptorId) { this.receptorId = receptorId; }
        public double getCantidad() { return cantidad; }
        public void setCantidad(double cantidad) { this.cantidad = cantidad; }
    }
}