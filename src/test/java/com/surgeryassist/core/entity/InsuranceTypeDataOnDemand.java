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

import com.surgeryassist.core.entity.InsuranceType;

@Configurable
@Component
public class InsuranceTypeDataOnDemand {
	
	private Random rnd = new SecureRandom();

	private List<InsuranceType> data;

	public InsuranceType getNewTransientInsuranceType(int index) {
		InsuranceType obj = new InsuranceType();
		setCreatedBy(obj, index);
		setCreatedDate(obj, index);
		setInsuranceCode(obj, index);
		setInsuranceDescription(obj, index);
		setInsuranceName(obj, index);
		setInsurancePolicyNumber(obj, index);
		setModifiedBy(obj, index);
		setModifiedDate(obj, index);
		return obj;
	}

	public void setCreatedBy(InsuranceType obj, int index) {
		Integer createdBy = new Integer(index);
		obj.setCreatedBy(createdBy);
	}

	public void setCreatedDate(InsuranceType obj, int index) {
		Calendar createdDate = Calendar.getInstance();
		obj.setCreatedDate(createdDate);
	}

	public void setInsuranceCode(InsuranceType obj, int index) {
		String insuranceCode = "insuranc_" + index;
		if (insuranceCode.length() > 10) {
			insuranceCode = insuranceCode.substring(0, 10);
		}
		obj.setInsuranceCode(insuranceCode);
	}

	public void setInsuranceDescription(InsuranceType obj, int index) {
		String insuranceDescription = "insuranceDescription_" + index;
		if (insuranceDescription.length() > 512) {
			insuranceDescription = insuranceDescription.substring(0, 512);
		}
		obj.setInsuranceDescription(insuranceDescription);
	}

	public void setInsuranceName(InsuranceType obj, int index) {
		String insuranceName = "insuranceName_" + index;
		if (insuranceName.length() > 100) {
			insuranceName = insuranceName.substring(0, 100);
		}
		obj.setInsuranceName(insuranceName);
	}

	public void setInsurancePolicyNumber(InsuranceType obj, int index) {
		String insurancePolicyNumber = "insurancePolicyNumber_" + index;
		if (insurancePolicyNumber.length() > 255) {
			insurancePolicyNumber = insurancePolicyNumber.substring(0, 255);
		}
		obj.setInsurancePolicyNumber(insurancePolicyNumber);
	}

	public void setModifiedBy(InsuranceType obj, int index) {
		Integer modifiedBy = new Integer(index);
		obj.setModifiedBy(modifiedBy);
	}

	public void setModifiedDate(InsuranceType obj, int index) {
		Calendar modifiedDate = Calendar.getInstance();
		obj.setModifiedDate(modifiedDate);
	}

	public InsuranceType getSpecificInsuranceType(int index) {
		init();
		if (index < 0) {
			index = 0;
		}
		if (index > (data.size() - 1)) {
			index = data.size() - 1;
		}
		InsuranceType obj = data.get(index);
		Integer id = obj.getInsuranceID();
		return InsuranceType.findInsuranceType(id);
	}

	public InsuranceType getRandomInsuranceType() {
		init();
		InsuranceType obj = data.get(rnd.nextInt(data.size()));
		Integer id = obj.getInsuranceID();
		return InsuranceType.findInsuranceType(id);
	}

	public boolean modifyInsuranceType(InsuranceType obj) {
		return false;
	}

	public void init() {
		int from = 0;
		int to = 10;
		data = InsuranceType.findInsuranceTypeEntries(from, to);
		if (data == null) {
			throw new IllegalStateException("Find entries implementation for 'InsuranceType' illegally returned null");
		}
		if (!data.isEmpty()) {
			return;
		}

		data = new ArrayList<InsuranceType>();
		for (int i = 0; i < 10; i++) {
			InsuranceType obj = getNewTransientInsuranceType(i);
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
