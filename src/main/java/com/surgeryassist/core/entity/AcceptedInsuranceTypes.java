package com.surgeryassist.core.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Table(name = "accepted_insurance_types")
@Configurable
public class AcceptedInsuranceTypes {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "accepted_insurance_id")
    private Integer acceptedInsuranceID;

	@Version
    @Column(name = "version")
    private Integer version;
	
	@JoinColumn(name = "user_info_id")
	@OneToMany(fetch = FetchType.LAZY)
	private List<UserInfo> userInfoIDs;
	
	@JoinColumn(name = "insurance_type_id")
	@OneToMany(fetch = FetchType.LAZY)
	private List<InsuranceType> insuranceTypeIDs;
	
	@Column(name = "created_by", updatable = false)
    private Integer createdBy;

    @Column(name = "created_date", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date createdDate;

    @Column(name = "modified_by")
    private Integer modifiedBy;

    @Column(name = "modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date modifiedDate;

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
            AcceptedInsuranceTypes attached = AcceptedInsuranceTypes.findAcceptedInsuranceTypes(this.acceptedInsuranceID);
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

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public List<UserInfo> getUserInfoIDs() {
        return this.userInfoIDs;
    }

	public void setUserInfoIDs(List<UserInfo> userInfoIDs) {
        this.userInfoIDs = userInfoIDs;
    }

	public List<InsuranceType> getInsuranceTypeIDs() {
        return this.insuranceTypeIDs;
    }

	public void setInsuranceTypeIDs(List<InsuranceType> insuranceTypeIDs) {
        this.insuranceTypeIDs = insuranceTypeIDs;
    }

	public Integer getCreatedBy() {
        return this.createdBy;
    }

	public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

	public Date getCreatedDate() {
        return this.createdDate;
    }

	public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

	public Integer getModifiedBy() {
        return this.modifiedBy;
    }

	public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

	public Date getModifiedDate() {
        return this.modifiedDate;
    }

	public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
