package com.surgeryassist.core.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(identifierColumn = "insurance_id", identifierField = "insuranceID", identifierType = Integer.class, table = "insurance_type")
public class InsuranceType {
	
	@Column(name = "insurance_code", length = 10)
	private String insuranceCode;
	
	@Column(name = "insurance_name")
	private String insuranceName;
	
	@Column(name = "insurance_description")
	private String insuranceDescription;
	
	@Column(name = "insurance_policy_number")
	private String insurancePolicyNumber;
	
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
