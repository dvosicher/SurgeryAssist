package com.surgeryassist.core.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.primefaces.component.schedule.Schedule;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import com.surgeryassist.util.faces.NewAvailabilityScheduleEvent;

/**
 * ScheduleDTO object to be used for the 
 * Primefaces {@link Schedule} component
 * @author Ankit Tyagi
 */
public class ScheduleDTO implements Serializable {

	private static final long serialVersionUID = -6288968959526039477L;

	/**
	 * The model that sets the data
	 */
	private ScheduleModel model;

	/**
	 * The singly held event during the context
	 */
	private NewAvailabilityScheduleEvent event;

	/**
	 * Force a default time zone to be GMT
	 */
	private TimeZone timeZone;
	
	public ScheduleDTO() {
		this.model = new DefaultScheduleModel();
		this.setTimeZone(TimeZone.getTimeZone("GMT"));
	}

	/**
	 * Add a list of schedule events to the 
	 * model to be used by Primefaces
	 * @param eventsToAdd List of {@link ScheduleEvent} to be added
	 * 	to the model
	 */
	public void addEvents(List<ScheduleEvent> eventsToAdd) {
		for(ScheduleEvent eventToAdd : eventsToAdd) {
			this.model.addEvent(eventToAdd);
		}
	}

	/**
	 * Ajax function call when a date is selected
	 * @param e The {@link DateSelectEvent} that contains the data for the 
	 * 	date selection
	 */
	public void onDateSelect(SelectEvent e) {
		Date selectedDate = (Date) e.getObject();
		
		DateTime endDate = new DateTime(selectedDate);
		endDate = endDate.plusHours(2);
		
		event = new NewAvailabilityScheduleEvent(" ", selectedDate, endDate.toDate(), "");
	}

	/**
	 * Ajax function call when the existing event is moved
	 * @param e The {@link ScheduleEntryMoveEvent} that contains the data for the
	 * 	event selection
	 */
	public void onEventMove(ScheduleEntryMoveEvent e) {
		event = (NewAvailabilityScheduleEvent) e.getScheduleEvent();
		this.addEvent();
	}

	/**
	 * Ajax function call when an existing event is resized
	 * @param e The {@link ScheduleEntryResizeEvent} that contains the data for
	 * 	the event selection
	 */
	public void onEventResize(ScheduleEntryResizeEvent e) {
		event = (NewAvailabilityScheduleEvent) e.getScheduleEvent();
		this.addEvent();
	}
	
	/**
	 * Ajax function to add new event to the model
	 * or update the existing event
	 */
	public void addEvent() {
		if(event.getId() == null) {
			model.addEvent(event);
		}
		else {
			model.updateEvent(event);
		}
		event = new NewAvailabilityScheduleEvent();
	}

	public NewAvailabilityScheduleEvent getEvent() {
		return event;
	}

	public ScheduleModel getModel() {
		return model;
	}

	public TimeZone getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}
}
