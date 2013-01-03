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

@Entity
@Table(schema = "SurgeryAssist", name = "bookings")
@Configurable
public class Bookings {
	
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
    private Integer createdBy;
    
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "MM")
    private Calendar createdDate;
    
    @Column(name = "modified_by")
    private Integer modifiedBy;
    
    @Column(name = "modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "MM")
    private Calendar modifiedDate;
    
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
            Bookings attached = findBookings(this.bookingId);
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
	
    /**
	 * @return the bookingCreatorId
	 */
	public ApplicationUser getBookingCreatorId() {
		return bookingCreatorId;
	}

	/**
	 * @param bookingCreatorId the bookingCreatorId to set
	 */
	public void setBookingCreatorId(ApplicationUser bookingCreatorId) {
		this.bookingCreatorId = bookingCreatorId;
	}

	/**
	 * @return the bookingLocationId
	 */
	public ApplicationUser getBookingLocationId() {
		return bookingLocationId;
	}

	/**
	 * @param bookingLocationId the bookingLocationId to set
	 */
	public void setBookingLocationId(ApplicationUser bookingLocationId) {
		this.bookingLocationId = bookingLocationId;
	}

	/**
	 * @return the patientId
	 */
	public Patient getPatientId() {
		return patientId;
	}

	/**
	 * @param patientId the patientId to set
	 */
	public void setPatientId(Patient patientId) {
		this.patientId = patientId;
	}

	/**
	 * @return the bookingRoom
	 */
	public Integer getBookingRoom() {
		return bookingRoom;
	}

	/**
	 * @param bookingRoom the bookingRoom to set
	 */
	public void setBookingRoom(Integer bookingRoom) {
		this.bookingRoom = bookingRoom;
	}

	/**
	 * @return the isCanceled
	 */
	public Boolean getIsCanceled() {
		return isCanceled;
	}

	/**
	 * @param isCanceled the isCanceled to set
	 */
	public void setIsCanceled(Boolean isCanceled) {
		this.isCanceled = isCanceled;
	}

	/**
	 * @return the cancellationReason
	 */
	public String getCancellationReason() {
		return cancellationReason;
	}

	/**
	 * @param cancellationReason the cancellationReason to set
	 */
	public void setCancellationReason(String cancellationReason) {
		this.cancellationReason = cancellationReason;
	}

	/**
	 * @return the createdBy
	 */
	public Integer getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the createdDate
	 */
	public Calendar getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Calendar createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the modifiedBy
	 */
	public Integer getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * @param modifiedBy the modifiedBy to set
	 */
	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/**
	 * @return the modifiedDate
	 */
	public Calendar getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * @param modifiedDate the modifiedDate to set
	 */
	public void setModifiedDate(Calendar modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	@Override
	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
