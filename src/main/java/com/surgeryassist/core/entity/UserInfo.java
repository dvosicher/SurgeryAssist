package com.surgeryassist.core.entity;

import java.io.Serializable;
import java.util.Calendar;
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
@Table(schema = "SurgeryAssist", name = "user_info")
@Configurable
public class UserInfo implements Serializable {

	private static final long serialVersionUID = -4253239702030861956L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_info_id")
    private Integer userInfoID;

	@Version
    @Column(name = "version")
    private Integer version;

    @OneToMany(mappedBy = "userInfoId")
    private Set<AcceptedInsuranceTypes> acceptedInsuranceTypeses;
    
    @OneToMany(mappedBy = "userInfoId")
    private Set<ApplicationUser> applicationUsers;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "contact_info_id", referencedColumnName = "contact_info_id", nullable = false)
    private ContactInfo contactInfoId;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "location_id", referencedColumnName = "location_id", nullable = false)
    private Location locationId;
    
    @Column(name = "first_name", length = 100)
    private String firstName;
    
    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(name = "photograph_file_path")
    private String photoFilePath;

    @Column(name = "video_file_path")
    private String videoFilePath;

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
    
    @Column(name = "website_url")
    private String websiteUrl;

	public String getFirstName() {
        return this.firstName;
    }

	public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

	public String getLastName() {
        return this.lastName;
    }

	public void setLastName(String lastName) {
        this.lastName = lastName;
    }

	public String getPhotoFilePath() {
        return this.photoFilePath;
    }

	public void setPhotoFilePath(String photoFilePath) {
        this.photoFilePath = photoFilePath;
    }

	public String getVideoFilePath() {
        return this.videoFilePath;
    }

	public void setVideoFilePath(String videoFilePath) {
        this.videoFilePath = videoFilePath;
    }

	/**
	 * @return the acceptedInsuranceTypeses
	 */
	public Set<AcceptedInsuranceTypes> getAcceptedInsuranceTypeses() {
		return acceptedInsuranceTypeses;
	}

	/**
	 * @param acceptedInsuranceTypeses the acceptedInsuranceTypeses to set
	 */
	public void setAcceptedInsuranceTypeses(
			Set<AcceptedInsuranceTypes> acceptedInsuranceTypeses) {
		this.acceptedInsuranceTypeses = acceptedInsuranceTypeses;
	}

	/**
	 * @return the applicationUsers
	 */
	public Set<ApplicationUser> getApplicationUsers() {
		return applicationUsers;
	}

	/**
	 * @param applicationUsers the applicationUsers to set
	 */
	public void setApplicationUsers(Set<ApplicationUser> applicationUsers) {
		this.applicationUsers = applicationUsers;
	}

	/**
	 * @return the contactInfoId
	 */
	public ContactInfo getContactInfoId() {
		return contactInfoId;
	}

	/**
	 * @param contactInfoId the contactInfoId to set
	 */
	public void setContactInfoId(ContactInfo contactInfoId) {
		this.contactInfoId = contactInfoId;
	}

	/**
	 * @return the locationId
	 */
	public Location getLocationId() {
		return locationId;
	}

	/**
	 * @param locationId the locationId to set
	 */
	public void setLocationId(Location locationId) {
		this.locationId = locationId;
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

	@PersistenceContext
    transient EntityManager entityManager;

	public static final EntityManager entityManager() {
        EntityManager em = new UserInfo().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countUserInfoes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM UserInfo o", Long.class).getSingleResult();
    }

	public static List<UserInfo> findAllUserInfoes() {
        return entityManager().createQuery("SELECT o FROM UserInfo o", UserInfo.class).getResultList();
    }

	public static UserInfo findUserInfo(Integer userInfoID) {
        if (userInfoID == null) return null;
        return entityManager().find(UserInfo.class, userInfoID);
    }

	public static List<UserInfo> findUserInfoEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM UserInfo o", UserInfo.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
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
    public UserInfo merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        UserInfo merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public Integer getUserInfoID() {
        return this.userInfoID;
    }

	public void setUserInfoID(Integer id) {
        this.userInfoID = id;
    }

	public Integer getVersion() {
        return this.version;
    }

	public void setVersion(Integer version) {
        this.version = version;
    }

	/**
	 * @return the webisteUrl
	 */
	public String getWebsiteUrl() {
		return websiteUrl;
	}

	/**
	 * @param webisteUrl the webisteUrl to set
	 */
	public void setWebsiteUrl(String webisteUrl) {
		this.websiteUrl = webisteUrl;
	}
}
