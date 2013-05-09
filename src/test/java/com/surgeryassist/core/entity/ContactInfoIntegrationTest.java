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

import com.surgeryassist.core.entity.ContactInfo;

@Configurable
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/config/applicationContext*.xml")
public class ContactInfoIntegrationTest {

	@Autowired
	ContactInfoDataOnDemand dod;

	@Test
	public void testCountContactInfoes() {
		Assert.assertNotNull("Data on demand for 'ContactInfo' failed to initialize correctly", dod.getRandomContactInfo());
		long count = ContactInfo.countContactInfoes();
		Assert.assertTrue("Counter for 'ContactInfo' incorrectly reported there were no entries", count > 0);
	}

	@Test
	public void testFindContactInfo() {
		ContactInfo obj = dod.getRandomContactInfo();
		Assert.assertNotNull("Data on demand for 'ContactInfo' failed to initialize correctly", obj);
		Integer id = obj.getContactInfoID();
		Assert.assertNotNull("Data on demand for 'ContactInfo' failed to provide an identifier", id);
		obj = ContactInfo.findContactInfo(id);
		Assert.assertNotNull("Find method for 'ContactInfo' illegally returned null for id '" + id + "'", obj);
		Assert.assertEquals("Find method for 'ContactInfo' returned the incorrect identifier", id, obj.getContactInfoID());
	}

	@Test
	public void testFindAllContactInfoes() {
		Assert.assertNotNull("Data on demand for 'ContactInfo' failed to initialize correctly", dod.getRandomContactInfo());
		long count = ContactInfo.countContactInfoes();
		Assert.assertTrue("Too expensive to perform a find all test for 'ContactInfo', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
		List<ContactInfo> result = ContactInfo.findAllContactInfoes();
		Assert.assertNotNull("Find all method for 'ContactInfo' illegally returned null", result);
		Assert.assertTrue("Find all method for 'ContactInfo' failed to return any data", result.size() > 0);
	}

	@Test
	public void testFindContactInfoEntries() {
		Assert.assertNotNull("Data on demand for 'ContactInfo' failed to initialize correctly", dod.getRandomContactInfo());
		long count = ContactInfo.countContactInfoes();
		if (count > 20) count = 20;
		int firstResult = 0;
		int maxResults = (int) count;
		List<ContactInfo> result = ContactInfo.findContactInfoEntries(firstResult, maxResults);
		Assert.assertNotNull("Find entries method for 'ContactInfo' illegally returned null", result);
		Assert.assertEquals("Find entries method for 'ContactInfo' returned an incorrect number of entries", count, result.size());
	}

	@Test
	public void testFlush() {
		ContactInfo obj = dod.getRandomContactInfo();
		Assert.assertNotNull("Data on demand for 'ContactInfo' failed to initialize correctly", obj);
		Integer id = obj.getContactInfoID();
		Assert.assertNotNull("Data on demand for 'ContactInfo' failed to provide an identifier", id);
		obj = ContactInfo.findContactInfo(id);
		Assert.assertNotNull("Find method for 'ContactInfo' illegally returned null for id '" + id + "'", obj);
		boolean modified =  dod.modifyContactInfo(obj);
		Integer currentVersion = obj.getVersion();
		obj.flush();
		Assert.assertTrue("Version for 'ContactInfo' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
	}

	@Test
	public void testMergeUpdate() {
		ContactInfo obj = dod.getRandomContactInfo();
		Assert.assertNotNull("Data on demand for 'ContactInfo' failed to initialize correctly", obj);
		Integer id = obj.getContactInfoID();
		Assert.assertNotNull("Data on demand for 'ContactInfo' failed to provide an identifier", id);
		obj = ContactInfo.findContactInfo(id);
		boolean modified =  dod.modifyContactInfo(obj);
		Integer currentVersion = obj.getVersion();
		ContactInfo merged = obj.merge();
		obj.flush();
		Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getContactInfoID(), id);
		Assert.assertTrue("Version for 'ContactInfo' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
	}

	@Test
	public void testPersist() {
		Assert.assertNotNull("Data on demand for 'ContactInfo' failed to initialize correctly", dod.getRandomContactInfo());
		ContactInfo obj = dod.getNewTransientContactInfo(Integer.MAX_VALUE);
		Assert.assertNotNull("Data on demand for 'ContactInfo' failed to provide a new transient entity", obj);
		Assert.assertNull("Expected 'ContactInfo' identifier to be null", obj.getContactInfoID());
		obj.persist();
		obj.flush();
		Assert.assertNotNull("Expected 'ContactInfo' identifier to no longer be null", obj.getContactInfoID());
	}

}
