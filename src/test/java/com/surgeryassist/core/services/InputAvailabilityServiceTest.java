package com.surgeryassist.core.services;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.ScheduleEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.surgeryassist.core.entity.AvailabilityDataOnDemand;
import com.surgeryassist.services.interfaces.InputAvailabilityService;

@Configurable
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/config/applicationContext*.xml")
public class InputAvailabilityServiceTest {
	
	@Autowired
	AvailabilityDataOnDemand availabilityDataOnDemand;
	
	@Autowired
	InputAvailabilityService inputAvailabilityService;
	
	List<ScheduleEvent> scheduleEvents;
	
	@Before
	public void setUp() {
		scheduleEvents = new ArrayList<ScheduleEvent>();
		
		DateTime startTime = new DateTime();
		DateTime endTime = new DateTime();
		
		endTime = endTime.plusHours(1);
		
		ScheduleEvent defaultScheduleEvent = 
				new DefaultScheduleEvent(" ", startTime.toDate(), endTime.toDate());
		
		scheduleEvents.add(defaultScheduleEvent);
	}
	
	@Test
	public void testGetNecessaryDayAvailabilitiesBasedOnScheduleEvents_AddExistingDayAvailability() {
		/*
		 * Test plan: 
		 * 1. Get a list containing the existing DayAvailability as a ScheduleEvent
		 * 2. Create a test user to use that has the day availabilities already there
		 * 3. 
		 */
		
	}
}
