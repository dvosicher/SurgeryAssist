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
 * info, such as confirmed/pending {@link Bookings} and more 
 * @author Reid Roman
 */
public class HomepageDataDTO implements Serializable {

	private static final long serialVersionUID = -1552229662211651335L;

	private List<Bookings> confirmedBookings;
	
	private List<Bookings> pendingBookings;
	
	private List<ApplicationUser> recentASCs;
	
	private List<ApplicationUser> favoriteASCs;
	
	private Bookings selectedBooking;
	
	private ApplicationUser selectedASC;
	
	private ApplicationUser loggedInUser;
	
	private BookingDataModel bookingDataModel;
	
	public HomepageDataDTO() { }
	
	/**
	 * Constructor that creates a bookingDataModel for
	 * selections by combining all the bookings in a single
	 * list
	 * @param pendingBookings List of pending bookings
	 * @param confirmedBookings List of confirmed bookings
	 */
	public HomepageDataDTO(List<Bookings> pendingBookings, List<Bookings> confirmedBookings) {
		this.confirmedBookings = confirmedBookings;
		this.pendingBookings = pendingBookings;
		
		List<Bookings> allBookings = new ArrayList<Bookings>();
		
		allBookings.addAll(confirmedBookings);
		allBookings.addAll(pendingBookings);
		
		bookingDataModel = new BookingDataModel(allBookings);
	}
	
	/**
	 * Same Primefaces deficiency, need to create this
	 * empty method
	 * @param event 
	 */
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

	public List<ApplicationUser> getRecentASCs() {
		return recentASCs;
	}

	public void setRecentASCs(List<ApplicationUser> recentASCs) {
		this.recentASCs = recentASCs;
	}

	public List<ApplicationUser> getFavoriteASCs() {
		return favoriteASCs;
	}

	public void setFavoriteASCs(List<ApplicationUser> favoriteASCs) {
		this.favoriteASCs = favoriteASCs;
	}

	public Bookings getSelectedBooking() {
		return selectedBooking;
	}

	public void setSelectedBooking(Bookings selectedBooking) {
		this.selectedBooking = selectedBooking;
	}

	public ApplicationUser getSelectedASC() {
		return selectedASC;
	}

	public void setSelectedASC(ApplicationUser selectedASC) {
		this.selectedASC = selectedASC;
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
