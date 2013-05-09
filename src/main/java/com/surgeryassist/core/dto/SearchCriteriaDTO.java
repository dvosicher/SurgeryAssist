package com.surgeryassist.core.dto;

import java.io.Serializable;
import java.util.Date;

import com.surgeryassist.core.entity.TimeAvailabilities;

/**
 * DTO to hold SearchCriteria Data, including
 * city, zipcode, start day, end day, time of surgery
 * and more.
 * @author Ankit Tyagi
 */
public class SearchCriteriaDTO implements Serializable {

	private static final long serialVersionUID = 959188744472636133L;
	
	private String cityName;
	
	private String zipCode;
	
	private Date startDate;
	
	private Date endDate;
	
	private Integer timeDuration;
	
	private TimeAvailabilities selectedAvailability;
	
	public SearchCriteriaDTO() {
		this.startDate = new Date();
		this.endDate = new Date();
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public TimeAvailabilities getSelectedAvailability() {
		return selectedAvailability;
	}

	public void setSelectedAvailability(TimeAvailabilities selectedAvailability) {
		this.selectedAvailability = selectedAvailability;
	}

	public Integer getTimeDuration() {
		return timeDuration;
	}

	public void setTimeDuration(Integer timeDuration) {
		this.timeDuration = timeDuration;
	}
	
}
