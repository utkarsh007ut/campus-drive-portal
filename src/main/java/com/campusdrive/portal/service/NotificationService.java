package com.campusdrive.portal.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Fires on every status change: drive accepted/declined, job published,
 * application shortlisted/rejected/selected. Currently logs to console -
 * swap the body of notify() for real email/SMS later without touching any
 * of the services that call this.
 */
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