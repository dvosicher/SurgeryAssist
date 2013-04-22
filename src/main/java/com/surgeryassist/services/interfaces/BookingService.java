	package com.surgeryassist.services.interfaces;

import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import com.surgeryassist.core.entity.ApplicationUser;
import com.surgeryassist.core.entity.Bookings;
import com.surgeryassist.core.entity.InsuranceType;
import com.surgeryassist.core.entity.Patient;
import com.surgeryassist.core.entity.TimeAvailabilities;

/**
 * Interface class for creating and saving bookings, thus
 * making booking different availabilities, and ideally
 * removing them from searches.
 * @author Ankit Tyagi
 */
public interface BookingService {
	
	/**
	 * Creates a {@link Bookings} object and 
	 * persists it based on the provided parameters
	 * @param bookingLocation The location of room (the {@link ApplicationUser} object 
	 * 		of the selected {@link TimeAvailabilities})
	 * @param selectedTimeAvailability The selected {@link TimeAvailabilities} of the booking
	 * @param patient The patient that will be treated with said {@link Bookings}
	 * @param insuranceType The {@link InsuranceType} of the patient
	 */
	public void createBooking(ApplicationUser bookingLocation, Patient patient, 
			TimeAvailabilities selectedTimeAvailability, InsuranceType insuranceType);
	
	/**
	 * Persists the patient object
	 * Starts by persisting the insurance type, setting it to the
	 * patient, and then persisting the patient
	 * @param insuranceType The {@link InsuranceType} of the patient
	 * @param patient The {@link Patient} object
	 */
	public void persistPatient(InsuranceType insuranceType, Patient patient);
	
	public void setAlreadyBookedErrorMessage();
	
	/**
	 * Creates a map that contains key-value pair,
	 * mapping a {@link String} to a {@link List} of 
	 * {@link SelectItem} to be referenced. 
	 * <br><br> 
	 * Currently contains:
	 * <ul>
	 * 	<li>SurgeryTypes</li>
	 * </ul>
	 * @return {@link Map} mapping {@link String} 
	 * 	to {@link List} of {@link SelectItem}
	 */
	public Map<String, List<SelectItem>> getDropdownMenuValues();
	
}
