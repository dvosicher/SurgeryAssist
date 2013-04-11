package com.surgeryassist.core.entity;

import java.io.Serializable;
import java.util.ArrayList;
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

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

import com.surgeryassist.core.UserTypeCode;

@Entity
@Table(schema = "SurgeryAssist", name = "bookings")
@Configurable
public class Bookings implements Serializable {
	
	private static final long serialVersionUID = -5873948734947715261L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "booking_id")
    private Integer bookingId;
    
    @Version
    @Column(name = "version")
    private Integer version;
    
    @ManyToOne
    @JoinColumn(name = "booking_creator_id", referencedColumnName = "user_id", nullable = false)
    private ApplicationUser bookingCreatorId;
    
    @ManyToOne
    @JoinColumn(name = "booking_location_id", referencedColumnName = "user_id", nullable = false)
    private ApplicationUser bookingLocationId;
    
    @ManyToOne
    @JoinColumn(name = "patient_id", referencedColumnName = "patient_id", nullable = false)
    private Patient patientId;
    
    @Column(name = "booking_room")
    private String bookingRoom;
    
    @Column(name = "is_canceled")
    private Boolean isCanceled;
    
    @Column(name = "cancellation_reason", length = 2000)
    private String cancellationReason;
    
    @Column(name = "created_by")
    public Integer createdBy;
    
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "MM")
    public Calendar createdDate;
    
    @Column(name = "modified_by")
    public Integer modifiedBy;
    
    @Column(name = "modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "MM")
    public Calendar modifiedDate;
    
    @ManyToOne
    @JoinColumn(name = "time_availability_id", referencedColumnName = "time_availability_id", nullable = true)
    private TimeAvailabilities timeAvailabilityId;
    
    @Column(name = "is_confirmed")
    private Boolean isConfirmed;
        
    @PersistenceContext
    transient EntityManager entityManager;
    
    public static final EntityManager entityManager() {
        EntityManager em = new Bookings().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long countBookingses() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Bookings o", Long.class).getSingleResult();
    }
    
    public static List<Bookings> findAllBookingses() {
        return entityManager().createQuery("SELECT o FROM Bookings o", Bookings.class).getResultList();
    }
    
    public static Bookings findBookings(Integer bookingId) {
        if (bookingId == null) return null;
        return entityManager().find(Bookings.class, bookingId);
    }
    
    public static List<Bookings> findBookingsEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Bookings o", Bookings.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<Bookings> findAllBookingsByUser(ApplicationUser currentUser) {
    	List<Bookings> returnList = new ArrayList<Bookings>();
    	
    	returnList = entityManager()
    			.createQuery("SELECT o FROM Bookings o WHERE o.bookingCreatorId = :currentUser " +
    					"AND o.timeAvailabilityId.availabilityId.dateOfAvailability >= :currentDate " +
    					"AND o.isCanceled = false", Bookings.class)
    			.setParameter("currentUser", currentUser)
    			.setParameter("currentDate", Calendar.getInstance())
    			.getResultList();
    	
    	return returnList;
    }
    
    public static List<Bookings> findPendingBookingsByUser(ApplicationUser currentUser) {
    	//create the session and criteria for the query
		Session session = entityManager().unwrap(Session.class);
		Criteria criteria = session.createCriteria(Bookings.class);
		
		//join tables and set aliases
		criteria.setFetchMode("timeAvailabilityId", FetchMode.DEFAULT).createAlias("timeAvailabilityId", "taid");
		criteria.setFetchMode("taid.availabilityId", FetchMode.DEFAULT).createAlias("taid.availabilityId", "aid");
		
		//add restriction based on whether the user is a surgeon or ASC
		if(currentUser.getUserTypeCode().equals(UserTypeCode.SURGEON)) {
			criteria.add(Restrictions.eq("bookingCreatorId", currentUser));
		}
		else if(currentUser.getUserTypeCode().equals(UserTypeCode.ASC)) {
			criteria.add(Restrictions.eq("bookingLocationId", currentUser));
		}
		
		//add default restrictions
		criteria.add(Restrictions.eq("isConfirmed", Boolean.FALSE));
		criteria.add(Restrictions.eq("isCanceled", Boolean.FALSE));
		criteria.add(Restrictions.ge("aid.dateOfAvailability", Calendar.getInstance()));
		criteria.add(Restrictions.eq("taid.isCancelled", Boolean.FALSE));
		
		@SuppressWarnings("unchecked")
		List<Bookings> returnList = criteria.list();
		
		return returnList;
    }
    
    public static List<Bookings> findConfirmedBookingsByUser(ApplicationUser currentUser) {
    	//create the session and criteria for the query
		Session session = entityManager().unwrap(Session.class);
		Criteria criteria = session.createCriteria(Bookings.class);
		
		//join tables and set aliases
		criteria.setFetchMode("timeAvailabilityId", FetchMode.DEFAULT).createAlias("timeAvailabilityId", "taid");
		criteria.setFetchMode("taid.availabilityId", FetchMode.DEFAULT).createAlias("taid.availabilityId", "aid");
		
		//add restriction based on whether the user is a surgeon or ASC
		if(currentUser.getUserTypeCode().equals(UserTypeCode.SURGEON)) {
			criteria.add(Restrictions.eq("bookingCreatorId", currentUser));
		}
		else if(currentUser.getUserTypeCode().equals(UserTypeCode.ASC)) {
			criteria.add(Restrictions.eq("bookingLocationId", currentUser));
		}
		
		//add default restrictions
		criteria.add(Restrictions.eq("isConfirmed", Boolean.TRUE));
		criteria.add(Restrictions.eq("isCanceled", Boolean.FALSE));
		criteria.add(Restrictions.ge("aid.dateOfAvailability", Calendar.getInstance()));
		criteria.add(Restrictions.eq("taid.isCancelled", Boolean.FALSE));
		
		@SuppressWarnings("unchecked")
		List<Bookings> returnList = criteria.list();
		
		return returnList;
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
            Bookings attached = findBookings(this.bookingId);
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
    public Bookings merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Bookings merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public Integer getBookingId() {
        return this.bookingId;
    }
    
    public void setBookingId(Integer id) {
        this.bookingId = id;
    }
    
    public Integer getVersion() {
        return this.version;
    }
    
    public void setVersion(Integer version) {
        this.version = version;
    }
	
	public ApplicationUser getBookingCreatorId() {
		return bookingCreatorId;
	}

	public void setBookingCreatorId(ApplicationUser bookingCreatorId) {
		this.bookingCreatorId = bookingCreatorId;
	}

	public ApplicationUser getBookingLocationId() {
		return bookingLocationId;
	}

	public void setBookingLocationId(ApplicationUser bookingLocationId) {
		this.bookingLocationId = bookingLocationId;
	}

	public Patient getPatientId() {
		return patientId;
	}

	public void setPatientId(Patient patientId) {
		this.patientId = patientId;
	}

	public String getBookingRoom() {
		return bookingRoom;
	}

	public void setBookingRoom(String bookingRoom) {
		this.bookingRoom = bookingRoom;
	}

	public Boolean getIsCanceled() {
		return isCanceled;
	}

	public void setIsCanceled(Boolean isCanceled) {
		this.isCanceled = isCanceled;
	}

	public String getCancellationReason() {
		return cancellationReason;
	}

	public void setCancellationReason(String cancellationReason) {
		this.cancellationReason = cancellationReason;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Calendar getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Calendar createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Calendar getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Calendar modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public TimeAvailabilities getTimeAvailabilityId() {
		return timeAvailabilityId;
	}

	public void setTimeAvailabilityId(TimeAvailabilities timeAvailabilityId) {
		this.timeAvailabilityId = timeAvailabilityId;
	}

	public Boolean getIsConfirmed() {
		return isConfirmed;
	}

	public void setIsConfirmed(Boolean isConfirmed) {
		this.isConfirmed = isConfirmed;
	}

	@Override
	public boolean equals(Object obj) {
		Bookings bookingsObj = (Bookings) obj;
		if(this.bookingId.equals(bookingsObj.bookingId)) {
			return true;
		}
		return false;
	}
}
