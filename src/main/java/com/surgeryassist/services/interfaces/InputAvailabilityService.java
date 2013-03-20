/**
 * 
 */
package com.surgeryassist.services.interfaces;

import java.util.List;

import org.primefaces.component.schedule.Schedule;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import com.surgeryassist.core.dto.ScheduleDTO;
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
	 * and converts that to a {@link List} of {@link TimeAvailabilities}
	 * @param scheduleDTO The DTO that contains the {@link ScheduleModel}
	 * @return A list of {@link TimeAvailabilities} associated with the {@link ScheduleModel}
	 */
	public List<TimeAvailabilities> convertScheduleEventsToTimeAvailabilities(ScheduleDTO scheduleDTO);
}
