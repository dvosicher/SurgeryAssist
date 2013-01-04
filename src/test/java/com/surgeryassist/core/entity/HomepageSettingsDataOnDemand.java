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
import com.surgeryassist.core.entity.HomepageSettings;
import com.surgeryassist.core.entity.HomepageWidget;

@Component
@Configurable
public class HomepageSettingsDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<HomepageSettings> data;

	@Autowired
	ApplicationUserDataOnDemand applicationUserDataOnDemand;

	@Autowired
	HomepageWidgetDataOnDemand homepageWidgetDataOnDemand;

	public HomepageSettings getNewTransientHomepageSettings(int index) {
		HomepageSettings obj = new HomepageSettings();
		setCreatedBy(obj, index);
		setCreatedDate(obj, index);
		setModifiedBy(obj, index);
		setModifiedDate(obj, index);
		setShowWidget(obj, index);
		setUserId(obj, index);
		setWidgetId(obj, index);
		return obj;
	}

	public void setCreatedBy(HomepageSettings obj, int index) {
		Integer createdBy = new Integer(index);
		obj.setCreatedBy(createdBy);
	}

	public void setCreatedDate(HomepageSettings obj, int index) {
		Calendar createdDate = Calendar.getInstance();
		obj.setCreatedDate(createdDate);
	}

	public void setModifiedBy(HomepageSettings obj, int index) {
		Integer modifiedBy = new Integer(index);
		obj.setModifiedBy(modifiedBy);
	}

	public void setModifiedDate(HomepageSettings obj, int index) {
		Calendar modifiedDate = Calendar.getInstance();
		obj.setModifiedDate(modifiedDate);
	}

	public void setShowWidget(HomepageSettings obj, int index) {
		Boolean showWidget = Boolean.TRUE;
		obj.setShowWidget(showWidget);
	}

	public void setUserId(HomepageSettings obj, int index) {
		ApplicationUser userId = applicationUserDataOnDemand.getRandomApplicationUser();
		obj.setUserId(userId);
	}

	public void setWidgetId(HomepageSettings obj, int index) {
		HomepageWidget widgetId = homepageWidgetDataOnDemand.getRandomHomepageWidget();
		obj.setWidgetId(widgetId);
	}

	public HomepageSettings getSpecificHomepageSettings(int index) {
		init();
		if (index < 0) {
			index = 0;
		}
		if (index > (data.size() - 1)) {
			index = data.size() - 1;
		}
		HomepageSettings obj = data.get(index);
		Integer id = obj.getHomepageSettingsId();
		return HomepageSettings.findHomepageSettings(id);
	}

	public HomepageSettings getRandomHomepageSettings() {
		init();
		HomepageSettings obj = data.get(rnd.nextInt(data.size()));
		Integer id = obj.getHomepageSettingsId();
		return HomepageSettings.findHomepageSettings(id);
	}

	public boolean modifyHomepageSettings(HomepageSettings obj) {
		return false;
	}

	public void init() {
		int from = 0;
		int to = 10;
		data = HomepageSettings.findHomepageSettingsEntries(from, to);
		if (data == null) {
			throw new IllegalStateException("Find entries implementation for 'HomepageSettings' illegally returned null");
		}
		if (!data.isEmpty()) {
			return;
		}

		data = new ArrayList<HomepageSettings>();
		for (int i = 0; i < 10; i++) {
			HomepageSettings obj = getNewTransientHomepageSettings(i);
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
