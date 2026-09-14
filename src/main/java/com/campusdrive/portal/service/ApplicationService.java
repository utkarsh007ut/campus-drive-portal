package com.campusdrive.portal.service;

import com.campusdrive.portal.dto.ApplicationResponse;
import com.campusdrive.portal.dto.RejectApplicationRequest;
import com.campusdrive.portal.entity.*;
import com.campusdrive.portal.repository.ApplicationRepository;
import com.campusdrive.portal.repository.JobRepository;
import com.campusdrive.portal.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final StudentRepository studentRepository;
    private final NotificationService notificationService;

    public ApplicationService(ApplicationRepository applicationRepository, JobRepository jobRepository,
                              StudentRepository studentRepository, NotificationService notificationService) {
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
        this.studentRepository = studentRepository;
        this.notificationService = notificationService;
    }

    public ApplicationResponse apply(Long jobId, Long studentId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        if (applicationRepository.findByJobIdAndStudentId(jobId, studentId).isPresent()) {
            throw new IllegalStateException("You have already applied to this job");
        }

        // eligibility pre-filter - stops obviously ineligible applications before
        // they ever reach the college's screening queue
        if (job.getMinCgpa() != null && student.getCgpa() != null && student.getCgpa() < job.getMinCgpa()) {
            throw new IllegalStateException(
                    "Your CGPA (" + student.getCgpa() + ") does not meet this job's minimum requirement of " + job.getMinCgpa());
        }

        Application application = new Application();
        application.setJob(job);
        application.setStudent(student);
        application.setStatus(ApplicationStatus.APPLIED);

        return new ApplicationResponse(applicationRepository.save(application));
    }

    public ApplicationResponse shortlist(Long applicationId, Long collegeId) {
        Application app = getOwnedByCollege(applicationId, collegeId);
        app.setStatus(ApplicationStatus.FORWARDED_TO_COMPANY); // shortlisted = immediately forwarded, for MVP simplicity
        app.setDecidedAt(LocalDateTime.now());

        notificationService.notify(app.getStudent().getEmail(), "Application Shortlisted",
                "Your application for " + app.getJob().getTitle() + " has been shortlisted and forwarded to the company.");
        return new ApplicationResponse(applicationRepository.save(app));
    }

    public ApplicationResponse rejectByCollege(Long applicationId, Long collegeId, RejectApplicationRequest req) {
        Application app = getOwnedByCollege(applicationId, collegeId);
        app.setStatus(ApplicationStatus.REJECTED_BY_COLLEGE);
        app.setRejectionReason(req.getReason());
        app.setDecidedAt(LocalDateTime.now());

        notificationService.notify(app.getStudent().getEmail(), "Application Update",
                "Your application for " + app.getJob().getTitle() + " was not shortlisted. Reason: " + req.getReason());
        return new ApplicationResponse(applicationRepository.save(app));
    }

    public ApplicationResponse companyDecision(Long applicationId, Long companyId, boolean select) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));

        if (!app.getJob().getCampusDrive().getCompany().getId().equals(companyId)) {
            throw new IllegalStateException("This application is not for one of your jobs");
        }
        if (app.getStatus() != ApplicationStatus.FORWARDED_TO_COMPANY) {
            throw new IllegalStateException("Only forwarded applications can be decided by the company");
        }

        app.setStatus(select ? ApplicationStatus.SELECTED : ApplicationStatus.REJECTED_BY_COMPANY);
        app.setDecidedAt(LocalDateTime.now());

        notificationService.notify(app.getStudent().getEmail(), "Final Placement Decision",
                select ? "Congratulations! You have been selected for " + app.getJob().getTitle() + "."
                        : "You were not selected for " + app.getJob().getTitle() + ".");
        return new ApplicationResponse(applicationRepository.save(app));
    }

    public List<ApplicationResponse> getApplicationsForJob(Long jobId) {
        return applicationRepository.findByJobId(jobId).stream().map(ApplicationResponse::new).toList();
    }

    public List<ApplicationResponse> getApplicationsForStudent(Long studentId) {
        return applicationRepository.findByStudentId(studentId).stream().map(ApplicationResponse::new).toList();
    }

    private Application getOwnedByCollege(Long applicationId, Long collegeId) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));

        Long appCollegeId = app.getStudent().getCollege().getId();
        if (!appCollegeId.equals(collegeId)) {
            throw new IllegalStateException("This application is not from a student at your college");
        }
        if (app.getStatus() != ApplicationStatus.APPLIED) {
            throw new IllegalStateException("This application has already been decided");
        }
        return app;
    }
}