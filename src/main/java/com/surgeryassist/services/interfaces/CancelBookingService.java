package com.surgeryassist.services.interfaces;

import org.primefaces.component.datatable.DataTable;

import com.surgeryassist.core.dto.CancelBookingsDTO;
import com.surgeryassist.core.entity.Bookings;

/**
 * Interface method for cancelling bookings
 * @author Ankit Tyagi
 */
public interface CancelBookingService {

	/**
	 * Returns the required bean for the {@link DataTable}
	 * for multiple selection of the cancel bookings page
	 * @param existingBookingsDTO The existing DTO if already on this page before
	 * 	session, otherwise a null object or empty list within the {@link CancelBookingsDTO} 
	 * @return The populated DTO instance
	 */
	public CancelBookingsDTO getListOfBookingsToCancel(CancelBookingsDTO existingBookingsDTO);
	
	/**
	 * Method to set all the selected bookings to be cancelled by setting
	 * the {@link Bookings#setIsCancelled(Boolean)} method
	 * @param cancelBookingsDTO The DTO that has the selected bookings
	 * 	to be cancelled.
	 */
	public void cancelSelectedBookings(CancelBookingsDTO cancelBookingsDTO);
	
}
