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

@Configurable
@Component
public class ContactInfoDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<ContactInfo> data;

	public ContactInfo getNewTransientContactInfo(int index) {
		ContactInfo obj = new ContactInfo();
		setCreatedBy(obj, index);
		setCreatedDate(obj, index);
		setFaxNumber(obj, index);
		setModifiedBy(obj, index);
		setModifiedDate(obj, index);
		setPhoneNumber(obj, index);
		setSecondaryEmail(obj, index);
		return obj;
	}

	public void setCreatedBy(ContactInfo obj, int index) {
		Integer createdBy = new Integer(index);
		obj.setCreatedBy(createdBy);
	}

	public void setCreatedDate(ContactInfo obj, int index) {
		Calendar createdDate = Calendar.getInstance();
		obj.setCreatedDate(createdDate);
	}

	public void setFaxNumber(ContactInfo obj, int index) {
		String faxNumber = String.valueOf(index);
		obj.setFaxNumber(faxNumber);
	}

	public void setModifiedBy(ContactInfo obj, int index) {
		Integer modifiedBy = new Integer(index);
		obj.setModifiedBy(modifiedBy);
	}

	public void setModifiedDate(ContactInfo obj, int index) {
		Calendar modifiedDate = Calendar.getInstance();
		obj.setModifiedDate(modifiedDate);
	}

	public void setPhoneNumber(ContactInfo obj, int index) {
		String phoneNumber = String.valueOf(index);
		obj.setPhoneNumber(phoneNumber);
	}

	public void setSecondaryEmail(ContactInfo obj, int index) {
		String secondaryEmail = "foo" + index + "@bar.com";
		if (secondaryEmail.length() > 100) {
			secondaryEmail = secondaryEmail.substring(0, 100);
		}
		obj.setSecondaryEmail(secondaryEmail);
	}

	public ContactInfo getSpecificContactInfo(int index) {
		init();
		if (index < 0) {
			index = 0;
		}
		if (index > (data.size() - 1)) {
			index = data.size() - 1;
		}
		ContactInfo obj = data.get(index);
		Integer id = obj.getContactInfoID();
		return ContactInfo.findContactInfo(id);
	}

	public ContactInfo getRandomContactInfo() {
		init();
		ContactInfo obj = data.get(rnd.nextInt(data.size()));
		Integer id = obj.getContactInfoID();
		return ContactInfo.findContactInfo(id);
	}

	public boolean modifyContactInfo(ContactInfo obj) {
		return false;
	}

	public void init() {
		int from = 0;
		int to = 10;
		data = ContactInfo.findContactInfoEntries(from, to);
		if (data == null) {
			throw new IllegalStateException("Find entries implementation for 'ContactInfo' illegally returned null");
		}
		if (!data.isEmpty()) {
			return;
		}

		data = new ArrayList<ContactInfo>();
		for (int i = 0; i < 10; i++) {
			ContactInfo obj = getNewTransientContactInfo(i);
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
