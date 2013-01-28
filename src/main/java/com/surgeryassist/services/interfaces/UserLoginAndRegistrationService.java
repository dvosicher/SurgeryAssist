package com.surgeryassist.services.interfaces;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.surgeryassist.core.entity.ApplicationUser;

/**
 * Custom Login authentication handler for SurgeryAssist
 * 
 * @author Ankit Tyagi
 */
public interface UserLoginAndRegistrationService extends UserDetailsService {

	public User convertApplicationUserToSpringUser(ApplicationUser applicationUser);
	
}
