package com.surgeryassist.core.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO to hold SearchCriteria Data
 * @author atyagi
 *
 */
public class SearchCriteriaDTO implements Serializable {

	private static final long serialVersionUID = 959188744472636133L;
	
	private String cityName;
	
	private String zipCode;
	
	private Date startDate;
	
	private Date endDate;
	
	public SearchCriteriaDTO() {
		this.startDate = new Date();
		this.endDate = new Date();
	}

	/**
	 * @return the cityName
	 */
	public String getCityName() {
		return cityName;
	}

	/**
	 * @param cityName the cityName to set
	 */
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	/**
	 * @return the zipCode
	 */
	public String getZipCode() {
		return zipCode;
	}

	/**
	 * @param zipCode the zipCode to set
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	
	
}
