package com.surgeryassist.core.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.surgeryassist.core.datamodel.TimeAvailabilitiesDataModel;
import com.surgeryassist.core.entity.TimeAvailabilities;

/**
 * Class to handle the Cancel DataTable used
 * for managing multiple selections of 
 * {@link TimeAvailabilities}
 * @author Ankit Tyagi
 */
public class CancelAvailabilityDTO implements Serializable {

	private static final long serialVersionUID = 7699468063287433191L;

	List<TimeAvailabilities> timeAvailabilitiesList;
	
	TimeAvailabilities[] selectedTimeAvailabilities;
	
	TimeAvailabilitiesDataModel dataModel;
	
	public CancelAvailabilityDTO() {
		this.timeAvailabilitiesList = new ArrayList<TimeAvailabilities>();
		this.selectedTimeAvailabilities = new TimeAvailabilities[10];
	}
	
	public CancelAvailabilityDTO(List<TimeAvailabilities> timeAvailabilitiesList) {
		this.timeAvailabilitiesList = timeAvailabilitiesList;
		this.dataModel = new TimeAvailabilitiesDataModel(this.timeAvailabilitiesList);
	}
	
	public List<TimeAvailabilities> getTimeAvailabilitiesList() {
		return timeAvailabilitiesList;
	}

	public void setTimeAvailabilitiesList(
			List<TimeAvailabilities> timeAvailabilitiesList) {
		this.timeAvailabilitiesList = timeAvailabilitiesList;
	}

	public TimeAvailabilities[] getSelectedTimeAvailabilities() {
		return selectedTimeAvailabilities;
	}

	public void setSelectedTimeAvailabilities(
			TimeAvailabilities[] selectedTimeAvailabilities) {
		this.selectedTimeAvailabilities = selectedTimeAvailabilities;
	}

	public TimeAvailabilitiesDataModel getDataModel() {
		return dataModel;
	}

	public void setDataModel(TimeAvailabilitiesDataModel dataModel) {
		this.dataModel = dataModel;
	}

}