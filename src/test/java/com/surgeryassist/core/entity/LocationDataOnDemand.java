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

@Component
@Configurable
public class LocationDataOnDemand {
	
	private Random rnd = new SecureRandom();

	private List<Location> data;
	
	@Autowired
	private StateCodeDataOnDemand dod;

	public Location getNewTransientLocation(int index) {
		Location obj = new Location();
		setAddress(obj, index);
		setCity(obj, index);
		setCreatedBy(obj, index);
		setCreatedDate(obj, index);
		setModifiedBy(obj, index);
		setModifiedDate(obj, index);
		setStateCode(obj, index);
		setZipCode(obj, index);
		return obj;
	}

	public void setAddress(Location obj, int index) {
		String address = "address_" + index;
		if (address.length() > 500) {
			address = address.substring(0, 500);
		}
		obj.setAddress(address);
	}

	public void setCity(Location obj, int index) {
		String city = "city_" + index;
		if (city.length() > 250) {
			city = city.substring(0, 250);
		}
		obj.setCity(city);
	}

	public void setCreatedBy(Location obj, int index) {
		Integer createdBy = new Integer(index);
		obj.setCreatedBy(createdBy);
	}

	public void setCreatedDate(Location obj, int index) {
		Calendar createdDate = Calendar.getInstance();
		obj.setCreatedDate(createdDate);
	}

	public void setModifiedBy(Location obj, int index) {
		Integer modifiedBy = new Integer(index);
		obj.setModifiedBy(modifiedBy);
	}

	public void setModifiedDate(Location obj, int index) {
		Calendar modifiedDate = Calendar.getInstance();
		obj.setModifiedDate(modifiedDate);
	}

	public void setStateCode(Location obj, int index) {
		//using a hack to allow tests to pull correct value
		StateCode stateCode = dod.getNewTransientStateCode();
		obj.setStateCode(stateCode);
	}

	public void setZipCode(Location obj, int index) {
		String zipCode = Integer.toString(index);
		obj.setZipCode(zipCode);
	}

	public Location getSpecificLocation(int index) {
		init();
		if (index < 0) {
			index = 0;
		}
		if (index > (data.size() - 1)) {
			index = data.size() - 1;
		}
		Location obj = data.get(index);
		Integer id = obj.getLocationID();
		return Location.findLocation(id);
	}

	public Location getRandomLocation() {
		init();
		Location obj = data.get(rnd.nextInt(data.size()));
		Integer id = obj.getLocationID();
		return Location.findLocation(id);
	}

	public boolean modifyLocation(Location obj) {
		return false;
	}

	public void init() {
		int from = 0;
		int to = 10;
		data = Location.findLocationEntries(from, to);
		if (data == null) {
			throw new IllegalStateException("Find entries implementation for 'Location' illegally returned null");
		}
		if (!data.isEmpty()) {
			return;
		}

		data = new ArrayList<Location>();
		for (int i = 0; i < 10; i++) {
			Location obj = getNewTransientLocation(i);
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
