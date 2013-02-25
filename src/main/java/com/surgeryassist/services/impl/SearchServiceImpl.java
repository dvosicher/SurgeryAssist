package com.surgeryassist.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.surgeryassist.core.SearchType;
import com.surgeryassist.core.entity.DayAvailability;
import com.surgeryassist.core.entity.TimeAvailabilities;
import com.surgeryassist.services.interfaces.SearchService;

/**
 * Implementation of {@link SearchService}
 * @see SearchService
 * @author Nick Karpov
 */
@Service("searchService")
public class SearchServiceImpl implements SearchService {

	@Override
	@Transactional
	public List<TimeAvailabilities> searchAll() {
		List<TimeAvailabilities> result = TimeAvailabilities.findAllTimeAvailabilitieses();
		
		if (result != null)
			return result;
		return new ArrayList<TimeAvailabilities>();
	}

	@Override
	public List<DayAvailability> searchByCity(String city) {
		List<DayAvailability> result = DayAvailability.findAllDayAvailabilitysByCity(city);
		
		if (result != null) {
			return result;
		}
		return new ArrayList<DayAvailability>();
	}

	@Override
	public List<DayAvailability> searchByZipCode(Integer zipCode) {
		List<DayAvailability> result = DayAvailability.findAllDayAvailabilitysByZipCode(zipCode);
		
		if (result != null) {
			return result;
		}
		return new ArrayList<DayAvailability>();
	}
	
	public Map<String, List<SelectItem>> getSelectItemValues() {
		Map<String, List<SelectItem>> mapOfSelectItems = new HashMap<String, List<SelectItem>>();
		List<SelectItem> searchTypeSelectList = new ArrayList<SelectItem>();
		
		for(SearchType searchType : SearchType.values()) {
			searchTypeSelectList.add(new SelectItem(searchType, searchType.toString()));
		}
		mapOfSelectItems.put("searchType", searchTypeSelectList);
		
		return mapOfSelectItems;
	}

}
