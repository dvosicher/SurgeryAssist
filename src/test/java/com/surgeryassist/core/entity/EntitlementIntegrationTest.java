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

import com.surgeryassist.core.entity.Entitlement;

@Configurable
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/config/applicationContext*.xml")
public class EntitlementIntegrationTest {

	@Autowired
	EntitlementDataOnDemand dod;

	@Test
	public void testCountEntitlements() {
		Assert.assertNotNull("Data on demand for 'Entitlement' failed to initialize correctly", dod.getRandomEntitlement());
		long count = Entitlement.countEntitlements();
		Assert.assertTrue("Counter for 'Entitlement' incorrectly reported there were no entries", count > 0);
	}

	@Test
	public void testFindEntitlement() {
		Entitlement obj = dod.getRandomEntitlement();
		Assert.assertNotNull("Data on demand for 'Entitlement' failed to initialize correctly", obj);
		Integer id = obj.getEntitlementId();
		Assert.assertNotNull("Data on demand for 'Entitlement' failed to provide an identifier", id);
		obj = Entitlement.findEntitlement(id);
		Assert.assertNotNull("Find method for 'Entitlement' illegally returned null for id '" + id + "'", obj);
		Assert.assertEquals("Find method for 'Entitlement' returned the incorrect identifier", id, obj.getEntitlementId());
	}

	@Test
	public void testFindAllEntitlements() {
		Assert.assertNotNull("Data on demand for 'Entitlement' failed to initialize correctly", dod.getRandomEntitlement());
		long count = Entitlement.countEntitlements();
		Assert.assertTrue("Too expensive to perform a find all test for 'Entitlement', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
		List<Entitlement> result = Entitlement.findAllEntitlements();
		Assert.assertNotNull("Find all method for 'Entitlement' illegally returned null", result);
		Assert.assertTrue("Find all method for 'Entitlement' failed to return any data", result.size() > 0);
	}

	@Test
	public void testFindEntitlementEntries() {
		Assert.assertNotNull("Data on demand for 'Entitlement' failed to initialize correctly", dod.getRandomEntitlement());
		long count = Entitlement.countEntitlements();
		if (count > 20) count = 20;
		int firstResult = 0;
		int maxResults = (int) count;
		List<Entitlement> result = Entitlement.findEntitlementEntries(firstResult, maxResults);
		Assert.assertNotNull("Find entries method for 'Entitlement' illegally returned null", result);
		Assert.assertEquals("Find entries method for 'Entitlement' returned an incorrect number of entries", count, result.size());
	}

	@Test
	public void testFlush() {
		Entitlement obj = dod.getRandomEntitlement();
		Assert.assertNotNull("Data on demand for 'Entitlement' failed to initialize correctly", obj);
		Integer id = obj.getEntitlementId();
		Assert.assertNotNull("Data on demand for 'Entitlement' failed to provide an identifier", id);
		obj = Entitlement.findEntitlement(id);
		Assert.assertNotNull("Find method for 'Entitlement' illegally returned null for id '" + id + "'", obj);
		boolean modified =  dod.modifyEntitlement(obj);
		Integer currentVersion = obj.getVersion();
		obj.flush();
		Assert.assertTrue("Version for 'Entitlement' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
	}

	@Test
	public void testMergeUpdate() {
		Entitlement obj = dod.getRandomEntitlement();
		Assert.assertNotNull("Data on demand for 'Entitlement' failed to initialize correctly", obj);
		Integer id = obj.getEntitlementId();
		Assert.assertNotNull("Data on demand for 'Entitlement' failed to provide an identifier", id);
		obj = Entitlement.findEntitlement(id);
		boolean modified =  dod.modifyEntitlement(obj);
		Integer currentVersion = obj.getVersion();
		Entitlement merged = obj.merge();
		obj.flush();
		Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getEntitlementId(), id);
		Assert.assertTrue("Version for 'Entitlement' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
	}

	@Test
	public void testPersist() {
		Assert.assertNotNull("Data on demand for 'Entitlement' failed to initialize correctly", dod.getRandomEntitlement());
		Entitlement obj = dod.getNewTransientEntitlement(Integer.MAX_VALUE);
		Assert.assertNotNull("Data on demand for 'Entitlement' failed to provide a new transient entity", obj);
		Assert.assertNull("Expected 'Entitlement' identifier to be null", obj.getEntitlementId());
		obj.persist();
		obj.flush();
		Assert.assertNotNull("Expected 'Entitlement' identifier to no longer be null", obj.getEntitlementId());
	}

	@Test
	public void testRemove() {
		Entitlement obj = dod.getRandomEntitlement();
		Assert.assertNotNull("Data on demand for 'Entitlement' failed to initialize correctly", obj);
		Integer id = obj.getEntitlementId();
		Assert.assertNotNull("Data on demand for 'Entitlement' failed to provide an identifier", id);
		obj = Entitlement.findEntitlement(id);
		obj.remove();
		obj.flush();
		Assert.assertNull("Failed to remove 'Entitlement' with identifier '" + id + "'", Entitlement.findEntitlement(id));
	}

}
