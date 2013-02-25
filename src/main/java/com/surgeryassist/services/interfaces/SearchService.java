package com.surgeryassist.services.interfaces;

import java.util.ArrayList;
import java.util.List;
import com.surgeryassist.core.entity.DayAvailability;

/**
 * Allows for searching for different records
 * based on parameters and passed in values.
 * 
 * @author Nick Karpov
 */
public interface SearchService {
	
	/**
	 * Returns a dump of all day availabilities
	 * @return {@link ArrayList} of {@link DayAvailability}
	 */
	public List<DayAvailability> searchAll();
	
	/**
	 * Returns a list of {@link DayAvailability}
	 * for a specific city (fuzzy search)
	 * @param city String of city name
	 * @return {@link ArrayList} of {@link DayAvailability}
	 */
	public List<DayAvailability> searchByCity(String city);
	
	/**
	 * Returns a list of {@link DayAvailability}
	 * for a zip code (fuzzy search)
	 * @param zipCode Integer of the zipcode
	 * @return {@link ArrayList} of {@link DayAvailability}
	 */
	public List<DayAvailability> searchByZipCode(Integer zipCode);
}