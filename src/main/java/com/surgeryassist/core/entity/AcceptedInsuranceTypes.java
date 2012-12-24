package com.surgeryassist.core.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(identifierColumn = "accepted_insurance_id", identifierField = "acceptedInsuranceID", identifierType = Integer.class, table = "accepted_insurance_types")
public class AcceptedInsuranceTypes {
	
	@JoinColumn(name = "user_info_id")
	@OneToMany(fetch = FetchType.LAZY)
	private List<UserInfo> userInfoIDs;
	
	@JoinColumn(name = "insurance_type_id")
	@OneToMany(fetch = FetchType.LAZY)
	private List<InsuranceType> insuranceTypeIDs;
	
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
}
