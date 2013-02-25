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

import com.surgeryassist.core.entity.UserInfo;

@Configurable
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/config/applicationContext*.xml")
public class UserInfoIntegrationTest {
    
	@Autowired
    UserInfoDataOnDemand dod;
    
    @Test
    public void testCountUserInfoes() {
        Assert.assertNotNull("Data on demand for 'UserInfo' failed to initialize correctly", dod.getRandomUserInfo());
        long count = UserInfo.countUserInfoes();
        Assert.assertTrue("Counter for 'UserInfo' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void testFindUserInfo() {
        UserInfo obj = dod.getRandomUserInfo();
        Assert.assertNotNull("Data on demand for 'UserInfo' failed to initialize correctly", obj);
        Integer id = obj.getUserInfoID();
        Assert.assertNotNull("Data on demand for 'UserInfo' failed to provide an identifier", id);
        obj = UserInfo.findUserInfo(id);
        Assert.assertNotNull("Find method for 'UserInfo' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'UserInfo' returned the incorrect identifier", id, obj.getUserInfoID());
    }
    
    @Test
    public void testFindAllUserInfoes() {
        Assert.assertNotNull("Data on demand for 'UserInfo' failed to initialize correctly", dod.getRandomUserInfo());
        long count = UserInfo.countUserInfoes();
        Assert.assertTrue("Too expensive to perform a find all test for 'UserInfo', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<UserInfo> result = UserInfo.findAllUserInfoes();
        Assert.assertNotNull("Find all method for 'UserInfo' illegally returned null", result);
        Assert.assertTrue("Find all method for 'UserInfo' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void testFindUserInfoEntries() {
        Assert.assertNotNull("Data on demand for 'UserInfo' failed to initialize correctly", dod.getRandomUserInfo());
        long count = UserInfo.countUserInfoes();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<UserInfo> result = UserInfo.findUserInfoEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'UserInfo' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'UserInfo' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testFlush() {
        UserInfo obj = dod.getRandomUserInfo();
        Assert.assertNotNull("Data on demand for 'UserInfo' failed to initialize correctly", obj);
        Integer id = obj.getUserInfoID();
        Assert.assertNotNull("Data on demand for 'UserInfo' failed to provide an identifier", id);
        obj = UserInfo.findUserInfo(id);
        Assert.assertNotNull("Find method for 'UserInfo' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyUserInfo(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'UserInfo' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testMergeUpdate() {
        UserInfo obj = dod.getRandomUserInfo();
        Assert.assertNotNull("Data on demand for 'UserInfo' failed to initialize correctly", obj);
        Integer id = obj.getUserInfoID();
        Assert.assertNotNull("Data on demand for 'UserInfo' failed to provide an identifier", id);
        obj = UserInfo.findUserInfo(id);
        boolean modified =  dod.modifyUserInfo(obj);
        Integer currentVersion = obj.getVersion();
        UserInfo merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getUserInfoID(), id);
        Assert.assertTrue("Version for 'UserInfo' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'UserInfo' failed to initialize correctly", dod.getRandomUserInfo());
        UserInfo obj = dod.getNewTransientUserInfo(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'UserInfo' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'UserInfo' identifier to be null", obj.getUserInfoID());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'UserInfo' identifier to no longer be null", obj.getUserInfoID());
    }

}
