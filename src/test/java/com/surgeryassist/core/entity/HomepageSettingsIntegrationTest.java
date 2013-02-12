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

import com.surgeryassist.core.entity.HomepageSettings;

@Configurable
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/config/applicationContext*.xml")
public class HomepageSettingsIntegrationTest {
	
	@Autowired
	HomepageSettingsDataOnDemand dod;

	@Test
	public void testCountHomepageSettingses() {
		Assert.assertNotNull("Data on demand for 'HomepageSettings' failed to initialize correctly", dod.getRandomHomepageSettings());
		long count = HomepageSettings.countHomepageSettingses();
		Assert.assertTrue("Counter for 'HomepageSettings' incorrectly reported there were no entries", count > 0);
	}

	@Test
	public void testFindHomepageSettings() {
		HomepageSettings obj = dod.getRandomHomepageSettings();
		Assert.assertNotNull("Data on demand for 'HomepageSettings' failed to initialize correctly", obj);
		Integer id = obj.getHomepageSettingsId();
		Assert.assertNotNull("Data on demand for 'HomepageSettings' failed to provide an identifier", id);
		obj = HomepageSettings.findHomepageSettings(id);
		Assert.assertNotNull("Find method for 'HomepageSettings' illegally returned null for id '" + id + "'", obj);
		Assert.assertEquals("Find method for 'HomepageSettings' returned the incorrect identifier", id, obj.getHomepageSettingsId());
	}

	@Test
	public void testFindAllHomepageSettingses() {
		Assert.assertNotNull("Data on demand for 'HomepageSettings' failed to initialize correctly", dod.getRandomHomepageSettings());
		long count = HomepageSettings.countHomepageSettingses();
		Assert.assertTrue("Too expensive to perform a find all test for 'HomepageSettings', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
		List<HomepageSettings> result = HomepageSettings.findAllHomepageSettingses();
		Assert.assertNotNull("Find all method for 'HomepageSettings' illegally returned null", result);
		Assert.assertTrue("Find all method for 'HomepageSettings' failed to return any data", result.size() > 0);
	}

	@Test
	public void testFindHomepageSettingsEntries() {
		Assert.assertNotNull("Data on demand for 'HomepageSettings' failed to initialize correctly", dod.getRandomHomepageSettings());
		long count = HomepageSettings.countHomepageSettingses();
		if (count > 20) count = 20;
		int firstResult = 0;
		int maxResults = (int) count;
		List<HomepageSettings> result = HomepageSettings.findHomepageSettingsEntries(firstResult, maxResults);
		Assert.assertNotNull("Find entries method for 'HomepageSettings' illegally returned null", result);
		Assert.assertEquals("Find entries method for 'HomepageSettings' returned an incorrect number of entries", count, result.size());
	}
	
	@Test
	public void testFindHomepageSettingsByHomepageWidget() {
		HomepageSettings setting = dod.getRandomHomepageSettings();
		HomepageWidget widget = setting.getWidgetId();
		Assert.assertNotNull("Data on demand for 'HomepageSettings' failed", setting);
		List<HomepageSettings> obj = HomepageSettings.findHomepageSettingsByHomepageWidget(widget);
    	Assert.assertNotNull("No settings available for the provided day availability", obj);
    	Assert.assertFalse("The size of the returned list is 0", obj.isEmpty());
    	Assert.assertTrue("The returned list has the provided setting", obj.contains(setting));
    	int index = obj.indexOf(setting);
    	Assert.assertEquals("The returned object is not equal to the expected", setting, obj.get(index));
	}

	@Test
	public void testFlush() {
		HomepageSettings obj = dod.getRandomHomepageSettings();
		Assert.assertNotNull("Data on demand for 'HomepageSettings' failed to initialize correctly", obj);
		Integer id = obj.getHomepageSettingsId();
		Assert.assertNotNull("Data on demand for 'HomepageSettings' failed to provide an identifier", id);
		obj = HomepageSettings.findHomepageSettings(id);
		Assert.assertNotNull("Find method for 'HomepageSettings' illegally returned null for id '" + id + "'", obj);
		boolean modified =  dod.modifyHomepageSettings(obj);
		Integer currentVersion = obj.getVersion();
		obj.flush();
		Assert.assertTrue("Version for 'HomepageSettings' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
	}

	@Test
	public void testMergeUpdate() {
		HomepageSettings obj = dod.getRandomHomepageSettings();
		Assert.assertNotNull("Data on demand for 'HomepageSettings' failed to initialize correctly", obj);
		Integer id = obj.getHomepageSettingsId();
		Assert.assertNotNull("Data on demand for 'HomepageSettings' failed to provide an identifier", id);
		obj = HomepageSettings.findHomepageSettings(id);
		boolean modified =  dod.modifyHomepageSettings(obj);
		Integer currentVersion = obj.getVersion();
		HomepageSettings merged = obj.merge();
		obj.flush();
		Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getHomepageSettingsId(), id);
		Assert.assertTrue("Version for 'HomepageSettings' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
	}

	@Test
	public void testPersist() {
		Assert.assertNotNull("Data on demand for 'HomepageSettings' failed to initialize correctly", dod.getRandomHomepageSettings());
		HomepageSettings obj = dod.getNewTransientHomepageSettings(Integer.MAX_VALUE);
		Assert.assertNotNull("Data on demand for 'HomepageSettings' failed to provide a new transient entity", obj);
		Assert.assertNull("Expected 'HomepageSettings' identifier to be null", obj.getHomepageSettingsId());
		obj.persist();
		obj.flush();
		Assert.assertNotNull("Expected 'HomepageSettings' identifier to no longer be null", obj.getHomepageSettingsId());
	}

	@Test
	public void testRemove() {
		HomepageSettings obj = dod.getRandomHomepageSettings();
		Assert.assertNotNull("Data on demand for 'HomepageSettings' failed to initialize correctly", obj);
		Integer id = obj.getHomepageSettingsId();
		Assert.assertNotNull("Data on demand for 'HomepageSettings' failed to provide an identifier", id);
		obj = HomepageSettings.findHomepageSettings(id);
		obj.remove();
		obj.flush();
		Assert.assertNull("Failed to remove 'HomepageSettings' with identifier '" + id + "'", HomepageSettings.findHomepageSettings(id));
	}

}
