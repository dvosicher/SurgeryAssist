package com.surgeryassist.services.interfaces;

import com.surgeryassist.core.dto.ConfirmBookingsDTO;

/**
 * Interface for service to confirm bookings
 * that are in a pending state for a specific
 * ASC
 * @author Ankit Tyagi
 */
public interface ConfirmBookingService {

	/**
	 * Returns a list of the pending bookings for the specific ASC
	 * for multiple selection on the confirm booking page. Utilizes
	 * the {@link ConfirmBookingsDTO} bean for the Primefaces
	 * @param confirmBookingsDTO The existing DTO if already on this page
	 * 	before session, otherwise a null object or empty list within 
	 * 	the {@link ConfirmBookingsDTO}
	 * @return The populated DTO instance
	 */
	public ConfirmBookingsDTO getListOfPendingBookings(ConfirmBookingsDTO confirmBookingsDTO);
	
	/**
	 * Method to set all the pending bookings to confirmed based on the 
	 * selected bookings in the DTO
	 * @param confirmBookingsDTO The DTO with the provided data
	 */
	public void confirmSelectedBookings(ConfirmBookingsDTO confirmBookingsDTO);
	
}
