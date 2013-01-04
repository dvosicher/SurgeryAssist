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
import com.surgeryassist.core.entity.UserFavorites;

@Configurable
@Component
public class UserFavoritesDataOnDemand {
	
    private Random rnd = new SecureRandom();
    
    private List<UserFavorites> data;
    
    @Autowired
    ApplicationUserDataOnDemand applicationUserDataOnDemand;
    
    public UserFavorites getNewTransientUserFavorites(int index) {
        UserFavorites obj = new UserFavorites();
        setCreatedBy(obj, index);
        setCreatedDate(obj, index);
        setModifiedBy(obj, index);
        setModifiedDate(obj, index);
        setUserFavorite(obj, index);
        setUserId(obj, index);
        return obj;
    }
    
    public void setCreatedBy(UserFavorites obj, int index) {
        Integer createdBy = new Integer(index);
        obj.setCreatedBy(createdBy);
    }
    
    public void setCreatedDate(UserFavorites obj, int index) {
        Calendar createdDate = Calendar.getInstance();
        obj.setCreatedDate(createdDate);
    }
    
    public void setModifiedBy(UserFavorites obj, int index) {
        Integer modifiedBy = new Integer(index);
        obj.setModifiedBy(modifiedBy);
    }
    
    public void setModifiedDate(UserFavorites obj, int index) {
        Calendar modifiedDate = Calendar.getInstance();
        obj.setModifiedDate(modifiedDate);
    }
    
    public void setUserFavorite(UserFavorites obj, int index) {
        ApplicationUser userFavorite = applicationUserDataOnDemand.getRandomApplicationUser();
        obj.setUserFavorite(userFavorite);
    }
    
    public void setUserId(UserFavorites obj, int index) {
        ApplicationUser userId = applicationUserDataOnDemand.getRandomApplicationUser();
        obj.setUserId(userId);
    }
    
    public UserFavorites getSpecificUserFavorites(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        UserFavorites obj = data.get(index);
        Integer id = obj.getFavoritesId();
        return UserFavorites.findUserFavorites(id);
    }
    
    public UserFavorites getRandomUserFavorites() {
        init();
        UserFavorites obj = data.get(rnd.nextInt(data.size()));
        Integer id = obj.getFavoritesId();
        return UserFavorites.findUserFavorites(id);
    }
    
    public boolean modifyUserFavorites(UserFavorites obj) {
        return false;
    }
    
    public void init() {
        int from = 0;
        int to = 10;
        data = UserFavorites.findUserFavoritesEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'UserFavorites' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<UserFavorites>();
        for (int i = 0; i < 10; i++) {
            UserFavorites obj = getNewTransientUserFavorites(i);
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
