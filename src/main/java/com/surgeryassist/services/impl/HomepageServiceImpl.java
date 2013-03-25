package com.surgeryassist.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.surgeryassist.core.UserTypeCode;
import com.surgeryassist.core.dto.HomepageDataDTO;
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
	@Transactional(readOnly = true)
	public List<Bookings> getPendingBookings(ApplicationUser loggedInUser) {
		List<Bookings> pendingBookings = 
				Bookings.findPendingBookingsByUser(loggedInUser);

		if(pendingBookings == null) {
			return new ArrayList<Bookings>();
		}

		return pendingBookings;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Bookings> getConfirmedBookings(ApplicationUser loggedInUser) {
		List<Bookings> confirmedBookings = 
				Bookings.findConfirmedBookingsByUser(loggedInUser);

		if(confirmedBookings == null) {
			return new ArrayList<Bookings>();
		}

		return confirmedBookings;
	}

	@Override
	public HomepageDataDTO populateSurgeonInfo(HomepageDataDTO homepageData) {
		if(homepageData != null) {
			if(homepageData.getPendingBookings().size() != 0 && 
					homepageData.getConfirmedBookings().size() != 0) {
				//return existing object
				return homepageData;
			}
		}
		//populate homepage
		HomepageDataDTO newHomepageData = new HomepageDataDTO();
		
		ApplicationUser loggedInUser = SurgeryAssistUtil.getLoggedInApplicationUser();
		
		newHomepageData.setLoggedInUser(loggedInUser);
		newHomepageData.setPendingBookings(this.getPendingBookings(loggedInUser));
		newHomepageData.setConfirmedBookings(this.getConfirmedBookings(loggedInUser));
		
		return newHomepageData;
	}


}
