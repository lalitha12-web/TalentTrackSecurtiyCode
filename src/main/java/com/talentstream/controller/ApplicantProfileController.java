package com.talentstream.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.talentstream.entity.ApplicantProfile;
import com.talentstream.service.ApplicantProfileService;
@RestController
public class ApplicantProfileController {
	private final ApplicantProfileService applicantProfileService;

    @Autowired
    public ApplicantProfileController(ApplicantProfileService applicantProfileService) {
        this.applicantProfileService = applicantProfileService;
    }

   @PostMapping("/insertdata")
    public ResponseEntity<ApplicantProfile> createOrUpdateApplicantProfile(@RequestBody ApplicantProfile applicantProfile) {
        ApplicantProfile savedProfile = applicantProfileService.createOrUpdateApplicantProfile(applicantProfile);
        return ResponseEntity.ok(savedProfile);
    }
   @GetMapping("/{applicantId}")
   public ResponseEntity<ApplicantProfile> getApplicantProfileById(@PathVariable int applicantId) {
       ApplicantProfile applicantProfile = applicantProfileService.getApplicantProfileById(applicantId);
       if (applicantProfile != null) {
           return ResponseEntity.ok(applicantProfile);
       } else {
           return ResponseEntity.notFound().build();
       }
   }
       @DeleteMapping("/{applicantId}")
       public ResponseEntity<Void> deleteApplicantProfile(@PathVariable int applicantId) {
           applicantProfileService.deleteApplicantProfile(applicantId);
           return ResponseEntity.noContent().build();
       }
}
