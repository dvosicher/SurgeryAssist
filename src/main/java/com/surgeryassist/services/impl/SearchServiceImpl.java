package com.surgeryassist.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.surgeryassist.core.SearchType;
import com.surgeryassist.core.dto.SearchCriteriaDTO;
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
	@Transactional(readOnly = true)
	public List<TimeAvailabilities> searchAll() {
		List<TimeAvailabilities> result = TimeAvailabilities.findAllTimeAvailabilitieses();
		
		if (result != null)
			return result;
		return new ArrayList<TimeAvailabilities>();
	}
	
	@Override
	public Map<String, List<SelectItem>> getSelectItemValues() {
		Map<String, List<SelectItem>> mapOfSelectItems = new HashMap<String, List<SelectItem>>();
		List<SelectItem> searchTypeSelectList = new ArrayList<SelectItem>();
		
		for(SearchType searchType : SearchType.values()) {
			searchTypeSelectList.add(new SelectItem(searchType, searchType.toString()));
		}
		mapOfSelectItems.put("searchType", searchTypeSelectList);
		
		return mapOfSelectItems;
	}

	@Override
	@Transactional(readOnly = true)
	public List<TimeAvailabilities> searchByCriteria(SearchCriteriaDTO searchCriteria) {
		
		List<TimeAvailabilities> returnList = new ArrayList<TimeAvailabilities>();
		Integer zipCodeInt = null;
		String city = null;
		
		if(!StringUtils.isEmpty(searchCriteria.getCityName())) {
			city = searchCriteria.getCityName();
		}
		if(!StringUtils.isEmpty(searchCriteria.getZipCode())) {
			zipCodeInt = Integer.parseInt(searchCriteria.getZipCode());
		}
		
		
		returnList = TimeAvailabilities.findTimeAvailabilitiesBySearchCriteria(
				city, zipCodeInt, searchCriteria.getStartDate(), searchCriteria.getEndDate(), 
				searchCriteria.getTimeDuration());
		
		return returnList;
	}

}
