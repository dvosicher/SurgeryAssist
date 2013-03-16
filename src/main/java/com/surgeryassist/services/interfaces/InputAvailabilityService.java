/**
 * 
 */
package com.surgeryassist.services.interfaces;

import org.primefaces.component.schedule.Schedule;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import com.surgeryassist.core.dto.ScheduleDTO;

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
	 * schedule model, which contains {@link ScheduleEvent}
	 * @return The populated Primefaces {@link ScheduleModel}
	 */
	public ScheduleDTO getCurrentScheduleDTO();
}
