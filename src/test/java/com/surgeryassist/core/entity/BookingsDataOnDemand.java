package com.surgeryassist.core.entity;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import com.surgeryassist.core.entity.ApplicationUser;
import com.surgeryassist.core.entity.Bookings;
import com.surgeryassist.core.entity.Patient;

@Configurable
@Component
public class BookingsDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<Bookings> data;

	@Autowired
	ApplicationUserDataOnDemand applicationUserDataOnDemand;

	@Autowired
	PatientDataOnDemand patientDataOnDemand;

	@Autowired
	TimeAvailabilitiesDataOnDemand timeAvailabilitiesDataOnDemand;
	
	public Bookings getNewTransientBookings(int index) {
		Bookings obj = new Bookings();
		setBookingCreatorId(obj, index);
		setBookingLocationId(obj, index);
		setBookingRoom(obj, index);
		setCancellationReason(obj, index);
		setCreatedBy(obj, index);
		setCreatedDate(obj, index);
		setIsCanceled(obj, index);
		setModifiedBy(obj, index);
		setModifiedDate(obj, index);
		setPatientId(obj, index);
		return obj;
	}

	public void setBookingCreatorId(Bookings obj, int index) {
		ApplicationUser bookingCreatorId = applicationUserDataOnDemand.getRandomApplicationUser();
		obj.setBookingCreatorId(bookingCreatorId);
	}

	public void setBookingLocationId(Bookings obj, int index) {
		ApplicationUser bookingLocationId = applicationUserDataOnDemand.getRandomApplicationUser();
		obj.setBookingLocationId(bookingLocationId);
	}

	public void setBookingRoom(Bookings obj, int index) {
		String bookingRoom = Integer.toString(index);
		obj.setBookingRoom(bookingRoom);
	}

	public void setCancellationReason(Bookings obj, int index) {
		String cancellationReason = "cancellationReason_" + index;
		if (cancellationReason.length() > 2000) {
			cancellationReason = cancellationReason.substring(0, 2000);
		}
		obj.setCancellationReason(cancellationReason);
	}

	public void setCreatedBy(Bookings obj, int index) {
		Integer createdBy = new Integer(index);
		obj.setCreatedBy(createdBy);
	}

	public void setCreatedDate(Bookings obj, int index) {
		Calendar createdDate = Calendar.getInstance();
		obj.setCreatedDate(createdDate);
	}

	public void setIsCanceled(Bookings obj, int index) {
		Boolean isCanceled = Boolean.TRUE;
		obj.setIsCanceled(isCanceled);
	}

	public void setModifiedBy(Bookings obj, int index) {
		Integer modifiedBy = new Integer(index);
		obj.setModifiedBy(modifiedBy);
	}

	public void setModifiedDate(Bookings obj, int index) {
		Calendar modifiedDate = Calendar.getInstance();
		obj.setModifiedDate(modifiedDate);
	}

	public void setPatientId(Bookings obj, int index) {
		Patient patientId = patientDataOnDemand.getRandomPatient();
		obj.setPatientId(patientId);
	}

	public Bookings getSpecificBookings(int index) {
		init();
		if (index < 0) {
			index = 0;
		}
		if (index > (data.size() - 1)) {
			index = data.size() - 1;
		}
		Bookings obj = data.get(index);
		Integer id = obj.getBookingId();
		return Bookings.findBookings(id);
	}

	public Bookings getRandomBookings() {
		init();
		Bookings obj = data.get(rnd.nextInt(data.size()));
		Integer id = obj.getBookingId();
		return Bookings.findBookings(id);
	}

	public boolean modifyBookings(Bookings obj) {
		return false;
	}

	public void init() {
		int from = 0;
		int to = 10;
		data = Bookings.findBookingsEntries(from, to);
		if (data == null) {
			throw new IllegalStateException("Find entries implementation for 'Bookings' illegally returned null");
		}
		if (!data.isEmpty()) {
			return;
		}

		data = new ArrayList<Bookings>();
		for (int i = 0; i < 10; i++) {
			Bookings obj = getNewTransientBookings(i);
			try {
				obj.persist();
			} catch (ConstraintViolationException e) {
				StringBuilder msg = new StringBuilder();
				for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
					ConstraintViolation<?> cv = iter.next();
					msg.append("[").append(cv.getConstraintDescriptor()).append(":").append(cv.getMessage()).append("=").append(cv.getInvalidValue()).append("]");
				}
				throw new RuntimeException(msg.toString(), e);
			}
			obj.flush();
			data.add(obj);
		}
	}

}
