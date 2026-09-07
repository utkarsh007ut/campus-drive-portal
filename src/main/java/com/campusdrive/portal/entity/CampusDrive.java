package com.campusdrive.portal.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "campus_drives")
@Getter
@Setter
@NoArgsConstructor
public class CampusDrive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "college_id", nullable = false)
    private College college;

    @Column(length = 2000, nullable = false)
    private String proposedRequirements;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DriveStatus status = DriveStatus.REQUESTED;

    @Column(length = 1000)
    private String declineNote; // only ever set when status = DECLINED

    private LocalDateTime requestedAt = LocalDateTime.now();
    private LocalDateTime respondedAt;
    private LocalDateTime expiresAt;

    // if this drive is a resend after a decline, this points to the original.
    // null for a first-time request.
    private Long previousDriveId;
}