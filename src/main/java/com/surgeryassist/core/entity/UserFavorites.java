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
@Table(schema = "SurgeryAssist", name = "user_favorites")
@Configurable
public class UserFavorites {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "favorites_id")
    private Integer favoritesId;
    
    @Version
    @Column(name = "version")
    private Integer version;
    
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private ApplicationUser userId;
    
    @ManyToOne
    @JoinColumn(name = "user_favorite", referencedColumnName = "user_id", nullable = false)
    private ApplicationUser userFavorite;
    
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
        EntityManager em = new UserFavorites().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long countUserFavoriteses() {
        return entityManager().createQuery("SELECT COUNT(o) FROM UserFavorites o", Long.class).getSingleResult();
    }
    
    public static List<UserFavorites> findAllUserFavoriteses() {
        return entityManager().createQuery("SELECT o FROM UserFavorites o", UserFavorites.class).getResultList();
    }
    
    public static UserFavorites findUserFavorites(Integer favoritesId) {
        if (favoritesId == null) return null;
        return entityManager().find(UserFavorites.class, favoritesId);
    }
    
    public static List<UserFavorites> findUserFavoritesEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM UserFavorites o", UserFavorites.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            UserFavorites attached = UserFavorites.findUserFavorites(this.favoritesId);
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
    public UserFavorites merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        UserFavorites merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public Integer getFavoritesId() {
        return this.favoritesId;
    }
    
    public void setFavoritesId(Integer id) {
        this.favoritesId = id;
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
	 * @return the userFavorite
	 */
	public ApplicationUser getUserFavorite() {
		return userFavorite;
	}

	/**
	 * @param userFavorite the userFavorite to set
	 */
	public void setUserFavorite(ApplicationUser userFavorite) {
		this.userFavorite = userFavorite;
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
