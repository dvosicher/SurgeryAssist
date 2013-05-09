package com.surgeryassist.util.faces.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang3.StringUtils;

import com.surgeryassist.core.UserTypeCode;

@FacesConverter("userTypeCodeConverter")
public class UserTypeCodeConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		if(value == null) {
			return null;
		}

		String enumValue = value.toUpperCase();
		UserTypeCode returnEnum = UserTypeCode.valueOf(enumValue);
		return returnEnum;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if(value != null) {
			if(value instanceof UserTypeCode) {
				//hack to make the Surgeon enum look a little prettier
				if(UserTypeCode.SURGEON.equals(value)) {
					String returnValue = 
							StringUtils.capitalize(
									((UserTypeCode) value).toString().toLowerCase());
					return returnValue;
				}
				else {
					return ((UserTypeCode) value).toString();
				}
			}
		}
		return new String();
	}

}
