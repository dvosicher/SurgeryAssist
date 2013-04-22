package com.surgeryassist.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.surgeryassist.core.entity.ApplicationUser;
import com.surgeryassist.core.entity.Bookings;
import com.surgeryassist.core.entity.InsuranceType;
import com.surgeryassist.core.entity.Patient;
import com.surgeryassist.core.entity.SurgeryType;
import com.surgeryassist.core.entity.TimeAvailabilities;
import com.surgeryassist.services.interfaces.BookingService;
import com.surgeryassist.util.SurgeryAssistUtil;

/**
 * Implementation of {@link BookingService}
 * 
 * @see BookingService
 * @author Ankit Tyagi
 */
@Service("bookingService")
public class BookingServiceImpl implements BookingService {

	@Override
	@Transactional
	public void createBooking(ApplicationUser bookingLocation, Patient patient, 
			TimeAvailabilities selectedTimeAvailability, InsuranceType insuranceType) {

		this.persistPatient(insuranceType, patient);
		
		//create a booking object
		Bookings newBooking = new Bookings();
		newBooking.setBookingCreatorId(
				SurgeryAssistUtil.getLoggedInApplicationUser());
		newBooking.setBookingLocationId(bookingLocation);
		newBooking.setPatientId(patient);
		newBooking.setTimeAvailabilityId(selectedTimeAvailability);
		newBooking.setBookingRoom(selectedTimeAvailability.getRoomNumber());
		newBooking.setIsCanceled(false);
		newBooking.setIsConfirmed(false);

		//merge the availability to be completely caught up
		selectedTimeAvailability.setIsBooked(true);
		selectedTimeAvailability = (TimeAvailabilities) SurgeryAssistUtil.setLastModifiedInfo(selectedTimeAvailability);
		selectedTimeAvailability.merge();

		newBooking = (Bookings) SurgeryAssistUtil.setAllHistoricalInfo(newBooking);

		//persist the booking object and flush it
		newBooking.persist();
		newBooking.flush();
	}

	@Override
	@Transactional
	public void persistPatient(InsuranceType insuranceType, Patient patient) {

		InsuranceType existingInsuranceType = 
				InsuranceType.findInsuranceType(insuranceType.getInsuranceID());

		Patient existingPatient =
				Patient.findPatient(patient.getPatientId());

		//store the insurance info if it doesn't exist already, otherwise merge it
		if(existingInsuranceType == null) {
			insuranceType = (InsuranceType) SurgeryAssistUtil.setAllHistoricalInfo(insuranceType);
			insuranceType.persist();
		}
		else {
			insuranceType = (InsuranceType) SurgeryAssistUtil.setLastModifiedInfo(insuranceType);
			insuranceType.merge();
		}

		patient.setInsuranceCode(insuranceType);

		if(existingPatient == null) {
			patient = (Patient) SurgeryAssistUtil.setAllHistoricalInfo(patient);
			patient.persist();
		}
		else {
			patient = (Patient) SurgeryAssistUtil.setLastModifiedInfo(patient);
			patient.merge();
		}
		patient.flush();
	}

	@Override
	public Map<String, List<SelectItem>> getDropdownMenuValues() {
		Map<String, List<SelectItem>> mapOfSelectItems = 
				new HashMap<String, List<SelectItem>>();
		List<SelectItem> surgeryTypeList = new ArrayList<SelectItem>();

		List<SurgeryType> surgeryTypeEntityList = SurgeryType.findAllSurgeryTypes();
		for(SurgeryType surgeryType : surgeryTypeEntityList) {
			surgeryTypeList.add(
					new SelectItem(
							surgeryType, surgeryType.getSurgeryFullName()));
		}
		mapOfSelectItems.put("surgeryTypes", surgeryTypeList);

		return mapOfSelectItems;
	}

	@Override
	public void setAlreadyBookedErrorMessage() {
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_ERROR,"The Availability Was Already Booked", "Please go back and search again"));
	}

}
