package com.surgeryassist.core.services;

import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.surgeryassist.core.entity.AvailabilityDataOnDemand;
import com.surgeryassist.core.entity.DayAvailability;
import com.surgeryassist.services.interfaces.SearchService;

@Configurable
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml")
public class SearchServiceTest {
	
	@Autowired
	AvailabilityDataOnDemand data;
	
	@Autowired
	SearchService searchService;

	@Test
	public void test() {
		// testing SearchAll
		Assert.assertNotNull("Data on demand for 'Availability' failed to initialize correctly", data.getRandomAvailability());
		ArrayList<DayAvailability> res = (ArrayList<DayAvailability>) searchService.searchAll();
		Assert.assertNotNull ("", res);
		
		long count = DayAvailability.countDayAvailabilitys();
		Assert.assertTrue("more than 0", count > 0);
		Assert.assertTrue ("Our counts match", res.size() == count);
		
		
		// testing SearchByCity
		// Hardcoded for now, we have 7 results of city_3
		ArrayList<DayAvailability> res1 = (ArrayList<DayAvailability>) searchService.searchByCity("city_3");
		Assert.assertNotNull ("", res1);
		System.out.println(res1.size());
		long count1 = DayAvailability.countDayAvailabilitys();
		Assert.assertTrue("more than 0", count1 > 0);
		Assert.assertTrue ("Our counts match111", res1.size() == 7);
	}

}
