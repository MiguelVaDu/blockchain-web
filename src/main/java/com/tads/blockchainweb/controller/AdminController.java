package com.tads.blockchainweb.controller;

import com.tads.blockchainweb.model.Transaction;
import com.tads.blockchainweb.model.UserDetail;
import com.tads.blockchainweb.repository.TransactionRepository;
import com.tads.blockchainweb.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.tads.blockchainweb.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired private ReportService reportService;
    @Autowired private UserService userService;
    @Autowired private TransactionRepository txRepo;

    @GetMapping("/transfers")
    public String verTransfers(Model model) {
        List<Transaction> lista = txRepo.findAll();
        // Mapa ID→username
        var userMap = userService.findAll().stream()
                .collect(Collectors.toMap(UserDetail::getId, UserDetail::getUsername));
        model.addAttribute("transfers", lista);
        model.addAttribute("userMap", userMap);
        return "admin/transfers";
    }

    @GetMapping("/minerReport")
    public String verMinerReport(Model model) {
        List<ReportService.MineroReporte> reportes = reportService.reportePorMiner();
        model.addAttribute("reportes", reportes);
        return "admin/minerReport";
    }
}