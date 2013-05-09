package com.surgeryassist.core.dto;

import java.io.Serializable;
import java.util.List;

import org.primefaces.event.SelectEvent;

import com.surgeryassist.core.datamodel.BookingDataModel;
import com.surgeryassist.core.entity.Bookings;

/**
 * Class to handle the {@link Bookings} data table for pending bookings.
 * @author Ankit Tyagi
 */
public class ConfirmBookingsDTO implements Serializable {
	
	private static final long serialVersionUID = -6744016422644698697L;

	private List<Bookings> pendingBookings;

	private Bookings[] selectedBookings;
	
	private BookingDataModel bookingDataModel;
	
	public ConfirmBookingsDTO() { }
	
	/**
	 * Creates a datamodel based on the parameter list
	 * @param pendingBookings List of Pending {@link Bookings}
	 */
	public ConfirmBookingsDTO(List<Bookings> pendingBookings) {
		this.pendingBookings = pendingBookings;
		this.bookingDataModel = new BookingDataModel(pendingBookings);
	}
	
	/**
	 * Empty method to alleviate the Primefaces 
	 * error
	 * @param event
	 */
	public void onRowSelect(SelectEvent event) {
		//do nothing, because primefaces is stupid
	}

	public List<Bookings> getPendingBookings() {
		return pendingBookings;
	}

	public void setPendingBookings(List<Bookings> pendingBookings) {
		this.pendingBookings = pendingBookings;
	}

	public BookingDataModel getBookingDataModel() {
		return bookingDataModel;
	}

	public void setBookingDataModel(BookingDataModel bookingDataModel) {
		this.bookingDataModel = bookingDataModel;
	}

	public Bookings[] getSelectedBookings() {
		return selectedBookings;
	}

	public void setSelectedBookings(Bookings[] selectedBookings) {
		this.selectedBookings = selectedBookings;
	}
}
