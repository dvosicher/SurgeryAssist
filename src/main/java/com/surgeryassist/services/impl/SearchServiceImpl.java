package com.surgeryassist.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.surgeryassist.core.dto.SearchCriteriaDTO;
import com.surgeryassist.core.entity.TimeAvailabilities;
import com.surgeryassist.services.interfaces.SearchService;

/**
 * Implementation of {@link SearchService}
 * @see SearchService
 * @author Nick Karpov and Ankit Tyagi
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
	@Transactional(readOnly = true)
	public List<TimeAvailabilities> searchByCriteria(SearchCriteriaDTO searchCriteria) {

		List<TimeAvailabilities> returnList = new ArrayList<TimeAvailabilities>();
		Integer zipCodeInt = null;
		String city = null;
		Integer timeDuration = null;

		if(!StringUtils.isEmpty(searchCriteria.getCityName())) {
			city = searchCriteria.getCityName();
		}
		if(!StringUtils.isEmpty(searchCriteria.getZipCode())) {
			zipCodeInt = Integer.parseInt(searchCriteria.getZipCode());
		}
		if(!searchCriteria.getTimeDuration().equals(0)) {
			timeDuration = searchCriteria.getTimeDuration();
		}

		returnList = TimeAvailabilities.findTimeAvailabilitiesBySearchCriteria(
				city, zipCodeInt, searchCriteria.getEndDate(), timeDuration, searchCriteria.getStartDate());

		return returnList;
	}

}
