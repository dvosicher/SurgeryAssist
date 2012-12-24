// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.surgeryassist.core.entity;

import com.surgeryassist.core.entity.AcceptedInsuranceTypes;
import com.surgeryassist.core.entity.InsuranceType;
import com.surgeryassist.core.entity.UserInfo;
import java.util.Date;
import java.util.List;

privileged aspect AcceptedInsuranceTypes_Roo_JavaBean {
    
    public List<UserInfo> AcceptedInsuranceTypes.getUserInfoIDs() {
        return this.userInfoIDs;
    }
    
    public void AcceptedInsuranceTypes.setUserInfoIDs(List<UserInfo> userInfoIDs) {
        this.userInfoIDs = userInfoIDs;
    }
    
    public List<InsuranceType> AcceptedInsuranceTypes.getInsuranceTypeIDs() {
        return this.insuranceTypeIDs;
    }
    
    public void AcceptedInsuranceTypes.setInsuranceTypeIDs(List<InsuranceType> insuranceTypeIDs) {
        this.insuranceTypeIDs = insuranceTypeIDs;
    }
    
    public Integer AcceptedInsuranceTypes.getCreatedBy() {
        return this.createdBy;
    }
    
    public void AcceptedInsuranceTypes.setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }
    
    public Date AcceptedInsuranceTypes.getCreatedDate() {
        return this.createdDate;
    }
    
    public void AcceptedInsuranceTypes.setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    
    public Integer AcceptedInsuranceTypes.getModifiedBy() {
        return this.modifiedBy;
    }
    
    public void AcceptedInsuranceTypes.setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
    
    public Date AcceptedInsuranceTypes.getModifiedDate() {
        return this.modifiedDate;
    }
    
    public void AcceptedInsuranceTypes.setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
    
}
