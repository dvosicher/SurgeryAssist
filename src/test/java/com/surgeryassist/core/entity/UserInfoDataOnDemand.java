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

@Configurable
@Component
public class UserInfoDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<UserInfo> data;

	@Autowired
	ContactInfoDataOnDemand contactInfoDataOnDemand;

	@Autowired
	LocationDataOnDemand locationDataOnDemand;

	public UserInfo getNewTransientUserInfo(int index) {
		UserInfo obj = new UserInfo();
		setContactInfoId(obj, index);
		setCreatedBy(obj, index);
		setCreatedDate(obj, index);
		setFirstName(obj, index);
		setLastName(obj, index);
		setLocationId(obj, index);
		setModifiedBy(obj, index);
		setModifiedDate(obj, index);
		setPhotographFilePath(obj, index);
		setVideoFilePath(obj, index);
		return obj;
	}

	public void setContactInfoId(UserInfo obj, int index) {
		ContactInfo contactInfoId = contactInfoDataOnDemand.getRandomContactInfo();
		obj.setContactInfoId(contactInfoId);
	}

	public void setCreatedBy(UserInfo obj, int index) {
		Integer createdBy = new Integer(index);
		obj.setCreatedBy(createdBy);
	}

	public void setCreatedDate(UserInfo obj, int index) {
		Calendar createdDate = Calendar.getInstance();
		obj.setCreatedDate(createdDate);
	}

	public void setFirstName(UserInfo obj, int index) {
		String firstName = "firstName_" + index;
		if (firstName.length() > 100) {
			firstName = firstName.substring(0, 100);
		}
		obj.setFirstName(firstName);
	}

	public void setLastName(UserInfo obj, int index) {
		String lastName = "lastName_" + index;
		if (lastName.length() > 100) {
			lastName = lastName.substring(0, 100);
		}
		obj.setLastName(lastName);
	}

	public void setLocationId(UserInfo obj, int index) {
		Location locationId = locationDataOnDemand.getRandomLocation();
		obj.setLocationId(locationId);
	}

	public void setModifiedBy(UserInfo obj, int index) {
		Integer modifiedBy = new Integer(index);
		obj.setModifiedBy(modifiedBy);
	}

	public void setModifiedDate(UserInfo obj, int index) {
		Calendar modifiedDate = Calendar.getInstance();
		obj.setModifiedDate(modifiedDate);
	}

	public void setPhotographFilePath(UserInfo obj, int index) {
		String photographFilePath = "photographFilePath_" + index;
		if (photographFilePath.length() > 512) {
			photographFilePath = photographFilePath.substring(0, 512);
		}
		obj.setPhotoFilePath(photographFilePath);
	}

	public void setVideoFilePath(UserInfo obj, int index) {
		String videoFilePath = "videoFilePath_" + index;
		if (videoFilePath.length() > 512) {
			videoFilePath = videoFilePath.substring(0, 512);
		}
		obj.setVideoFilePath(videoFilePath);
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
