package com.surgeryassist.services.interfaces;

import com.surgeryassist.core.entity.ApplicationUser;
import com.surgeryassist.core.entity.Bookings;
import com.surgeryassist.core.entity.Patient;
import com.surgeryassist.core.entity.TimeAvailabilities;

/**
 * Interface class for creating and saving bookings, thus
 * making booking different availabilities, and ideally
 * removing them from searches.
 * @author Ankit Tyagi
 */
public interface BookingService {
	
	/**
	 * Returns the current ApplicationUser that
	 * is logged in.
	 * @return {@link ApplicationUser} object of the logged in
	 * 	user
	 */
	public ApplicationUser getCurrentUser();
	
	/**
	 * Creates a {@link Bookings} object and 
	 * persists it based on the provided parameters
	 * @param bookingCreator The creator of the booking (whoever's currently logged in)
	 * @param bookingLocation The location of room (the {@link ApplicationUser} object 
	 * 		of the selected {@link TimeAvailabilities})
	 * @param selectedTimeAvailability The selected {@link TimeAvailabilities} of the booking
	 * @param patient The patient that will be treated with said {@link Bookings}
	 */
	public void createBooking(ApplicationUser bookingCreator, 
			ApplicationUser bookingLocation, Patient patient, 
			TimeAvailabilities selectedTimeAvailability);
}
