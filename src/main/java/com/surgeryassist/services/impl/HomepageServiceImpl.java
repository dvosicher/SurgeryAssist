package com.surgeryassist.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

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
	
	
	

}
