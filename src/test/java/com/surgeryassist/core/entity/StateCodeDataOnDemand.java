package com.surgeryassist.core.entity;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import com.surgeryassist.core.entity.StateCode;

@Configurable
@Component
public class StateCodeDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<StateCode> data;

	public StateCode getNewTransientStateCode(int index) {
		StateCode obj = new StateCode();
		setCreatedBy(obj, index);
		setCreatedDate(obj, index);
		setModifiedBy(obj, index);
		setModifiedDate(obj, index);
		setStateName(obj, index);
		return obj;
	}

	public void setCreatedBy(StateCode obj, int index) {
		Integer createdBy = new Integer(index);
		obj.setCreatedBy(createdBy);
	}

	public void setCreatedDate(StateCode obj, int index) {
		Calendar createdDate = Calendar.getInstance();
		obj.setCreatedDate(createdDate);
	}

	public void setModifiedBy(StateCode obj, int index) {
		Integer modifiedBy = new Integer(index);
		obj.setModifiedBy(modifiedBy);
	}

	public void setModifiedDate(StateCode obj, int index) {
		Calendar modifiedDate = Calendar.getInstance();
		obj.setModifiedDate(modifiedDate);
	}

	public void setStateName(StateCode obj, int index) {
		String stateName = "stateName_" + index;
		if (stateName.length() > 150) {
			stateName = stateName.substring(0, 150);
		}
		obj.setStateName(stateName);
	}

	public StateCode getSpecificStateCode(int index) {
		init();
		if (index < 0) {
			index = 0;
		}
		if (index > (data.size() - 1)) {
			index = data.size() - 1;
		}
		StateCode obj = data.get(index);
		String id = obj.getStateCodeID();
		return StateCode.findStateCode(id);
	}

	public StateCode getRandomStateCode() {
		init();
		StateCode obj = data.get(rnd.nextInt(data.size()));
		String id = obj.getStateCodeID();
		return StateCode.findStateCode(id);
	}

	public boolean modifyStateCode(StateCode obj) {
		return false;
	}

	public void init() {
		int from = 0;
		int to = 10;
		data = StateCode.findStateCodeEntries(from, to);
		if (data == null) {
			throw new IllegalStateException("Find entries implementation for 'StateCode' illegally returned null");
		}
		if (!data.isEmpty()) {
			return;
		}

		data = new ArrayList<StateCode>();
		for (int i = 0; i < 10; i++) {
			StateCode obj = getNewTransientStateCode(i);
			try {
				obj.persist();
			} catch (ConstraintViolationException e) {
				StringBuilder msg = new StringBuilder();
				for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
					ConstraintViolation<?> cv = iter.next();
					msg.append("[").append(cv.getConstraintDescriptor()).append(":").append(cv.getMessage()).append("=").append(cv.getInvalidValue()).append("]");
				}
				throw new RuntimeException(msg.toString(), e);
			}
			obj.flush();
			data.add(obj);
		}
	}

	/**
	 * Hack to get a random State code
	 * @return Random state code
	 */
	public StateCode getNewTransientStateCode() {
		List<StateCode> obj = StateCode.findAllStateCodes();
		if(obj == null || obj.isEmpty()) {
			return new StateCode();
		}
		return obj.get(0);
	}
}
