package com.surgeryassist.services.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.surgeryassist.core.dto.ConfirmBookingsDTO;
import com.surgeryassist.core.entity.Bookings;
import com.surgeryassist.services.interfaces.ConfirmBookingService;
import com.surgeryassist.util.SurgeryAssistUtil;

/**
 * Implementation of {@link ConfirmBookingService}
 * @author Ankit Tyagi
 */
@Service("confirmBookingService")
public class ConfirmBookingServiceImpl implements ConfirmBookingService {

	@Override
	@Transactional(readOnly = true)
	public ConfirmBookingsDTO getListOfPendingBookings(
			ConfirmBookingsDTO confirmBookingsDTO) {
		if(confirmBookingsDTO != null 
				&& confirmBookingsDTO.getPendingBookings() != null 
				&& confirmBookingsDTO.getPendingBookings().size() != 0) {
			return confirmBookingsDTO;
		}
		else {
			List<Bookings> pendingBookingsList = 
					Bookings.findPendingBookingsByUser(
							SurgeryAssistUtil.getLoggedInApplicationUser());
			
			ConfirmBookingsDTO newConfirmBookingsDTO = 
					new ConfirmBookingsDTO(pendingBookingsList);
			
			return newConfirmBookingsDTO;
		}
	}

	@Override
	@Transactional
	public void confirmSelectedBookings(ConfirmBookingsDTO confirmBookingsDTO) {
		if(confirmBookingsDTO != null) {
			List<Bookings> selectedBookings = 
					Arrays.asList(confirmBookingsDTO.getSelectedBookings());
			
			for(Bookings bookingToConfirm : selectedBookings) {
				bookingToConfirm.setIsConfirmed(true);
				bookingToConfirm = (Bookings) SurgeryAssistUtil.setLastModifiedInfo(bookingToConfirm);
				bookingToConfirm.merge();
			}
			
			Bookings.entityManager().flush();
		}
	}

}
