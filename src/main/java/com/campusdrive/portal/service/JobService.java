package com.campusdrive.portal.service;

import com.campusdrive.portal.dto.JobResponse;
import com.campusdrive.portal.dto.PublishJobRequest;
import com.campusdrive.portal.entity.CampusDrive;
import com.campusdrive.portal.entity.DriveStatus;
import com.campusdrive.portal.entity.Job;
import com.campusdrive.portal.repository.CampusDriveRepository;
import com.campusdrive.portal.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final CampusDriveRepository driveRepository;
    private final NotificationService notificationService;


    public JobService(JobRepository jobRepository, CampusDriveRepository driveRepository,
                      NotificationService notificationService) {
        this.jobRepository = jobRepository;
        this.driveRepository = driveRepository;
        this.notificationService=notificationService;
    }

    public JobResponse publishJob(Long driveId, Long collegeId, PublishJobRequest req) {
        CampusDrive drive = driveRepository.findById(driveId)
                .orElseThrow(() -> new IllegalArgumentException("Drive not found"));

        if (!drive.getCollege().getId().equals(collegeId)) {
            throw new IllegalStateException("This drive was not sent to your college");
        }
        if (drive.getStatus() != DriveStatus.ACCEPTED) {
            throw new IllegalStateException("Only an ACCEPTED drive can have a job published");
        }
        if (jobRepository.findByCampusDriveId(driveId).isPresent()) {
            throw new IllegalStateException("A job has already been published for this drive");
        }

        Job job = new Job();
        job.setCampusDrive(drive);
        job.setTitle(req.getTitle());
        job.setDescription(req.getDescription());
        job.setRequirements(req.getRequirements());
        job.setMinCgpa(req.getMinCgpa());

        notificationService.notify(drive.getCompany().getEmail(), "Job Published",
                drive.getCollege().getName() + " has published a job for your accepted drive: " + req.getTitle());

        return new JobResponse(jobRepository.save(job));
    }

    public List<JobResponse> getAllJobs() {
        return jobRepository.findAll().stream().map(JobResponse::new).toList();
    }
}