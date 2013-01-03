package com.surgeryassist.core.entity;

import java.util.List;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Configurable
@Component
public class StateCodeDataOnDemand {

	public StateCode getNewTransientStateCode() {
		List<StateCode> obj = StateCode.findAllStateCodes();
		if(obj == null || obj.isEmpty()) {
			return new StateCode();
		}
		return obj.get(0);
	}
}
