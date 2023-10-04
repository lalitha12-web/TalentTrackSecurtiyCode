package com.talentstream.controller;
import java.util.HashMap;

import java.util.List;

import java.util.Map;

 

 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.talentstream.entity.ApplicantRegistration;
import com.talentstream.entity.AuthenticationResponse;
import com.talentstream.entity.Login;

import com.talentstream.entity.NewPasswordRequest;

import com.talentstream.entity.OtpVerificationRequest;
import com.talentstream.entity.RecruiterLogin;
import com.talentstream.response.ResponseHandler;
import com.talentstream.service.EmailService;
import com.talentstream.service.JwtUtil;
import com.talentstream.service.MyUserDetailsService;
import com.talentstream.service.OtpService;

import com.talentstream.service.RegisterService;
import com.talentstream.service.JobRecruiterService;

 

 

@RestController

public class RegisterController {
	@Autowired
    MyUserDetailsService myUserDetailsService;
	 @Autowired

	    private OtpService otpService;

		 private Map<String, Boolean> otpVerificationMap = new HashMap<>();

		 @Autowired
			private AuthenticationManager authenticationManager;
		     @Autowired
			private JwtUtil jwtTokenUtil;
		    

	    @Autowired
	    private EmailService emailService;

 

		@Autowired

	     RegisterService regsiterService;
		@Autowired
		JobRecruiterService recruiterService;
	   

	    @Autowired

	    public RegisterController(RegisterService regsiterService)

	    {

	        this.regsiterService = regsiterService;

	    }

 

	

 

	    @PostMapping("/saveApplicant")

	    public ResponseEntity<String> register(@RequestBody ApplicantRegistration applicant) {

	       return regsiterService.saveapplicant(applicant);

	    }

 

//	   @PostMapping("/applicantlogin")
//
//	    public String login(@RequestBody Login loginRequest) throws Exception {
//
//	        boolean loginResult = regsiterService.login(loginRequest.getEmail(), loginRequest.getPassword());
//
//
//
//	        System.out.println(loginResult);
//
//	        System.out.println(loginRequest.getEmail());
//
// 
//
//	        if (loginResult) {
//
//	        	return "login sucessful";
//
//	        } else {
//
//	            return "invalid login ";
//
//	        }
//
//	    }
	    
	    @PostMapping("/applicantLogin")
	    public ResponseEntity<Object> login(@RequestBody Login loginRequest) throws Exception {
	    	 ApplicantRegistration applicant = regsiterService.login(loginRequest.getEmail(), loginRequest.getPassword());

	 

	        //System.out.println(loginResult);
	        System.out.println(loginRequest.getEmail());
	        
	        System.out.println(applicant.getEmail());

	        if (applicant!=null) {
	        	return createAuthenticationToken(applicant);
	        } else {
	            return new ResponseEntity<>("failed", HttpStatus.BAD_REQUEST);
	        }
	    }

	    private ResponseEntity<Object> createAuthenticationToken(@RequestBody  ApplicantRegistration applicant ) throws Exception {
			// TODO Auto-generated method stub
	    	try {
				authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(applicant.getEmail(), applicant.getPassword())
				);
			}
			catch (BadCredentialsException e) {
				throw new Exception("Incorrect username or password", e);
			}
	    	 UserDetails userDetails = myUserDetailsService.loadUserByUsername(applicant.getEmail());
	    	System.out.println(userDetails.getUsername());

	 

			final String jwt = jwtTokenUtil.generateToken(userDetails);
			System.out.println(jwt);

	 

	  
			return ResponseHandler.generateResponse("Login successfully"+userDetails.getAuthorities(), HttpStatus.OK, new AuthenticationResponse(jwt),applicant.getEmail(),applicant.getName(),applicant.getId());
		}


	    @PostMapping("/applicant/sendotp")

	    public ResponseEntity<String> sendOtp(@RequestBody ApplicantRegistration  request) {

	        String userEmail = request.getEmail();

	        ApplicantRegistration applicant = regsiterService.findByEmailAddress(userEmail);

 

	        if (applicant == null) {     

	            String otp = otpService.generateOtp(userEmail);

	         	            emailService.sendOtpEmail(userEmail, otp);

 

	 	            otpVerificationMap.put(userEmail, true);

 

	 	            return ResponseEntity.ok("OTP sent to your email.");

	        }

	        else {

	        	 return ResponseEntity.badRequest().body("Email is already  registered.");

	        }

	    }

 

	    

	    @PostMapping("/applicant/verify-otp")

	    public ResponseEntity<String> verifyOtp(

	       @RequestBody  OtpVerificationRequest verificationRequest

	    ) {

	       

	        String otp=verificationRequest.getOtp();

 

	        String email=verificationRequest.getEmail();

	    	

	        System.out.println(otp+email);

	        if (!otpService.isEmailAssociatedWithOtp(email)) {

	            return ResponseEntity.badRequest().body("Email is not correct");

	        }

 

	        if (otpService.isOtpExpired(email)) {

	            return ResponseEntity.badRequest().body("Expired OTP. Please request a new one.");

	        }

 

	        if (otpService.validateOtp(email, otp)) {

	            return ResponseEntity.ok("OTP verified. Proceed with Registration.");

	        } else {

	            return ResponseEntity.badRequest().body("Incorrect OTP.");

	        }

	    }

 

	    @PostMapping("/applicant/reset-password/{email}")

	    public ResponseEntity<String> setNewPassword(@RequestBody NewPasswordRequest request,  @PathVariable String email) {

	        String newPassword = request.getNewPassword();

	        String confirmedPassword = request.getConfirmedPassword();

	      

	        if (email == null) {

	            return ResponseEntity.badRequest().body("Email not found.");

	        }

 

	   

	        ApplicantRegistration applicant = regsiterService.findByEmailAddress(email);

 

	        if (applicant == null) {

	            return ResponseEntity.badRequest().body("User not found.");

	        }

 

	     

	        applicant.setPassword(newPassword);

	     	        regsiterService.saveapplicant(applicant);

 

	        return ResponseEntity.ok("Password updated successfully.");

	    }

 

 

		@GetMapping("/viewApplicants")

	    public ResponseEntity<List<ApplicantRegistration>> getAllJobRecruiters() {

	        List<ApplicantRegistration> applicants = regsiterService.getAllApplicants();

	        return ResponseEntity.ok(applicants);

	    }
}