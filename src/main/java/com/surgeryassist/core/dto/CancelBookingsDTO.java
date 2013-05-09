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
public class CancelBookingsDTO implements Serializable {
	
	private static final long serialVersionUID = -6744016422644698697L;

	private List<Bookings> bookingsList;

	private Bookings[] selectedBookings;
	
	private BookingDataModel bookingDataModel;
	
	public CancelBookingsDTO() { }
	
	/**
	 * Creates a datamodel based on the passed in list
	 * @param bookingsList A list of bookings that could be cancelled
	 */
	public CancelBookingsDTO(List<Bookings> bookingsList) {
		this.setBookingsList(bookingsList);
		this.bookingDataModel = new BookingDataModel(bookingsList);
	}
	
	/**
	 * Empty method for Primefaces deficiency
	 * @param event
	 */
	public void onRowSelect(SelectEvent event) {
		//do nothing, because primefaces is stupid
	}

	public List<Bookings> getBookingsList() {
		return bookingsList;
	}

	public void setBookingsList(List<Bookings> bookingsList) {
		this.bookingsList = bookingsList;
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
