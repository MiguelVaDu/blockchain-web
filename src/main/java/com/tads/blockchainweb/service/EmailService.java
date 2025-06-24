package com.tads.blockchainweb.service;

import com.tads.blockchainweb.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;
    public void enviarCorreoConfirmacion(String to, Transaction tx) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("Transferencia Realizada");
        msg.setText("Tu transferencia a usuario ID " + tx.getId2()
                + " por S/." + tx.getCantidad()
                + " ha sido procesada el " + tx.getFecha()
                + " a las " + tx.getHora() + ".");
        mailSender.send(msg);
    }
}
