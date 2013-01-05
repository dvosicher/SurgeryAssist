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
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
@Table(schema = "SurgeryAssist", name = "time_availabilities")
public class TimeAvailabilities {

	@ManyToOne
	@JoinColumn(name = "availability_id")
	private DayAvailability availabilityId;
	
    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Calendar startTime;

    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Calendar endTime;
    
    @Column(name = "created_by", updatable = false)
    private Integer createdBy;

    @Column(name = "created_date", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Calendar createdDate;

    @Column(name = "modified_by")
    private Integer modifiedBy;

    @Column(name = "modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Calendar modifiedDate;


	@PersistenceContext
    transient EntityManager entityManager;

	public static final EntityManager entityManager() {
        EntityManager em = new TimeAvailabilities().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countTimeAvailabilitieses() {
        return entityManager().createQuery("SELECT COUNT(o) FROM TimeAvailabilities o", Long.class).getSingleResult();
    }

	public static List<TimeAvailabilities> findAllTimeAvailabilitieses() {
        return entityManager().createQuery("SELECT o FROM TimeAvailabilities o", TimeAvailabilities.class).getResultList();
    }

	public static TimeAvailabilities findTimeAvailabilities(Integer timeAvailabilityID) {
        if (timeAvailabilityID == null) return null;
        return entityManager().find(TimeAvailabilities.class, timeAvailabilityID);
    }
	
	public static List<TimeAvailabilities> findTimeAvailabilitiesByDayAvailability(DayAvailability dayAvailabilityID) {
		if(dayAvailabilityID == null) {
			return null;
		}
		@SuppressWarnings("unchecked")
		List<TimeAvailabilities> returnObj = entityManager()
				.createQuery("SELECT o FROM TimeAvailabilities o WHERE o.availabilityId = :dayAvailabilityID")
				.setParameter("dayAvailabilityID", dayAvailabilityID)
				.getResultList();
		return returnObj;
	}

	public static List<TimeAvailabilities> findTimeAvailabilitiesEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM TimeAvailabilities o", TimeAvailabilities.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            TimeAvailabilities attached = TimeAvailabilities.findTimeAvailabilities(this.timeAvailabilityID);
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
    public TimeAvailabilities merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        TimeAvailabilities merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public DayAvailability getDayAvailability() {
        return this.availabilityId;
    }

	public void setDayAvailability(DayAvailability dayAvailability) {
        this.availabilityId = dayAvailability;
    }

	public Calendar getStartTime() {
        return this.startTime;
    }

	public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

	public Calendar getEndTime() {
        return this.endTime;
    }

	public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
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
    @Column(name = "time_availability_id")
    private Integer timeAvailabilityID;

	@Version
    @Column(name = "version")
    private Integer version;

	public Integer getTimeAvailabilityID() {
        return this.timeAvailabilityID;
    }

	public void setTimeAvailabilityID(Integer id) {
        this.timeAvailabilityID = id;
    }

	public Integer getVersion() {
        return this.version;
    }

	public void setVersion(Integer version) {
        this.version = version;
    }
}
