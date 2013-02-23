package com.surgeryassist.services.interfaces;

import java.util.List;
import com.surgeryassist.core.entity.DayAvailability;

public interface SearchService {
	public List<DayAvailability> searchAll ();
	public List<DayAvailability> searchByCity (String city);
	public List<DayAvailability> searchByZipCode(Integer zipCode);
}