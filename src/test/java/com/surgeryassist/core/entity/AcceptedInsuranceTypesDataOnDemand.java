package com.surgeryassist.core.entity;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import com.surgeryassist.core.entity.InsuranceType;
import com.surgeryassist.core.entity.InsuranceTypeDataOnDemand;
import com.surgeryassist.core.entity.AcceptedInsuranceTypes;
import com.surgeryassist.core.entity.UserInfo;
import com.surgeryassist.core.entity.UserInfoDataOnDemand;

@Configurable
@Component
public class AcceptedInsuranceTypesDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<AcceptedInsuranceTypes> data;

	@Autowired
	InsuranceTypeDataOnDemand insuranceTypeDataOnDemand;

	@Autowired
	UserInfoDataOnDemand userInfoDataOnDemand;

	public AcceptedInsuranceTypes getNewTransientAcceptedInsuranceTypes(int index) {
		AcceptedInsuranceTypes obj = new AcceptedInsuranceTypes();
		setCreatedBy(obj, index);
		setCreatedDate(obj, index);
		setInsuranceTypeId(obj, index);
		setModifiedBy(obj, index);
		setModifiedDate(obj, index);
		setUserInfoId(obj, index);
		return obj;
	}

	public void setCreatedBy(AcceptedInsuranceTypes obj, int index) {
		Integer createdBy = new Integer(index);
		obj.setCreatedBy(createdBy);
	}

	public void setCreatedDate(AcceptedInsuranceTypes obj, int index) {
		Calendar createdDate = Calendar.getInstance();
		obj.setCreatedDate(createdDate);
	}

	public void setInsuranceTypeId(AcceptedInsuranceTypes obj, int index) {
		InsuranceType insuranceTypeId = insuranceTypeDataOnDemand.getRandomInsuranceType();
		obj.setInsuranceTypeId(insuranceTypeId);
	}

	public void setModifiedBy(AcceptedInsuranceTypes obj, int index) {
		Integer modifiedBy = new Integer(index);
		obj.setModifiedBy(modifiedBy);
	}

	public void setModifiedDate(AcceptedInsuranceTypes obj, int index) {
		Calendar modifiedDate = Calendar.getInstance();
		obj.setModifiedDate(modifiedDate);
	}

	public void setUserInfoId(AcceptedInsuranceTypes obj, int index) {
		UserInfo userInfoId = userInfoDataOnDemand.getRandomUserInfo();
		obj.setUserInfoId(userInfoId);
	}

	public AcceptedInsuranceTypes getSpecificAcceptedInsuranceTypes(int index) {
		init();
		if (index < 0) {
			index = 0;
		}
		if (index > (data.size() - 1)) {
			index = data.size() - 1;
		}
		AcceptedInsuranceTypes obj = data.get(index);
		Integer id = obj.getAcceptedInsuranceID();
		return AcceptedInsuranceTypes.findAcceptedInsuranceTypes(id);
	}

	public AcceptedInsuranceTypes getRandomAcceptedInsuranceTypes() {
		init();
		AcceptedInsuranceTypes obj = data.get(rnd.nextInt(data.size()));
		Integer id = obj.getAcceptedInsuranceID();
		return AcceptedInsuranceTypes.findAcceptedInsuranceTypes(id);
	}

	public boolean modifyAcceptedInsuranceTypes(AcceptedInsuranceTypes obj) {
		return false;
	}

	public void init() {
		int from = 0;
		int to = 10;
		data = AcceptedInsuranceTypes.findAcceptedInsuranceTypesEntries(from, to);
		if (data == null) {
			throw new IllegalStateException("Find entries implementation for 'AcceptedInsuranceTypes' illegally returned null");
		}
		if (!data.isEmpty()) {
			return;
		}

		data = new ArrayList<AcceptedInsuranceTypes>();
		for (int i = 0; i < 10; i++) {
			AcceptedInsuranceTypes obj = getNewTransientAcceptedInsuranceTypes(i);
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
