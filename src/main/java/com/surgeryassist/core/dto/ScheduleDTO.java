package com.surgeryassist.core.dto;

import java.io.Serializable;
import java.util.List;

import org.primefaces.component.schedule.Schedule;
import org.primefaces.event.DateSelectEvent;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.ScheduleEntrySelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

/**
 * ScheduleDTO object to be used for the 
 * Primefaces {@link Schedule} component
 * @author Ankit Tyagi
 */
public class ScheduleDTO implements Serializable {

	private static final long serialVersionUID = -6288968959526039477L;

	private ScheduleModel model;

	private DefaultScheduleEvent event;

	public ScheduleDTO() {
		this.model = new DefaultScheduleModel();
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
	public void onDateSelect(DateSelectEvent e) {
		event = new DefaultScheduleEvent(" ", e.getDate(), e.getDate());
	}

	/**
	 * Ajax function call when an existing event is selected
	 * @param e The {@link DateSelectEvent} that contains the data for the
	 * 	event selection
	 */
	public void onEventSelect(ScheduleEntrySelectEvent e) {
		event = (DefaultScheduleEvent) e.getScheduleEvent();
		this.addEvent();
	}

	
	/**
	 * Ajax function call when the existing event is moved
	 * @param e The {@link ScheduleEntryMoveEvent} that contains the data for the
	 * 	event selection
	 */
	public void onEventMove(ScheduleEntryMoveEvent e) {
		event = (DefaultScheduleEvent) e.getScheduleEvent();
		this.addEvent();
	}

	/**
	 * Ajax function call when an existing event is resized
	 * @param e The {@link ScheduleEntryResizeEvent} that contains the data for
	 * 	the event selection
	 */
	public void onEventResize(ScheduleEntryResizeEvent e) {
		event = (DefaultScheduleEvent) e.getScheduleEvent();
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
		event = new DefaultScheduleEvent();
	}

	public DefaultScheduleEvent getEvent() {
		return event;
	}

	public ScheduleModel getModel() {
		return model;
	}
}
