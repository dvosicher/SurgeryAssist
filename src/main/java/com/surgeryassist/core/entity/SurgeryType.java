package com.surgeryassist.core.entity;

import java.io.Serializable;
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

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Table(schema = "MetaData", name = "surgery_type")
@Configurable
public class SurgeryType implements Serializable {
	
	private static final long serialVersionUID = -2431279427455792053L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "surgery_id")
    private Integer surgeryId;
    
    @Version
    @Column(name = "version")
    private Integer version;
    
    public Integer getSurgeryId() {
        return this.surgeryId;
    }
    
    public void setSurgeryId(Integer id) {
        this.surgeryId = id;
    }
    
    public Integer getVersion() {
        return this.version;
    }
    
    public void setVersion(Integer version) {
        this.version = version;
    }
    
    @OneToMany(mappedBy = "surgeryTypeCode")
    private Set<Patient> patients;
    
    @Column(name = "surgery_type_code", length = 10)
    private String surgeryTypeCode;
    
    @Column(name = "surgery_full_name", length = 100)
    private String surgeryFullName;
    
    @Column(name = "surgery_description", length = 512)
    private String surgeryDescription;
    
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
        EntityManager em = new SurgeryType().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long countSurgeryTypes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM SurgeryType o", Long.class).getSingleResult();
    }
    
    public static List<SurgeryType> findAllSurgeryTypes() {
        return entityManager().createQuery("SELECT o FROM SurgeryType o", SurgeryType.class).getResultList();
    }
    
    public static SurgeryType findSurgeryType(Integer surgeryId) {
        if (surgeryId == null) return null;
        return entityManager().find(SurgeryType.class, surgeryId);
    }
    
    public static List<SurgeryType> findSurgeryTypeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM SurgeryType o", SurgeryType.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            SurgeryType attached = SurgeryType.findSurgeryType(this.surgeryId);
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
    public SurgeryType merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        SurgeryType merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	/**
	 * @return the patients
	 */
	public Set<Patient> getPatients() {
		return patients;
	}

	/**
	 * @param patients the patients to set
	 */
	public void setPatients(Set<Patient> patients) {
		this.patients = patients;
	}

	/**
	 * @return the surgeryTypeCode
	 */
	public String getSurgeryTypeCode() {
		return surgeryTypeCode;
	}

	/**
	 * @param surgeryTypeCode the surgeryTypeCode to set
	 */
	public void setSurgeryTypeCode(String surgeryTypeCode) {
		this.surgeryTypeCode = surgeryTypeCode;
	}

	/**
	 * @return the surgeryFullName
	 */
	public String getSurgeryFullName() {
		return surgeryFullName;
	}

	/**
	 * @param surgeryFullName the surgeryFullName to set
	 */
	public void setSurgeryFullName(String surgeryFullName) {
		this.surgeryFullName = surgeryFullName;
	}

	/**
	 * @return the surgeryDescription
	 */
	public String getSurgeryDescription() {
		return surgeryDescription;
	}

	/**
	 * @param surgeryDescription the surgeryDescription to set
	 */
	public void setSurgeryDescription(String surgeryDescription) {
		this.surgeryDescription = surgeryDescription;
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
	public boolean equals(Object obj) {
		if(obj instanceof SurgeryType) {
			if(((SurgeryType) obj).surgeryId.equals(this.surgeryId)) {
				return true;
			}
		}
		return false;
	}
}
