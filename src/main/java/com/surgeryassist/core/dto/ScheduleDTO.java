/**
 * 
 */
package com.surgeryassist.core.dto;

import java.io.Serializable;
import java.util.Date;

import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;

/**
 * ScheduleDTO object to be used for the 
 * Primefaces Schedule component
 * @author Ankit Tyagi
 */
public class ScheduleDTO implements Serializable {

	private static final long serialVersionUID = -6288968959526039477L;
	
	private ScheduleModel scheduleModel;
	
	/**
	 * Sets the scheduleModel to have currently existing 
	 * availabilities
	 */
	public ScheduleDTO() {
		//TODO: write a converter from schedule event to day/time models
		this.scheduleModel = new DefaultScheduleModel();
		this.scheduleModel.addEvent(new DefaultScheduleEvent("Test", new Date(), new Date()));
	}

	/**
	 * @return the scheduleModel
	 */
	public ScheduleModel getScheduleModel() {
		return scheduleModel;
	}


}
