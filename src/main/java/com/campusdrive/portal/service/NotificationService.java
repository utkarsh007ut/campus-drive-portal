package com.campusdrive.portal.service;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class NotificationService {
    public void notify(String toEmail, String subject, String body) {
        System.out.println("=".repeat(60));
        System.out.println("NOTIFICATION  [" + LocalDateTime.now() + "]");
        System.out.println("To:      " + toEmail);
        System.out.println("Subject: " + subject);
        System.out.println("Body:    " + body);
        System.out.println("=".repeat(60));
    }
}