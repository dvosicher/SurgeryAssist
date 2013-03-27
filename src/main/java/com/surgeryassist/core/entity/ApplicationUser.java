package com.surgeryassist.core.entity;

import com.surgeryassist.core.UserTypeCode;
import com.surgeryassist.core.VerificationStatus;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Table(schema = "SurgeryAssist", name = "application_user")
@Configurable
public class ApplicationUser implements Serializable {

	private static final long serialVersionUID = -6314608411402226894L;

	@OneToMany(mappedBy = "userId", fetch = FetchType.LAZY)
	private Set<DayAvailability> availabilities;

	@OneToMany(mappedBy = "bookingCreatorId", fetch = FetchType.LAZY)
	private Set<Bookings> bookingss;

	@OneToMany(mappedBy = "bookingLocationId", fetch = FetchType.LAZY)
	private Set<Bookings> bookingss1;

	@OneToMany(mappedBy = "userId", fetch = FetchType.LAZY)
	private Set<Entitlement> entitlements;

	@OneToMany(mappedBy = "userId", fetch = FetchType.LAZY)
	private Set<HomepageSettings> homepageSettingss;

	@OneToMany(mappedBy = "userFavorite", fetch = FetchType.LAZY)
	private Set<UserFavorites> userFavoriteses;

	@OneToMany(mappedBy = "userId", fetch = FetchType.LAZY)
	private Set<UserFavorites> userFavoriteses1;

	@OneToMany(mappedBy = "parentId")
	private Set<UserParentLookup> userParentLookups;

	@OneToMany(mappedBy = "userId")
	private Set<UserParentLookup> userParentLookups1;

	@OneToMany(mappedBy = "userId", fetch = FetchType.EAGER)
	private Set<Authorities> authorities;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_info_id", referencedColumnName = "user_info_id", nullable = false)
	private UserInfo userInfoId;

	@Column(name = "user_email", length = 100)
	private String userEmail;

	@Column(name = "user_pass", length = 512)
	private String userPass;

	@Enumerated(EnumType.STRING)
	@Column(name = "user_type_code")
	private UserTypeCode userTypeCode;

	@Enumerated(EnumType.STRING)
	@Column(name = "is_verified")
	private VerificationStatus verificationStatus;

	@Column(name = "created_by", updatable = false)
	public Integer createdBy;

	@Column(name = "created_date", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "MM")
	public Calendar createdDate;

