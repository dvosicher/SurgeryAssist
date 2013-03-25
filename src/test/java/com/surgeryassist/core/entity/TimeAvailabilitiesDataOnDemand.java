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

import com.surgeryassist.core.entity.DayAvailability;
import com.surgeryassist.core.entity.TimeAvailabilities;

@Configurable
@Component
public class TimeAvailabilitiesDataOnDemand {
	private Random rnd = new SecureRandom();

	private List<TimeAvailabilities> data;

	@Autowired
	AvailabilityDataOnDemand availabilityDataOnDemand;

	public TimeAvailabilities getNewTransientTimeAvailabilities(int index) {
		TimeAvailabilities obj = new TimeAvailabilities();
		setAvailabilityId(obj, index);
		setCreatedBy(obj, index);
		setCreatedDate(obj, index);
		setEndTime(obj, index);
		setModifiedBy(obj, index);
		setModifiedDate(obj, index);
		setStartTime(obj, index);
		setIsBooked(obj);
		return obj;
	}


	public void setAvailabilityId(TimeAvailabilities obj, int index) {
		DayAvailability availabilityId = availabilityDataOnDemand.getRandomAvailability();
		obj.setAvailabilityId(availabilityId);
	}

	private void setIsBooked(TimeAvailabilities obj) {
		obj.setIsBooked(false);
		obj.setIsCancelled(false);
	}
	
	public void setCreatedBy(TimeAvailabilities obj, int index) {
		Integer createdBy = new Integer(index);
		obj.setCreatedBy(createdBy);
	}

	public void setCreatedDate(TimeAvailabilities obj, int index) {
		Calendar createdDate = Calendar.getInstance();
		obj.setCreatedDate(createdDate);
	}

	public void setEndTime(TimeAvailabilities obj, int index) {
		Calendar endTime = Calendar.getInstance();
		obj.setEndTime(endTime);
	}

	public void setModifiedBy(TimeAvailabilities obj, int index) {
		Integer modifiedBy = new Integer(index);
		obj.setModifiedBy(modifiedBy);
	}

	public void setModifiedDate(TimeAvailabilities obj, int index) {
		Calendar modifiedDate = Calendar.getInstance();
		obj.setModifiedDate(modifiedDate);
	}

	public void setStartTime(TimeAvailabilities obj, int index) {
		Calendar startTime = Calendar.getInstance();
		obj.setStartTime(startTime);
	}

	public TimeAvailabilities getSpecificTimeAvailabilities(int index) {
		init();
		if (index < 0) {
			index = 0;
		}
		if (index > (data.size() - 1)) {
			index = data.size() - 1;
		}
		TimeAvailabilities obj = data.get(index);
		Integer id = obj.getTimeAvailabilityID();
		return TimeAvailabilities.findTimeAvailabilities(id);
	}

	public TimeAvailabilities getRandomTimeAvailabilities() {
		init();
		TimeAvailabilities obj = data.get(rnd.nextInt(data.size()));
		Integer id = obj.getTimeAvailabilityID();
		return TimeAvailabilities.findTimeAvailabilities(id);
	}

	public boolean modifyTimeAvailabilities(TimeAvailabilities obj) {
		return false;
	}

	public void init() {
		int from = 0;
		int to = 10;
		data = TimeAvailabilities.findTimeAvailabilitiesEntries(from, to);
		if (data == null) {
			throw new IllegalStateException("Find entries implementation for 'TimeAvailabilities' illegally returned null");
		}
		if (!data.isEmpty()) {
			return;
		}

		data = new ArrayList<TimeAvailabilities>();
		for (int i = 0; i < 10; i++) {
			TimeAvailabilities obj = getNewTransientTimeAvailabilities(i);
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
