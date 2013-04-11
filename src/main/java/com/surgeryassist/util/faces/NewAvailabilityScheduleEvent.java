package com.surgeryassist.util.faces;

import java.io.Serializable;
import java.util.Date;

import org.primefaces.model.ScheduleEvent;

public class NewAvailabilityScheduleEvent implements ScheduleEvent, Serializable {

	private static final long serialVersionUID = 1186520150235785361L;

	private String id;

	private String title;

	private Date startDate;

	private Date endDate;

	private boolean allDay = false;

	private String styleClass;

	private Object data;

	private boolean editable = true;
	
	private String roomNumber;

	public NewAvailabilityScheduleEvent() {}
    
    public NewAvailabilityScheduleEvent(String title, Date start, Date end, String roomNumber) {
            this.title = title;
            this.startDate = start;
            this.endDate = end;
            this.setRoomNumber(roomNumber);
    }
    
    public NewAvailabilityScheduleEvent(String title, Date start, Date end, String roomNumber, boolean allDay) {
            this.title = title;
            this.startDate = start;
            this.endDate = end;
            this.setRoomNumber(roomNumber);
            this.allDay = allDay;
    }
    
    public NewAvailabilityScheduleEvent(String title, Date start, Date end, String roomNumber, String styleClass) {
            this.title = title;
            this.startDate = start;
            this.endDate = end;
            this.setRoomNumber(roomNumber);
            this.styleClass = styleClass;
    }
    
    public NewAvailabilityScheduleEvent(String title, Date start, Date end, String roomNumber, Object data) {
            this.title = title;
            this.startDate = start;
            this.endDate = end;
            this.setRoomNumber(roomNumber);
            this.data = data;
    }
	
	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public Object getData() {
		return this.data;
	}

	@Override
	public String getTitle() {
		return this.title;
	}

	@Override
	public Date getStartDate() {
		return this.startDate;
	}

	@Override
	public Date getEndDate() {
		return this.endDate;
	}

	@Override
	public boolean isAllDay() {
		return this.allDay;
	}

	@Override
	public String getStyleClass() {
		return this.styleClass;
	}

	@Override
	public boolean isEditable() {
		return this.editable;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setAllDay(boolean allDay) {
		this.allDay = allDay;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	public void setEditable(boolean editable) {
		this.editable = editable;
	}

}
