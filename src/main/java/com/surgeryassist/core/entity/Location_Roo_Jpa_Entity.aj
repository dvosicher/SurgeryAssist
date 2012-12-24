// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.surgeryassist.core.entity;

import com.surgeryassist.core.entity.Location;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

privileged aspect Location_Roo_Jpa_Entity {
    
    declare @type: Location: @Entity;
    
    declare @type: Location: @Table(name = "location");
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "location_id")
    private Integer Location.locationID;
    
    @Version
    @Column(name = "version")
    private Integer Location.version;
    
    public Integer Location.getLocationID() {
        return this.locationID;
    }
    
    public void Location.setLocationID(Integer id) {
        this.locationID = id;
    }
    
    public Integer Location.getVersion() {
        return this.version;
    }
    
    public void Location.setVersion(Integer version) {
        this.version = version;
    }
    
}
