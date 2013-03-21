package com.surgeryassist.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.LocalDate;
import org.primefaces.model.ScheduleEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.surgeryassist.core.dto.ScheduleDTO;
import com.surgeryassist.core.entity.ApplicationUser;
import com.surgeryassist.core.entity.DayAvailability;
import com.surgeryassist.core.entity.TimeAvailabilities;
import com.surgeryassist.services.interfaces.InputAvailabilityService;
import com.surgeryassist.util.SurgeryAssistUtil;

/**
 * @see InputAvailabilityService 
 * @author Ankit Tyagi
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
	public void convertScheduleEventsToTimeAvailabilities(ScheduleDTO scheduleDTO) {
		if(scheduleDTO != null) {
			List<ScheduleEvent> scheduleEvents = scheduleDTO.getModel().getEvents();
			List<TimeAvailabilities> timeAvailabilitiesList = new ArrayList<TimeAvailabilities>();

			//create DayAvailabilities based on the provided TimeAvailabilities
			Map<LocalDate, DayAvailability> dayAvailabilitiesMap = 
					this.getNecessaryDayAvailabilitiesBasedOnScheduleEvents(scheduleEvents);
			
			List<TimeAvailabilities> timeAvailabilitiesInDatabase = 
					TimeAvailabilities.findOpenTimeAvailabilitiesByASCUser(
							SurgeryAssistUtil.getLoggedInApplicationUser()); 
			
			//logic to check if this schedule already exists as a TimeAvailability
			for(ScheduleEvent scheduleEvent : scheduleEvents) {
				for(TimeAvailabilities timeAvailability : timeAvailabilitiesInDatabase) {
					
				}
			}
			
			//persist the TimeAvailabilities
			for(TimeAvailabilities timeAvailabilities : timeAvailabilitiesList) {
				timeAvailabilities.persist();
			}
			//force the persistence context to flush
			TimeAvailabilities.entityManager().flush();
		}
	}

	@Override
	public Map<LocalDate, DayAvailability> getNecessaryDayAvailabilitiesBasedOnScheduleEvents(
			List<ScheduleEvent> scheduleEvents) {
		if(scheduleEvents != null) {
			ApplicationUser currentUser = SurgeryAssistUtil.getLoggedInApplicationUser();
			Map<LocalDate, DayAvailability> returnMap = new HashMap<LocalDate, DayAvailability>();

			//grab all DayAvailabilities based on the user
			List<DayAvailability> dayAvailabilitiesList = 
					DayAvailability.findDayAvailabilitiesByApplicationUser(currentUser);

			//look at each individual scheduleEvent, compare it to each dayAvailability, and add it to the map if necessary 
			for(ScheduleEvent scheduleEvent : scheduleEvents) {
				for(DayAvailability dayAvailability : dayAvailabilitiesList) {

					//convert times into joda times
					DateTime startingDateTime = new DateTime(scheduleEvent.getStartDate());
					DateTime endingDateTime = new DateTime(scheduleEvent.getEndDate());
					DateTime jodaDayAvailability = new DateTime(dayAvailability.getDateOfAvailability());

					//check to make sure that the startDate and endDate are valid
					if(DateTimeComparator.getDateOnlyInstance().compare(startingDateTime, endingDateTime) == 0) {
						LocalDate scheduleDateWithoutTime = startingDateTime.toLocalDate();
						LocalDate dayAvailabilityWithoutTime = jodaDayAvailability.toLocalDate();
						
						//make sure to only add days that need to be added
						if(!returnMap.containsKey(scheduleDateWithoutTime)) { 
							//compare the schedule event with the dayAvailability
							int compareResults = DateTimeComparator.getDateOnlyInstance()
									.compare(scheduleDateWithoutTime, dayAvailabilityWithoutTime);

							//if the results are equal, that means that there is a dayAvailability with the same day as a scheduleEvent
							if(compareResults == 0) {
								//check if the map already has the DayAvailability. if it does not, then add it
								if(!returnMap.containsKey(dayAvailabilityWithoutTime)) {
									returnMap.put(dayAvailabilityWithoutTime, dayAvailability);
								}
							}
							//the scheduleDay and dayAvailability don't exist in the map, need to add them
							else {
								DayAvailability newDayAvailability = 
										this.createNewDayAvailability(currentUser, startingDateTime);
								returnMap.put(dayAvailabilityWithoutTime, newDayAvailability);
							}
						}
					}
				}
			}
			
			//finally return the map
			return returnMap;
		}
		return new HashMap<LocalDate, DayAvailability>();
	}

	/**
	 * Creates a new {@link DayAvailability} based on the parameters
	 * @param currentUser The {@link ApplicationUser} that owns this availability
	 * @param dateTime The date of the availability
	 * @return
	 */
	private DayAvailability createNewDayAvailability(ApplicationUser currentUser, DateTime dateTime) {
		DayAvailability newDayAvailability = new DayAvailability();
		newDayAvailability.setUserId(currentUser);
		newDayAvailability.setDateOfAvailability(
				dateTime.toGregorianCalendar());
		newDayAvailability = (DayAvailability) SurgeryAssistUtil.setAllHistoricalInfo(newDayAvailability);
		
		return newDayAvailability;
	}
}
