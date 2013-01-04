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
import com.surgeryassist.core.entity.DayAvailability;

@Configurable
@Component
public class AvailabilityDataOnDemand {
	
	private Random rnd = new SecureRandom();

	private List<DayAvailability> data;

	@Autowired
	ApplicationUserDataOnDemand applicationUserDataOnDemand;

	public DayAvailability getNewTransientAvailability(int index) {
		DayAvailability obj = new DayAvailability();
		setCreatedBy(obj, index);
		setCreatedDate(obj, index);
		setDateOfAvailability(obj, index);
		setModifiedBy(obj, index);
		setModifiedDate(obj, index);
		setUserId(obj, index);
		return obj;
	}

	public void setCreatedBy(DayAvailability obj, int index) {
		Integer createdBy = new Integer(index);
		obj.setCreatedBy(createdBy);
	}

	public void setCreatedDate(DayAvailability obj, int index) {
		Calendar createdDate = Calendar.getInstance();
		obj.setCreatedDate(createdDate);
	}

	public void setDateOfAvailability(DayAvailability obj, int index) {
		Calendar dateOfAvailability = Calendar.getInstance();
		obj.setDateOfAvailability(dateOfAvailability);
	}

	public void setModifiedBy(DayAvailability obj, int index) {
		Integer modifiedBy = new Integer(index);
		obj.setModifiedBy(modifiedBy);
	}

	public void setModifiedDate(DayAvailability obj, int index) {
		Calendar modifiedDate = Calendar.getInstance();
		obj.setModifiedDate(modifiedDate);
	}

	public void setUserId(DayAvailability obj, int index) {
		ApplicationUser userId = applicationUserDataOnDemand.getRandomApplicationUser();
		obj.setUserId(userId);
	}

	public DayAvailability getSpecificAvailability(int index) {
		init();
		if (index < 0) {
			index = 0;
		}
		if (index > (data.size() - 1)) {
			index = data.size() - 1;
		}
		DayAvailability obj = data.get(index);
		Integer id = obj.getAvailabilityID();
		return DayAvailability.findDayAvailability(id);
	}

	public DayAvailability getRandomAvailability() {
		init();
		DayAvailability obj = data.get(rnd.nextInt(data.size()));
		Integer id = obj.getAvailabilityID();
		return DayAvailability.findDayAvailability(id);
	}

	public boolean modifyAvailability(DayAvailability obj) {
		return false;
	}

	public void init() {
		int from = 0;
		int to = 10;
		data = DayAvailability.findDayAvailabilityEntries(from, to);
		if (data == null) {
			throw new IllegalStateException("Find entries implementation for 'Availability' illegally returned null");
		}
		if (!data.isEmpty()) {
			return;
		}

		data = new ArrayList<DayAvailability>();
		for (int i = 0; i < 10; i++) {
			DayAvailability obj = getNewTransientAvailability(i);
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
