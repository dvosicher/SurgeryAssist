package com.surgeryassist.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.surgeryassist.core.entity.DayAvailability;
import com.surgeryassist.services.interfaces.SearchService;

@Service("searchService")
public class SearchServiceImpl implements SearchService {

	@Override
	public List<DayAvailability> searchAll() {
		List<DayAvailability> result = DayAvailability.findAllDayAvailabilitys();
		
		if (result != null)
			return result;
		return new ArrayList<DayAvailability> ();
	}

	@Override
	public List<DayAvailability> searchByCity(String city) {
		List<DayAvailability> result = DayAvailability.findAllDayAvailabilitysByCity(city);
		
		if (result != null)
			return result;
		return new ArrayList<DayAvailability> ();
	}

	@Override
	public List<DayAvailability> searchByZipCode(Integer zipCode) {
		List<DayAvailability> result = DayAvailability.findAllDayAvailabilitysByZipCode(zipCode);
		
		if (result != null)
			return result;
		return new ArrayList<DayAvailability> ();
	}

}
