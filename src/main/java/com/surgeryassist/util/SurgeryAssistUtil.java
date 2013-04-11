package com.surgeryassist.util;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;

import org.primefaces.model.ScheduleEvent;
import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.surgeryassist.core.entity.ApplicationUser;
import com.surgeryassist.core.entity.TimeAvailabilities;
import com.surgeryassist.util.faces.NewAvailabilityScheduleEvent;

/**
 * A utility class for the SurgeryAssist application.
 * Contains many different static methods to be used 
 * for typical application tasks, such as object type 
 * conversions and setting audit info for {@link Entity}
 * type objects.
 *  
 * @author Ankit Tyagi
 */
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
	
	/**
	 * Converts a {@link TimeAvailabilities} object to a {@link ScheduleEvent}
	 * @param timeAvailability The {@link TimeAvailabilities} to convert
	 * @return The converted {@link ScheduleEvent} object
	 */
	public static ScheduleEvent convertTimeAvailabilityToScheduleEvent(TimeAvailabilities timeAvailability) {
		if(timeAvailability != null) {
			NewAvailabilityScheduleEvent scheduleEvent = new NewAvailabilityScheduleEvent();
			scheduleEvent.setStartDate(timeAvailability.getStartTime().getTime());
			scheduleEvent.setEndDate(timeAvailability.getEndTime().getTime());
			scheduleEvent.setTitle("Existing Availability - Cannot Edit");
			scheduleEvent.setEditable(false);
			scheduleEvent.setRoomNumber(timeAvailability.getRoomNumber());
			return scheduleEvent;
		}
		return new NewAvailabilityScheduleEvent();
	}
	
	/**
	 * Returns a {@link TimeAvailabilities} based on the provided
	 * {@link ScheduleEvent}
	 * @param scheduleEvent The {@link ScheduleEvent} to convert into a {@link TimeAvailabilities}
	 * @return The {@link TimeAvailabilities} object
	 */
	public static TimeAvailabilities convertScheduleEventToTimeAvailability(ScheduleEvent scheduleEvent) {
		if(scheduleEvent != null) {
			TimeAvailabilities timeAvailabilities = new TimeAvailabilities();
			
			timeAvailabilities.setStartTime(
					SurgeryAssistUtil.convertDateToCalendar(
							scheduleEvent.getStartDate()));
			timeAvailabilities.setEndTime(
					SurgeryAssistUtil.convertDateToCalendar(
							scheduleEvent.getEndDate()));
			
		}
		return new TimeAvailabilities();
	}
	
	/**
	 * Grabs the currently logged in ApplicationUser and returns
	 * the appropriate entity. Unfortunately, it makes a DB call. 
	 * @todo Figure out how to do this without a DB call
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

	/**
	 * Returns the difference between
	 * two {@link Calendar} objects as a 
	 * primative int 
	 * @param startTime The start time 
	 * @param endTime The end time
	 */
	public static int getTimeDifferenceInHours(Calendar startTime, Calendar endTime) {
		DateTime jodaStartTime = new DateTime(startTime);
		DateTime jodaEndTime = new DateTime(endTime);
		
		Integer difference = Hours.hoursBetween(jodaStartTime, jodaEndTime).getHours();
		
		return difference;
	}
}
