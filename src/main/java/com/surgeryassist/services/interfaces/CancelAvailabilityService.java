package com.surgeryassist.services.interfaces;

import org.primefaces.component.datatable.DataTable;

import com.surgeryassist.core.dto.CancelAvailabilityDTO;

/**
 * Service implementation for cancelling 
 * unbooked availabilities
 * @author Ankit Tyagi
 */
public interface CancelAvailabilityService {

	/**
	 * Returns the required bean for the {@link DataTable}
	 * for multiple selection of the cancel availability page
	 * @param existingAvailabilityDTO The existing DTO if already on this page before
	 * 	session, otherwise a null object or empty list within the {@link CancelAvailabilityDTO}
	 * @return The populated DTO instance
	 */
	public CancelAvailabilityDTO getListOfAvailabilitiesToCancel(CancelAvailabilityDTO existingAvailabilityDTO);
	
	
}