	@Column(name = "modified_by")
	public Integer modifiedBy;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "MM")
	public Calendar modifiedDate;

	@Column(name = "is_enabled", nullable = false)
	private Boolean isEnabled;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id")
	private Integer userID;

	@Version
	@Column(name = "version")
	private Integer version;

	public Integer getUserID() {
		return this.userID;
	}

	public void setUserID(Integer id) {
		this.userID = id;
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
		EntityManager em = new ApplicationUser().entityManager;
		if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	public static long countApplicationUsers() {
		return entityManager().createQuery("SELECT COUNT(o) FROM ApplicationUser o", Long.class).getSingleResult();
	}

	public static ApplicationUser findApplicationUser(Integer userID) {
		if (userID == null) return null;
		return entityManager().find(ApplicationUser.class, userID);
	}

	public static List<ApplicationUser> findApplicationUserEntries(int firstResult, int maxResults) {
		return entityManager().createQuery("SELECT o FROM ApplicationUser o", ApplicationUser.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
	}

	public static ApplicationUser findApplicationUserByEmailAddress(String emailAddress) {
		ApplicationUser returnObj = null;
		if(emailAddress != null) {
			try {
				returnObj = (ApplicationUser) entityManager()
						.createQuery("SELECT o FROM ApplicationUser o WHERE o.userEmail = :emailAddress")
						.setParameter("emailAddress", emailAddress)
						.getSingleResult();
			}
			catch(NonUniqueResultException e) {
				e.printStackTrace();
			}
			catch (EmptyResultDataAccessException e) {
				//do nothing since this is acceptable functionality
				System.err.println("No user found");
			}
			catch(NoResultException e) {
				//do nothing since this is acceptable functionality
				System.err.println("No user found");
			}
		}
		return returnObj; 
	}
	
	public static ApplicationUser findValidApplicationUserByEmail(String emailAddress) {
		ApplicationUser returnObj = null;
		if(emailAddress != null) {
			try {
				returnObj = (ApplicationUser) entityManager()
						.createQuery("SELECT o FROM ApplicationUser o WHERE o.userEmail = :emailAddress " +
								"AND o.verificationStatus = 'VERIFIED'")
						.setParameter("emailAddress", emailAddress)
						.getSingleResult();
			}
			catch(NonUniqueResultException e) {
				e.printStackTrace();
				
			}
		}
		return returnObj; 
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
	public ApplicationUser merge() {
		if (this.entityManager == null) this.entityManager = entityManager();
		ApplicationUser merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}

	public String getUserEmail() {
		return this.userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public UserTypeCode getUserTypeCode() {
		return this.userTypeCode;
	}

	public void setUserTypeCode(UserTypeCode userTypeCode) {
		this.userTypeCode = userTypeCode;
	}

	public VerificationStatus getVerificationStatus() {
		return this.verificationStatus;
	}

	public void setVerificationStatus(VerificationStatus verificationStatus) {
		this.verificationStatus = verificationStatus;
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

	/**
	 * @return the userParentLookups
	 */
	 public Set<UserParentLookup> getUserParentLookups() {
		return userParentLookups;
	}

	/**
	 * @param userParentLookups the userParentLookups to set
	 */
	 public void setUserParentLookups(Set<UserParentLookup> userParentLookups) {
		 this.userParentLookups = userParentLookups;
	 }

	 /**
	  * @return the userParentLookups1
	  */
	 public Set<UserParentLookup> getUserParentLookups1() {
		 return userParentLookups1;
	 }

	 /**
	  * @param userParentLookups1 the userParentLookups1 to set
	  */
	 public void setUserParentLookups1(Set<UserParentLookup> userParentLookups1) {
		 this.userParentLookups1 = userParentLookups1;
	 }

	 /**
	  * @return the userInfoId
	  */
	 public UserInfo getUserInfoId() {
		 return userInfoId;
	 }

	 /**
	  * @param userInfoId the userInfoId to set
	  */
	 public void setUserInfoId(UserInfo userInfoId) {
		 this.userInfoId = userInfoId;
	 }

	 /**
	  * @return the userPass
	  */
	 public String getUserPass() {
		 return userPass;
	 }

	 /**
	  * @param userPass the userPass to set
	  */
	 public void setUserPass(String userPass) {
		 this.userPass = userPass;
	 }

	 /**
	  * @return the availabilities
	  */
	 public Set<DayAvailability> getAvailabilities() {
		 return availabilities;
	 }

	 /**
	  * @param availabilities the availabilities to set
	  */
	 public void setAvailabilities(Set<DayAvailability> availabilities) {
		 this.availabilities = availabilities;
	 }

	 /**
	  * @return the bookingss
	  */
	 public Set<Bookings> getBookingss() {
		 return bookingss;
	 }

	 /**
	  * @param bookingss the bookingss to set
	  */
	 public void setBookingss(Set<Bookings> bookingss) {
		 this.bookingss = bookingss;
	 }

	 /**
	  * @return the bookingss1
	  */
	 public Set<Bookings> getBookingss1() {
		 return bookingss1;
	 }

	 /**
	  * @param bookingss1 the bookingss1 to set
	  */
	 public void setBookingss1(Set<Bookings> bookingss1) {
		 this.bookingss1 = bookingss1;
	 }

	 /**
	  * @return the entitlements
	  */
	 public Set<Entitlement> getEntitlements() {
		 return entitlements;
	 }

	 /**
	  * @param entitlements the entitlements to set
	  */
	 public void setEntitlements(Set<Entitlement> entitlements) {
		 this.entitlements = entitlements;
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
	  * @return the userFavoriteses
	  */
	 public Set<UserFavorites> getUserFavoriteses() {
		 return userFavoriteses;
	 }

	 /**
	  * @param userFavoriteses the userFavoriteses to set
	  */
	 public void setUserFavoriteses(Set<UserFavorites> userFavoriteses) {
		 this.userFavoriteses = userFavoriteses;
	 }

	 /**
	  * @return the userFavoriteses1
	  */
	 public Set<UserFavorites> getUserFavoriteses1() {
		 return userFavoriteses1;
	 }

	 /**
	  * @param userFavoriteses1 the userFavoriteses1 to set
	  */
	 public void setUserFavoriteses1(Set<UserFavorites> userFavoriteses1) {
		 this.userFavoriteses1 = userFavoriteses1;
	 }

	 /**
	  * @return the isEnabled
	  */
	 public Boolean getIsEnabled() {
		 return isEnabled;
	 }

	 /**
	  * @param isEnabled the isEnabled to set
	  */
	 public void setIsEnabled(Boolean isEnabled) {
		 this.isEnabled = isEnabled;
	 }

	 /**
	  * @return the authorities
	  */
	 public Set<Authorities> getAuthorities() {
		 return authorities;
	 }

	 /**
	  * @param authorities the authorities to set
	  */
	 public void setAuthorities(Set<Authorities> authorities) {
		 this.authorities = authorities;
	 }

}
