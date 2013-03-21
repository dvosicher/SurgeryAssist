package com.surgeryassist.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

			//create DayAvailabilities based on the provided TimeAvailabilities
			Map<LocalDate, DayAvailability> dayAvailabilitiesMap = 
					this.getNecessaryDayAvailabilitiesBasedOnScheduleEvents(scheduleEvents);

			List<TimeAvailabilities> timeAvailabilitiesInDatabase = 
					TimeAvailabilities.findOpenTimeAvailabilitiesByASCUser(
							SurgeryAssistUtil.getLoggedInApplicationUser()); 

			//only go through this logic if there are existing values to add, otherwise just add new availabilities
			if(timeAvailabilitiesInDatabase.size() != 0) {
				//logic to check if this schedule already exists as a TimeAvailability
				//if the scheduleEvent start time AND endtime are the same as the timeAvail
				//start time and end time, then we don't need to add it, but we need to update it
				for(ScheduleEvent scheduleEvent : scheduleEvents) {
					for(TimeAvailabilities timeAvailability : timeAvailabilitiesInDatabase) {

						if(scheduleEvent.isEditable()) {
							//the two times are not equal, so we have to create a new TimeAvailability
							if(!(DateTimeComparator.getTimeOnlyInstance()
									.compare(new DateTime(scheduleEvent.getStartDate()), 
											new DateTime(timeAvailability.getStartTime())) == 0)
											|| !(DateTimeComparator.getTimeOnlyInstance()
													.compare(new DateTime(scheduleEvent.getEndDate()), 
															new DateTime(timeAvailability.getEndTime())) == 0)) {

								TimeAvailabilities newTimeAvailability = 
										this.createNewTimeAvailability(dayAvailabilitiesMap, scheduleEvent);

								newTimeAvailability.persist();
							}
							else {
								timeAvailability.setStartTime(
										SurgeryAssistUtil.convertDateToCalendar(scheduleEvent.getStartDate()));
								timeAvailability.setEndTime(
										SurgeryAssistUtil.convertDateToCalendar(scheduleEvent.getEndDate()));
								timeAvailability.merge();
							}
						}
					}
				}
			}
			else {
				//we know that each scheduleEvent is going to be different, so just add it to the DB
				for(ScheduleEvent scheduleEvent : scheduleEvents) {
					TimeAvailabilities newTimeAvailabilities = 
							this.createNewTimeAvailability(dayAvailabilitiesMap, scheduleEvent);
					newTimeAvailabilities.persist();
				}
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

			//only go through this logic if the list has data, otherwise we need to create DayAvailabilities
			if(dayAvailabilitiesList.size() != 0) {
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
										.compare(startingDateTime, jodaDayAvailability);

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
			}
			else {
				//create new DayAvailability for each unique day in the ScheduleEvents list and put it into the map
				for(ScheduleEvent scheduleEvent : scheduleEvents) {
					LocalDate scheduleEventDate = new LocalDate(scheduleEvent.getStartDate());
					if(!returnMap.containsKey(scheduleEventDate)) {
						DayAvailability newDayAvailability = 
								this.createNewDayAvailability(currentUser, new DateTime(scheduleEvent.getStartDate()));
						returnMap.put(scheduleEventDate, newDayAvailability);
					}
				}
			}

			//finally return the map
			return returnMap;
		}
		return new HashMap<LocalDate, DayAvailability>();
	}

	/**
	 * Creates a new {@link DayAvailability} based on the parameters and persists/flushes it.
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
		newDayAvailability.persist();
		newDayAvailability.flush();

		return newDayAvailability;
	}

	/**
	 * Creates a new {@link TimeAvailabilities} object
	 * based on the parameters
	 * @param dayAvailabilitiesMap A {@link Map} that contains day availability data
	 * @param scheduleEvent The schedule event that contains the start and end times
	 * @return The created {@link TimeAvailabilities}
	 */
	private TimeAvailabilities createNewTimeAvailability(
			Map<LocalDate, DayAvailability> dayAvailabilitiesMap,
			ScheduleEvent scheduleEvent) {
		TimeAvailabilities timeAvailabilityToAdd = new TimeAvailabilities();

		LocalDate mapKey = new LocalDate(scheduleEvent.getStartDate()); 

		timeAvailabilityToAdd = 
				(TimeAvailabilities) SurgeryAssistUtil
				.setAllHistoricalInfo(timeAvailabilityToAdd);
		timeAvailabilityToAdd.setStartTime(
				SurgeryAssistUtil.convertDateToCalendar(scheduleEvent.getStartDate()));
		timeAvailabilityToAdd.setEndTime(
				SurgeryAssistUtil.convertDateToCalendar(scheduleEvent.getEndDate()));
		
		DayAvailability availabilityFromMap = dayAvailabilitiesMap.get(mapKey);
		if(availabilityFromMap != null) {
			timeAvailabilityToAdd.setAvailabilityId(availabilityFromMap);
		}
		timeAvailabilityToAdd.setIsBooked(false);

		return timeAvailabilityToAdd;
	}
}
