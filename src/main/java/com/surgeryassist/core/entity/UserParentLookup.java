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
@Table(schema = "SurgeryAssist", name = "user_parent_lookup")
public class UserParentLookup {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "upl_id")
    private Integer userParentLookupID;

	@Version
    @Column(name = "version")
    private Integer version;
	
	@ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private ApplicationUser userId;
    
    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "user_id", nullable = false)
    private ApplicationUser parentId;
	
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
        EntityManager em = new UserParentLookup().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countUserParentLookups() {
        return entityManager().createQuery("SELECT COUNT(o) FROM UserParentLookup o", Long.class).getSingleResult();
    }

	public static List<UserParentLookup> findAllUserParentLookups() {
        return entityManager().createQuery("SELECT o FROM UserParentLookup o", UserParentLookup.class).getResultList();
    }

	public static UserParentLookup findUserParentLookup(Integer userParentLookupID) {
        if (userParentLookupID == null) return null;
        return entityManager().find(UserParentLookup.class, userParentLookupID);
    }

	public static List<UserParentLookup> findUserParentLookupEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM UserParentLookup o", UserParentLookup.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            UserParentLookup attached = UserParentLookup.findUserParentLookup(this.userParentLookupID);
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
    public UserParentLookup merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        UserParentLookup merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public Integer getUserParentLookupID() {
        return this.userParentLookupID;
    }

	public void setUserParentLookupID(Integer id) {
        this.userParentLookupID = id;
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
	 * @return the parentId
	 */
	public ApplicationUser getParentId() {
		return parentId;
	}

	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(ApplicationUser parentId) {
		this.parentId = parentId;
	}

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
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
