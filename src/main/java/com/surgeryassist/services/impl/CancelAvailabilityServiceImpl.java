/**
 * 
 */
package com.surgeryassist.services.impl;

import org.springframework.stereotype.Service;

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
	public CancelAvailabilityDTO getListOfAvailabilitiesToCancel(CancelAvailabilityDTO existingAvailabilityDTO) {
		if(existingAvailabilityDTO != null && existingAvailabilityDTO.getTimeAvailabilitiesList().size() != 0) {
			return existingAvailabilityDTO;
		}
		else {
			CancelAvailabilityDTO cancelAvailabilityDTO = new CancelAvailabilityDTO();
			
			cancelAvailabilityDTO.setTimeAvailabilitiesList(
					TimeAvailabilities.findUnbookedAndNotCancelledTimeAvailabilitiesByASCUser(
							SurgeryAssistUtil.getLoggedInApplicationUser()));
			
			return cancelAvailabilityDTO;
		}
	}

}
