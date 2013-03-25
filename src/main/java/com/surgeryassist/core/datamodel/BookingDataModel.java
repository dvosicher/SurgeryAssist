package com.surgeryassist.core.datamodel;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import com.surgeryassist.core.entity.Bookings;

public class BookingDataModel extends ListDataModel<Bookings> implements
		SelectableDataModel<Bookings> {

	@Override
	public Object getRowKey(Bookings object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bookings getRowData(String rowKey) {
		// TODO Auto-generated method stub
		return null;
	}

}
