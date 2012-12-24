package com.surgeryassist.core.entity;

import com.surgeryassist.core.UserTypeCode;
import com.surgeryassist.core.VerificationStatus;
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

@Component
@Configurable
public class ApplicationUserDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<ApplicationUser> data;

	public ApplicationUser getNewTransientApplicationUser(int index) {
        ApplicationUser obj = new ApplicationUser();
        setCreatedBy(obj, index);
        setCreatedDate(obj, index);
        setModifiedBy(obj, index);
        setModifiedDate(obj, index);
        setUserEmail(obj, index);
        setUserInfoID(obj, index);
        setUserTypeCode(obj, index);
        setVerificationStatus(obj, index);
        return obj;
    }

	public void setCreatedBy(ApplicationUser obj, int index) {
        Integer createdBy = new Integer(index);
        obj.setCreatedBy(createdBy);
    }

	public void setCreatedDate(ApplicationUser obj, int index) {
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

	public void setModifiedBy(ApplicationUser obj, int index) {
        Integer modifiedBy = new Integer(index);
        obj.setModifiedBy(modifiedBy);
    }

	public void setModifiedDate(ApplicationUser obj, int index) {
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

	public void setUserEmail(ApplicationUser obj, int index) {
        String userEmail = "foo" + index + "@bar.com";
        obj.setUserEmail(userEmail);
    }

	public void setUserInfoID(ApplicationUser obj, int index) {
        UserInfo userInfoID = null;
        obj.setUserInfoID(userInfoID);
    }

	public void setUserTypeCode(ApplicationUser obj, int index) {
        UserTypeCode userTypeCode = UserTypeCode.class.getEnumConstants()[0];
        obj.setUserTypeCode(userTypeCode);
    }

	public void setVerificationStatus(ApplicationUser obj, int index) {
        VerificationStatus verificationStatus = VerificationStatus.class.getEnumConstants()[0];
        obj.setVerificationStatus(verificationStatus);
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
