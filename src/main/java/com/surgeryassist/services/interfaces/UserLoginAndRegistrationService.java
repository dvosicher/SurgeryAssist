package com.surgeryassist.services.interfaces;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.surgeryassist.core.entity.ApplicationUser;

/**
 * Custom Login authentication handler for SurgeryAssist
 * 
 * @author Ankit Tyagi
 */
public interface UserLoginAndRegistrationService extends UserDetailsService {

	/**
	 * Converts an {@link ApplicationUser} into a {@link UserDetails}
	 * for Spring Security
	 * @param applicationUser The ApplicationUser to convert
	 * @return The spring security user class
	 */
	public UserDetails convertApplicationUserToSpringUser(ApplicationUser applicationUser);
	
	/**
	 * Registers a user based on Spring Security protocols.
	 * Includes password hashing (SHA-256 + SALT)
	 * @param applicationUser
	 */
	public void registerUser(ApplicationUser applicationUser);
	
}
