package com.campusdrive.portal.service;

import com.campusdrive.portal.entity.CampusDrive;
import com.campusdrive.portal.entity.DriveStatus;
import com.campusdrive.portal.repository.CampusDriveRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DriveExpiryScheduler {

    private final CampusDriveRepository driveRepository;

    public DriveExpiryScheduler(CampusDriveRepository driveRepository) {
        this.driveRepository = driveRepository;
    }

   @Scheduled(fixedRate = 60 * 60 * 1000)


    public void expireOverdueDrives() {
        List<CampusDrive> overdue = driveRepository
                .findByStatusAndExpiresAtBefore(DriveStatus.REQUESTED, LocalDateTime.now());

        for (CampusDrive drive : overdue) {

            drive.setStatus(DriveStatus.EXPIRED);
            drive.setRespondedAt(LocalDateTime.now());


        }

        if (!overdue.isEmpty()) {
            driveRepository.saveAll(overdue);
            System.out.println("Expired " + overdue.size() + " overdue drive request(s).");
        }
    }
}