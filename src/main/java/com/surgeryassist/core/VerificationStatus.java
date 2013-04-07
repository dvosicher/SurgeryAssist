package com.surgeryassist.core;

/**
 * Verification Status of different 
 * accounts
 * @author Ankit Tyagi
 */
public enum VerificationStatus {
	
	/**
	 * The User has been verified
	 */
	VERIFIED, 
	
	/**
	 * The User was looked at, and was not verified
	 */
	NOT_VERIFIED, 
	
	/**
	 * The User is waiting for verification
	 */
	WAITING_VERIFICATION;
}
