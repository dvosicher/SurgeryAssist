package com.surgeryassist.util;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;

import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.ScheduleEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.surgeryassist.core.entity.ApplicationUser;
import com.surgeryassist.core.entity.TimeAvailabilities;

public class SurgeryAssistUtil {

	/**
	 * Returns a {@link Calendar} object based on 
	 * the {@link Date} parameter
	 * @param date The {@link Date} to convert
	 * @return {@link Calendar} object of the appropriate type
	 */
	public static Calendar convertDateToCalendar(Date date){ 
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}
	
	public static ScheduleEvent convertTimeAvailabilityToScheduleEvent(TimeAvailabilities timeAvailability) {
		if(timeAvailability != null) {
			DefaultScheduleEvent scheduleEvent = new DefaultScheduleEvent();
			scheduleEvent.setStartDate(timeAvailability.getStartTime().getTime());
			scheduleEvent.setEndDate(timeAvailability.getEndTime().getTime());
			scheduleEvent.setTitle(" ");
			return scheduleEvent;
		}
		return new DefaultScheduleEvent();
		
	}
	
	/**
	 * Grabs the currently logged in ApplicationUser and returns
	 * the appropriate entity. Unfortunately, it makes a DB call. 
	 * TODO: figure out how to do this without a DB call
	 * @return Currently logged in {@link ApplicationUser} object
	 */
	public static ApplicationUser getLoggedInApplicationUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth != null) {
			Object principal = auth.getPrincipal();
			UserDetails user = (UserDetails) principal;
			String username = user.getUsername();
			
			return ApplicationUser.findApplicationUserByEmailAddress(username);
		}
		
		return null;
	}
	
	/**
	 * Sets historical information (created by, created date, 
	 * modified by, modified date) for an entity object via 
	 * reflection. 
	 * @param obj The Entity object to set history info
	 * @return The entity object to return
	 * @author Ankit Tyagi
	 */
	public static Object setAllHistoricalInfo(Object obj) {
		try {
			//if the object is an entity in the entity package, then set it
			if(obj.getClass().isAnnotationPresent(Entity.class) && 
					obj.getClass().getPackage().getName().contains("com.surgeryassist.core.entity")) {
				
				obj.getClass().getDeclaredField("createdBy").setAccessible(true);
				obj.getClass().getDeclaredField("createdBy").set(obj, new Integer(1));
				obj.getClass().getDeclaredField("createdBy").setAccessible(false);

				obj.getClass().getDeclaredField("createdDate").setAccessible(true);
				obj.getClass().getDeclaredField("createdDate").set(obj, Calendar.getInstance());
				obj.getClass().getDeclaredField("createdDate").setAccessible(false);
				
				obj.getClass().getDeclaredField("modifiedBy").setAccessible(true);
				obj.getClass().getDeclaredField("modifiedBy").set(obj, new Integer(1));
				obj.getClass().getDeclaredField("modifiedBy").setAccessible(false);
				
				obj.getClass().getDeclaredField("modifiedDate").setAccessible(true);
				obj.getClass().getDeclaredField("modifiedDate").set(obj, Calendar.getInstance());
				obj.getClass().getDeclaredField("modifiedDate").setAccessible(false);
			}
		} 
		//catch errors
		catch(IllegalAccessException e) {
			//tried to access something illegally
			System.err.println("One of the historical fields in the entity is probably private. Check that first.");
			e.printStackTrace();
		}
		catch(IllegalArgumentException e) {
			e.printStackTrace();
		}
		catch(ExceptionInInitializerError e) {
			e.printStackTrace();
		}
		catch(NullPointerException e) {
			//null pointer. oops
			e.printStackTrace();
		}
		catch(NoSuchFieldException e) {
			//field doesn't exist
			System.err.println("Does the entity have the historical fields?");
			e.printStackTrace();
		}
		catch (SecurityException e) {
			e.printStackTrace();
		}
		//return the object regardless
		return obj;
	}
	
	/**
	 * Sets historical information (modified by, modified date) 
	 * for an entity object via reflection.
	 * @param obj The Entity object to set history info
	 * @return The entity object to return
	 * @author Ankit Tyagi
	 */
	public static Object setLastModifiedInfo(Object obj) {
		try {
			//if the object is an entity in the entity package, then set it
			if(obj.getClass().isAnnotationPresent(Entity.class) && 
					obj.getClass().getPackage().getName().contains("com.surgeryassist.core.entity")) {

				obj.getClass().getDeclaredField("modifiedBy").setAccessible(true);
				obj.getClass().getDeclaredField("modifiedBy").set(obj, new Integer(1));
				obj.getClass().getDeclaredField("modifiedBy").setAccessible(false);
				
				obj.getClass().getDeclaredField("modifiedDate").setAccessible(true);
				obj.getClass().getDeclaredField("modifiedDate").set(obj, Calendar.getInstance());
				obj.getClass().getDeclaredField("modifiedDate").setAccessible(false);
			}
		} 
		//catch errors
		catch(IllegalAccessException e) {
			//tried to access something illegally
			System.err.println("One of the historical fields in the entity is probably private. Check that first.");
			e.printStackTrace();
		}
		catch(IllegalArgumentException e) {
			e.printStackTrace();
		}
		catch(ExceptionInInitializerError e) {
			e.printStackTrace();
		}
		catch(NullPointerException e) {
			//null pointer. oops
			e.printStackTrace();
		}
		catch(NoSuchFieldException e) {
			//field doesn't exist
			System.err.println("Does the entity have the historical fields?");
			e.printStackTrace();
		}
		catch (SecurityException e) {
			e.printStackTrace();
		}
		//return the object regardless
		return obj;
	}

}
