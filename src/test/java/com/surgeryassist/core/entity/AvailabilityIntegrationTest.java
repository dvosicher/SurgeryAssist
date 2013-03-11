package com.surgeryassist.core.entity;

import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.surgeryassist.core.entity.DayAvailability;
import com.surgeryassist.core.entity.AvailabilityDataOnDemand;

@Configurable
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/config/applicationContext*.xml")
public class AvailabilityIntegrationTest {
	
	@Autowired
	AvailabilityDataOnDemand dod;

	@Test
	public void testCountAvailabilitys() {
		Assert.assertNotNull("Data on demand for 'Availability' failed to initialize correctly", dod.getRandomAvailability());
		long count = DayAvailability.countDayAvailabilitys();
		Assert.assertTrue("Counter for 'Availability' incorrectly reported there were no entries", count > 0);
	}

	@Test
	public void testFindAvailability() {
		DayAvailability obj = dod.getRandomAvailability();
		Assert.assertNotNull("Data on demand for 'Availability' failed to initialize correctly", obj);
		Integer id = obj.getAvailabilityID();
		Assert.assertNotNull("Data on demand for 'Availability' failed to provide an identifier", id);
		obj = DayAvailability.findDayAvailability(id);
		Assert.assertNotNull("Find method for 'Availability' illegally returned null for id '" + id + "'", obj);
		Assert.assertEquals("Find method for 'Availability' returned the incorrect identifier", id, obj.getAvailabilityID());
	}

	@Test
	public void testFindAllAvailabilitys() {
		Assert.assertNotNull("Data on demand for 'Availability' failed to initialize correctly", dod.getRandomAvailability());
		long count = DayAvailability.countDayAvailabilitys();
		Assert.assertTrue("Too expensive to perform a find all test for 'Availability', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
		List<DayAvailability> result = DayAvailability.findAllDayAvailabilitys();
		Assert.assertNotNull("Find all method for 'Availability' illegally returned null", result);
		Assert.assertTrue("Find all method for 'Availability' failed to return any data", result.size() > 0);
	}

	@Test
	public void testFindAvailabilityEntries() {
		Assert.assertNotNull("Data on demand for 'Availability' failed to initialize correctly", dod.getRandomAvailability());
		long count = DayAvailability.countDayAvailabilitys();
		if (count > 20) count = 20;
		int firstResult = 0;
		int maxResults = (int) count;
		List<DayAvailability> result = DayAvailability.findDayAvailabilityEntries(firstResult, maxResults);
		Assert.assertNotNull("Find entries method for 'Availability' illegally returned null", result);
		Assert.assertEquals("Find entries method for 'Availability' returned an incorrect number of entries", count, result.size());
	}

	@Test
	public void testFlush() {
		DayAvailability obj = dod.getRandomAvailability();
		Assert.assertNotNull("Data on demand for 'Availability' failed to initialize correctly", obj);
		Integer id = obj.getAvailabilityID();
		Assert.assertNotNull("Data on demand for 'Availability' failed to provide an identifier", id);
		obj = DayAvailability.findDayAvailability(id);
		Assert.assertNotNull("Find method for 'Availability' illegally returned null for id '" + id + "'", obj);
		boolean modified =  dod.modifyAvailability(obj);
		Integer currentVersion = obj.getVersion();
		obj.flush();
		Assert.assertTrue("Version for 'Availability' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
	}

	@Test
	public void testMergeUpdate() {
		DayAvailability obj = dod.getRandomAvailability();
		Assert.assertNotNull("Data on demand for 'Availability' failed to initialize correctly", obj);
		Integer id = obj.getAvailabilityID();
		Assert.assertNotNull("Data on demand for 'Availability' failed to provide an identifier", id);
		obj = DayAvailability.findDayAvailability(id);
		boolean modified =  dod.modifyAvailability(obj);
		Integer currentVersion = obj.getVersion();
		DayAvailability merged = obj.merge();
		obj.flush();
		Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getAvailabilityID(), id);
		Assert.assertTrue("Version for 'Availability' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
	}

	@Test
	public void testPersist() {
		Assert.assertNotNull("Data on demand for 'Availability' failed to initialize correctly", dod.getRandomAvailability());
		DayAvailability obj = dod.getNewTransientAvailability(Integer.MAX_VALUE);
		Assert.assertNotNull("Data on demand for 'Availability' failed to provide a new transient entity", obj);
		Assert.assertNull("Expected 'Availability' identifier to be null", obj.getAvailabilityID());
		obj.persist();
		obj.flush();
		Assert.assertNotNull("Expected 'Availability' identifier to no longer be null", obj.getAvailabilityID());
	}

	@Test
	@Ignore //ignoring test because we shouldn't remove availabilities
	public void testRemove() {
		DayAvailability obj = dod.getRandomAvailability();
		Assert.assertNotNull("Data on demand for 'Availability' failed to initialize correctly", obj);
		Integer id = obj.getAvailabilityID();
		Assert.assertNotNull("Data on demand for 'Availability' failed to provide an identifier", id);
		List<TimeAvailabilities> timeAvails = TimeAvailabilities.findTimeAvailabilitiesByDayAvailability(obj);
		Assert.assertNotNull("TimeAvailabilities query failed", timeAvails);
		for(TimeAvailabilities timeAvail : timeAvails) {
			timeAvail.remove();
			timeAvail.flush();
		}
		obj = DayAvailability.findDayAvailability(id);
		obj.remove();
		obj.flush();
		Assert.assertNull("Failed to remove 'Availability' with identifier '" + id + "'", DayAvailability.findDayAvailability(id));
	}

}
