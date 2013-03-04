package com.surgeryassist.core.services;

import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.surgeryassist.core.entity.AvailabilityDataOnDemand;
import com.surgeryassist.core.entity.DayAvailability;
import com.surgeryassist.core.entity.TimeAvailabilities;
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
	@Ignore
	public void test() {
//		// testing SearchAll
//		Assert.assertNotNull("Data on demand for 'Availability' failed to initialize correctly", data.getRandomAvailability());
//		ArrayList<TimeAvailabilities> res = (ArrayList<TimeAvailabilities>) searchService.searchAll();
//		Assert.assertNotNull ("", res);
//		
//		long count = DayAvailability.countDayAvailabilitys();
//		Assert.assertTrue("more than 0", count > 0);
//		Assert.assertTrue ("Our counts match", res.size() == count);
//		
//		
//		// testing SearchByCity
//		// Hardcoded for now, we have 7 results of city_3
//		ArrayList<DayAvailability> res1 = (ArrayList<DayAvailability>) searchService.searchByCity("city_3");
//		Assert.assertNotNull ("", res1);
//		System.out.println(res1.size());
//		long count1 = DayAvailability.countDayAvailabilitys();
//		Assert.assertTrue("more than 0", count1 > 0);
//		Assert.assertTrue ("City counts don't match", res1.size() == 3);
//		
//		// testing SearchByZipCode
//		ArrayList<DayAvailability> res2 = (ArrayList<DayAvailability>) searchService.searchByZipCode(3);
//		Assert.assertNotNull ("", res2);
//		System.out.println(res2.size());
//		long count2 = DayAvailability.countDayAvailabilitys();
//		Assert.assertTrue("more than 0", count2 > 0);
//		Assert.assertTrue ("ZipCode counts don't match", res2.size() == 3);
	}

}
