package com.surgeryassist.core;

/**
 * The result of the registration
 * @author Ankit Tyagi
 */
public enum RegistrationResult {
	
	/**
	 * The registration was successful
	 */
	SUCCESS, 
	
	/**
	 * The user already exists in the DB
	 */
	ALREADY_EXISTS;
}
