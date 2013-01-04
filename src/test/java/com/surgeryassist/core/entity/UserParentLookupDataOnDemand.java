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
import com.surgeryassist.core.entity.UserParentLookup;

@Configurable
@Component
public class UserParentLookupDataOnDemand {
	private Random rnd = new SecureRandom();

	private List<UserParentLookup> data;

	@Autowired
	ApplicationUserDataOnDemand applicationUserDataOnDemand;

	public UserParentLookup getNewTransientUserParentLookup(int index) {
		UserParentLookup obj = new UserParentLookup();
		setCreatedBy(obj, index);
		setCreatedDate(obj, index);
		setModifiedBy(obj, index);
		setModifiedDate(obj, index);
		setParentId(obj, index);
		setUserId(obj, index);
		return obj;
	}

	public void setCreatedBy(UserParentLookup obj, int index) {
		Integer createdBy = new Integer(index);
		obj.setCreatedBy(createdBy);
	}

	public void setCreatedDate(UserParentLookup obj, int index) {
		Calendar createdDate = Calendar.getInstance();
		obj.setCreatedDate(createdDate);
	}

	public void setModifiedBy(UserParentLookup obj, int index) {
		Integer modifiedBy = new Integer(index);
		obj.setModifiedBy(modifiedBy);
	}

	public void setModifiedDate(UserParentLookup obj, int index) {
		Calendar modifiedDate = Calendar.getInstance();
		obj.setModifiedDate(modifiedDate);
	}

	public void setParentId(UserParentLookup obj, int index) {
		ApplicationUser parentId = applicationUserDataOnDemand.getRandomApplicationUser();
		obj.setParentId(parentId);
	}

	public void setUserId(UserParentLookup obj, int index) {
		ApplicationUser userId = applicationUserDataOnDemand.getRandomApplicationUser();
		obj.setUserId(userId);
	}

	public UserParentLookup getSpecificUserParentLookup(int index) {
		init();
		if (index < 0) {
			index = 0;
		}
		if (index > (data.size() - 1)) {
			index = data.size() - 1;
		}
		UserParentLookup obj = data.get(index);
		Integer id = obj.getUserParentLookupID();
		return UserParentLookup.findUserParentLookup(id);
	}

	public UserParentLookup getRandomUserParentLookup() {
		init();
		UserParentLookup obj = data.get(rnd.nextInt(data.size()));
		Integer id = obj.getUserParentLookupID();
		return UserParentLookup.findUserParentLookup(id);
	}

	public boolean modifyUserParentLookup(UserParentLookup obj) {
		return false;
	}

	public void init() {
		int from = 0;
		int to = 10;
		data = UserParentLookup.findUserParentLookupEntries(from, to);
		if (data == null) {
			throw new IllegalStateException("Find entries implementation for 'UserParentLookup' illegally returned null");
		}
		if (!data.isEmpty()) {
			return;
		}

		data = new ArrayList<UserParentLookup>();
		for (int i = 0; i < 10; i++) {
			UserParentLookup obj = getNewTransientUserParentLookup(i);
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
