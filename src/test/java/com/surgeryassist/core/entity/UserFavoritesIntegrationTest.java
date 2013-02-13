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

import com.surgeryassist.core.entity.UserFavorites;

@Configurable
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/config/applicationContext*.xml")
public class UserFavoritesIntegrationTest {

    @Autowired
    UserFavoritesDataOnDemand dod;
    
    @Test
    public void testCountUserFavoriteses() {
        Assert.assertNotNull("Data on demand for 'UserFavorites' failed to initialize correctly", dod.getRandomUserFavorites());
        long count = UserFavorites.countUserFavoriteses();
        Assert.assertTrue("Counter for 'UserFavorites' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void testFindUserFavorites() {
        UserFavorites obj = dod.getRandomUserFavorites();
        Assert.assertNotNull("Data on demand for 'UserFavorites' failed to initialize correctly", obj);
        Integer id = obj.getFavoritesId();
        Assert.assertNotNull("Data on demand for 'UserFavorites' failed to provide an identifier", id);
        obj = UserFavorites.findUserFavorites(id);
        Assert.assertNotNull("Find method for 'UserFavorites' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'UserFavorites' returned the incorrect identifier", id, obj.getFavoritesId());
    }
    
    @Test
    public void testFindAllUserFavoriteses() {
        Assert.assertNotNull("Data on demand for 'UserFavorites' failed to initialize correctly", dod.getRandomUserFavorites());
        long count = UserFavorites.countUserFavoriteses();
        Assert.assertTrue("Too expensive to perform a find all test for 'UserFavorites', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<UserFavorites> result = UserFavorites.findAllUserFavoriteses();
        Assert.assertNotNull("Find all method for 'UserFavorites' illegally returned null", result);
        Assert.assertTrue("Find all method for 'UserFavorites' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void testFindUserFavoritesEntries() {
        Assert.assertNotNull("Data on demand for 'UserFavorites' failed to initialize correctly", dod.getRandomUserFavorites());
        long count = UserFavorites.countUserFavoriteses();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<UserFavorites> result = UserFavorites.findUserFavoritesEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'UserFavorites' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'UserFavorites' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testFlush() {
        UserFavorites obj = dod.getRandomUserFavorites();
        Assert.assertNotNull("Data on demand for 'UserFavorites' failed to initialize correctly", obj);
        Integer id = obj.getFavoritesId();
        Assert.assertNotNull("Data on demand for 'UserFavorites' failed to provide an identifier", id);
        obj = UserFavorites.findUserFavorites(id);
        Assert.assertNotNull("Find method for 'UserFavorites' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyUserFavorites(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'UserFavorites' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testMergeUpdate() {
        UserFavorites obj = dod.getRandomUserFavorites();
        Assert.assertNotNull("Data on demand for 'UserFavorites' failed to initialize correctly", obj);
        Integer id = obj.getFavoritesId();
        Assert.assertNotNull("Data on demand for 'UserFavorites' failed to provide an identifier", id);
        obj = UserFavorites.findUserFavorites(id);
        boolean modified =  dod.modifyUserFavorites(obj);
        Integer currentVersion = obj.getVersion();
        UserFavorites merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getFavoritesId(), id);
        Assert.assertTrue("Version for 'UserFavorites' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'UserFavorites' failed to initialize correctly", dod.getRandomUserFavorites());
        UserFavorites obj = dod.getNewTransientUserFavorites(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'UserFavorites' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'UserFavorites' identifier to be null", obj.getFavoritesId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'UserFavorites' identifier to no longer be null", obj.getFavoritesId());
    }
    
    @Test
    public void testRemove() {
        UserFavorites obj = dod.getRandomUserFavorites();
        Assert.assertNotNull("Data on demand for 'UserFavorites' failed to initialize correctly", obj);
        Integer id = obj.getFavoritesId();
        Assert.assertNotNull("Data on demand for 'UserFavorites' failed to provide an identifier", id);
        obj = UserFavorites.findUserFavorites(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'UserFavorites' with identifier '" + id + "'", UserFavorites.findUserFavorites(id));
    }

}
