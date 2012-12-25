/**
 * 
 */
package com.surgeryassist.core.entity;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;


/**
 * @author atyagi
 *
 */
@Component
@Configurable
public class UserInfoDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<UserInfo> data;

	public UserInfo getNewTransientUserInfo(int index) {
		UserInfo obj = new UserInfo();
		setCreatedBy(obj, index);
		setCreatedDate(obj, index);
		setModifiedBy(obj, index);
		setModifiedDate(obj, index);
		setFirstName(obj, index);
		setLastName(obj, index);
		//setLocation(obj, index);
		//setContactInfo(obj, index);
		//setPhotoFilePath(obj, index);
		//setVideoFilePath(obj, index);
		return obj;
	}

	public void setCreatedBy(UserInfo obj, int index) {
		Integer createdBy = new Integer(index);
		obj.setCreatedBy(createdBy);
	}

	public void setCreatedDate(UserInfo obj, int index) {
		Date createdDate = 
				new GregorianCalendar(
						Calendar.getInstance().get(Calendar.YEAR), 
						Calendar.getInstance().get(Calendar.MONTH), 
						Calendar.getInstance().get(Calendar.DAY_OF_MONTH), 
						Calendar.getInstance().get(Calendar.HOUR_OF_DAY), 
						Calendar.getInstance().get(Calendar.MINUTE), 
						Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
		obj.setCreatedDate(createdDate);
	}

	public void setModifiedBy(UserInfo obj, int index) {
		Integer modifiedBy = new Integer(index);
		obj.setModifiedBy(modifiedBy);
	}

	public void setModifiedDate(UserInfo obj, int index) {
		Date modifiedDate = 
				new GregorianCalendar(
						Calendar.getInstance().get(Calendar.YEAR), 
						Calendar.getInstance().get(Calendar.MONTH), 
						Calendar.getInstance().get(Calendar.DAY_OF_MONTH), 
						Calendar.getInstance().get(Calendar.HOUR_OF_DAY), 
						Calendar.getInstance().get(Calendar.MINUTE), 
						Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
		obj.setModifiedDate(modifiedDate);
	}

	public void setFirstName(UserInfo obj, int index) {
		String firstName = "foo" + index;
		obj.setFirstName(firstName);
	}

	public void setLastName(UserInfo obj, int index) {
		String lastName = index + "bar";
		obj.setLastName(lastName);
	}


	public UserInfo getSpecificUserInfo(int index) {
		init();
		if (index < 0) {
			index = 0;
		}
		if (index > (data.size() - 1)) {
			index = data.size() - 1;
		}
		UserInfo obj = data.get(index);
		Integer id = obj.getUserInfoID();
		return UserInfo.findUserInfo(id);
	}

	public UserInfo getRandomUserInfo() {
		init();
		UserInfo obj = data.get(rnd.nextInt(data.size()));
		Integer id = obj.getUserInfoID();
		return UserInfo.findUserInfo(id);
	}

	public boolean modifyUserInfo(UserInfo obj) {
		return false;
	}

	public void init() {
		int from = 0;
		int to = 10;
		data = UserInfo.findUserInfoEntries(from, to);
		if (data == null) {
			throw new IllegalStateException("Find entries implementation for 'UserInfo' illegally returned null");
		}
		if (!data.isEmpty()) {
			return;
		}

		data = new ArrayList<UserInfo>();
		for (int i = 0; i < 10; i++) {
			UserInfo obj = getNewTransientUserInfo(i);
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

