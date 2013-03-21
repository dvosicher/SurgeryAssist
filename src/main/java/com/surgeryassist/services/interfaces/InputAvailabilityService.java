/**
 * 
 */
package com.surgeryassist.services.interfaces;

import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;
import org.primefaces.component.schedule.Schedule;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import com.surgeryassist.core.dto.ScheduleDTO;
import com.surgeryassist.core.entity.DayAvailability;
import com.surgeryassist.core.entity.TimeAvailabilities;

/**
 * Interface service class for inputting time and day
 * availabilities into the database via the Primefaces
 * {@link Schedule} component.
 * 
 * @author Ankit Tyagi
 */
public interface InputAvailabilityService {
	
	/**
	 * Returns a {@link ScheduleDTO} which contains the
	 * {@link ScheduleModel}, which contains a list of {@link ScheduleEvent}
	 * based on the currently open availabilities
	 * @param scheduleDto If this is <code>null</code>, then return a
	 * 	new instance, otherwise return this instance
	 * @return The populated Primefaces {@link ScheduleDTO}
	 */
	public ScheduleDTO getCurrentScheduleDTO(ScheduleDTO scheduleDto);
	
	/**
	 * Creates a {@link List} off of the {@link ScheduleDTO}'s {@link ScheduleModel}
	 * and converts that to a {@link List} of {@link TimeAvailabilities}, then 
	 * persists those objects. Also creates associated {@link DayAvailability} as necessary
	 * @param scheduleDTO The DTO that contains the {@link ScheduleModel}
	 * @return A list of {@link TimeAvailabilities} associated with the {@link ScheduleModel}
	 */
	public void convertScheduleEventsToTimeAvailabilities(ScheduleDTO scheduleDTO);
	
	/**
	 * Creates a {@link Map} of {@link LocalDate} and {@link DayAvailability}
	 * key-value pairs that contains all the day availabilities needed for
	 * the {@link TimeAvailabilities} based on the {@link ScheduleEvent}. This
	 * method also persists the {@link DayAvailability} objects
	 * @param scheduleEvents The {@link List} of {@link ScheduleEvent} that 
	 * 	pertain to the {@link TimeAvailabilities}
	 * @return A {@link Map} that contains a {@link LocalDate} - meaning date only, not time - 
	 * 	and uses that to tie a persisted {@link DayAvailability} value to it
	 */
	public Map<LocalDate, DayAvailability> getNecessaryDayAvailabilitiesBasedOnScheduleEvents(
			List<ScheduleEvent> scheduleEvents);
}
