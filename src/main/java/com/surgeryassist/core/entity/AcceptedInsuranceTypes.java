package com.surgeryassist.core.entity;

import java.util.Calendar;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Table(schema = "SurgeryAssist", name = "accepted_insurance_types")
@Configurable
public class AcceptedInsuranceTypes {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "accepted_insurance_id")
    private Integer acceptedInsuranceID;

	@Version
    @Column(name = "version")
    private Integer version;
	
    @ManyToOne
    @JoinColumn(name = "user_info_id", referencedColumnName = "user_info_id", nullable = false)
    private UserInfo userInfoId;
	
    @ManyToOne
    @JoinColumn(name = "insurance_type_id", referencedColumnName = "insurance_id", nullable = false)
    private InsuranceType insuranceTypeId;
	
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

	@PersistenceContext
    transient EntityManager entityManager;

	public static final EntityManager entityManager() {
        EntityManager em = new AcceptedInsuranceTypes().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countAcceptedInsuranceTypeses() {
        return entityManager().createQuery("SELECT COUNT(o) FROM AcceptedInsuranceTypes o", Long.class).getSingleResult();
    }

	public static List<AcceptedInsuranceTypes> findAllAcceptedInsuranceTypeses() {
        return entityManager().createQuery("SELECT o FROM AcceptedInsuranceTypes o", AcceptedInsuranceTypes.class).getResultList();
    }

	public static AcceptedInsuranceTypes findAcceptedInsuranceTypes(Integer acceptedInsuranceID) {
        if (acceptedInsuranceID == null) return null;
        return entityManager().find(AcceptedInsuranceTypes.class, acceptedInsuranceID);
    }

	public static List<AcceptedInsuranceTypes> findAcceptedInsuranceTypesEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM AcceptedInsuranceTypes o", AcceptedInsuranceTypes.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	/**
	 * @see EntityManager#persist(Object)
	 */
	@Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

	/**
	 * @see EntityManager#remove(Object)
	 */
	@Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            AcceptedInsuranceTypes attached = AcceptedInsuranceTypes.findAcceptedInsuranceTypes(this.acceptedInsuranceID);
            this.entityManager.remove(attached);
        }
    }

	/**
	 * @see EntityManager#flush()
	 */
	@Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }

	/**
	 * @see EntityManager#clear()
	 */
	@Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }

	/**
	 * @see EntityManager#merge(Object)
	 */
	@Transactional
    public AcceptedInsuranceTypes merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        AcceptedInsuranceTypes merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public Integer getAcceptedInsuranceID() {
        return this.acceptedInsuranceID;
    }

	public void setAcceptedInsuranceID(Integer id) {
        this.acceptedInsuranceID = id;
    }

	public Integer getVersion() {
        return this.version;
    }

	public void setVersion(Integer version) {
        this.version = version;
    }

	public UserInfo getUserInfoId() {
		return userInfoId;
	}

	public void setUserInfoId(UserInfo userInfoId) {
		this.userInfoId = userInfoId;
	}

	public InsuranceType getInsuranceTypeId() {
		return insuranceTypeId;
	}

	public void setInsuranceTypeId(InsuranceType insuranceTypeId) {
		this.insuranceTypeId = insuranceTypeId;
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
}
