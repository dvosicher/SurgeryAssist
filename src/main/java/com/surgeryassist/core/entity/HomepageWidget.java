package com.surgeryassist.core.entity;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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

@Entity
@Table(schema = "SurgeryAssist", name = "homepage_widget")
@Configurable
public class HomepageWidget {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "widget_id")
	private Integer widgetId;

	@Version
	@Column(name = "version")
	private Integer version;

	@OneToMany(mappedBy = "widgetId")
	private Set<HomepageSettings> homepageSettingss;

	@Column(name = "widget_name", length = 10)
	private String widgetName;

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

	public Integer getWidgetId() {
		return this.widgetId;
	}

	public void setWidgetId(Integer id) {
		this.widgetId = id;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@PersistenceContext
	transient EntityManager entityManager;

	public static final EntityManager entityManager() {
		EntityManager em = new HomepageWidget().entityManager;
		if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	public static long countHomepageWidgets() {
		return entityManager().createQuery("SELECT COUNT(o) FROM HomepageWidget o", Long.class).getSingleResult();
	}

	public static List<HomepageWidget> findAllHomepageWidgets() {
		return entityManager().createQuery("SELECT o FROM HomepageWidget o", HomepageWidget.class).getResultList();
	}

	public static HomepageWidget findHomepageWidget(Integer widgetId) {
		if (widgetId == null) return null;
		return entityManager().find(HomepageWidget.class, widgetId);
	}

	public static List<HomepageWidget> findHomepageWidgetEntries(int firstResult, int maxResults) {
		return entityManager().createQuery("SELECT o FROM HomepageWidget o", HomepageWidget.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
			HomepageWidget attached = HomepageWidget.findHomepageWidget(this.widgetId);
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
	public HomepageWidget merge() {
		if (this.entityManager == null) this.entityManager = entityManager();
		HomepageWidget merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}

	/**
	 * @return the homepageSettingss
	 */
	public Set<HomepageSettings> getHomepageSettingss() {
		return homepageSettingss;
	}

	/**
	 * @param homepageSettingss the homepageSettingss to set
	 */
	public void setHomepageSettingss(Set<HomepageSettings> homepageSettingss) {
		this.homepageSettingss = homepageSettingss;
	}

	/**
	 * @return the widgetName
	 */
	public String getWidgetName() {
		return widgetName;
	}

	/**
	 * @param widgetName the widgetName to set
	 */
	public void setWidgetName(String widgetName) {
		this.widgetName = widgetName;
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
