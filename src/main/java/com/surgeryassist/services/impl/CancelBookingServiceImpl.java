package com.surgeryassist.services.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.surgeryassist.core.dto.CancelBookingsDTO;
import com.surgeryassist.core.entity.Bookings;
import com.surgeryassist.core.entity.TimeAvailabilities;
import com.surgeryassist.services.interfaces.CancelBookingService;
import com.surgeryassist.util.SurgeryAssistUtil;

/**
 * Implementation of the {@link CancelBookingService}
 * @author Ankit Tyagi
 */
@Service("cancelBookingService")
public class CancelBookingServiceImpl implements CancelBookingService {

	@Override
	@Transactional(readOnly = true)
	public CancelBookingsDTO getListOfBookingsToCancel(
			CancelBookingsDTO existingBookingsDTO) {
		if(existingBookingsDTO != null
				&& existingBookingsDTO.getBookingsList() != null
				&& existingBookingsDTO.getBookingsList().size() > 0) {
			return existingBookingsDTO;
		}
		else {
			List<Bookings> bookingsList = 
					Bookings.findAllBookingsByUser(
							SurgeryAssistUtil.getLoggedInApplicationUser());
			
			CancelBookingsDTO newCancelBookingsDTO = new CancelBookingsDTO(bookingsList);
			
			return newCancelBookingsDTO;
		}
	}

	@Override
	@Transactional
	public void cancelSelectedBookings(CancelBookingsDTO cancelBookingsDTO) {
		if(cancelBookingsDTO != null) {
			//grab the selected availabilities
			List<Bookings> selectedBookings = Arrays.asList(cancelBookingsDTO.getSelectedBookings());
			
			for(Bookings bookingsToCancel : selectedBookings) {
				bookingsToCancel.setIsCanceled(true);
				bookingsToCancel = (Bookings) SurgeryAssistUtil.setLastModifiedInfo(bookingsToCancel);
				
				//make the timeAvailability available to be booked again
				bookingsToCancel.getTimeAvailabilityId().setIsBooked(false);
				bookingsToCancel.getTimeAvailabilityId().setIsCancelled(false);
				bookingsToCancel.setTimeAvailabilityId(
						((TimeAvailabilities) SurgeryAssistUtil.setLastModifiedInfo(bookingsToCancel.getTimeAvailabilityId())));
				
				bookingsToCancel.getTimeAvailabilityId().merge();
				bookingsToCancel.merge();
				
				
			}
			
			Bookings.entityManager().flush();
		}	
	}

}
