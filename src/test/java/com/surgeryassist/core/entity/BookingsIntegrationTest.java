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

import com.surgeryassist.core.entity.Bookings;

@Configurable
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/config/applicationContext*.xml")
public class BookingsIntegrationTest {
    
	@Autowired
    BookingsDataOnDemand dod;
    
    @Test
    public void testCountBookingses() {
        Assert.assertNotNull("Data on demand for 'Bookings' failed to initialize correctly", dod.getRandomBookings());
        long count = Bookings.countBookingses();
        Assert.assertTrue("Counter for 'Bookings' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void testFindBookings() {
        Bookings obj = dod.getRandomBookings();
        Assert.assertNotNull("Data on demand for 'Bookings' failed to initialize correctly", obj);
        Integer id = obj.getBookingId();
        Assert.assertNotNull("Data on demand for 'Bookings' failed to provide an identifier", id);
        obj = Bookings.findBookings(id);
        Assert.assertNotNull("Find method for 'Bookings' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Bookings' returned the incorrect identifier", id, obj.getBookingId());
    }
    
    @Test
    public void testFindAllBookingses() {
        Assert.assertNotNull("Data on demand for 'Bookings' failed to initialize correctly", dod.getRandomBookings());
        long count = Bookings.countBookingses();
        Assert.assertTrue("Too expensive to perform a find all test for 'Bookings', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Bookings> result = Bookings.findAllBookingses();
        Assert.assertNotNull("Find all method for 'Bookings' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Bookings' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void testFindBookingsEntries() {
        Assert.assertNotNull("Data on demand for 'Bookings' failed to initialize correctly", dod.getRandomBookings());
        long count = Bookings.countBookingses();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Bookings> result = Bookings.findBookingsEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'Bookings' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Bookings' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testFlush() {
        Bookings obj = dod.getRandomBookings();
        Assert.assertNotNull("Data on demand for 'Bookings' failed to initialize correctly", obj);
        Integer id = obj.getBookingId();
        Assert.assertNotNull("Data on demand for 'Bookings' failed to provide an identifier", id);
        obj = Bookings.findBookings(id);
        Assert.assertNotNull("Find method for 'Bookings' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyBookings(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'Bookings' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testMergeUpdate() {
        Bookings obj = dod.getRandomBookings();
        Assert.assertNotNull("Data on demand for 'Bookings' failed to initialize correctly", obj);
        Integer id = obj.getBookingId();
        Assert.assertNotNull("Data on demand for 'Bookings' failed to provide an identifier", id);
        obj = Bookings.findBookings(id);
        boolean modified =  dod.modifyBookings(obj);
        Integer currentVersion = obj.getVersion();
        Bookings merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getBookingId(), id);
        Assert.assertTrue("Version for 'Bookings' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'Bookings' failed to initialize correctly", dod.getRandomBookings());
        Bookings obj = dod.getNewTransientBookings(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Bookings' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Bookings' identifier to be null", obj.getBookingId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'Bookings' identifier to no longer be null", obj.getBookingId());
    }
    
    @Test
    public void testRemove() {
        Bookings obj = dod.getRandomBookings();
        Assert.assertNotNull("Data on demand for 'Bookings' failed to initialize correctly", obj);
        Integer id = obj.getBookingId();
        Assert.assertNotNull("Data on demand for 'Bookings' failed to provide an identifier", id);
        obj = Bookings.findBookings(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'Bookings' with identifier '" + id + "'", Bookings.findBookings(id));
    }

}
