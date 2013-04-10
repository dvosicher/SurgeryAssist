package com.surgeryassist.core.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

import com.surgeryassist.util.SurgeryAssistUtil;

@Configurable
@Entity
@Table(schema = "SurgeryAssist", name = "time_availabilities")
public class TimeAvailabilities implements Serializable {

	private static final long serialVersionUID = -8110342378591294302L;

	@OneToMany(mappedBy = "timeAvailabilityId", fetch = FetchType.LAZY)
	private Set<Bookings> bookings; 
	
	@ManyToOne(fetch = FetchType.EAGER)
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
    
    @Column(name = "is_booked")
    private Boolean isBooked;

    @Column(name = "is_cancelled")
    private Boolean isCancelled;
    
    @Column(name = "room_number")
    private String roomNumber;
    
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
        return entityManager().createQuery("SELECT o FROM TimeAvailabilities o " +
        		"INNER JOIN FETCH o.availabilityId aid " +
        		"INNER JOIN FETCH aid.userId uid " +
        		"INNER JOIN FETCH uid.userInfoId uiid " +
        		"INNER JOIN FETCH uiid.locationId lid " +
        		"WHERE o.isBooked = 0 AND o.isCancelled = 0", TimeAvailabilities.class).getResultList();
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
	
	public static List<TimeAvailabilities> findTimeAvailabilitiesBySearchCriteria(String city,
			Integer zipCode, Date endDate, Integer timeDuration, Date startDate) {
		
		//create the session and criteria for the query
		Session session = entityManager().unwrap(Session.class);
		Criteria criteria = session.createCriteria(TimeAvailabilities.class);
		
		//join all the appropriate tables
		criteria.setFetchMode("availabilityId", FetchMode.DEFAULT).createAlias("availabilityId", "aid");
		criteria.setFetchMode("aid.userId", FetchMode.DEFAULT).createAlias("aid.userId", "uid");
		criteria.setFetchMode("uid.userInfoId", FetchMode.DEFAULT).createAlias("uid.userInfoId", "uiid");
		criteria.setFetchMode("uiid.locationId", FetchMode.DEFAULT).createAlias("uiid.locationId", "lid");
		
		Calendar calEndDate = SurgeryAssistUtil.convertDateToCalendar(endDate);
		Calendar calStartDate = SurgeryAssistUtil.convertDateToCalendar(startDate);
		
		//make sure they're not booked and that the date is after today
		criteria.add(Restrictions.eq("isBooked", Boolean.FALSE));
		criteria.add(Restrictions.eq("isCancelled", Boolean.FALSE));
		
		//add the city
		if(city != null && !StringUtils.isEmpty(city)) {
			criteria.add(Restrictions.ilike(
				"lid.city", city, MatchMode.ANYWHERE));
		}
		//add zip code
		if(zipCode != null) {
			criteria.add(Restrictions.ilike(
				"lid.zipCode", zipCode.toString(), MatchMode.ANYWHERE));
		}
		//check the start/end dates
		if(endDate != null) {
			if(startDate != null) {
				criteria.add(Restrictions
					.between("aid.dateOfAvailability", calStartDate, calEndDate));
			}
			else {
				criteria.add(Restrictions
						.between("aid.dateOfAvailability", Calendar.getInstance(), calEndDate));
			}
		}
		//if the end date doesn't exist, force the end date to include >= GETDATE()
		else {
			criteria.add(Restrictions.ge("aid.dateOfAvailability", Calendar.getInstance()));
		}
		if(timeDuration != null) {
			criteria.add(
					Restrictions.sqlRestriction(
							"DATEDIFF(HH, {alias}.start_time, {alias}.end_time) > ?", timeDuration, StandardBasicTypes.INTEGER));
		}
		
		@SuppressWarnings("unchecked")
		List<TimeAvailabilities> result = criteria.list();
		return result;
	}

	@SuppressWarnings("unchecked")
	public static List<TimeAvailabilities> findUnbookedAndNotCancelledTimeAvailabilitiesByASCUser(ApplicationUser ascUser) {
		if(ascUser != null) {
			Query query = entityManager().createQuery("SELECT o FROM TimeAvailabilities o " +
					"INNER JOIN FETCH o.availabilityId aid " +
					"WHERE aid.userId = :userId AND aid.dateOfAvailability > :todaysDate " +
					"AND o.isBooked = 0 and o.isCancelled = 0", TimeAvailabilities.class)
					.setParameter("userId", ascUser)
					.setParameter("todaysDate", Calendar.getInstance());
			List<TimeAvailabilities> result = query.getResultList();
			return result;
		}
		return new ArrayList<TimeAvailabilities>();
	}
	
	@SuppressWarnings("unchecked")
	public static List<TimeAvailabilities> findNotCancelledTimeAvailabilitiesByASCUser(ApplicationUser ascUser) {
		if(ascUser != null) {
			Query query = entityManager().createQuery("SELECT o FROM TimeAvailabilities o " +
					"INNER JOIN FETCH o.availabilityId aid " +
					"WHERE aid.userId = :userId AND " +
					"o.isCancelled = 0", TimeAvailabilities.class)
					.setParameter("userId", ascUser);
			List<TimeAvailabilities> result = query.getResultList();
			return result;
		}
		return new ArrayList<TimeAvailabilities>();
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

	public DayAvailability getAvailabilityId() {
		return availabilityId;
	}
	
	public void setAvailabilityId(DayAvailability availabilityId) {
		this.availabilityId = availabilityId;
	}

	public Set<Bookings> getBookings() {
		return bookings;
	}

	public void setBookings(Set<Bookings> bookings) {
		this.bookings = bookings;
	}

	public Boolean getIsBooked() {
		return isBooked;
	}

	public void setIsBooked(Boolean isBooked) {
		this.isBooked = isBooked;
	}

	public Boolean getIsCancelled() {
		return isCancelled;
	}

	public void setIsCancelled(Boolean isCancelled) {
		this.isCancelled = isCancelled;
	}
	
	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

	@Override
	public boolean equals(Object obj) {
		if(this.timeAvailabilityID.equals(((TimeAvailabilities) obj).timeAvailabilityID)) {
			return true;
		}
		return false;
	}
}
