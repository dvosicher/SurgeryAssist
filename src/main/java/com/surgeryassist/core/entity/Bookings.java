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

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

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
    private Integer bookingRoom;
    
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
    			.createQuery("SELECT o FROM Bookings o WHERE o.bookingCreatorId = :currentUser ", Bookings.class)
    			.setParameter("currentUser", currentUser)
    			.getResultList();
    	
    	return returnList;
    }
    
    public static List<Bookings> findPendingBookingsByUser(ApplicationUser currentUser) {
    	List<Bookings> returnList = new ArrayList<Bookings>();
    	
    	returnList = entityManager()
    			.createQuery("SELECT o FROM Bookings o WHERE o.bookingCreatorId = :currentUser " +
    					"AND o.isConfirmed = false", Bookings.class)
    			.setParameter("currentUser", currentUser)
    			.getResultList();
    	
    	return returnList;
    }
    
    public static List<Bookings> findConfirmedBookingsByUser(ApplicationUser currentUser) {
    	List<Bookings> returnList = new ArrayList<Bookings>();
    	
    	returnList = entityManager()
    			.createQuery("SELECT o FROM Bookings o WHERE o.bookingCreatorId = :currentUser " +
    					"AND o.isConfirmed = true", Bookings.class)
    			.setParameter("currentUser", currentUser)
    			.getResultList();
    	
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

	public Integer getBookingRoom() {
		return bookingRoom;
	}

	public void setBookingRoom(Integer bookingRoom) {
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
}
