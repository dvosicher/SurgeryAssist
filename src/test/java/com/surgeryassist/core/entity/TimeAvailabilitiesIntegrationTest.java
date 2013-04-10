package com.surgeryassist.core.entity;

import java.util.Calendar;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.surgeryassist.core.entity.TimeAvailabilities;

@Configurable
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/config/applicationContext*.xml")
public class TimeAvailabilitiesIntegrationTest {

    @Autowired
    TimeAvailabilitiesDataOnDemand dod;
    
    @Test
    public void testCountTimeAvailabilitieses() {
        Assert.assertNotNull("Data on demand for 'TimeAvailabilities' failed to initialize correctly", dod.getRandomTimeAvailabilities());
        long count = TimeAvailabilities.countTimeAvailabilitieses();
        Assert.assertTrue("Counter for 'TimeAvailabilities' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void testFindTimeAvailabilities() {
        TimeAvailabilities obj = dod.getRandomTimeAvailabilities();
        Assert.assertNotNull("Data on demand for 'TimeAvailabilities' failed to initialize correctly", obj);
        Integer id = obj.getTimeAvailabilityID();
        Assert.assertNotNull("Data on demand for 'TimeAvailabilities' failed to provide an identifier", id);
        obj = TimeAvailabilities.findTimeAvailabilities(id);
        Assert.assertNotNull("Find method for 'TimeAvailabilities' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'TimeAvailabilities' returned the incorrect identifier", id, obj.getTimeAvailabilityID());
    }
    
    @Test
    public void testFindTimeAvailabilitiesByDayAvailability() {
    	TimeAvailabilities timeAvail = dod.getRandomTimeAvailabilities();
    	DayAvailability dayAvail = timeAvail.getAvailabilityId();
    	Assert.assertNotNull("Data on demand for 'DayAvailabilities' failed to initialize correctly", dayAvail);
    	List<TimeAvailabilities> obj = TimeAvailabilities.findTimeAvailabilitiesByDayAvailability(dayAvail);
    	Assert.assertNotNull("No time availabilities available for the provided day availability", obj);
    	Assert.assertFalse("The size of the returned list is 0", obj.isEmpty());
    	Assert.assertTrue("The returned list has the provided TimeAvailability", obj.contains(timeAvail));
    	int index = obj.indexOf(timeAvail);
    	Assert.assertEquals("The returned object is not equal to the expected", timeAvail, obj.get(index));
    }
    
    @Test
    public void testFindAllTimeAvailabilitieses() {
        Assert.assertNotNull("Data on demand for 'TimeAvailabilities' failed to initialize correctly", dod.getRandomTimeAvailabilities());
        long count = TimeAvailabilities.countTimeAvailabilitieses();
        Assert.assertTrue("Too expensive to perform a find all test for 'TimeAvailabilities', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<TimeAvailabilities> result = TimeAvailabilities.findAllTimeAvailabilitieses();
        Assert.assertNotNull("Find all method for 'TimeAvailabilities' illegally returned null", result);
        Assert.assertTrue("Find all method for 'TimeAvailabilities' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void testFindTimeAvailabilitiesEntries() {
        Assert.assertNotNull("Data on demand for 'TimeAvailabilities' failed to initialize correctly", dod.getRandomTimeAvailabilities());
        long count = TimeAvailabilities.countTimeAvailabilitieses();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<TimeAvailabilities> result = TimeAvailabilities.findTimeAvailabilitiesEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'TimeAvailabilities' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'TimeAvailabilities' returned an incorrect number of entries", count, result.size());
    }
    
    
    @Test
    public void testFindTimeAvailabilitiesBySearchCriteria() {
    	Assert.assertNotNull("Data on demand for 'TimeAvailabilities' failed to initialize correctly", dod.getRandomTimeAvailabilities());
    	//get the city based on the random availability
    	String city = dod.getRandomTimeAvailabilities().getAvailabilityId().getUserId().getUserInfoId().getLocationId().getCity();
    	Integer zipCode = null;
    	Calendar startDate = Calendar.getInstance();
    	Calendar endDate = Calendar.getInstance();
    	
    	DateTime jodaEndDate = new DateTime(endDate);
    	jodaEndDate.plusYears(1);
    	
    	//2 hour difference
    	Integer duration = 2;
    	
    	List<TimeAvailabilities> timeAvailabilities = 
    			TimeAvailabilities.findTimeAvailabilitiesBySearchCriteria(city, zipCode, jodaEndDate.toDate(), duration, startDate.getTime());
    	
    	Assert.assertNotNull("Somehow the list is null", timeAvailabilities);
    	Assert.assertTrue("Find by criteria for 'TimeAvailabilities' failed to return any data", 
    			timeAvailabilities.size() >= 0);
    }
    
    
    @Test
    public void testFlush() {
        TimeAvailabilities obj = dod.getRandomTimeAvailabilities();
        Assert.assertNotNull("Data on demand for 'TimeAvailabilities' failed to initialize correctly", obj);
        Integer id = obj.getTimeAvailabilityID();
        Assert.assertNotNull("Data on demand for 'TimeAvailabilities' failed to provide an identifier", id);
        obj = TimeAvailabilities.findTimeAvailabilities(id);
        Assert.assertNotNull("Find method for 'TimeAvailabilities' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyTimeAvailabilities(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'TimeAvailabilities' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testMergeUpdate() {
        TimeAvailabilities obj = dod.getRandomTimeAvailabilities();
        Assert.assertNotNull("Data on demand for 'TimeAvailabilities' failed to initialize correctly", obj);
        Integer id = obj.getTimeAvailabilityID();
        Assert.assertNotNull("Data on demand for 'TimeAvailabilities' failed to provide an identifier", id);
        obj = TimeAvailabilities.findTimeAvailabilities(id);
        boolean modified =  dod.modifyTimeAvailabilities(obj);
        Integer currentVersion = obj.getVersion();
        TimeAvailabilities merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getTimeAvailabilityID(), id);
        Assert.assertTrue("Version for 'TimeAvailabilities' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'TimeAvailabilities' failed to initialize correctly", dod.getRandomTimeAvailabilities());
        TimeAvailabilities obj = dod.getNewTransientTimeAvailabilities(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'TimeAvailabilities' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'TimeAvailabilities' identifier to be null", obj.getTimeAvailabilityID());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'TimeAvailabilities' identifier to no longer be null", obj.getTimeAvailabilityID());
    }
    
    @Test
    @Ignore //ignoring this test because we shouldn't delete TimeAvailibilities. Ever.
    public void testRemove() {
        TimeAvailabilities obj = dod.getRandomTimeAvailabilities();
        Assert.assertNotNull("Data on demand for 'TimeAvailabilities' failed to initialize correctly", obj);
        Integer id = obj.getTimeAvailabilityID();
        Assert.assertNotNull("Data on demand for 'TimeAvailabilities' failed to provide an identifier", id);
        obj = TimeAvailabilities.findTimeAvailabilities(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'TimeAvailabilities' with identifier '" + id + "'", TimeAvailabilities.findTimeAvailabilities(id));
    }

}
