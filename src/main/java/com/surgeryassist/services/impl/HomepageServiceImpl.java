package com.surgeryassist.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.surgeryassist.core.UserTypeCode;
import com.surgeryassist.core.entity.ApplicationUser;
import com.surgeryassist.core.entity.Bookings;
import com.surgeryassist.services.interfaces.HomepageService;
import com.surgeryassist.util.SurgeryAssistUtil;

/**
 * Implementation of {@link HomepageService}
 * @author Ankit Tyagi
 */
@Service("homepageService")
public class HomepageServiceImpl implements HomepageService {

	@Override
	public UserTypeCode isUserSurgeonOrASC() {
		ApplicationUser loggedInUser = SurgeryAssistUtil.getLoggedInApplicationUser();
		return loggedInUser.getUserTypeCode();
	}

	@Override
	public ApplicationUser getLoggedInUser(ApplicationUser loggedInUser) {
		if(loggedInUser != null) {
			return loggedInUser;
		}
		return SurgeryAssistUtil.getLoggedInApplicationUser();
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Bookings> getPendingBookings() {
		ApplicationUser loggedInUser = 
				SurgeryAssistUtil.getLoggedInApplicationUser();
		List<Bookings> pendingBookings = 
				Bookings.findPendingBookingsByUser(loggedInUser);
		
		if(pendingBookings == null) {
			return new ArrayList<Bookings>();
		}
		
		return pendingBookings;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Bookings> getConfirmedBookings() {
		ApplicationUser loggedInUser = 
				SurgeryAssistUtil.getLoggedInApplicationUser();
		List<Bookings> confirmedBookings = 
				Bookings.findConfirmedBookingsByUser(loggedInUser);
		
		if(confirmedBookings == null) {
			return new ArrayList<Bookings>();
		}
		
		return confirmedBookings;
	}


}
