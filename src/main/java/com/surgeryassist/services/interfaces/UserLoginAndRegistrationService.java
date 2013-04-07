package com.surgeryassist.services.interfaces;

import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.surgeryassist.core.RegistrationResult;
import com.surgeryassist.core.entity.ApplicationUser;
import com.surgeryassist.core.entity.ContactInfo;
import com.surgeryassist.core.entity.Location;
import com.surgeryassist.core.entity.UserInfo;

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
	 * @author Ankit Tyagi
	 */
	public UserDetails convertApplicationUserToSpringUser(ApplicationUser applicationUser);
	
	/**
	 * Registers a user based on Spring Security protocols.
	 * Includes password hashing (SHA + SALT)
	 * @param applicationUser The {@link ApplicationUser} entity
	 * @param userInfo The {@link UserInfo} entity
	 * @param contactInfo The {@link ContactInfo} entity
	 * @param location The {@link Location} entity
	 * @return {@link RegistrationResult} the result of the registration
	 * @author Ankit Tyagi
	 */
	public RegistrationResult registerUser(ApplicationUser applicationUser, UserInfo userInfo, ContactInfo contactInfo, Location location);
	
	/**
	 * Creates a default {@link ApplicationUser} based on the parameters
	 * @param newApplicationUser the new applicationUser to be returned
	 * @param userInfo the new {@link UserInfo}
	 * @param contactInfo the new {@link ContactInfo}
	 * @param location the new {@link Location}
	 * @return An application user ready to be inserted into the database
	 * @author Ankit Tyagi
	 */
	public ApplicationUser createDefaultApplicationUser(ApplicationUser newApplicationUser, UserInfo userInfo, ContactInfo contactInfo, Location location);
	
	/**
	 * Returns a map tying a String to a List
	 * of each different drop down menu utilized by 
	 * the front end application.
	 * Includes the following: 
	 * <ul>
	 * 	<li>State Code</li>
	 * 	<li>User Type Code</li>
	 * 	<li>Verification Status</li>
	 * </ul>
	 * @return Map with following example: pair "state" with list of
	 * 		state drop downs (AL, Alabama) etc.
	 * @author Ankit Tyagi
	 */
	public Map<String, List<SelectItem>> getDropdownMenuValues();
	
}
