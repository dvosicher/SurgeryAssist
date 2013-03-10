package com.surgeryassist.core.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
@Table(schema = "MetaData", name = "insurance_type")
public class InsuranceType implements Serializable {
	
	private static final long serialVersionUID = -5857695163321192146L;

	@OneToMany(mappedBy = "insuranceTypeId")
    private Set<AcceptedInsuranceTypes> acceptedInsuranceTypeses;
    
    @OneToMany(mappedBy = "insuranceCode")
    private Set<Patient> patients;
	
	@Column(name = "insurance_code", length = 10)
	private String insuranceCode;
	
	@Column(name = "insurance_name", length = 100)
	private String insuranceName;
	
	@Column(name = "insurance_description", length = 512)
	private String insuranceDescription;
	
	@Column(name = "insurance_policy_number", length = 255)
	private String insurancePolicyNumber;
	
	@Column(name = "created_by", updatable = false)
    public Integer createdBy;

    @Column(name = "created_date", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    public Calendar createdDate;

    @Column(name = "modified_by")
    public Integer modifiedBy;

    @Column(name = "modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    public Calendar modifiedDate;

	public String getInsuranceCode() {
        return this.insuranceCode;
    }

	public void setInsuranceCode(String insuranceCode) {
        this.insuranceCode = insuranceCode;
    }

	public String getInsuranceName() {
        return this.insuranceName;
    }

	public void setInsuranceName(String insuranceName) {
        this.insuranceName = insuranceName;
    }

	public String getInsuranceDescription() {
        return this.insuranceDescription;
    }

	public void setInsuranceDescription(String insuranceDescription) {
        this.insuranceDescription = insuranceDescription;
    }

	public String getInsurancePolicyNumber() {
        return this.insurancePolicyNumber;
    }

	public void setInsurancePolicyNumber(String insurancePolicyNumber) {
        this.insurancePolicyNumber = insurancePolicyNumber;
    }

	public Integer getCreatedBy() {
        return this.createdBy;
    }

	public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

	public Calendar getCreatedDate() {
        return this.createdDate;
    }

	public void setCreatedDate(Calendar createdDate) {
        this.createdDate = createdDate;
    }

	public Integer getModifiedBy() {
        return this.modifiedBy;
    }

	public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

	public Calendar getModifiedDate() {
        return this.modifiedDate;
    }

	public void setModifiedDate(Calendar modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "insurance_id")
    private Integer insuranceID;

	@Version
    @Column(name = "version")
    private Integer version;

	public Integer getInsuranceID() {
        return this.insuranceID;
    }

	public void setInsuranceID(Integer id) {
        this.insuranceID = id;
    }

	public Integer getVersion() {
        return this.version;
    }

	public void setVersion(Integer version) {
        this.version = version;
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final EntityManager entityManager() {
        EntityManager em = new InsuranceType().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countInsuranceTypes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM InsuranceType o", Long.class).getSingleResult();
    }

	public static List<InsuranceType> findAllInsuranceTypes() {
        return entityManager().createQuery("SELECT o FROM InsuranceType o", InsuranceType.class).getResultList();
    }

	public static InsuranceType findInsuranceType(Integer insuranceID) {
        if (insuranceID == null) return null;
        return entityManager().find(InsuranceType.class, insuranceID);
    }

	public static List<InsuranceType> findInsuranceTypeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM InsuranceType o", InsuranceType.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

	@Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            InsuranceType attached = InsuranceType.findInsuranceType(this.insuranceID);
            this.entityManager.remove(attached);
        }
    }

	@Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }

	@Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }

	@Transactional
    public InsuranceType merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        InsuranceType merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	/**
	 * @return the acceptedInsuranceTypeses
	 */
	public Set<AcceptedInsuranceTypes> getAcceptedInsuranceTypeses() {
		return acceptedInsuranceTypeses;
	}

	/**
	 * @param acceptedInsuranceTypeses the acceptedInsuranceTypeses to set
	 */
	public void setAcceptedInsuranceTypeses(Set<AcceptedInsuranceTypes> acceptedInsuranceTypeses) {
		this.acceptedInsuranceTypeses = acceptedInsuranceTypeses;
	}

	/**
	 * @return the patients
	 */
	public Set<Patient> getPatients() {
		return patients;
	}

	/**
	 * @param patients the patients to set
	 */
	public void setPatients(Set<Patient> patients) {
		this.patients = patients;
	}
}
