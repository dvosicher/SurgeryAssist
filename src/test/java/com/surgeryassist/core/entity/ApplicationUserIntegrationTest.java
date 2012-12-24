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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml")
@Transactional
@Configurable
public class ApplicationUserIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    private ApplicationUserDataOnDemand dod;

	@Test
    public void testCountApplicationUsers() {
        Assert.assertNotNull("Data on demand for 'ApplicationUser' failed to initialize correctly", dod.getRandomApplicationUser());
        long count = ApplicationUser.countApplicationUsers();
        Assert.assertTrue("Counter for 'ApplicationUser' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindApplicationUser() {
        ApplicationUser obj = dod.getRandomApplicationUser();
        Assert.assertNotNull("Data on demand for 'ApplicationUser' failed to initialize correctly", obj);
        Integer id = obj.getUserID();
        Assert.assertNotNull("Data on demand for 'ApplicationUser' failed to provide an identifier", id);
        obj = ApplicationUser.findApplicationUser(id);
        Assert.assertNotNull("Find method for 'ApplicationUser' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'ApplicationUser' returned the incorrect identifier", id, obj.getUserID());
    }

	@Test
    public void testFindAllApplicationUsers() {
        Assert.assertNotNull("Data on demand for 'ApplicationUser' failed to initialize correctly", dod.getRandomApplicationUser());
        long count = ApplicationUser.countApplicationUsers();
        Assert.assertTrue("Too expensive to perform a find all test for 'ApplicationUser', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<ApplicationUser> result = ApplicationUser.findAllApplicationUsers();
        Assert.assertNotNull("Find all method for 'ApplicationUser' illegally returned null", result);
        Assert.assertTrue("Find all method for 'ApplicationUser' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindApplicationUserEntries() {
        Assert.assertNotNull("Data on demand for 'ApplicationUser' failed to initialize correctly", dod.getRandomApplicationUser());
        long count = ApplicationUser.countApplicationUsers();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<ApplicationUser> result = ApplicationUser.findApplicationUserEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'ApplicationUser' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'ApplicationUser' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        ApplicationUser obj = dod.getRandomApplicationUser();
        Assert.assertNotNull("Data on demand for 'ApplicationUser' failed to initialize correctly", obj);
        Integer id = obj.getUserID();
        Assert.assertNotNull("Data on demand for 'ApplicationUser' failed to provide an identifier", id);
        obj = ApplicationUser.findApplicationUser(id);
        Assert.assertNotNull("Find method for 'ApplicationUser' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyApplicationUser(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'ApplicationUser' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testMergeUpdate() {
        ApplicationUser obj = dod.getRandomApplicationUser();
        Assert.assertNotNull("Data on demand for 'ApplicationUser' failed to initialize correctly", obj);
        Integer id = obj.getUserID();
        Assert.assertNotNull("Data on demand for 'ApplicationUser' failed to provide an identifier", id);
        obj = ApplicationUser.findApplicationUser(id);
        boolean modified =  dod.modifyApplicationUser(obj);
        Integer currentVersion = obj.getVersion();
        ApplicationUser merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getUserID(), id);
        Assert.assertTrue("Version for 'ApplicationUser' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'ApplicationUser' failed to initialize correctly", dod.getRandomApplicationUser());
        ApplicationUser obj = dod.getNewTransientApplicationUser(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'ApplicationUser' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'ApplicationUser' identifier to be null", obj.getUserID());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'ApplicationUser' identifier to no longer be null", obj.getUserID());
    }

	@Test
    public void testRemove() {
        ApplicationUser obj = dod.getRandomApplicationUser();
        Assert.assertNotNull("Data on demand for 'ApplicationUser' failed to initialize correctly", obj);
        Integer id = obj.getUserID();
        Assert.assertNotNull("Data on demand for 'ApplicationUser' failed to provide an identifier", id);
        obj = ApplicationUser.findApplicationUser(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'ApplicationUser' with identifier '" + id + "'", ApplicationUser.findApplicationUser(id));
    }
}
