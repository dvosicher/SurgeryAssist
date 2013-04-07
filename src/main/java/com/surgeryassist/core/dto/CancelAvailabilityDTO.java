package com.surgeryassist.core.dto;

import java.io.Serializable;
import java.util.List;

import org.primefaces.event.SelectEvent;

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

	/**
	 * All the {@link TimeAvailabilities} to be shown
	 */
	private List<TimeAvailabilities> timeAvailabilitiesList;

	/**
	 * The selected {@link TimeAvailabilities} for the next page
	 */
	private TimeAvailabilities[] selectedTimeAvailabilities;
	
	/**
	 * The data model to select/unselect data
	 */
	private TimeAvailabilitiesDataModel timeAvailabilityDataModel;

	public CancelAvailabilityDTO() { }

	/**
	 * Constructor that sets the data model based on the
	 * passed in list 
	 * @param timeAvailabilitiesList The list of {@link TimeAvailabilities}
	 */
	public CancelAvailabilityDTO(List<TimeAvailabilities> timeAvailabilitiesList) {
		this.timeAvailabilitiesList = timeAvailabilitiesList;
		timeAvailabilityDataModel = 
				new TimeAvailabilitiesDataModel(timeAvailabilitiesList);
	}

	/**
	 * Empty method that does nothing, but is necessary
	 * for Primefaces functionality
	 * @param event {@link SelectEvent} that contains data
	 * 	about the event
	 */
	public void onRowSelect(SelectEvent event) {
		//do nothing, because primefaces is stupid
	}
	
	/************************ Getters and Setters *************************/
	
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

	public TimeAvailabilitiesDataModel getTimeAvailabilityDataModel() {
		return timeAvailabilityDataModel;
	}

	public void setTimeAvailabilityDataModel(TimeAvailabilitiesDataModel timeAvailabilityDataModel) {
		this.timeAvailabilityDataModel = timeAvailabilityDataModel;
	}

}