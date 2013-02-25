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


@Configurable
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/config/applicationContext*.xml")
public class AcceptedInsuranceTypesIntegrationTest {

	@Autowired
	AcceptedInsuranceTypesDataOnDemand dod;

	@Test
	public void testMarkerMethod() {
	}

	@Test
	public void testCountAcceptedInsuranceTypeses() {
		Assert.assertNotNull("Data on demand for 'AcceptedInsuranceTypes' failed to initialize correctly", dod.getRandomAcceptedInsuranceTypes());
		long count = AcceptedInsuranceTypes.countAcceptedInsuranceTypeses();
		Assert.assertTrue("Counter for 'AcceptedInsuranceTypes' incorrectly reported there were no entries", count > 0);
	}

	@Test
	public void testFindAcceptedInsuranceTypes() {
		AcceptedInsuranceTypes obj = dod.getRandomAcceptedInsuranceTypes();
		Assert.assertNotNull("Data on demand for 'AcceptedInsuranceTypes' failed to initialize correctly", obj);
		Integer id = obj.getAcceptedInsuranceID();
		Assert.assertNotNull("Data on demand for 'AcceptedInsuranceTypes' failed to provide an identifier", id);
		obj = AcceptedInsuranceTypes.findAcceptedInsuranceTypes(id);
		Assert.assertNotNull("Find method for 'AcceptedInsuranceTypes' illegally returned null for id '" + id + "'", obj);
		Assert.assertEquals("Find method for 'AcceptedInsuranceTypes' returned the incorrect identifier", id, obj.getAcceptedInsuranceID());
	}

	@Test
	public void testFindAllAcceptedInsuranceTypeses() {
		Assert.assertNotNull("Data on demand for 'AcceptedInsuranceTypes' failed to initialize correctly", dod.getRandomAcceptedInsuranceTypes());
		long count = AcceptedInsuranceTypes.countAcceptedInsuranceTypeses();
		Assert.assertTrue("Too expensive to perform a find all test for 'AcceptedInsuranceTypes', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
		List<AcceptedInsuranceTypes> result = AcceptedInsuranceTypes.findAllAcceptedInsuranceTypeses();
		Assert.assertNotNull("Find all method for 'AcceptedInsuranceTypes' illegally returned null", result);
		Assert.assertTrue("Find all method for 'AcceptedInsuranceTypes' failed to return any data", result.size() > 0);
	}

	@Test
	public void testFindAcceptedInsuranceTypesEntries() {
		Assert.assertNotNull("Data on demand for 'AcceptedInsuranceTypes' failed to initialize correctly", dod.getRandomAcceptedInsuranceTypes());
		long count = AcceptedInsuranceTypes.countAcceptedInsuranceTypeses();
		if (count > 20) count = 20;
		int firstResult = 0;
		int maxResults = (int) count;
		List<AcceptedInsuranceTypes> result = AcceptedInsuranceTypes.findAcceptedInsuranceTypesEntries(firstResult, maxResults);
		Assert.assertNotNull("Find entries method for 'AcceptedInsuranceTypes' illegally returned null", result);
		Assert.assertEquals("Find entries method for 'AcceptedInsuranceTypes' returned an incorrect number of entries", count, result.size());
	}

	@Test
	public void testFlush() {
		AcceptedInsuranceTypes obj = dod.getRandomAcceptedInsuranceTypes();
		Assert.assertNotNull("Data on demand for 'AcceptedInsuranceTypes' failed to initialize correctly", obj);
		Integer id = obj.getAcceptedInsuranceID();
		Assert.assertNotNull("Data on demand for 'AcceptedInsuranceTypes' failed to provide an identifier", id);
		obj = AcceptedInsuranceTypes.findAcceptedInsuranceTypes(id);
		Assert.assertNotNull("Find method for 'AcceptedInsuranceTypes' illegally returned null for id '" + id + "'", obj);
		boolean modified =  dod.modifyAcceptedInsuranceTypes(obj);
		Integer currentVersion = obj.getVersion();
		obj.flush();
		Assert.assertTrue("Version for 'AcceptedInsuranceTypes' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
	}

	@Test
	public void testMergeUpdate() {
		AcceptedInsuranceTypes obj = dod.getRandomAcceptedInsuranceTypes();
		Assert.assertNotNull("Data on demand for 'AcceptedInsuranceTypes' failed to initialize correctly", obj);
		Integer id = obj.getAcceptedInsuranceID();
		Assert.assertNotNull("Data on demand for 'AcceptedInsuranceTypes' failed to provide an identifier", id);
		obj = AcceptedInsuranceTypes.findAcceptedInsuranceTypes(id);
		boolean modified =  dod.modifyAcceptedInsuranceTypes(obj);
		Integer currentVersion = obj.getVersion();
		AcceptedInsuranceTypes merged = obj.merge();
		obj.flush();
		Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getAcceptedInsuranceID(), id);
		Assert.assertTrue("Version for 'AcceptedInsuranceTypes' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
	}

	@Test
	public void testPersist() {
		Assert.assertNotNull("Data on demand for 'AcceptedInsuranceTypes' failed to initialize correctly", dod.getRandomAcceptedInsuranceTypes());
		AcceptedInsuranceTypes obj = dod.getNewTransientAcceptedInsuranceTypes(Integer.MAX_VALUE);
		Assert.assertNotNull("Data on demand for 'AcceptedInsuranceTypes' failed to provide a new transient entity", obj);
		Assert.assertNull("Expected 'AcceptedInsuranceTypes' identifier to be null", obj.getAcceptedInsuranceID());
		obj.persist();
		obj.flush();
		Assert.assertNotNull("Expected 'AcceptedInsuranceTypes' identifier to no longer be null", obj.getAcceptedInsuranceID());
	}

	@Test
	public void testRemove() {
		AcceptedInsuranceTypes obj = dod.getRandomAcceptedInsuranceTypes();
		Assert.assertNotNull("Data on demand for 'AcceptedInsuranceTypes' failed to initialize correctly", obj);
		Integer id = obj.getAcceptedInsuranceID();
		Assert.assertNotNull("Data on demand for 'AcceptedInsuranceTypes' failed to provide an identifier", id);
		obj = AcceptedInsuranceTypes.findAcceptedInsuranceTypes(id);
		obj.remove();
		obj.flush();
		Assert.assertNull("Failed to remove 'AcceptedInsuranceTypes' with identifier '" + id + "'", AcceptedInsuranceTypes.findAcceptedInsuranceTypes(id));
	}
}
