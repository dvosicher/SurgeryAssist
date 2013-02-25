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

import com.surgeryassist.core.entity.HomepageWidget;

@Configurable
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/config/applicationContext*.xml")
public class HomepageWidgetIntegrationTest {
    
	@Autowired
    HomepageWidgetDataOnDemand dod;
    
    @Test
    public void testCountHomepageWidgets() {
        Assert.assertNotNull("Data on demand for 'HomepageWidget' failed to initialize correctly", dod.getRandomHomepageWidget());
        long count = HomepageWidget.countHomepageWidgets();
        Assert.assertTrue("Counter for 'HomepageWidget' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void testFindHomepageWidget() {
        HomepageWidget obj = dod.getRandomHomepageWidget();
        Assert.assertNotNull("Data on demand for 'HomepageWidget' failed to initialize correctly", obj);
        Integer id = obj.getWidgetId();
        Assert.assertNotNull("Data on demand for 'HomepageWidget' failed to provide an identifier", id);
        obj = HomepageWidget.findHomepageWidget(id);
        Assert.assertNotNull("Find method for 'HomepageWidget' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'HomepageWidget' returned the incorrect identifier", id, obj.getWidgetId());
    }
    
    @Test
    public void testFindAllHomepageWidgets() {
        Assert.assertNotNull("Data on demand for 'HomepageWidget' failed to initialize correctly", dod.getRandomHomepageWidget());
        long count = HomepageWidget.countHomepageWidgets();
        Assert.assertTrue("Too expensive to perform a find all test for 'HomepageWidget', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<HomepageWidget> result = HomepageWidget.findAllHomepageWidgets();
        Assert.assertNotNull("Find all method for 'HomepageWidget' illegally returned null", result);
        Assert.assertTrue("Find all method for 'HomepageWidget' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void testFindHomepageWidgetEntries() {
        Assert.assertNotNull("Data on demand for 'HomepageWidget' failed to initialize correctly", dod.getRandomHomepageWidget());
        long count = HomepageWidget.countHomepageWidgets();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<HomepageWidget> result = HomepageWidget.findHomepageWidgetEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'HomepageWidget' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'HomepageWidget' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testFlush() {
        HomepageWidget obj = dod.getRandomHomepageWidget();
        Assert.assertNotNull("Data on demand for 'HomepageWidget' failed to initialize correctly", obj);
        Integer id = obj.getWidgetId();
        Assert.assertNotNull("Data on demand for 'HomepageWidget' failed to provide an identifier", id);
        obj = HomepageWidget.findHomepageWidget(id);
        Assert.assertNotNull("Find method for 'HomepageWidget' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyHomepageWidget(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'HomepageWidget' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testMergeUpdate() {
        HomepageWidget obj = dod.getRandomHomepageWidget();
        Assert.assertNotNull("Data on demand for 'HomepageWidget' failed to initialize correctly", obj);
        Integer id = obj.getWidgetId();
        Assert.assertNotNull("Data on demand for 'HomepageWidget' failed to provide an identifier", id);
        obj = HomepageWidget.findHomepageWidget(id);
        boolean modified =  dod.modifyHomepageWidget(obj);
        Integer currentVersion = obj.getVersion();
        HomepageWidget merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getWidgetId(), id);
        Assert.assertTrue("Version for 'HomepageWidget' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'HomepageWidget' failed to initialize correctly", dod.getRandomHomepageWidget());
        HomepageWidget obj = dod.getNewTransientHomepageWidget(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'HomepageWidget' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'HomepageWidget' identifier to be null", obj.getWidgetId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'HomepageWidget' identifier to no longer be null", obj.getWidgetId());
    }
    
    @Test
    public void testRemove() {
        HomepageWidget obj = dod.getRandomHomepageWidget();
        Assert.assertNotNull("Data on demand for 'HomepageWidget' failed to initialize correctly", obj);
        Integer id = obj.getWidgetId();
        Assert.assertNotNull("Data on demand for 'HomepageWidget' failed to provide an identifier", id);
        List<HomepageSettings> settings = HomepageSettings.findHomepageSettingsByHomepageWidget(obj);
        Assert.assertNotNull("HomepageSettings query failed", settings);
        for(HomepageSettings setting : settings) {
        	setting.remove();
        	setting.flush();
        }
        obj = HomepageWidget.findHomepageWidget(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'HomepageWidget' with identifier '" + id + "'", HomepageWidget.findHomepageWidget(id));
    }

}
