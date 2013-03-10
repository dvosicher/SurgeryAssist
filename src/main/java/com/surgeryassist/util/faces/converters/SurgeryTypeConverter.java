package com.surgeryassist.util.faces.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.surgeryassist.core.entity.SurgeryType;

@FacesConverter(value = "surgeryTypeConverter")
public class SurgeryTypeConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		if(value == null) {
			return null;
		}
		
		Integer surgeryTypeId = null;
		try {
			surgeryTypeId = Integer.parseInt(value);
		}
		catch(NumberFormatException e) {
			e.printStackTrace();
		}
		
		SurgeryType surgeryType = SurgeryType.findSurgeryType(surgeryTypeId);
		
		return surgeryType;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if(value != null) {
			if(value instanceof SurgeryType) {
				String stringValue = ((SurgeryType) value).getSurgeryId().toString();
				return stringValue;
			}
		}
		return new String();
	}

}
