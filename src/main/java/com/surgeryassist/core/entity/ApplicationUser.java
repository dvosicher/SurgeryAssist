package com.surgeryassist.core.entity;

import com.surgeryassist.core.UserTypeCode;
import com.surgeryassist.core.VerificationStatus;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(identifierColumn = "user_id", identifierField = "userID", identifierType = Integer.class, table = "user")
public class ApplicationUser {
	
    @NotNull
    @Column(name = "user_email")
    private String userEmail;

    @Enumerated
    @Column(name = "user_type_code")
    private UserTypeCode userTypeCode;

    @Enumerated
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

}
