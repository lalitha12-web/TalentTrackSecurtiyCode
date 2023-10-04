package com.talentstream.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Getter
@Setter
@Entity
public class ApplicantProfile {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int applicantid;

    @Embedded
    private BasicDetails basicDetails;

    @Embedded
    private XClassDetails xClassDetails;

    @Embedded
    private IntermediateDetails intermediateDetails;

    @Embedded
    private GraduationDetails graduationDetails;

    @ElementCollection
    private List<String> skills;
    @Column(nullable = false)
    private String roles="ROLE_JOBAPPLICANT";

    @ElementCollection
    private List<ExperienceDetails> experienceDetails;

	public List<String> getSkills() {
		// TODO Auto-generated method stub
		return skills;
	}

	public int getApplicantid() {
		// TODO Auto-generated method stub
		return applicantid;
	}
}
