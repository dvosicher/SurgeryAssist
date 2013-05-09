package com.surgeryassist.core.datamodel;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SelectableDataModel;

import com.surgeryassist.core.entity.TimeAvailabilities;

/**
 * Data Model for the {@link TimeAvailabilities} object,
 * used for Canceling Availabilities, based off of the
 * Primefaces data models. Returns a row key and the row data
 * when necessary based on the referenced object.
 * @author Ankit Tyagi
 * @see ListDataModel
 * @see SelectableDataModel
 * @see Serializable
 */
public class TimeAvailabilitiesDataModel extends
		ListDataModel<TimeAvailabilities> implements
		SelectableDataModel<TimeAvailabilities>, Serializable {

	private static final long serialVersionUID = -7521862952682554514L;

	public TimeAvailabilitiesDataModel() { }
	
	public TimeAvailabilitiesDataModel(List<TimeAvailabilities> data) {
		super(data);
	}
	
	@Override
	public Object getRowKey(TimeAvailabilities object) {
		if(object != null) {
			return object.getTimeAvailabilityID();
		}
		return null;
	}

	@Override
	public TimeAvailabilities getRowData(String rowKey) {
		if(rowKey != null && StringUtils.isNumeric(rowKey)) {
			return TimeAvailabilities.findTimeAvailabilities(Integer.parseInt(rowKey));
		}
		return null;
	}

}
