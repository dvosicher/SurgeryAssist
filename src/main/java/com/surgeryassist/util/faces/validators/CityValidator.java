package com.surgeryassist.util.faces.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.apache.commons.lang3.StringUtils;

@FacesValidator("cityValidator")
public class CityValidator implements Validator {

	@Override
	public void validate(FacesContext context, UIComponent component, Object value)
			throws ValidatorException {

		//empty case is okay
		if(!StringUtils.isEmpty(value.toString())) {
			if(value instanceof String) {
				String stringValue = value.toString();
				if(!StringUtils.isAlphanumericSpace(stringValue)) {
					throwError();
				}
			} 
			else {
				throwError();
			}
		}
	}

	private void throwError() throws ValidatorException {
		FacesMessage msg = new FacesMessage("Not a valid city");
		msg.setSeverity(FacesMessage.SEVERITY_ERROR);
		throw new ValidatorException(msg);
	}
}
