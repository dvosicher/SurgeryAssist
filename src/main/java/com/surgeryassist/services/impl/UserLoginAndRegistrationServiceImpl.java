package com.surgeryassist.services.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.surgeryassist.core.UserTypeCode;
import com.surgeryassist.core.VerificationStatus;
import com.surgeryassist.core.entity.ApplicationUser;
import com.surgeryassist.core.entity.Authorities;
import com.surgeryassist.core.entity.StateCode;
import com.surgeryassist.services.interfaces.UserLoginAndRegistrationService;

/**
 * Implementation of custom auth handler
 * 
 * @author Ankit Tyagi
 *
 */
@Service("userLoginAndRegistrationService")
public class UserLoginAndRegistrationServiceImpl implements UserLoginAndRegistrationService {

	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	SaltSource saltSource;
	
	public static final Integer APPLICATION_IDENTIFIER = new Integer(1);
	
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		UserDetails springSecurityUser = null;
		ApplicationUser databaseUser = ApplicationUser.findApplicationUserByEmailAddress(username);
		if(databaseUser == null) {
			throw new UsernameNotFoundException("User not found");
		}
		
		springSecurityUser = convertApplicationUserToSpringUser(databaseUser);
		
		return springSecurityUser;
	}

	@Override
	public UserDetails convertApplicationUserToSpringUser(ApplicationUser applicationUser) {
		
		//create collection of authorities
		Collection<GrantedAuthority> springAuthorities = new ArrayList<GrantedAuthority>();
		
		//add to spring based collection
		for(Authorities auth : applicationUser.getAuthorities()) {
			springAuthorities.add(auth);
		}
		
		//create the user to return
		User springUser = new User(applicationUser.getUserEmail(), 
				applicationUser.getUserPass(), 
				applicationUser.getIsEnabled(),
				applicationUser.getIsEnabled(),
				applicationUser.getIsEnabled(),
				applicationUser.getIsEnabled(),
				springAuthorities);

		return springUser;
	}

	@Override
	public void registerUser(ApplicationUser applicationUser) {
		//set defaults
		applicationUser = this.createDefaultApplicationUser(applicationUser);
		
		//salt it, might be ugly/not work...
		Object salt = saltSource.getSalt(
				new User(applicationUser.getUserEmail(), 
						applicationUser.getUserPass(), 
						new ArrayList<GrantedAuthority>()));
		
		//TODO: test this with new salting
		String encodedPass = passwordEncoder.encodePassword(applicationUser.getUserPass(), salt);
		applicationUser.setUserPass(encodedPass);
		
		//save to the database
		applicationUser.persist();
		applicationUser.flush();
		
	}
	
	@Override
	public ApplicationUser createDefaultApplicationUser(
			ApplicationUser newApplicationUser) {
		
		newApplicationUser.setCreatedBy(APPLICATION_IDENTIFIER);
		newApplicationUser.setModifiedBy(APPLICATION_IDENTIFIER);
		newApplicationUser.setIsEnabled(Boolean.TRUE);
		newApplicationUser.setCreatedDate(Calendar.getInstance());
		newApplicationUser.setModifiedDate(Calendar.getInstance());
		
		return newApplicationUser;
	}
	
	@Override
	public Map<String, List<SelectItem>> getDropdownMenuValues() {
		//declare map, and lists to go into map
		Map<String, List<SelectItem>> mapOfSelectItems = new HashMap<String, List<SelectItem>>();
		List<SelectItem> stateCodeList = new ArrayList<SelectItem>();
		List<SelectItem> userTypeCodeList = new ArrayList<SelectItem>();
		List<SelectItem> verificationStatusList = new ArrayList<SelectItem>();
		
		//get state codes, convert them into SelectItems, and put them into map
		List<StateCode> stateCodesEntityList = StateCode.findAllStateCodes();
		for(StateCode stateCode : stateCodesEntityList) {
			stateCodeList.add(new SelectItem(stateCode, stateCode.getStateCodeID()));
		}
		mapOfSelectItems.put("stateCode", stateCodeList);
		
		//get user type codes
		for(UserTypeCode userTypeCode : UserTypeCode.values()) {
			userTypeCodeList.add(new SelectItem(userTypeCode, userTypeCode.name()));
		}
		mapOfSelectItems.put("userTypeCode", userTypeCodeList);
		
		//get verification status
		for(VerificationStatus verificationStatus : VerificationStatus.values()) {
			verificationStatusList.add(new SelectItem(verificationStatus, verificationStatus.name()));
		}
		mapOfSelectItems.put("verificationStatus", verificationStatusList);
		
		return mapOfSelectItems;
	}
	
}
