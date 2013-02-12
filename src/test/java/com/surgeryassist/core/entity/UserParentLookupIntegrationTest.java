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

import com.surgeryassist.core.entity.UserParentLookup;

@Configurable
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/config/applicationContext*.xml")
public class UserParentLookupIntegrationTest {

    @Autowired
    UserParentLookupDataOnDemand dod;
    
    @Test
    public void testCountUserParentLookups() {
        Assert.assertNotNull("Data on demand for 'UserParentLookup' failed to initialize correctly", dod.getRandomUserParentLookup());
        long count = UserParentLookup.countUserParentLookups();
        Assert.assertTrue("Counter for 'UserParentLookup' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void testFindUserParentLookup() {
        UserParentLookup obj = dod.getRandomUserParentLookup();
        Assert.assertNotNull("Data on demand for 'UserParentLookup' failed to initialize correctly", obj);
        Integer id = obj.getUserParentLookupID();
        Assert.assertNotNull("Data on demand for 'UserParentLookup' failed to provide an identifier", id);
        obj = UserParentLookup.findUserParentLookup(id);
        Assert.assertNotNull("Find method for 'UserParentLookup' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'UserParentLookup' returned the incorrect identifier", id, obj.getUserParentLookupID());
    }
    
    @Test
    public void testFindAllUserParentLookups() {
        Assert.assertNotNull("Data on demand for 'UserParentLookup' failed to initialize correctly", dod.getRandomUserParentLookup());
        long count = UserParentLookup.countUserParentLookups();
        Assert.assertTrue("Too expensive to perform a find all test for 'UserParentLookup', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<UserParentLookup> result = UserParentLookup.findAllUserParentLookups();
        Assert.assertNotNull("Find all method for 'UserParentLookup' illegally returned null", result);
        Assert.assertTrue("Find all method for 'UserParentLookup' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void testFindUserParentLookupEntries() {
        Assert.assertNotNull("Data on demand for 'UserParentLookup' failed to initialize correctly", dod.getRandomUserParentLookup());
        long count = UserParentLookup.countUserParentLookups();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<UserParentLookup> result = UserParentLookup.findUserParentLookupEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'UserParentLookup' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'UserParentLookup' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testFlush() {
        UserParentLookup obj = dod.getRandomUserParentLookup();
        Assert.assertNotNull("Data on demand for 'UserParentLookup' failed to initialize correctly", obj);
        Integer id = obj.getUserParentLookupID();
        Assert.assertNotNull("Data on demand for 'UserParentLookup' failed to provide an identifier", id);
        obj = UserParentLookup.findUserParentLookup(id);
        Assert.assertNotNull("Find method for 'UserParentLookup' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyUserParentLookup(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'UserParentLookup' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testMergeUpdate() {
        UserParentLookup obj = dod.getRandomUserParentLookup();
        Assert.assertNotNull("Data on demand for 'UserParentLookup' failed to initialize correctly", obj);
        Integer id = obj.getUserParentLookupID();
        Assert.assertNotNull("Data on demand for 'UserParentLookup' failed to provide an identifier", id);
        obj = UserParentLookup.findUserParentLookup(id);
        boolean modified =  dod.modifyUserParentLookup(obj);
        Integer currentVersion = obj.getVersion();
        UserParentLookup merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getUserParentLookupID(), id);
        Assert.assertTrue("Version for 'UserParentLookup' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'UserParentLookup' failed to initialize correctly", dod.getRandomUserParentLookup());
        UserParentLookup obj = dod.getNewTransientUserParentLookup(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'UserParentLookup' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'UserParentLookup' identifier to be null", obj.getUserParentLookupID());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'UserParentLookup' identifier to no longer be null", obj.getUserParentLookupID());
    }
    
    @Test
    public void testRemove() {
        UserParentLookup obj = dod.getRandomUserParentLookup();
        Assert.assertNotNull("Data on demand for 'UserParentLookup' failed to initialize correctly", obj);
        Integer id = obj.getUserParentLookupID();
        Assert.assertNotNull("Data on demand for 'UserParentLookup' failed to provide an identifier", id);
        obj = UserParentLookup.findUserParentLookup(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'UserParentLookup' with identifier '" + id + "'", UserParentLookup.findUserParentLookup(id));
    }

}
