package com.surgeryassist.services.interfaces;

import java.util.List;

import com.surgeryassist.core.UserTypeCode;
import com.surgeryassist.core.entity.Bookings;

/**
 * Interface for homepage content 
 * generation.
 * 
 * @author Ankit Tyagi
 */
public interface HomepageService {

	/**
	 * Returns an Enum of {@link UserTypeCode} that
	 * tells whether the user is a Surgeon,
	 * ASC, or a delegate account.
	 * @return {@link UserTypeCode} of logged in user, otherwise null.
	 */
	public UserTypeCode isUserSurgeonOrASC();
	
	/**
	 * Returns a list of {@link Bookings}
	 * that are in pending status for the
	 * currently logged in user
	 * @return {@link List} of {@link Bookings}, or empty list
	 */
	public List<Bookings> getPendingBookings(); 
}
