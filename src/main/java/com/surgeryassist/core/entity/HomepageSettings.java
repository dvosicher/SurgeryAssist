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
@Table(schema = "SurgeryAssist", name = "homepage_settings")
@Configurable
public class HomepageSettings {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "homepage_settings_id")
    private Integer homepageSettingsId;
    
    @Version
    @Column(name = "version")
    private Integer version;
    
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private ApplicationUser userId;
    
    @ManyToOne
    @JoinColumn(name = "widget_id", referencedColumnName = "widget_id", nullable = false)
    private HomepageWidget widgetId;
    
    @Column(name = "show_widget")
    private Boolean showWidget;
    
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
        EntityManager em = new HomepageSettings().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long countHomepageSettingses() {
        return entityManager().createQuery("SELECT COUNT(o) FROM HomepageSettings o", Long.class).getSingleResult();
    }
    
    public static List<HomepageSettings> findAllHomepageSettingses() {
        return entityManager().createQuery("SELECT o FROM HomepageSettings o", HomepageSettings.class).getResultList();
    }
    
    public static HomepageSettings findHomepageSettings(Integer homepageSettingsId) {
        if (homepageSettingsId == null) return null;
        return entityManager().find(HomepageSettings.class, homepageSettingsId);
    }
    
    public static List<HomepageSettings> findHomepageSettingsEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM HomepageSettings o", HomepageSettings.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<HomepageSettings> findHomepageSettingsByHomepageWidget(HomepageWidget widget) {
    	if(widget == null) {
    		return null;
    	}
    	@SuppressWarnings("unchecked")
		List<HomepageSettings> returnObj = entityManager()
    			.createQuery("SELECT o FROM HomepageSettings o WHERE o.widgetId = :widgetId")
    			.setParameter("widgetId", widget)
    			.getResultList();
    	return returnObj;
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
            HomepageSettings attached = HomepageSettings.findHomepageSettings(this.homepageSettingsId);
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
    public HomepageSettings merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        HomepageSettings merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public Integer getHomepageSettingsId() {
        return this.homepageSettingsId;
    }
    
    public void setHomepageSettingsId(Integer id) {
        this.homepageSettingsId = id;
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
	 * @return the widgetId
	 */
	public HomepageWidget getWidgetId() {
		return widgetId;
	}

	/**
	 * @param widgetId the widgetId to set
	 */
	public void setWidgetId(HomepageWidget widgetId) {
		this.widgetId = widgetId;
	}

	/**
	 * @return the showWidget
	 */
	public Boolean getShowWidget() {
		return showWidget;
	}

	/**
	 * @param showWidget the showWidget to set
	 */
	public void setShowWidget(Boolean showWidget) {
		this.showWidget = showWidget;
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
