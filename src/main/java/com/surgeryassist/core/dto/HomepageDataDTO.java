package com.surgeryassist.core.dto;

import java.io.Serializable;
import java.util.List;

import com.surgeryassist.core.entity.ApplicationUser;
import com.surgeryassist.core.entity.Bookings;

/**
 * Homepage object to maintain data for homepage
 * info, such as confirmed/pending bookings and more 
 * @author Reid Roman
 */
public class HomepageDataDTO implements Serializable{

	private static final long serialVersionUID = -1552229662211651335L;

	List<Bookings> confirmedBookings;
	
	List<Bookings> pendingBookings;
	
	Bookings selectedBooking;
	
	ApplicationUser loggedInUser;

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
	
}
