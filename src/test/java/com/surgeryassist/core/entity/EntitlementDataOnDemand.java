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

import com.surgeryassist.core.entity.ApplicationUser;
import com.surgeryassist.core.entity.Entitlement;

@Configurable
@Component
public class EntitlementDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<Entitlement> data;

	@Autowired
	ApplicationUserDataOnDemand applicationUserDataOnDemand;

	public Entitlement getNewTransientEntitlement(int index) {
		Entitlement obj = new Entitlement();
		setCreatedBy(obj, index);
		setCreatedDate(obj, index);
		setEntitlementEndDate(obj, index);
		setEntitlementRenewalCount(obj, index);
		setEntitlementStartDate(obj, index);
		setModifiedBy(obj, index);
		setModifiedDate(obj, index);
		setUserId(obj, index);
		return obj;
	}

	public void setCreatedBy(Entitlement obj, int index) {
		Integer createdBy = new Integer(index);
		obj.setCreatedBy(createdBy);
	}

	public void setCreatedDate(Entitlement obj, int index) {
		Calendar createdDate = Calendar.getInstance();
		obj.setCreatedDate(createdDate);
	}

	public void setEntitlementEndDate(Entitlement obj, int index) {
		Calendar entitlementEndDate = Calendar.getInstance();
		obj.setEntitlementEndDate(entitlementEndDate);
	}

	public void setEntitlementRenewalCount(Entitlement obj, int index) {
		Integer entitlementRenewalCount = new Integer(index);
		obj.setEntitlementRenewalCount(entitlementRenewalCount);
	}

	public void setEntitlementStartDate(Entitlement obj, int index) {
		Calendar entitlementStartDate = Calendar.getInstance();
		obj.setEntitlementStartDate(entitlementStartDate);
	}

	public void setModifiedBy(Entitlement obj, int index) {
		Integer modifiedBy = new Integer(index);
		obj.setModifiedBy(modifiedBy);
	}

	public void setModifiedDate(Entitlement obj, int index) {
		Calendar modifiedDate = Calendar.getInstance();
		obj.setModifiedDate(modifiedDate);
	}

	public void setUserId(Entitlement obj, int index) {
		ApplicationUser userId = applicationUserDataOnDemand.getRandomApplicationUser();
		obj.setUserId(userId);
	}

	public Entitlement getSpecificEntitlement(int index) {
		init();
		if (index < 0) {
			index = 0;
		}
		if (index > (data.size() - 1)) {
			index = data.size() - 1;
		}
		Entitlement obj = data.get(index);
		Integer id = obj.getEntitlementId();
		return Entitlement.findEntitlement(id);
	}

	public Entitlement getRandomEntitlement() {
		init();
		Entitlement obj = data.get(rnd.nextInt(data.size()));
		Integer id = obj.getEntitlementId();
		return Entitlement.findEntitlement(id);
	}

	public boolean modifyEntitlement(Entitlement obj) {
		return false;
	}

	public void init() {
		int from = 0;
		int to = 10;
		data = Entitlement.findEntitlementEntries(from, to);
		if (data == null) {
			throw new IllegalStateException("Find entries implementation for 'Entitlement' illegally returned null");
		}
		if (!data.isEmpty()) {
			return;
		}

		data = new ArrayList<Entitlement>();
		for (int i = 0; i < 10; i++) {
			Entitlement obj = getNewTransientEntitlement(i);
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
