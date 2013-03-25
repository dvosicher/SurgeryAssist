package com.surgeryassist.core.dto;

import java.io.Serializable;
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

	private List<TimeAvailabilities> timeAvailabilitiesList;

	private TimeAvailabilities[] selectedTimeAvailabilities;
	
	private TimeAvailabilitiesDataModel dataModel;

	public CancelAvailabilityDTO() { }

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