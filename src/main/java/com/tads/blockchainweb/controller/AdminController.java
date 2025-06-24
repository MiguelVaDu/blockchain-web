package com.tads.blockchainweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.tads.blockchainweb.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired private ReportService reportService;

    @GetMapping("/transfers")
    public String verTransfers(Model model) {
        model.addAttribute("transfers", reportService.todasTransacciones());
        return "admin/transfers";
    }

    @GetMapping("/minerReport")
    public String verMinerReport(Model model) {
        model.addAttribute("reporte", reportService.reporteGananciasPorMiner());
        return "admin/minerReport";
    }
}