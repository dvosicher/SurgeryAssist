package com.surgeryassist.services.interfaces;

import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

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
	 * Includes password hashing (SHA + SALT)
	 * @param applicationUser
	 */
	public void registerUser(ApplicationUser applicationUser);
	
	/**
	 * Creates a default {@link ApplicationUser} based on the parameters
	 * @param newApplicationUser the new applicationUser to be returned
	 * @return An application user ready to be inserted into the database
	 */
	public ApplicationUser createDefaultApplicationUser(ApplicationUser newApplicationUser);
	
	/**
	 * Returns a map tying a String to a List
	 * of each different drop down menu utilized by 
	 * the front end application.
	 * Includes the following: 
	 * <ul>
	 * 	<li>State Code</li>
	 * </ul>
	 * @return Map with following example: pair "state" with list of
	 * 		state drop downs (AL, Alabama) etc.
	 */
	public Map<String, List<SelectItem>> getDropdownMenuValues();
}
