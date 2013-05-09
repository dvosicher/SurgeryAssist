package com.surgeryassist.services.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.surgeryassist.core.dto.CancelAvailabilityDTO;
import com.surgeryassist.core.entity.TimeAvailabilities;
import com.surgeryassist.services.interfaces.CancelAvailabilityService;
import com.surgeryassist.util.SurgeryAssistUtil;

/**
 * Implementation of {@link CancelAvailabilityService}
 * @author Ankit Tyagi
 */
@Service("cancelAvailabilityService")
public class CancelAvailabilityServiceImpl implements CancelAvailabilityService {

	@Override
	@Transactional(readOnly = true)
	public CancelAvailabilityDTO getListOfAvailabilitiesToCancel(CancelAvailabilityDTO existingAvailabilityDTO) {
		if(existingAvailabilityDTO != null 
				&& existingAvailabilityDTO.getTimeAvailabilitiesList() != null 
				&& existingAvailabilityDTO.getTimeAvailabilitiesList().size() != 0) {
			return existingAvailabilityDTO;
		}
		else {
			List<TimeAvailabilities> timeAvailabilitiesList = 
					TimeAvailabilities.findUnbookedAndNotCancelledTimeAvailabilitiesByASCUser(
					SurgeryAssistUtil.getLoggedInApplicationUser());
			
			CancelAvailabilityDTO cancelAvailabilityDTO = 
					new CancelAvailabilityDTO(timeAvailabilitiesList);
			
			return cancelAvailabilityDTO;
		}
	}

	@Override
	@Transactional
	public void cancelSelectedAvailabilities(
			CancelAvailabilityDTO cancelAvailabilityDTO) {
		if(cancelAvailabilityDTO != null) {
			//grab the selected availabilities
			List<TimeAvailabilities> selectedAvailabilities = Arrays.asList(cancelAvailabilityDTO.getSelectedTimeAvailabilities());
			
			for(TimeAvailabilities timeAvailabilities : selectedAvailabilities) {
				timeAvailabilities.setIsCancelled(true);
				timeAvailabilities = (TimeAvailabilities) SurgeryAssistUtil.setLastModifiedInfo(timeAvailabilities);
				timeAvailabilities.merge();
			}
			
			TimeAvailabilities.entityManager().flush();
		}
	}
	
}
