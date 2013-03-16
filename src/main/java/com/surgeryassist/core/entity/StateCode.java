package com.surgeryassist.core.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.Id;
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

@Configurable
@Entity
@Table(schema = "MetaData", name = "state_code")
public class StateCode implements Serializable {

	private static final long serialVersionUID = -1882532832660713743L;

	@OneToMany(mappedBy = "stateCode", fetch = FetchType.LAZY)
	private Set<Location> locations;

	@Id
	@Column(name = "state_code", length = 2, updatable = false)
	private String stateCodeID;

	@Column(name = "state_name")
	private String stateName;

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

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public String getStateCodeID() {
		return this.stateCodeID;
	}

	public void setStateCodeID(String stateCodeID) {
		this.stateCodeID = stateCodeID;
	}

	public String getStateName() {
		return this.stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
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
	 * @return the locations
	 */
	public Set<Location> getLocations() {
		return locations;
	}

	/**
	 * @param locations the locations to set
	 */
	public void setLocations(Set<Location> locations) {
		this.locations = locations;
	}

	@PersistenceContext
	transient EntityManager entityManager;

	public static final EntityManager entityManager() {
		EntityManager em = new StateCode().entityManager;
		if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	public static long countStateCodes() {
		return entityManager().createQuery("SELECT COUNT(o) FROM StateCode o", Long.class).getSingleResult();
	}

	public static List<StateCode> findAllStateCodes() {
		return entityManager().createQuery("SELECT o FROM StateCode o", StateCode.class).getResultList();
	}

	public static StateCode findStateCode(String stateCodeID) {
		if (stateCodeID == null || stateCodeID.length() == 0) return null;
		return entityManager().find(StateCode.class, stateCodeID);
	}

	public static List<StateCode> findStateCodeEntries(int firstResult, int maxResults) {
		return entityManager().createQuery("SELECT o FROM StateCode o", StateCode.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
			StateCode attached = StateCode.findStateCode(this.stateCodeID);
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
	public StateCode merge() {
		if (this.entityManager == null) this.entityManager = entityManager();
		StateCode merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}

	@Version
	@Column(name = "version")
	private Integer version;

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof StateCode) {
			return ((StateCode) obj).getStateCodeID().equals(this.stateCodeID);
		}
		return false;
	}
}
