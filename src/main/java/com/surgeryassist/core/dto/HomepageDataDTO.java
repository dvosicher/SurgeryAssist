package com.surgeryassist.core.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.primefaces.event.SelectEvent;

import com.surgeryassist.core.datamodel.BookingDataModel;
import com.surgeryassist.core.entity.ApplicationUser;
import com.surgeryassist.core.entity.Bookings;

/**
 * Homepage object to maintain data for homepage
 * info, such as confirmed/pending bookings and more 
 * @author Reid Roman
 */
public class HomepageDataDTO implements Serializable {

	private static final long serialVersionUID = -1552229662211651335L;

	private List<Bookings> confirmedBookings;
	
	private List<Bookings> pendingBookings;
	
	private Bookings selectedBooking;
	
	private ApplicationUser loggedInUser;
	
	private BookingDataModel bookingDataModel;
	
	public HomepageDataDTO() { }
	
	public HomepageDataDTO(List<Bookings> pendingBookings, List<Bookings> confirmedBookings) {
		this.confirmedBookings = confirmedBookings;
		this.pendingBookings = pendingBookings;
		
		List<Bookings> allBookings = new ArrayList<Bookings>();
		
		allBookings.addAll(confirmedBookings);
		allBookings.addAll(pendingBookings);
		
		bookingDataModel = new BookingDataModel(allBookings);
	}
	
	public void onRowSelect(SelectEvent event) {
		//do nothing, for now...
	}

	public List<Bookings> getConfirmedBookings() {
		return confirmedBookings;
	}

	public void setConfirmedBookings(List<Bookings> confirmedBookings) {
		this.confirmedBookings = confirmedBookings;
	}

	public List<Bookings> getPendingBookings() {
		return pendingBookings;
	}

	public void setPendingBookings(List<Bookings> pendingBookings) {
		this.pendingBookings = pendingBookings;
	}

	public Bookings getSelectedBooking() {
		return selectedBooking;
	}

	public void setSelectedBooking(Bookings selectedBooking) {
		this.selectedBooking = selectedBooking;
	}

	public ApplicationUser getLoggedInUser() {
		return loggedInUser;
	}

	public void setLoggedInUser(ApplicationUser loggedInUser) {
		this.loggedInUser = loggedInUser;
	}

	public BookingDataModel getBookingDataModel() {
		return bookingDataModel;
	}

	public void setBookingDataModel(BookingDataModel bookingDataModel) {
		this.bookingDataModel = bookingDataModel;
	}
	
}
