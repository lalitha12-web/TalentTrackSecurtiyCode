package com.talentstream.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.talentstream.entity.ApplicantProfile;
import com.talentstream.entity.Job;
import com.talentstream.entity.RecommendedJob;
import com.talentstream.repository.ApplicantProfileRepository;
import com.talentstream.repository.JobRepository;

import net.bytebuddy.dynamic.DynamicType.Builder.FieldDefinition.Optional;

@Service

public class RecommendedJobsService {

	

	 @Autowired

	    private ApplicantProfileRepository applicantProfileRepository;

	

	@Autowired  

	private JobRepository  jobRepository;

 

		 public List<RecommendedJob> getRecommendedJobsForAllApplicants() {

		 List<ApplicantProfile> allApplicants = applicantProfileRepository.findAll();

	        List<Job> allJobs = jobRepository.findAll();

 

	        List<RecommendedJob> recommendedJobs = new ArrayList<>();

 

	        for (ApplicantProfile applicant : allApplicants) {

	            for (Job job : allJobs) {

	                boolean skillMatch = hasMatchingSkill(applicant.getSkills(), job.getSkillsRequired());

	                if (skillMatch) {

	                    RecommendedJob recommendedJob = new RecommendedJob(

	                        applicant.getApplicantid(),

	                        job.getId(),

	                        job.getJobTitle(),

	                        job.getJobRecruiter().getCompanyname(),

	                        job.getLocation(),

	                        job.getMinSalary(),

	                        job.getMaxSalary(),

	                        job.getMinimumExperience(),

	                        job.getMaximumExperience()

	                    );

	                    recommendedJobs.add(recommendedJob);

	                }

	            }

	        }

 

	        return recommendedJobs;

	    }

 

	    private boolean hasMatchingSkill(List<String> applicantSkills, List<String> jobSkillsRequired) {

	        // Check if there is any common skill between applicant and job

	        for (String applicantSkill : applicantSkills) {

	            if (jobSkillsRequired.contains(applicantSkill)) {

	                return true;

	            }

	        }

	        return false;

	    }

 

 

 

	    public List<RecommendedJob> getRecommendedJobsForApplicant(int applicantId) {

	        java.util.Optional<ApplicantProfile> applicantProfileOptional = applicantProfileRepository.findById(applicantId);

 

	        if (applicantProfileOptional.isPresent()) {

	            ApplicantProfile applicant = applicantProfileOptional.get();

	            List<Job> allJobs = jobRepository.findAll();

 

	            List<RecommendedJob> recommendedJobs = new ArrayList<>();

 

	            for (Job job : allJobs) {

	                boolean skillMatch = hasMatchingSkill(applicant.getSkills(), job.getSkillsRequired());

	                if (skillMatch) {

	                    RecommendedJob recommendedJob = new RecommendedJob(

	                        applicant.getApplicantid(),

	                        job.getId(),

	                        job.getJobTitle(),

	                        job.getJobRecruiter().getCompanyname(),

	                        job.getLocation(),

	                        job.getMinSalary(),

	                        job.getMaxSalary(),

	                        job.getMinimumExperience(),

	                        job.getMaximumExperience()

	                    );

	                    recommendedJobs.add(recommendedJob);

	                }

	            }

 

	            return recommendedJobs;

	        } else {

	            

	            return Collections.emptyList();

	        }

	    }

	

}
