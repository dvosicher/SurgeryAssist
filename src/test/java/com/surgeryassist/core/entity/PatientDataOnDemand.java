package com.surgeryassist.core.entity;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.stereotype.Component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.surgeryassist.core.entity.InsuranceType;
import com.surgeryassist.core.entity.SurgeryType;
import com.surgeryassist.core.entity.Patient;

@Configurable
@Component
public class PatientDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<Patient> data;

	@Autowired
	InsuranceTypeDataOnDemand insuranceTypeDataOnDemand;

	@Autowired
	SurgeryTypeDataOnDemand surgeryTypeDataOnDemand;

	public Patient getNewTransientPatient(int index) {
		Patient obj = new Patient();
		setCreatedBy(obj, index);
		setCreatedDate(obj, index);
		setFirstName(obj, index);
		setInsuranceCode(obj, index);
		setLastName(obj, index);
		setModifiedBy(obj, index);
		setModifiedDate(obj, index);
		setSurgeryTypeCode(obj, index);
		return obj;
	}

	public void setCreatedBy(Patient obj, int index) {
		Integer createdBy = new Integer(index);
		obj.setCreatedBy(createdBy);
	}

	public void setCreatedDate(Patient obj, int index) {
		Calendar createdDate = Calendar.getInstance();
		obj.setCreatedDate(createdDate);
	}

	public void setFirstName(Patient obj, int index) {
		String firstName = "firstName_" + index;
		if (firstName.length() > 100) {
			firstName = firstName.substring(0, 100);
		}
		obj.setFirstName(firstName);
	}

	public void setInsuranceCode(Patient obj, int index) {
		InsuranceType insuranceCode = insuranceTypeDataOnDemand.getRandomInsuranceType();
		obj.setInsuranceCode(insuranceCode);
	}

	public void setLastName(Patient obj, int index) {
		String lastName = "lastName_" + index;
		if (lastName.length() > 100) {
			lastName = lastName.substring(0, 100);
		}
		obj.setLastName(lastName);
	}

	public void setModifiedBy(Patient obj, int index) {
		Integer modifiedBy = new Integer(index);
		obj.setModifiedBy(modifiedBy);
	}

	public void setModifiedDate(Patient obj, int index) {
		Calendar modifiedDate = Calendar.getInstance();
		obj.setModifiedDate(modifiedDate);
	}

	public void setSurgeryTypeCode(Patient obj, int index) {
		SurgeryType surgeryTypeCode = surgeryTypeDataOnDemand.getRandomSurgeryType();
		obj.setSurgeryTypeCode(surgeryTypeCode);
	}

	public Patient getSpecificPatient(int index) {
		init();
		if (index < 0) {
			index = 0;
		}
		if (index > (data.size() - 1)) {
			index = data.size() - 1;
		}
		Patient obj = data.get(index);
		Integer id = obj.getPatientId();
		return Patient.findPatient(id);
	}

	public Patient getRandomPatient() {
		init();
		Patient obj = data.get(rnd.nextInt(data.size()));
		Integer id = obj.getPatientId();
		return Patient.findPatient(id);
	}

	public boolean modifyPatient(Patient obj) {
		return false;
	}

	public void init() {
		int from = 0;
		int to = 10;
		data = Patient.findPatientEntries(from, to);
		if (data == null) {
			throw new IllegalStateException("Find entries implementation for 'Patient' illegally returned null");
		}
		if (!data.isEmpty()) {
			return;
		}

		data = new ArrayList<Patient>();
		for (int i = 0; i < 10; i++) {
			Patient obj = getNewTransientPatient(i);
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
