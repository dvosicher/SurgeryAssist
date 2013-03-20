package com.surgeryassist.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.primefaces.model.ScheduleEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.surgeryassist.core.dto.ScheduleDTO;
import com.surgeryassist.core.entity.ApplicationUser;
import com.surgeryassist.core.entity.TimeAvailabilities;
import com.surgeryassist.services.interfaces.InputAvailabilityService;
import com.surgeryassist.util.SurgeryAssistUtil;

/**
 * @see InputAvailabilityService 
 * @author Ankit Tyagi
 *
 */
@Service("inputAvailabilityService")
public class InputAvailabilityServiceImpl implements InputAvailabilityService {

	@Override
	public ScheduleDTO getCurrentScheduleDTO(ScheduleDTO scheduleDto) {
		//if the scheduleDto already exists and there are events in it, then return it
		if(scheduleDto != null && scheduleDto.getModel().getEvents().size() != 0) {
			return scheduleDto;
		}
		//otherwise return a new instance of it
		else {
			ScheduleDTO myObject = new ScheduleDTO();

			ApplicationUser currentUser = SurgeryAssistUtil.getLoggedInApplicationUser();
			//grab list of TimeAvailabilities for the week based on the user
			List<TimeAvailabilities> availabilitiesForUser = 
					TimeAvailabilities.findOpenTimeAvailabilitiesByASCUser(currentUser);

			//convert that list of TimeAvailabilities to list of DefaultScheduleEvents
			List<ScheduleEvent> scheduledEvents = new ArrayList<ScheduleEvent>();
			for(TimeAvailabilities timeAvailability : availabilitiesForUser) {
				ScheduleEvent scheduleEvent = 
						SurgeryAssistUtil.convertTimeAvailabilityToScheduleEvent(timeAvailability);
				scheduledEvents.add(scheduleEvent);
			}

			//pass the list of ScheduleEvents into the ScheduleDTO to add into the model
			myObject.addEvents(scheduledEvents);

			return myObject;
		}
	}

	@Override
	@Transactional
	public List<TimeAvailabilities> convertScheduleEventsToTimeAvailabilities(
			ScheduleDTO scheduleDTO) {
		if(scheduleDTO != null) {
			List<ScheduleEvent> scheduleEvents = scheduleDTO.getModel().getEvents();
			List<TimeAvailabilities> timeAvailabilitiesList = new ArrayList<TimeAvailabilities>();
			
			for(ScheduleEvent scheduleEvent : scheduleEvents) {
				//logic to check if this date was already created as a DayAvailability
				
				//logic to check if this schedule already exists as a TimeAvailability
			}
			
			//persist the DayAvailability, return the list of TimeAvailabilities
			
			
			return timeAvailabilitiesList;
		}
		return new ArrayList<TimeAvailabilities>();
	}
}
