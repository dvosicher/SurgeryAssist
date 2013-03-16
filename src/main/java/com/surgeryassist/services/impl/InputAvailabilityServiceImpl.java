package com.surgeryassist.services.impl;

import org.springframework.stereotype.Service;

import com.surgeryassist.core.dto.ScheduleDTO;
import com.surgeryassist.services.interfaces.InputAvailabilityService;

/**
 * 
 * @author Ankit Tyagi
 *
 */
@Service("inputAvailabilityService")
public class InputAvailabilityServiceImpl implements InputAvailabilityService {
	
	@Override
	public ScheduleDTO getCurrentScheduleDTO() {
		return new ScheduleDTO();
	}
}
