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

import com.surgeryassist.core.UserTypeCode;
import com.surgeryassist.core.VerificationStatus;

@Component
@Configurable
public class ApplicationUserDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<ApplicationUser> data;

	@Autowired
	UserInfoDataOnDemand userInfoDataOnDemand;

	public ApplicationUser getNewTransientApplicationUser(int index) {
		ApplicationUser obj = new ApplicationUser();
		setCreatedBy(obj, index);
		setCreatedDate(obj, index);
		setIsVerified(obj);
		setIsEnabled(obj);
		setModifiedBy(obj, index);
		setModifiedDate(obj, index);
		setUserEmail(obj, index);
		setUserInfoId(obj, index);
		setUserPass(obj, index);
		setUserTypeCode(obj, index);
		return obj;
	}

	public void setCreatedBy(ApplicationUser obj, int index) {
		Integer createdBy = new Integer(index);
		obj.setCreatedBy(createdBy);
	}
	
	public void setIsEnabled(ApplicationUser obj) {
		obj.setIsEnabled(true);
	}

	public void setCreatedDate(ApplicationUser obj, int index) {
		Calendar createdDate = Calendar.getInstance();
		obj.setCreatedDate(createdDate);
	}

	public void setIsVerified(ApplicationUser obj) {
		obj.setVerificationStatus(VerificationStatus.VERIFIED);
	}

	public void setModifiedBy(ApplicationUser obj, int index) {
		Integer modifiedBy = new Integer(index);
		obj.setModifiedBy(modifiedBy);
	}

	public void setModifiedDate(ApplicationUser obj, int index) {
		Calendar modifiedDate = Calendar.getInstance();
		obj.setModifiedDate(modifiedDate);
	}

	public void setUserEmail(ApplicationUser obj, int index) {
		String userEmail = "foo" + index + "@bar.com";
		if (userEmail.length() > 100) {
			userEmail = userEmail.substring(0, 100);
		}
		obj.setUserEmail(userEmail);
	}

	public void setUserInfoId(ApplicationUser obj, int index) {
		UserInfo userInfoId = userInfoDataOnDemand.getRandomUserInfo();
		obj.setUserInfoId(userInfoId);
	}

	public void setUserPass(ApplicationUser obj, int index) {
		String userPass = "userPass_" + index;
		if (userPass.length() > 512) {
			userPass = userPass.substring(0, 512);
		}
		obj.setUserPass(userPass);
	}

	public void setUserTypeCode(ApplicationUser obj, int index) {
		obj.setUserTypeCode(UserTypeCode.SURGEON);
	}

	public ApplicationUser getSpecificApplicationUser(int index) {
		init();
		if (index < 0) {
			index = 0;
		}
		if (index > (data.size() - 1)) {
			index = data.size() - 1;
		}
		ApplicationUser obj = data.get(index);
		Integer id = obj.getUserID();
		return ApplicationUser.findApplicationUser(id);
	}

	public ApplicationUser getRandomApplicationUser() {
		init();
		ApplicationUser obj = data.get(rnd.nextInt(data.size()));
		Integer id = obj.getUserID();
		return ApplicationUser.findApplicationUser(id);
	}

	public boolean modifyApplicationUser(ApplicationUser obj) {
		return false;
	}

	public void init() {
		int from = 0;
		int to = 10;
		data = ApplicationUser.findApplicationUserEntries(from, to);
		if (data == null) {
			throw new IllegalStateException("Find entries implementation for 'ApplicationUser' illegally returned null");
		}
		if (!data.isEmpty()) {
			return;
		}

		data = new ArrayList<ApplicationUser>();
		for (int i = 0; i < 10; i++) {
			ApplicationUser obj = getNewTransientApplicationUser(i);
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
