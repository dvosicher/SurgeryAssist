package com.surgeryassist.core.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(schema = "SurgeryAssist", name = "location")
public class Location implements Serializable {

	private static final long serialVersionUID = -1731798035243878275L;

	@OneToMany(mappedBy = "locationId", fetch = FetchType.LAZY)
    private Set<UserInfo> userInfoes;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "state_code", referencedColumnName = "state_code", nullable = false)
    private StateCode stateCode;
    
    @Column(name = "address", length = 500)
    private String address;
    
    @Column(name = "city", length = 250)
    private String city;
    
    @Column(name = "zip_code")
    private String zipCode;
    
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

	public String getAddress() {
        return this.address;
    }

	public void setAddress(String address) {
        this.address = address;
    }

	public String getCity() {
        return this.city;
    }

	public void setCity(String city) {
        this.city = city;
    }

	public String getZipCode() {
        return this.zipCode;
    }

	public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
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

	/**
	 * @return the userInfoes
	 */
	public Set<UserInfo> getUserInfoes() {
		return userInfoes;
	}

	/**
	 * @param userInfoes the userInfoes to set
	 */
	public void setUserInfoes(Set<UserInfo> userInfoes) {
		this.userInfoes = userInfoes;
	}

	/**
	 * @return the stateCode
	 */
	public StateCode getStateCode() {
		return stateCode;
	}

	/**
	 * @param stateCode the stateCode to set
	 */
	public void setStateCode(StateCode stateCode) {
		this.stateCode = stateCode;
	}

	@PersistenceContext
    transient EntityManager entityManager;

	public static final EntityManager entityManager() {
        EntityManager em = new Location().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countLocations() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Location o", Long.class).getSingleResult();
    }

	public static List<Location> findAllLocations() {
        return entityManager().createQuery("SELECT o FROM Location o", Location.class).getResultList();
    }

	public static Location findLocation(Integer locationID) {
        if (locationID == null) return null;
        return entityManager().find(Location.class, locationID);
    }

	public static List<Location> findLocationEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Location o", Location.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
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
    public Location merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Location merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "location_id")
    private Integer locationID;

	@Version
    @Column(name = "version")
    private Integer version;

	public Integer getLocationID() {
        return this.locationID;
    }

	public void setLocationID(Integer id) {
        this.locationID = id;
    }

	public Integer getVersion() {
        return this.version;
    }

	public void setVersion(Integer version) {
        this.version = version;
    }
}
