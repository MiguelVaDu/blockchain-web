package com.tads.blockchainweb.controller;

import com.tads.blockchainweb.model.Transaction;
import com.tads.blockchainweb.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tads.blockchainweb.model.UserDetail;
import com.tads.blockchainweb.service.TransactionService;
import com.tads.blockchainweb.service.UserService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class TransferController {

    @Autowired private UserService userService;
    @Autowired private TransactionService txService;
    @Autowired private TransactionRepository txRepo; // para myTransfers
    @Autowired private PasswordEncoder passwordEncoder; // para profile

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
            Model model,
            RedirectAttributes redirectAttributes
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
            redirectAttributes.addFlashAttribute("mensaje", "Transferencia registrada correctamente. Se procesará en breve.");
        } catch (Exception e) {
            model.addAttribute("error", "Error al procesar transferencia: " + e.getMessage());
            return mostrarFormulario(model, authUser);
        }
        return "home";
    }

    @GetMapping("/transferResult")
    public String mostrarResultadoTransferencia() {
        return "transferResult";
    }

    @GetMapping("/myTransfers")
    public String misTransfers(Model model, @AuthenticationPrincipal User authUser) {
        UserDetail u = userService.findByUsername(authUser.getUsername()).orElseThrow();
        List<Transaction> lista = txRepo.findById1OrId2OrderByFechaAscHoraAsc(u.getId(), u.getId());
        Map<Long, UserDetail> userMap = userService.findAll().stream()
                .collect(Collectors.toMap(UserDetail::getId, user -> user)); // Cambiado aquí
        model.addAttribute("myTransfers", lista);
        model.addAttribute("userMap", userMap);
        model.addAttribute("userId", u.getId());
        return "myTransfers";
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