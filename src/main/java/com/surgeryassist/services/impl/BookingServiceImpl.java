package com.surgeryassist.services.impl;

import org.springframework.stereotype.Service;

import com.surgeryassist.core.entity.ApplicationUser;
import com.surgeryassist.core.entity.Bookings;
import com.surgeryassist.core.entity.Patient;
import com.surgeryassist.core.entity.TimeAvailabilities;
import com.surgeryassist.services.interfaces.BookingService;
import com.surgeryassist.util.SurgeryAssistUtil;

/**
 * Implementation of {@link BookingService}
 * 
 * @see BookingService
 * @author Ankit Tyagi
 */
@Service("bookingService")
public class BookingServiceImpl implements BookingService {

	@Override
	public ApplicationUser getCurrentUser() {
		return SurgeryAssistUtil.getLoggedInApplicationUser();
	}

	@Override
	public void createBooking(ApplicationUser bookingCreator,
			ApplicationUser bookingLocation, Patient patient, 
			TimeAvailabilities selectedTimeAvailability) {
		
		Bookings newBooking = new Bookings();
		newBooking.setBookingCreatorId(bookingCreator);
		newBooking.setBookingLocationId(bookingLocation);
		newBooking.setPatientId(patient);
		newBooking.setTimeAvailabilityId(selectedTimeAvailability);
		newBooking.setIsCanceled(false);
		
		newBooking = (Bookings) SurgeryAssistUtil.setHistoricalInfo(newBooking);
		
		newBooking.persist();
		newBooking.flush();
		
	}

}
