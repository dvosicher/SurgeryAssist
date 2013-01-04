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

import com.surgeryassist.core.entity.SurgeryType;

@Component
@Configurable
public class SurgeryTypeDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<SurgeryType> data;

	public SurgeryType getNewTransientSurgeryType(int index) {
		SurgeryType obj = new SurgeryType();
		setCreatedBy(obj, index);
		setCreatedDate(obj, index);
		setModifiedBy(obj, index);
		setModifiedDate(obj, index);
		setSurgeryDescription(obj, index);
		setSurgeryFullName(obj, index);
		setSurgeryTypeCode(obj, index);
		return obj;
	}

	public void setCreatedBy(SurgeryType obj, int index) {
		Integer createdBy = new Integer(index);
		obj.setCreatedBy(createdBy);
	}

	public void setCreatedDate(SurgeryType obj, int index) {
		Calendar createdDate = Calendar.getInstance();
		obj.setCreatedDate(createdDate);
	}

	public void setModifiedBy(SurgeryType obj, int index) {
		Integer modifiedBy = new Integer(index);
		obj.setModifiedBy(modifiedBy);
	}

	public void setModifiedDate(SurgeryType obj, int index) {
		Calendar modifiedDate = Calendar.getInstance();
		obj.setModifiedDate(modifiedDate);
	}

	public void setSurgeryDescription(SurgeryType obj, int index) {
		String surgeryDescription = "surgeryDescription_" + index;
		if (surgeryDescription.length() > 512) {
			surgeryDescription = surgeryDescription.substring(0, 512);
		}
		obj.setSurgeryDescription(surgeryDescription);
	}

	public void setSurgeryFullName(SurgeryType obj, int index) {
		String surgeryFullName = "surgeryFullName_" + index;
		if (surgeryFullName.length() > 100) {
			surgeryFullName = surgeryFullName.substring(0, 100);
		}
		obj.setSurgeryFullName(surgeryFullName);
	}

	public void setSurgeryTypeCode(SurgeryType obj, int index) {
		String surgeryTypeCode = "surgeryT_" + index;
		if (surgeryTypeCode.length() > 10) {
			surgeryTypeCode = surgeryTypeCode.substring(0, 10);
		}
		obj.setSurgeryTypeCode(surgeryTypeCode);
	}

	public SurgeryType getSpecificSurgeryType(int index) {
		init();
		if (index < 0) {
			index = 0;
		}
		if (index > (data.size() - 1)) {
			index = data.size() - 1;
		}
		SurgeryType obj = data.get(index);
		Integer id = obj.getSurgeryId();
		return SurgeryType.findSurgeryType(id);
	}

	public SurgeryType getRandomSurgeryType() {
		init();
		SurgeryType obj = data.get(rnd.nextInt(data.size()));
		Integer id = obj.getSurgeryId();
		return SurgeryType.findSurgeryType(id);
	}

	public boolean modifySurgeryType(SurgeryType obj) {
		return false;
	}

	public void init() {
		int from = 0;
		int to = 10;
		data = SurgeryType.findSurgeryTypeEntries(from, to);
		if (data == null) {
			throw new IllegalStateException("Find entries implementation for 'SurgeryType' illegally returned null");
		}
		if (!data.isEmpty()) {
			return;
		}

		data = new ArrayList<SurgeryType>();
		for (int i = 0; i < 10; i++) {
			SurgeryType obj = getNewTransientSurgeryType(i);
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

}
