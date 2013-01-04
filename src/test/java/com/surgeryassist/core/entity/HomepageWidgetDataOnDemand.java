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

import com.surgeryassist.core.entity.HomepageWidget;

@Configurable
@Component
public class HomepageWidgetDataOnDemand {
	
	private Random rnd = new SecureRandom();

	private List<HomepageWidget> data;

	public HomepageWidget getNewTransientHomepageWidget(int index) {
		HomepageWidget obj = new HomepageWidget();
		setCreatedBy(obj, index);
		setCreatedDate(obj, index);
		setModifiedBy(obj, index);
		setModifiedDate(obj, index);
		setWidgetName(obj, index);
		return obj;
	}

	public void setCreatedBy(HomepageWidget obj, int index) {
		Integer createdBy = new Integer(index);
		obj.setCreatedBy(createdBy);
	}

	public void setCreatedDate(HomepageWidget obj, int index) {
		Calendar createdDate = Calendar.getInstance();
		obj.setCreatedDate(createdDate);
	}

	public void setModifiedBy(HomepageWidget obj, int index) {
		Integer modifiedBy = new Integer(index);
		obj.setModifiedBy(modifiedBy);
	}

	public void setModifiedDate(HomepageWidget obj, int index) {
		Calendar modifiedDate = Calendar.getInstance();
		obj.setModifiedDate(modifiedDate);
	}

	public void setWidgetName(HomepageWidget obj, int index) {
		String widgetName = "widgetNa_" + index;
		if (widgetName.length() > 10) {
			widgetName = widgetName.substring(0, 10);
		}
		obj.setWidgetName(widgetName);
	}

	public HomepageWidget getSpecificHomepageWidget(int index) {
		init();
		if (index < 0) {
			index = 0;
		}
		if (index > (data.size() - 1)) {
			index = data.size() - 1;
		}
		HomepageWidget obj = data.get(index);
		Integer id = obj.getWidgetId();
		return HomepageWidget.findHomepageWidget(id);
	}

	public HomepageWidget getRandomHomepageWidget() {
		init();
		HomepageWidget obj = data.get(rnd.nextInt(data.size()));
		Integer id = obj.getWidgetId();
		return HomepageWidget.findHomepageWidget(id);
	}

	public boolean modifyHomepageWidget(HomepageWidget obj) {
		return false;
	}

	public void init() {
		int from = 0;
		int to = 10;
		data = HomepageWidget.findHomepageWidgetEntries(from, to);
		if (data == null) {
			throw new IllegalStateException("Find entries implementation for 'HomepageWidget' illegally returned null");
		}
		if (!data.isEmpty()) {
			return;
		}

		data = new ArrayList<HomepageWidget>();
		for (int i = 0; i < 10; i++) {
			HomepageWidget obj = getNewTransientHomepageWidget(i);
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
