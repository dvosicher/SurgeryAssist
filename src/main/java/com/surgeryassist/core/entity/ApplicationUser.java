package com.surgeryassist.core.entity;

import com.surgeryassist.core.UserTypeCode;
import com.surgeryassist.core.VerificationStatus;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Table(schema = "SurgeryAssist", name = "application_user")
@Configurable
public class ApplicationUser {
	
    @NotNull
    @Column(name = "user_email")
    private String userEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type_code")
    private UserTypeCode userTypeCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "is_verified")
    private VerificationStatus verificationStatus;

    @Column(name = "created_by", updatable = false)
    private Integer createdBy;

    @Column(name = "created_date", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date createdDate;

    @Column(name = "modified_by")
    private Integer modifiedBy;

    @Column(name = "modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date modifiedDate;

    @OneToOne
    @JoinColumn(name = "user_info_id")
    private UserInfo userInfoID;

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

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
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

	public static List<ApplicationUser> findAllApplicationUsers() {
        return entityManager().createQuery("SELECT o FROM ApplicationUser o", ApplicationUser.class).getResultList();
    }

	public static ApplicationUser findApplicationUser(Integer userID) {
        if (userID == null) return null;
        return entityManager().find(ApplicationUser.class, userID);
    }

	public static List<ApplicationUser> findApplicationUserEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ApplicationUser o", ApplicationUser.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            ApplicationUser attached = ApplicationUser.findApplicationUser(this.userID);
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

	public Date getCreatedDate() {
        return this.createdDate;
    }

	public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

	public Integer getModifiedBy() {
        return this.modifiedBy;
    }

	public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

	public Date getModifiedDate() {
        return this.modifiedDate;
    }

	public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

	public UserInfo getUserInfoID() {
        return this.userInfoID;
    }

	public void setUserInfoID(UserInfo userInfoID) {
        this.userInfoID = userInfoID;
    }
}
