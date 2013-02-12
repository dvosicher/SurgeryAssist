package com.surgeryassist.core.entity;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.surgeryassist.core.entity.Location;

@Configurable
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/config/applicationContext*.xml")
public class LocationIntegrationTest {

	@Autowired
	LocationDataOnDemand dod;

	@Test
	public void testCountLocations() {
		Assert.assertNotNull("Data on demand for 'Location' failed to initialize correctly", dod.getRandomLocation());
		long count = Location.countLocations();
		Assert.assertTrue("Counter for 'Location' incorrectly reported there were no entries", count > 0);
	}

	@Test
	public void testFindLocation() {
		Location obj = dod.getRandomLocation();
		Assert.assertNotNull("Data on demand for 'Location' failed to initialize correctly", obj);
		Integer id = obj.getLocationID();
		Assert.assertNotNull("Data on demand for 'Location' failed to provide an identifier", id);
		obj = Location.findLocation(id);
		Assert.assertNotNull("Find method for 'Location' illegally returned null for id '" + id + "'", obj);
		Assert.assertEquals("Find method for 'Location' returned the incorrect identifier", id, obj.getLocationID());
	}

	@Test
	public void testFindAllLocations() {
		Assert.assertNotNull("Data on demand for 'Location' failed to initialize correctly", dod.getRandomLocation());
		long count = Location.countLocations();
		Assert.assertTrue("Too expensive to perform a find all test for 'Location', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
		List<Location> result = Location.findAllLocations();
		Assert.assertNotNull("Find all method for 'Location' illegally returned null", result);
		Assert.assertTrue("Find all method for 'Location' failed to return any data", result.size() > 0);
	}

	@Test
	public void testFindLocationEntries() {
		Assert.assertNotNull("Data on demand for 'Location' failed to initialize correctly", dod.getRandomLocation());
		long count = Location.countLocations();
		if (count > 20) count = 20;
		int firstResult = 0;
		int maxResults = (int) count;
		List<Location> result = Location.findLocationEntries(firstResult, maxResults);
		Assert.assertNotNull("Find entries method for 'Location' illegally returned null", result);
		Assert.assertEquals("Find entries method for 'Location' returned an incorrect number of entries", count, result.size());
	}

	@Test
	public void testFlush() {
		Location obj = dod.getRandomLocation();
		Assert.assertNotNull("Data on demand for 'Location' failed to initialize correctly", obj);
		Integer id = obj.getLocationID();
		Assert.assertNotNull("Data on demand for 'Location' failed to provide an identifier", id);
		obj = Location.findLocation(id);
		Assert.assertNotNull("Find method for 'Location' illegally returned null for id '" + id + "'", obj);
		boolean modified =  dod.modifyLocation(obj);
		Integer currentVersion = obj.getVersion();
		obj.flush();
		Assert.assertTrue("Version for 'Location' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
	}

	@Test
	public void testMergeUpdate() {
		Location obj = dod.getRandomLocation();
		Assert.assertNotNull("Data on demand for 'Location' failed to initialize correctly", obj);
		Integer id = obj.getLocationID();
		Assert.assertNotNull("Data on demand for 'Location' failed to provide an identifier", id);
		obj = Location.findLocation(id);
		boolean modified =  dod.modifyLocation(obj);
		Integer currentVersion = obj.getVersion();
		Location merged = obj.merge();
		obj.flush();
		Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getLocationID(), id);
		Assert.assertTrue("Version for 'Location' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
	}

	@Test
	public void testPersist() {
		Assert.assertNotNull("Data on demand for 'Location' failed to initialize correctly", dod.getRandomLocation());
		Location obj = dod.getNewTransientLocation(Integer.MAX_VALUE);
		Assert.assertNotNull("Data on demand for 'Location' failed to provide a new transient entity", obj);
		Assert.assertNull("Expected 'Location' identifier to be null", obj.getLocationID());
		obj.persist();
		obj.flush();
		Assert.assertNotNull("Expected 'Location' identifier to no longer be null", obj.getLocationID());
	}

}
