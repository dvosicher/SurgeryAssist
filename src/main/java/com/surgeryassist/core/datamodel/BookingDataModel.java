package com.surgeryassist.core.datamodel;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SelectableDataModel;

import com.surgeryassist.core.entity.Bookings;

/**
 * Data Model for the {@link Bookings} object,
 * used for Canceling Bookings, based off of the
 * Primefaces data models. Returns a row key and the row data
 * when necessary based on the referenced object.
 * @author Ankit Tyagi
 * @see ListDataModel
 * @see SelectableDataModel
 * @see Serializable
 */
public class BookingDataModel extends ListDataModel<Bookings> implements
		SelectableDataModel<Bookings>, Serializable {

	private static final long serialVersionUID = -7776276299056321716L;

	public BookingDataModel() { }
	
	public BookingDataModel(List<Bookings> data) {
		super(data);
	}
	
	@Override
	public Object getRowKey(Bookings object) {
		if(object != null) {
			return object.getBookingId();
		}
		return null;
	}

	@Override
	public Bookings getRowData(String rowKey) {
		if(rowKey != null && StringUtils.isNumeric(rowKey)) {
			return Bookings.findBookings(Integer.parseInt(rowKey));
		}
		return null;
	}

}
