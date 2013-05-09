package com.surgeryassist.util.faces.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.surgeryassist.core.entity.StateCode;

@FacesConverter(value = "stateCodeConverter")
public class StateCodeConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		if(value == null) {
			return null;
		}
		
		StateCode stateCode = StateCode.findStateCode(value);
		
		return stateCode;
		
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		
		if(value != null) {
			if(value instanceof StateCode) {
				String stringValue = ((StateCode) value).getStateCodeID();
				return stringValue;
			}
		}
		return new String();
	}

}
