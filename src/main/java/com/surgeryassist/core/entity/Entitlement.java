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
@Table(schema = "SurgeryAssist", name = "entitlement")
@Configurable
public class Entitlement {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "entitlement_id")
    private Integer entitlementId;
    
    @Version
    @Column(name = "version")
    private Integer version;
    
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private ApplicationUser userId;
    
    @Column(name = "entitlement_start_date")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "MM")
    private Calendar entitlementStartDate;
    
    @Column(name = "entitlement_end_date")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "MM")
    private Calendar entitlementEndDate;
    
    @Column(name = "entitlement_renewal_count")
    private Integer entitlementRenewalCount;
    
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
    
    @PersistenceContext
    transient EntityManager entityManager;
    
    public static final EntityManager entityManager() {
        EntityManager em = new Entitlement().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long countEntitlements() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Entitlement o", Long.class).getSingleResult();
    }
    
    public static List<Entitlement> findAllEntitlements() {
        return entityManager().createQuery("SELECT o FROM Entitlement o", Entitlement.class).getResultList();
    }
    
    public static Entitlement findEntitlement(Integer entitlementId) {
        if (entitlementId == null) return null;
        return entityManager().find(Entitlement.class, entitlementId);
    }
    
    public static List<Entitlement>findEntitlementEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Entitlement o", Entitlement.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Entitlement attached = Entitlement.findEntitlement(this.entitlementId);
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
    public Entitlement merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Entitlement merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public Integer getEntitlementId() {
        return this.entitlementId;
    }
    
    public void setEntitlementId(Integer id) {
        this.entitlementId = id;
    }
    
    public Integer getVersion() {
        return this.version;
    }
    
    public void setVersion(Integer version) {
        this.version = version;
    }
    
	/**
	 * @return the userId
	 */
	public ApplicationUser getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(ApplicationUser userId) {
		this.userId = userId;
	}

	/**
	 * @return the entitlementStartDate
	 */
	public Calendar getEntitlementStartDate() {
		return entitlementStartDate;
	}

	/**
	 * @param entitlementStartDate the entitlementStartDate to set
	 */
	public void setEntitlementStartDate(Calendar entitlementStartDate) {
		this.entitlementStartDate = entitlementStartDate;
	}

	/**
	 * @return the entitlementEndDate
	 */
	public Calendar getEntitlementEndDate() {
		return entitlementEndDate;
	}

	/**
	 * @param entitlementEndDate the entitlementEndDate to set
	 */
	public void setEntitlementEndDate(Calendar entitlementEndDate) {
		this.entitlementEndDate = entitlementEndDate;
	}

	/**
	 * @return the entitlementRenewalCount
	 */
	public Integer getEntitlementRenewalCount() {
		return entitlementRenewalCount;
	}

	/**
	 * @param entitlementRenewalCount the entitlementRenewalCount to set
	 */
	public void setEntitlementRenewalCount(Integer entitlementRenewalCount) {
		this.entitlementRenewalCount = entitlementRenewalCount;
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

	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
